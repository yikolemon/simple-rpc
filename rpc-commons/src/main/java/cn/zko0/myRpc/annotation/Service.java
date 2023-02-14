package cn.zko0.myRpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duanfuqiang
 * @date 2023/2/13 13:50
 * @description 放在被注册服务上
 */
@Target(ElementType.TYPE)
//运行时保留注解
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    public String name() default "";

}
