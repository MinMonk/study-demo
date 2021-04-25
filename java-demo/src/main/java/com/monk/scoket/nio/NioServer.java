package com.monk.scoket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monk.scoket.CommonConstant;

public class NioServer {
    
    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);
    
    public static void main(String[] args) {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void startServer() throws IOException{
        
        ServerSocketChannel channel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        channel.bind(new InetSocketAddress(CommonConstant.PORT));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        
        while(true) {
            selector.select(200);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (Iterator iterator = selectionKeys.iterator(); iterator.hasNext();) {
                SelectionKey key = (SelectionKey) iterator.next();
                
                if(key.isAcceptable()) {
                    SocketChannel socket = channel.accept();
                    logger.info("新客户端连接");
                    socket.configureBlocking(false);
                    socket.register(selector, SelectionKey.OP_READ);
                }
                
                if(key.isReadable()) {
                    SocketChannel socket = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socket.read(byteBuffer);
                    byteBuffer.flip();
                    logger.info("{}", Charset.defaultCharset().decode(byteBuffer));
                    socket.register(selector, SelectionKey.OP_READ);
                }
                
                iterator.remove();
            }
            
        }
        
        
    }

}
