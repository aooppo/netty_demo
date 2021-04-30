package cc.voox.netty.c1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;



@Slf4j
public class TestByteBuffer {
    @Test
    void test1() {
        log.info("hello: {}, study {}", "TJ", "netty");
    }

    @Test
    void testBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        buffer.put((byte) 5);
        buffer.put((byte) 6); // 初始化一个写满的buffer

        buffer.flip();
        // position: 0, limit: 6, capacity: 6  -- 切换为读取模式

        buffer.get();
        buffer.get();
        // position: 2, limit: 6, capacity: 6  -- 读取两个字节后，还剩余四个字节

        buffer.compact();
        // position: 4, limit: 6, capacity: 6  -- 进行压缩之后将从第五个字节开始

//        buffer.put((byte) 7);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        System.out.println("init...");
        info(byteBuffer);
        byteBuffer.put(new byte[]{'a', 'b', 'c', 'd'});
        System.out.println("write into buffer after");
        info(byteBuffer);
        System.out.println("flip before");
        byteBuffer.flip();
        System.out.println("flip after");
        info(byteBuffer);
        System.out.println("读取ing");
        int i = 1;
        while(byteBuffer.hasRemaining()){
            if(i++ >2) break;
            System.out.println((char) byteBuffer.get());
        }
        System.out.println("读取后, compact before");
        info(byteBuffer);
        byteBuffer.compact();
        System.out.println("读取后, compact after");
        info(byteBuffer);
        System.out.println("clear..");
        byteBuffer.clear();
        info(byteBuffer);
    }

    static void info(ByteBuffer byteBuffer) {
        System.out.println("position "+ byteBuffer.position());
        System.out.println("limit "+ byteBuffer.limit());
        System.out.println("capacity "+ byteBuffer.capacity());
    }
    @Test
    void testChannel() {

        URL url = getClass().getClassLoader().getResource("data.txt");
        if (url != null) {
            log.info(url.toString());
        }
        try {
            assert url != null;
            try (FileChannel channel = new FileInputStream(url.getPath()).getChannel()) {
                ByteBuffer buffer = ByteBuffer.allocate(5);
                int read = channel.read(buffer);
                log.info("读取字节数：{}", read);
                buffer.flip();
                while(buffer.hasRemaining()){
                    System.out.print((char) buffer.get());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
