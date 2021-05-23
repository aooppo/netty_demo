package cc.voox.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            ChannelFuture future = new Bootstrap().group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .handler(new ChannelInitializer<SocketChannel>() {


                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast("encoder", new StringEncoder());
//                            ch.pipeline().addLast("decoder",new StringDecoder());
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                                      @Override
                                                      protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
                                                          log.info("receive server msg: {}", s);
//                                                          ctx.writeAndFlush("hello~");
                                                      }

                                                      @Override
                                                      public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                                          log.info("channel active in client");
                                                          ctx.writeAndFlush("hello~ active..");
                                                          super.channelActive(ctx);
                                                      }
                                                  }

                            );
                        }
                    })
//                    .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .connect("localhost", 8000).sync();
            future.channel().closeFuture().sync();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }

    }
}
