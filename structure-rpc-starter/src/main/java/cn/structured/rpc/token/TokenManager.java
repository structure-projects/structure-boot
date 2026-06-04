package cn.structured.rpc.token;

import cn.structured.rpc.entity.TokenInfo;

/**
 * Token管理器接口
 *
 * @author chuck
 */
public interface TokenManager {

    /**
     * 获取有效的Token
     *
     * @return TokenInfo
     */
    TokenInfo getToken();

    /**
     * 刷新Token
     *
     * @return 新的TokenInfo
     */
    TokenInfo refreshToken();

    /**
     * 手动失效Token
     */
    void invalidateToken();

    /**
     * 检查Token是否有效
     *
     * @return true表示有效
     */
    boolean isValid();
}