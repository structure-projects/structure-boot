package cn.structure.starter.minio.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * minio 配置信息
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 14:06
 */
@Configuration
@ConfigurationProperties(prefix = "structure.minio")
public class MinioProperties {
	/**
	 * minio 服务地址 http://ip:port
	 */
	private String url;

	/**
	 * 用户名
	 */
	private String accessKey;

	/**
	 * 密码
	 */
	private String secretKey;

	/**
	 * 过期时间(默认 60 * 60 * 24 * 7 = 604800 秒)
	 */
	private int expiresSecond = 604800;

	/**
	 * 是否启用 endpoint
	 */
	private Boolean endpointEnable;

	public Boolean getEndpointEnable() {
		return endpointEnable;
	}

	public void setEndpointEnable(Boolean endpointEnable) {
		this.endpointEnable = endpointEnable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getExpiresSecond() {
		return expiresSecond;
	}

	public void setExpiresSecond(int expiresSecond) {
		this.expiresSecond = expiresSecond;
	}
}
