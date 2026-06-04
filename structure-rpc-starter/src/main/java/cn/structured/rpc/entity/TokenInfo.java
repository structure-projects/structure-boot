package cn.structured.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Token信息
 *
 * @author chuck
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

    /**
     * access token
     */
    private String accessToken;

    /**
     * refresh token
     */
    private String refreshToken;

    /**
     * token类型 (Bearer)
     */
    private String tokenType;

    /**
     * 过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 过期时间点
     */
    private LocalDateTime expireTime;

    /**
     * 是否过期
     */
    public boolean isExpired() {
        return expireTime != null && LocalDateTime.now().isAfter(expireTime);
    }

    /**
     * 是否已过期或即将过期（提前60秒）
     */
    public boolean isExpiredOrExpiring() {
        if (expireTime == null) {
            return true;
        }
        return LocalDateTime.now().plusSeconds(60).isAfter(expireTime);
    }
}