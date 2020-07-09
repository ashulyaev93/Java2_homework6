package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {

        ServerSocket server = null; //создаём сервер;
        Socket socket = null; //создаём сокет;

        try {
            server = new ServerSocket(8189); //определяем порт;
            System.out.println("Сервер запущен");
            socket = server.accept(); //точка подключения;
            System.out.println("Клиент подключен");

            Scanner in = new Scanner(socket.getInputStream());//входящий поток;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//возврат потока вывода;
            Scanner console = new Scanner(System.in);//ввод сообщений в консоль;

            Thread t1 = new Thread(new Runnable() { //поток выхода и его запуск;
                @Override
                public void run() {
                    while (true) {
                        String str = in.nextLine();
                        if(str.equals("/end")) {
                            out.println("/end");
                            break;
                        }
                        System.out.println("Client " + str);
                    }
                }
            });
            t1.start();

            Thread t2 = new Thread(new Runnable() { //поток сообщений и его запуск;
                @Override
                public void run() {
                    while (true) {
                        System.out.println("Введите сообщение");
                        String str = console.nextLine();
                        System.out.println("Сообщение отправлено!");
                        out.println(str);
                    }
                }
            });
            t2.setDaemon(true);//фоновый процесс, который очищает кэш, актуализирует значения. В данном случае для чтения с консоли;
            t2.start();

            //отладка;
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
