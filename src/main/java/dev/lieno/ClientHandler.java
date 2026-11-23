package dev.lieno;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final File fileFolder;

    public ClientHandler(Socket clientSocket, File fileFolder) {
        this.socket = clientSocket;
        this.fileFolder = fileFolder;
    }

    @Override
    public void run() {
        try (
            Socket socket = this.socket;
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter socketOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        ) {

            socketOut.println(readFile(socketIn.readLine()));

        } catch (IOException e ) {
            e.printStackTrace();
        }
    }


    private String readFile(String fileName) {
        File file = new File(fileFolder, fileName);

        try (
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
        ) {
            String line = fileReader.readLine();
            String result = "";

            while (line != null) {
                result += line + "\n";

                line = fileReader.readLine();
            }

            return result;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return "err";

    }
}



