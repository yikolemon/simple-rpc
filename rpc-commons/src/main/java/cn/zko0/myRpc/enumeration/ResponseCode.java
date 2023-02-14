package cn.zko0.myRpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author duanfuqiang
 * @date 2023/2/7 16:11
 * @description
 */

@AllArgsConstructor
@Getter
public enum ResponseCode{
    SUCCESS(200,"调用成功"),
    FALL(500,"调用失败"),
    NOT_FOUND_METHOD(500,"未找到指定方法"),
    NOT_FOUND_CLASS(500,"未找到指定类");

    private final int code;

    private final String message;

}
