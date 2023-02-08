package cn.zko0.myRpc.client;

import cn.zko0.myRpc.entity.RpcRequest;

/**
 * @author duanfuqiang
 * @date 2023/2/8 13:29
 * @description
 */
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);

}
