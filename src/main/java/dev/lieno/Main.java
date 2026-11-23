package dev.lieno;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            
            while(true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).run();
            }

        } catch(IOException e) {
            System.out.println("Server failed: " + e.getMessage());
            e.printStackTrace();
        }  
    }
}