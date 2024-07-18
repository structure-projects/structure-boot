package cn.structure.oauth.client;

import cn.structured.rpc.annotation.RpcClient;
import cn.structured.rpc.handler.BaseHttpClient;
import cn.structured.rpc.handler.IRpcHandler;

/**
 * @author chuck
 * @version 2024/07/18 上午11:44
 * @since 1.8
 */
@RpcClient(value = "user-center", host = "127.0.0.1", port = 18001)
public class UserClient extends BaseHttpClient implements IRpcHandler {
}
