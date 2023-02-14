package cn.zko0.myRpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author duanfuqiang
 * @date 2023/2/8 16:35
 * @description
 */
@AllArgsConstructor
@Getter
public enum MessageType {

    REQUEST(0),
    RESPONSE(1);

    private Integer num;
}
