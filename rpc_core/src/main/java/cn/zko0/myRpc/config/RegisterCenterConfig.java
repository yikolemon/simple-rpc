package cn.zko0.myRpc.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;


/**
 * @author duanfuqiang
 * @date 2023/2/9 11:23
 * @description
 */

public class RegisterCenterConfig {

    //配置Nacos地址
    private static String host;

    private static Integer port;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("registerCenter");
        host=bundle.getString("registerCenter.host");
        port = Integer.parseInt(bundle.getString("registerCenter.port"));
    }

    public static String getHostName(){
        return host;
    }

    public static int getServerPort(){
        return port;
    }

}
