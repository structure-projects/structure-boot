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
package cn.structured.rpc.handler;

import cn.structure.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import java.io.IOException;

/**
 * client处理器
 *
 * @author chuck
 * @version 2024/07/17 下午4:47
 * @since 1.8
 */
@Slf4j
public class BaseHttpClient implements IRpcHandler {

    private HttpHost httpHost;
    private HttpClient httpClient;


    @Override
    public void init(String host, Integer port) {
        log.info("[RpcClient] 初始化RPC客户端 - host: {}, port: {}", host, port);
        this.httpHost = new HttpHost(host, port);
        this.httpClient = HttpClientUtil.getHttpClient();
        log.info("[RpcClient] RPC客户端初始化完成 - httpHost: {}", httpHost);
    }

    @Override
    public HttpResponse handler(HttpRequest httpRequest) {
        try {
            preposition(httpRequest);
            log.debug("[RpcClient] 发送RPC请求 - target: {}, requestLine: {}", httpHost, httpRequest.getRequestLine());
            HttpResponse execute = httpClient.execute(httpHost, httpRequest);
            postposition(execute);
            log.info("[RpcClient] RPC请求成功 - target: {}, statusLine: {}", httpHost, execute.getStatusLine());
            return execute;
        } catch (IOException e) {
            log.error("[RpcClient] RPC请求失败 - target: {}, error: {}", httpHost, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
