package cc.voox.nio;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import java.time.LocalDateTime;

public class WriteClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8080));
        socketChannel.write(Charset.defaultCharset().encode("hello" + LocalDateTime.now()));
        System.in.read();
    }
}
