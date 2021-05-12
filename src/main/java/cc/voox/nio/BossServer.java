package cc.voox.nio;

import cc.voox.util.ByteBufferUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class BossServer {
    static class Worker implements Runnable {
        private Selector selector;
        private String name;
        private Thread thread;
        private volatile boolean start;


        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel socketChannel) throws IOException {
            if (!start) {
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            selector.wakeup();
            socketChannel.register(selector, SelectionKey.OP_READ, null);
        }


        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select();
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            log.info("read before {}", key.channel());
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            int read = channel.read(buffer);
                            buffer.flip();
                            ByteBufferUtils.debugAll(buffer);
                            log.info("read after {}", key.channel());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        Selector boss = Selector.open();
        ssc.register(boss, SelectionKey.OP_ACCEPT, null);
        Worker worker = new Worker("work-0");

        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {

                    SocketChannel socketChannel = ssc.accept();
                    log.info("connected {}", socketChannel);
                    socketChannel.configureBlocking(false);
                    log.info("before register {}", socketChannel.getRemoteAddress());
                    worker.register(socketChannel);

                    log.info("after register {}", socketChannel.getRemoteAddress());
                }
            }
        }

    }
}
