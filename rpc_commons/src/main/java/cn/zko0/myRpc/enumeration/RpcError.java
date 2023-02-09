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

    UNSUPPORT_MSG_TYPE("不支持的消息类型"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("无法连接到注册中心"),
    REGISTER_SERVICE_FAILED("服务注册失败"),

    SERVICE_NONE_INSTANCE("服务获取失败"),

    NONE_SERIALIZER("序列化工具未注入"),

    SEND_ERROR("RPC请求发送失败");


    private String message;
}
