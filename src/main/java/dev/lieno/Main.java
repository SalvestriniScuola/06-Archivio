package dev.lieno;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int SERVER_PORT = 3000;
    private static final File fileFolder = new File("documenti");

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            
            while(true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, fileFolder), "client-" + clientSocket.getRemoteSocketAddress()).start();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }  
    }
}