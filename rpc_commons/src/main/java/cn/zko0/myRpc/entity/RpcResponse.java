package cn.zko0.myRpc.entity;

import cn.zko0.myRpc.util.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author duanfuqiang
 * @date 2023/2/7 15:58
 * @description
 */

@Data
public class RpcResponse<T> implements Serializable {
    //响应状态码
    private Integer statusCode;

    //响应信息
    private String message;

    //返回数据
    private T data;

    //调用成功返回
    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response=new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    //调用失败返回
    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response=new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}
