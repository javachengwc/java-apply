package com.netty.file;

import static org.jboss.netty.handler.codec.http.HttpHeaders.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelFutureProgressListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DefaultFileRegion;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.FileRegion;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedFile;
import org.jboss.netty.util.CharsetUtil;

public class FileServerHandler extends SimpleChannelUpstreamHandler
{

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception
    {
        HttpRequest request = (HttpRequest) e.getMessage();
        if (request.getMethod() != GET)
        {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        final String path = request.getUri();
        System.out.println("request uri="+path);
        if (path == null)
        {
            sendError(ctx, FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (file.isHidden() || !file.exists())
        {
            sendError(ctx, NOT_FOUND);
            return;
        }
        if (!file.isFile())
        {
            sendError(ctx, FORBIDDEN);
            return;
        }

        RandomAccessFile raf;
        try
        {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException fnfe)
        {
            sendError(ctx, NOT_FOUND);
            return;
        }
        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

        response.addHeader("fileName", file.getName());
        setContentLength(response, fileLength);

        Channel ch = e.getChannel();
        ch.write(response);

        ChannelFuture writeFuture;
        if (ch.getPipeline().get(SslHandler.class) != null)
        {
            writeFuture = ch.write(new ChunkedFile(raf, 0, fileLength, 8192));
        } else
        {
            final FileRegion region = new DefaultFileRegion(raf.getChannel(),
                    0, fileLength);
            writeFuture = ch.write(region);
            writeFuture.addListener(new ChannelFutureProgressListener()
            {
                public void operationComplete(ChannelFuture future)
                {
                    region.releaseExternalResources();
                }

                public void operationProgressed(ChannelFuture future,
                                                long amount, long current, long total)
                {
                    System.out.printf("%s: %d / %d (+%d)%n", path, current,
                            total, amount);
                }
            });
        }

        if (!isKeepAlive(request))
        {
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception
    {
        Channel ch = e.getChannel();
        Throwable cause = e.getCause();
        if (cause instanceof TooLongFrameException)
        {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        cause.printStackTrace();
        if (ch.isConnected())
        {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }
}