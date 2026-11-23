package dev.lieno;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Main {

    private static final int SERVER_PORT = 3000;
    private static final String FILE_FOLDER_PATH = "documenti";
    private static final Map<String, String> ALLOWED_EXTENTIONS_MAP = Map.of("txt", "text/plain; charset=utf-8", "md", "text/markdown; charset=utf-8", "csv", "text/csv; charset=utf-8");

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            
            while(true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, FILE_FOLDER_PATH, ALLOWED_EXTENTIONS_MAP), "client-" + clientSocket.getRemoteSocketAddress()).start();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }  
    }
}