package com.rbj_games.idle_siege.net;

import com.badlogic.gdx.math.Vector2;
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
            while((test = in.readLine()) != null) {
                Vector2 point = new Vector2().fromString(test);
                if (game.queuedGraphPoints != null) {
                    game.queuedGraphPoints.add(point);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
