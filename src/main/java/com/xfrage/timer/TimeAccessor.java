package com.xfrage.timer;

import java.io.*;

public class TimeAccessor {

    private static File file;

    private TimeAccessor() {
        // verhindert Instanziierung
    }

    public static void init(File dataFolder) {
        file = new File(dataFolder, "timer.txt");

        try {
            if (!file.exists()) {
                dataFolder.mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Could not create timer.txt: " + e.getMessage());
        }
    }

    public static void saveTime(int time) {
        if (file == null) {
            throw new IllegalStateException("TimeAccessor not initialized!");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(time));
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static int getCurrentTime() {
        if (file == null) {
            throw new IllegalStateException("TimeAccessor not initialized!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line) : 0;
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return 0;
        }
    }
}