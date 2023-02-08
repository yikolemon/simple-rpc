package cn.zko0.myRpc.server;

import cn.zko0.myRpc.service.handler.HandlerThread;
import cn.zko0.myRpc.registry.ServiceRegistry;
import cn.zko0.myRpc.service.handler.RequestHandler;
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
public class SocketRpcServer implements RpcServer{
    private final ExecutorService threadPool;

    private RequestHandler requestHandler;

    private final ServiceRegistry serviceRegistry;

    private static final int CORE_POOL_SIZE=5;
    private static final int MAXIMUM_POOL_SIZE=50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    public SocketRpcServer(ServiceRegistry serviceRegistry, RequestHandler requestHandler) {
        this.serviceRegistry=serviceRegistry;
        this.requestHandler = requestHandler;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool=new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public void start(int port){
        try(ServerSocket serverSocket=new ServerSocket(port)) {
            log.info("服务器启动中");
            Socket socket;
            while ((socket=serverSocket.accept())!=null){
                log.info("客户端连接,IP为==>{}",socket.getInetAddress());
                threadPool.execute(new HandlerThread(socket,serviceRegistry, requestHandler));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
