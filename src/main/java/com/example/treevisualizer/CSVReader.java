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

                int commaIndex = line.indexOf(",");

                if (commaIndex > 0) {
                    String nickname = line.substring(0, commaIndex).trim();
                    int ranking = Integer.parseInt(line.substring(commaIndex + 1).trim());

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