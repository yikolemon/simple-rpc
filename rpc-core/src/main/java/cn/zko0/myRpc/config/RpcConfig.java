package cn.zko0.myRpc.config;


import lombok.Getter;

import java.util.ResourceBundle;


/**
 * @author duanfuqiang
 * @date 2023/2/9 11:23
 * @description
 */



public class RpcConfig {



    //注册中心类型
    private static String registerCenterType;

    //序列化类型
    private static String serializerType;

    //负载均衡类型
    private static String loadBalanceType;

    //配置Nacos地址
    private static String registerCenterHost;

    private static Integer registerCenterPort;


    private static boolean zookeeperDestoryIsEphemeral;

    private static String serverHostName;

    private static Integer serverPort;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("rpc");
        registerCenterType=bundle.getString("registerCenter.type");
        loadBalanceType=bundle.getString("loadBalance.type");
        registerCenterHost=bundle.getString("registerCenter.host");
        registerCenterPort = Integer.parseInt(bundle.getString("registerCenter.port"));
        try {
            zookeeperDestoryIsEphemeral="true".equals(bundle.getString("registerCenter.destory.isEphemeral"));
        } catch (Exception e) {
            zookeeperDestoryIsEphemeral=false;
        }
        serializerType=bundle.getString("serializer.type");
        serverHostName=bundle.getString("server.hostName");
        serverPort=Integer.parseInt(bundle.getString("server.port"));
    }

    public static String getRegisterCenterType() {
        return registerCenterType;
    }

    public static String getSerializerType() {
        return serializerType;
    }

    public static String getLoadBalanceType() {
        return loadBalanceType;
    }

    public static String getRegisterCenterHost() {
        return registerCenterHost;
    }

    public static Integer getRegisterCenterPort() {
        return registerCenterPort;
    }


    public static String getServerHostName() {
        return serverHostName;
    }

    public static Integer getServerPort() {
        return serverPort;
    }

    public static boolean isZookeeperDestoryIsEphemeral() {
        return zookeeperDestoryIsEphemeral;
    }

}
