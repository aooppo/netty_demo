package cc.voox.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestHttp {
    public static void main(String[] args) {

//        final byte[] LINE = {'\r','\n'};
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
//            Bootstrap bootstrap = new Bootstrap();
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            ChannelFuture future = serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.info("{}", msg.getClass());
                                }
                            });

                        }
                    })
                    .bind(8080)
                    .sync();
            future.channel().closeFuture().sync();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
