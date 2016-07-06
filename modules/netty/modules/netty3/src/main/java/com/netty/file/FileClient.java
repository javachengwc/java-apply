package com.netty.file;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.util.ThreadUtil;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

public class FileClient
{
    public static void main(String[] args)
    {

        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory()
        {

            @Override
            public ChannelPipeline getPipeline() throws Exception
            {
                ChannelPipeline pipeline = pipeline();

                pipeline.addLast("decoder", new HttpResponseDecoder());

                //不加此，此对文件做了大小限制
                //pipeline.addLast("aggregator", new HttpChunkAggregator(6048576));
                pipeline.addLast("encoder", new HttpRequestEncoder());
                pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
                pipeline.addLast("handler", new FileClientHandler());

                return pipeline;
            }

        });

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(
                "localhost", 8080));

        ThreadUtil.sleep(1000);

        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET, "E:\\eclipse.rar");
        future.getChannel().write(request);

        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
}