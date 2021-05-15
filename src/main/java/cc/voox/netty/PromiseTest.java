package cc.voox.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class PromiseTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoop executors = eventLoopGroup.next();
        DefaultPromise<String> promise = new DefaultPromise<>(executors);
        new Thread(()->{
            log.info("exec...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("done in thread~");
           promise.setSuccess("hello");
        }).start();
        log.info("{}", promise.get(4, TimeUnit.SECONDS));

    }
}
