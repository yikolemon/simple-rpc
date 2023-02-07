package cn.zko0.myRpc;

import cn.zko0.myRpc.entity.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author duanfuqiang
 * @date 2023/2/7 16:56
 * @description
 */
@Slf4j
public class RpcClient {
    //远程请求，返回Object
    //通过socket请求，需要host和端口号
    public Object sendRequest(RpcRequest rpcRequest,String host,int port){
        try(Socket socket=new Socket(host,port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException|ClassNotFoundException e) {
            log.error("调用出错==>{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
