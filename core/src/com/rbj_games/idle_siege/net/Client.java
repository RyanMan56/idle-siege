package com.rbj_games.idle_siege.net;

import com.rbj_games.idle_siege.IdleSiege;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip = "127.0.0.1";
    private int port = 6666;
    private IdleSiege game;

    public Client(IdleSiege game) {
        this.game = game;
    }

    public void startConnection() {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        System.out.println("sending " + message);
        out.println(message);
//        try {
//            String resp = in.readLine();
//            return resp;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public void run() {
        startConnection();
        String test = null;
        try {
            test = in.readLine();
            System.out.println("Client has received: " + test);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
