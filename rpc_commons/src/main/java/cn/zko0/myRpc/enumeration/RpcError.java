package cn.zko0.myRpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author duanfuqiang
 * @date 2023/2/8 10:37
 * @description
 */

@Getter
@AllArgsConstructor
public enum RpcError {
    REGSISRTY_FAILL("服务未实现接口，注册失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_INTERFACE("注册的服务未实现接口"),

    UNKNOWN_PORTOCOL("未知的协议包"),

    UNSUPPORT_MSG_TYPE("不支持的消息类型");


    private String message;
}
