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
