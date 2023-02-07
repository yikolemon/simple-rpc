package cn.zko0.myRpc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author duanfuqiang
 * @date 2023/2/7 21:08
 * @description
 */
@Slf4j
public class RpcServer {
    private final ExecutorService threadPool;


    public RpcServer() {
        int corePoolSize=5;
        int maximumPooSize=50;
        long keepAliveTume=60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool=new ThreadPoolExecutor(corePoolSize,maximumPooSize,keepAliveTume,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public void register(Object service,int port){
        try(ServerSocket serverSocket=new ServerSocket(port)) {
            log.info("服务器启动中");
            Socket socket;
            while ((socket=serverSocket.accept())!=null){
                log.info("客户端连接,IP为==>{}",socket.getInetAddress());
                threadPool.execute(new WorkThread(socket,service));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
