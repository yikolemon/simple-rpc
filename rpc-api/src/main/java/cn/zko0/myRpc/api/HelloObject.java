package cn.zko0.myRpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author duanfuqiang
 * @date 2023/2/7 10:05
 * @description
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
