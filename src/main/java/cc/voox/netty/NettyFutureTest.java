package cc.voox.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class NettyFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoop next = eventLoopGroup.next();

        Future<String> future = next.submit(() -> {
            try {
                log.info("clacing");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "test~";
        });

        future.addListener(new GenericFutureListener<Future<? super String>>() {
            @Override
            public void operationComplete(Future<? super String> future) throws Exception {
                log.info("{} in listener}", future.getNow());
            }
        });
        log.info("wait");
        log.info("{} async}", future.getNow());
        log.info("{}", future.get());
        log.info("done");

        eventLoopGroup.shutdownGracefully();
    }
}
