package cc.voox.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
@Slf4j
public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            SocketAddress localAddress = socketChannel.getLocalAddress();

            System.out.println("waiting...");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
