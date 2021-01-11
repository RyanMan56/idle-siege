package com.rbj_games.idle_siege.files;

import java.io.*;

public class FileHandler {
    private File file;
    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter out;


    public FileHandler() {
    }

    public void createFile(String filename) {
        createFile(filename, false);
    }

    public void createFile(String filename, boolean deleteOnExit) {
        try {
            File file = new File(filename);
            file.createNewFile();
            if (deleteOnExit) {
                file.deleteOnExit();
            }
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            this.file = file;
            this.fw = fw;
            this.bw = bw;
            this.out = out;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendLine(String text) {
        out.println(text);
        out.flush();
    }

//    https://docs.oracle.com/javase/tutorial/essential/io/notification.html
//    TODO: Follow this to build a file listener, for the graph Process to read from the file

    public void dispose() {
        try {
            fw.close();
            bw.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
