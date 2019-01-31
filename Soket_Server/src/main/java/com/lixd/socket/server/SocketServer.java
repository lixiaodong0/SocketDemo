package com.lixd.socket.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class SocketServer {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.startServer();
    }

    private void startServer() {
        Socket socket = null;
        try {
            //1.创建服务端Socket,并指定要监听的端口
            ServerSocket serverSocket = new ServerSocket(8989);
            System.out.println("server start!!!");
            //2.阻塞线程，等待多个客户端连接
            while (true) {
                socket = serverSocket.accept();
                System.out.println("client: " + socket.hashCode() + " connect success ");
                handleSocket(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void handleSocket(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                try {
                    //3.获取socket输入流，读取客户端消息
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String contentMsg = null;
                    while ((contentMsg = reader.readLine()) != null) {
                        System.out.println("client:" + socket.hashCode() + ",contentMsg:" + contentMsg);

                        //收到消息，向客户端发送一条消息
                        writer.write("SUCCESS");
                        writer.write("\n");
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 每隔3秒向客户端发送一条消息
     */
    public void testSend(final BufferedWriter writer) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    writer.write("3!!!");
                    writer.write("\n");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 3000);
    }


}