package cc.voox.nio;

import lombok.extern.slf4j.Slf4j;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

@Slf4j
public class WriteServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8080));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel channel = serverSocketChannel.accept();
                    channel.configureBlocking(false);
                    SelectionKey selectionKey = channel.register(selector, 0, null);
                    selectionKey.interestOps(selectionKey.interestOps() + SelectionKey.OP_READ);
                    StringBuilder sf = new StringBuilder();
                    for (long i = 0; i < 50000000; i++) {
                        sf.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sf.toString());
                    int write = channel.write(buffer);
                    System.out.println(write);
                    if (buffer.hasRemaining()) {
                        selectionKey.interestOps(selectionKey.interestOps() + SelectionKey.OP_WRITE);
                        selectionKey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer =(ByteBuffer) key.attachment();
                    SocketChannel channel =(SocketChannel) key.channel();
                    int write = channel.write(buffer);
                    System.out.println(write);
                    if (!buffer.hasRemaining()) {
                        key.attach(null);
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }

        }

    }
}
