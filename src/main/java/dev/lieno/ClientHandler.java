package dev.lieno;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final File fileFolder;
    private final Map<String, String> allowedExtentions;

    public ClientHandler(Socket clientSocket, String fileFolder, Map<String, String> allowedExtentions) {
        this.socket = clientSocket;
        this.fileFolder = new File(fileFolder);
        this.allowedExtentions = allowedExtentions;
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
                } else {

                    switch (input.split(" ")[0]) {
                        case "LEGGI":
                            if(input.split(" ")[1].contains(".")) {
                                socketOut.println("ERR SYNTAX");
                                break;
                            }                            

                            String fileName = findFile(input.split(" ")[1]);

                            if(fileName.equals("ERR NOTFOUND")) {
                                socketOut.println(fileName);
                                break;
                            }

                            socketOut.println(readFile(fileName));
                        
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

    private String findFile(String input) {
        for (String extention: allowedExtentions.keySet()) {
            File file = new File(fileFolder, (input + "." + extention) );

            if(file.exists())
                return input + "." + extention;
        }

        return "ERR NOTFOUND";
    }

    private String readFile(String fileName) {

        String[] fileNameParts = fileName.split("\\.");
        
        File file = new File(fileFolder, fileName);
        String result =  "";

        try (
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
        ) {
            byte[] dati = Files.readAllBytes(file.toPath());
            String line = fileReader.readLine();
            result = "OK FILE \nContent-Type: " + allowedExtentions.get(fileNameParts[fileNameParts.length - 1]) + "\nContent-Length: " + dati.length +  "\n\n";

            while (line != null) {
                result += line + "\n";

                line = fileReader.readLine();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}



