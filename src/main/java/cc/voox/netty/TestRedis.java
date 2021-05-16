package cc.voox.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

public class TestRedis {
    public static void main(String[] args) throws InterruptedException {
        final byte[] LINE = {'\r','\n'};
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            ChannelFuture future = bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    buf.writeBytes("*3".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("$3".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("set".getBytes());
                                    buf.writeBytes(LINE);

                                    buf.writeBytes("$4".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("name".getBytes());
                                    buf.writeBytes(LINE);

                                    buf.writeBytes("$8".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("zhangsan".getBytes());
                                    buf.writeBytes(LINE);
//                                    ctx.writeAndFlush(buf);
                                    ch.writeAndFlush(buf);
                                }



                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("Server channelRead....");
                                    System.out.println(ctx.channel().remoteAddress()+"->Server :"+msg.toString());
                                    ctx.write("Server write"+msg);
                                    ctx.flush();
                                    ByteBuf byteBuf = (ByteBuf) msg;
                                    System.out.println(byteBuf.toString(Charset.defaultCharset()));
                                    super.channelRead(ctx, msg);
                                }
                            });

                        }
                    })
                    .connect("127.0.0.1", 6379)
                    .sync();
            future.channel().close().sync();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            nioEventLoopGroup.shutdownGracefully();
        }

    }
}
