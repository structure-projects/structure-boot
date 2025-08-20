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
public class BaseHttpClient implements IRpcHandler {

    private HttpHost httpHost;
    private HttpClient httpClient;


    @Override
    public void init(String host, Integer port) {
        this.httpHost = new HttpHost(host, port);
        this.httpClient = HttpClientUtil.getHttpClient();
    }

    @Override
    public HttpResponse handler(HttpRequest httpRequest) {
        try {
            preposition(httpRequest);
            HttpResponse execute = httpClient.execute(httpHost, httpRequest);
            postposition(execute);
            return execute;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
