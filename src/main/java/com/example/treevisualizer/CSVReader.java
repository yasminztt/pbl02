package com.example.treevisualizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVReader {
    public static BinarySearchTree loadPlayersFromCSV(String filePath) {
        BinarySearchTree tree = new BinarySearchTree();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(CSVReader.class.getResourceAsStream(filePath)))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String nickname = parts[0].trim();
                    int ranking = Integer.parseInt(parts[1].trim());
                    Player player = new Player(nickname, ranking);
                    tree.insert(player);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return tree;
    }
}
