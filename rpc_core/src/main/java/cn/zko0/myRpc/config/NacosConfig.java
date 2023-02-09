package cn.zko0.myRpc.config;

/**
 * @author duanfuqiang
 * @date 2023/2/9 11:23
 * @description
 */
public class NacosConfig {

    //配置Nacos地址
    public static String getHostName(){
        return "127.0.0.1";
    }

    public static int getServerPort(){
        return 9000;
    }
}
