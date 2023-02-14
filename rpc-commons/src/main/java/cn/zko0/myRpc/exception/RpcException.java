package cn.zko0.myRpc.exception;

import cn.zko0.myRpc.enumeration.RpcError;
import lombok.AllArgsConstructor;

/**
 * @author duanfuqiang
 * @date 2023/2/8 10:31
 * @description
 */

public class RpcException extends RuntimeException{

    //RpcException
    public RpcException(RpcError rpcError){
        super(rpcError.getMessage());
    }

}
