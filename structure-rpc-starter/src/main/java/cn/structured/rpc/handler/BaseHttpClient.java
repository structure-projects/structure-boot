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

    private final HttpHost httpHost;
    private final HttpClient httpClient;

    public BaseHttpClient(String host) {
        this.httpHost = new HttpHost(host);
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
