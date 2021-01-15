package com.rbj_games.idle_siege.net;

import com.rbj_games.idle_siege.IdleSiege;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip = "127.0.0.1";
    private int port = 6666;
    private IdleSiege game;

    public Server(IdleSiege game) {
        this.game = game;
    }

    public void startConnection() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("awaiting connection...");
            clientSocket = serverSocket.accept();
            System.out.println("established connection!");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String test = in.readLine();
            System.out.println("Server has received: " + test);
            out.println(test + " reply");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (clientSocket != null && clientSocket.isConnected()) {
            out.println(message);
        }
    }

    @Override
    public void run() {
        startConnection();
    }
}
