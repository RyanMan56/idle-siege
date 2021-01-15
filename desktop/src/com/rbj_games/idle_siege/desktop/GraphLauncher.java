package com.rbj_games.idle_siege.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rbj_games.idle_siege.IdleSiege;

public class GraphLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640;
        config.height = 480;
        config.x = 20;
        config.y = 80;
        new LwjglApplication(new IdleSiege("Graph", false), config);
    }
}
