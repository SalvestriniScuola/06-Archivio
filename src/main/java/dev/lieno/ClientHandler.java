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
            PrintWriter socketOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        ) {
            
            while (socket != null) {
                String input = socketIn.readLine();
                
                if(input == null)
                    continue;
                
                input.trim();

                if(input.split(" ").length < 2) {
                    if(input.equals("QUIT"))
                        socket.close();
                    else
                        socketOut.println("ERR SYNTAX");
                    socketOut.flush();
                } else {

                    switch (input.split(" ")[0]) {
                        case "LEGGI":
                            
                            socketOut.println(readFile(input.split(" ")[1]));
                            
                            break;
                    
                        default:

                            socketOut.println("ERR UNKNOWNCMD");
                            break;
                    }
                }
            }


        } catch (IOException e ) {
            e.printStackTrace();
        }
    }


    private String readFile(String fileName) {
        File file = new File(fileFolder, fileName);

        if(!file.exists())
            return "ERR NOTFOUND";

        try (
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
        ) {
            String line = fileReader.readLine();
            String result = "OK FILE\n\n";

            while (line != null) {
                result += line + "\n";

                line = fileReader.readLine();
            }

            return result;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return "ERR HOW DID WE GET HERE?";

    }
}



