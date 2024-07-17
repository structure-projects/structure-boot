package cn.structured.rpc.entity;

import lombok.Data;
import org.springframework.util.MultiValueMap;

/**
 * 远程服务定义
 *
 * @author chuck
 * @version 2024/07/17 下午5:01
 * @since 1.8
 */
@Data
public class RemoteService {

    /**
     * 远程服务应用ID
     */
    private String accessKey;

    /**
     * 远程服务密钥
     */
    private String secretKey;

    /**
     * host
     */
    private String host;

    /**
     * 请求头
     */
    private MultiValueMap<String, String> headers;

}
