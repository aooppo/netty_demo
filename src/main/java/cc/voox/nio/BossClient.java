package cc.voox.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Slf4j
public class BossClient {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
//            SocketAddress localAddress = socketChannel.getLocalAddress();
            socketChannel.write(Charset.defaultCharset().encode("123456789abcdef"));
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
