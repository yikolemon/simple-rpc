package cn.zko0.myRpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duanfuqiang
 * @date 2023/2/13 13:51
 * @description 注册服务扫描，放在入口启动类上
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScan {

    //扫描的包的范围
    public String value() default "";

}
