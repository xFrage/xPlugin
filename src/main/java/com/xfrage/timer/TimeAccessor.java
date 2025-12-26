package com.xfrage.timer;

import java.io.*;

public class TimeAccessor {

    static final String filepath = "C:\\Users\\Anwender\\Dropbox\\PC\\Desktop\\xServer\\plugins\\timer.txt";

    public static void saveTime(int time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));) {
            writer.write(String.valueOf(time));
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

    }

    public static int getCurrentTime() {
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            line = reader.readLine();
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return Integer.parseInt(line);
    }

}
