package com.monk.scoket.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.utils.DateUtils;

import com.monk.scoket.CommonConstant;

public class Client1 {
    
    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
    public static void main(String[] args) {
        try {
            startClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void startClient() throws Exception{
        Socket socket = new Socket("127.0.0.1", CommonConstant.PORT);
        
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    String message = String.format("%s %s %s", new Object[] {DateUtils.formatDate(new Date()), socket.getLocalSocketAddress().toString(), "client_1"});
                    outputStream.write(message.getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

}
