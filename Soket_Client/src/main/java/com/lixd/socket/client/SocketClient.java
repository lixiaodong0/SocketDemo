package com.lixd.socket.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient();
        socketClient.connect();
    }

    public void connect() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedReader socketReader = null;
        Socket socket = null;
        try {
            //1.创建客户端Socket，指定服务器地址及端口
            socket = new Socket("192.168.115.195", 8989);
            //2.获取socket 输出流，用于输出数据给服务器
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            startServerListener(socketReader);
            String inputContent = null;
            while (!(inputContent = reader.readLine()).equals("bye")) {
                writer.write(inputContent);
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


            if (socketReader != null) {
                try {
                    socketReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startServerListener(final BufferedReader reader) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start server listener....");
                try {
                    String response = null;
                    while ((response = reader.readLine()) != null) {
                        System.out.println("server response:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
