package cn.structured.rpc.token;

import cn.structured.rpc.entity.TokenInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 默认Token管理器实现
 * 支持token缓存、自动刷新和线程安全
 *
 * @author chuck
 */
@Slf4j
public class DefaultTokenManager implements TokenManager {

    private final TokenProvider tokenProvider;
    private volatile TokenInfo tokenInfo;
    private final ReentrantLock lock = new ReentrantLock();

    public DefaultTokenManager(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public TokenInfo getToken() {
        if (tokenInfo == null || tokenInfo.isExpiredOrExpiring()) {
            refreshToken();
        }
        return tokenInfo;
    }

    @Override
    public TokenInfo refreshToken() {
        if (lock.tryLock()) {
            try {
                if (tokenInfo == null || tokenInfo.isExpiredOrExpiring()) {
                    log.info("[TokenManager] 开始刷新Token");
                    
                    boolean hasRefreshToken = tokenInfo != null && tokenInfo.getRefreshToken() != null && !tokenInfo.getRefreshToken().isEmpty();
                    
                    if (hasRefreshToken) {
                        try {
                            tokenInfo = tokenProvider.refreshToken(tokenInfo.getRefreshToken());
                        } catch (Exception e) {
                            log.warn("[TokenManager] 使用refresh_token刷新失败，尝试重新获取 - error: {}", e.getMessage());
                            tokenInfo = tokenProvider.fetchToken();
                        }
                    } else {
                        tokenInfo = tokenProvider.fetchToken();
                    }
                    
                    log.info("[TokenManager] Token刷新成功 - expiresAt: {}", tokenInfo.getExpireTime());
                }
            } finally {
                lock.unlock();
            }
        } else {
            while (tokenInfo == null || tokenInfo.isExpiredOrExpiring()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Token refresh interrupted", e);
                }
            }
        }
        return tokenInfo;
    }

    @Override
    public void invalidateToken() {
        log.info("[TokenManager] 手动失效Token");
        this.tokenInfo = null;
    }

    @Override
    public boolean isValid() {
        return tokenInfo != null && !tokenInfo.isExpired();
    }
}