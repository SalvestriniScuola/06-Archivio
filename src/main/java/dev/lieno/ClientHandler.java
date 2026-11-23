package dev.lieno;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket clienSocket) {
        this.socket = clienSocket;
    }

    @Override
    public void run() {
        try (socket) {
            
        } catch (IOException e ) {
            System.out.println("Handler failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
