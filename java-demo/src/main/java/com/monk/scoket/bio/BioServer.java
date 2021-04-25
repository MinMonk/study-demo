package com.monk.scoket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.monk.scoket.CommonConstant;

public class BioServer {
    public static void main(String[] args) {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer() throws IOException {
        ServerSocket server = new ServerSocket(CommonConstant.PORT);
        while (true) {
            // accept()方法是个阻塞方法，如果没有客户端来连接，线程就会一直阻塞在这儿
            Socket socket = server.accept();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            // read()方法是一个阻塞方法，当没有数据可读时，线程会一直阻塞在read()方法上
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, len));
            }
        }
    }

}
