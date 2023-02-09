package cn.zko0.myRpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author duanfuqiang
 * @date 2023/2/7 15:56
 * @description
 */

@Data
@Builder

public class RpcRequest implements Serializable {
    //接口名
    private String interfaceName;

    //方法名
    private String methodName;

    //调用方法的参数
    private Object[] parameters;

    //参数类型
    private Class<?>[] paramTypes;

    @Tolerate
    public RpcRequest() {
    }
}
