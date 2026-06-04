/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structured.rpc.proxy;

import cn.structured.rpc.handler.BaseHttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC动态代理处理器
 * 实现类似FeignClient的动态代理调用
 * 使用Spring Web的注解来定义HTTP请求
 * 支持泛型返回类型和完整的RESTful风格
 *
 * @author chuck
 */
@Slf4j
public class RpcProxyHandler implements InvocationHandler {

    private final BaseHttpClient httpClient;
    private final String baseUrl;

    public RpcProxyHandler(BaseHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        String httpMethod = getHttpMethod(method);
        String path = getPath(method);

        String fullUrl = baseUrl + path;
        fullUrl = replacePathVariables(fullUrl, method, args);
        fullUrl = appendQueryParams(fullUrl, method, args);

        Object requestBody = extractRequestBody(method, args);
        Map<String, String> requestHeaders = extractRequestHeaders(method, args);
        
        log.debug("[RpcProxy] 执行远程调用 - method: {}, url: {}, requestBody: {}, headers: {}", 
                httpMethod, fullUrl, requestBody, requestHeaders);

        Object result = executeRequest(httpMethod, fullUrl, requestBody, requestHeaders, method.getGenericReturnType());

        log.debug("[RpcProxy] 远程调用完成 - method: {}, url: {}, result: {}", 
                httpMethod, fullUrl, result);

        return result;
    }

    private String getHttpMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return "GET";
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            return "POST";
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            return "PUT";
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            return "DELETE";
        }
        if (method.isAnnotationPresent(PatchMapping.class)) {
            return "PATCH";
        }
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping.method().length > 0) {
                return mapping.method()[0].name();
            }
        }
        return "GET";
    }

    private String getPath(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            String[] values = method.getAnnotation(GetMapping.class).value();
            return values.length > 0 ? values[0] : "";
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            String[] values = method.getAnnotation(PostMapping.class).value();
            return values.length > 0 ? values[0] : "";
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            String[] values = method.getAnnotation(PutMapping.class).value();
            return values.length > 0 ? values[0] : "";
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            String[] values = method.getAnnotation(DeleteMapping.class).value();
            return values.length > 0 ? values[0] : "";
        }
        if (method.isAnnotationPresent(PatchMapping.class)) {
            String[] values = method.getAnnotation(PatchMapping.class).value();
            return values.length > 0 ? values[0] : "";
        }
        if (method.isAnnotationPresent(RequestMapping.class)) {
            String[] values = method.getAnnotation(RequestMapping.class).value();
            return values.length > 0 ? values[0] : "";
        }
        return "";
    }

    private String replacePathVariables(String url, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String paramName = getParamName(pathVariable.value(), parameters[i].getName());
                Object value = args[i];
                url = url.replace("{" + paramName + "}", String.valueOf(value));
            }
        }
        return url;
    }

    private String appendQueryParams(String url, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        StringBuilder queryParams = new StringBuilder();
        
        for (int i = 0; i < parameters.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null && args[i] != null) {
                String paramName = getParamName(requestParam.value(), parameters[i].getName());
                if (queryParams.length() == 0) {
                    queryParams.append("?");
                } else {
                    queryParams.append("&");
                }
                queryParams.append(paramName).append("=").append(args[i]);
            }
        }
        
        return url + queryParams.toString();
    }

    private Object extractRequestBody(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                return args[i];
            }
        }
        return null;
    }

    private Map<String, String> extractRequestHeaders(Method method, Object[] args) {
        Map<String, String> headers = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        
        for (int i = 0; i < parameters.length; i++) {
            RequestHeader requestHeader = parameters[i].getAnnotation(RequestHeader.class);
            if (requestHeader != null && args[i] != null) {
                String headerName = getParamName(requestHeader.value(), parameters[i].getName());
                headers.put(headerName, String.valueOf(args[i]));
            }
        }
        
        return headers;
    }

    private String getParamName(String annotationValue, String defaultName) {
        return (annotationValue != null && !annotationValue.isEmpty()) ? annotationValue : defaultName;
    }

    private Object executeRequest(String httpMethod, String url, Object requestBody, 
                                  Map<String, String> requestHeaders, Type returnType) {
        try {
            if (returnType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) returnType;
                TypeReference<?> typeReference = new TypeReference<Object>() {
                    @Override
                    public Type getType() {
                        return parameterizedType;
                    }
                };
                
                return executeWithTypeReference(httpMethod, url, requestBody, requestHeaders, typeReference);
            } else {
                Class<?> returnClass = returnType instanceof Class ? (Class<?>) returnType : Object.class;
                return executeWithClass(httpMethod, url, requestBody, requestHeaders, returnClass);
            }
        } catch (Exception e) {
            log.error("[RpcProxy] 远程调用失败 - method: {}, url: {}, error: {}", 
                    httpMethod, url, e.getMessage(), e);
            throw e;
        }
    }

    private Object executeWithClass(String httpMethod, String url, Object requestBody,
                                     Map<String, String> requestHeaders, Class<?> returnClass) {
        switch (httpMethod.toUpperCase()) {
            case "GET":
                return httpClient.get(url, requestHeaders, returnClass);
            case "POST":
                return httpClient.post(url, requestBody, requestHeaders, returnClass);
            case "PUT":
                return httpClient.put(url, requestBody, requestHeaders, returnClass);
            case "PATCH":
                return httpClient.patch(url, requestBody, requestHeaders, returnClass);
            case "DELETE":
                return httpClient.delete(url, requestHeaders, returnClass);
            default:
                throw new RuntimeException("不支持的HTTP方法: " + httpMethod);
        }
    }

    private Object executeWithTypeReference(String httpMethod, String url, Object requestBody,
                                             Map<String, String> requestHeaders, TypeReference<?> typeReference) {
        switch (httpMethod.toUpperCase()) {
            case "GET":
                return httpClient.get(url, requestHeaders, typeReference);
            case "POST":
                return httpClient.post(url, requestBody, requestHeaders, typeReference);
            case "PUT":
                return httpClient.put(url, requestBody, requestHeaders, typeReference);
            case "PATCH":
                return httpClient.patch(url, requestBody, requestHeaders, typeReference);
            case "DELETE":
                httpClient.delete(url, requestHeaders);
                return null;
            default:
                throw new RuntimeException("不支持的HTTP方法: " + httpMethod);
        }
    }
}
