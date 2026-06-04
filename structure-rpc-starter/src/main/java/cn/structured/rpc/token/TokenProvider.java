package cn.structured.rpc.token;

import cn.structured.rpc.entity.TokenInfo;

/**
 * Token提供者接口
 * 用于从远程服务器获取Token
 *
 * @author chuck
 */
public interface TokenProvider {

    /**
     * 获取Token
     *
     * @return TokenInfo
     */
    TokenInfo fetchToken();

    /**
     * 刷新Token
     *
     * @param refreshToken refresh token
     * @return TokenInfo
     */
    TokenInfo refreshToken(String refreshToken);
}