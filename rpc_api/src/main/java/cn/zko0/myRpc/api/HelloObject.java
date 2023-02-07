package cn.zko0.myRpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author duanfuqiang
 * @date 2023/2/7 10:05
 * @description
 */
@AllArgsConstructor
@Data
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
