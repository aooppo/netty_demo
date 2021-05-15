package cc.voox.netty;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
@Slf4j
public class JdkFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> future = executor.submit(() -> {
            try {
                log.info("clacing");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "test~";
        });
        log.info("wait");
        log.info("{}", future.get());
        log.info("done");
    }
}
