package com.example.treevisualizer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TreeVisualizer extends Application {

    static class Node {
        char letter;
        Node left;
        Node right;

        public Node(char letter) {
            this.letter = letter;
        }
    }

    static class MorseBST {
        private Node root;

        public MorseBST() {
            root = new Node(' ');
        }

        public void insert(char letter, String morseCode) {
            Node current = root;

            for (char c : morseCode.toCharArray()) {
                if (c == '.') {
                    if (current.left == null)
                        current.left = new Node(' ');
                    current = current.left;
                } else {
                    if (current.right == null)
                        current.right = new Node(' ');
                    current = current.right;
                }
            }

            current.letter = letter;
        }

        public int getHeight() {
            return getHeight(root);
        }

        private int getHeight(Node node) {
            if (node == null) return 0;
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }

        public void drawTree(Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4);
        }

        private void drawNode(GraphicsContext gc, Node node, double x, double y, double offset) {
            if (node == null) return;

            gc.strokeOval(x - 15, y - 15, 30, 30);
            gc.strokeText(String.valueOf(node.letter), x - 5, y + 5);

            if (node.left != null) {
                gc.strokeLine(x, y, x - offset, y + 80);
                drawNode(gc, node.left, x - offset, y + 80, offset / 2);
            }

            if (node.right != null) {
                gc.strokeLine(x, y, x + offset, y + 80);
                drawNode(gc, node.right, x + offset, y + 80, offset / 2);
            }
        }
    }

    @Override
    public void start(Stage stage) {
        MorseBST bst = new MorseBST();

        // Inserindo alfabeto
        bst.insert('A', ".-");
        bst.insert('B', "-...");
        bst.insert('C', "-.-.");
        bst.insert('D', "-..");
        bst.insert('E', ".");
        bst.insert('F', "..-.");
        bst.insert('G', "--.");
        bst.insert('H', "....");
        bst.insert('I', "..");
        bst.insert('J', ".---");
        bst.insert('K', "-.-");
        bst.insert('L', ".-..");
        bst.insert('M', "--");
        bst.insert('N', "-.");
        bst.insert('O', "---");
        bst.insert('P', ".--.");
        bst.insert('Q', "--.-");
        bst.insert('R', ".-.");
        bst.insert('S', "...");
        bst.insert('T', "-");
        bst.insert('U', "..-");
        bst.insert('V', "...-");
        bst.insert('W', ".--");
        bst.insert('X', "-..-");
        bst.insert('Y', "-.--");
        bst.insert('Z', "--..");

        int height = bst.getHeight();
        Canvas canvas = new Canvas(800, 600);

        bst.drawTree(canvas);

        Group root = new Group(canvas);
        Scene scene = new Scene(root);

        stage.setTitle("Árvore Morse");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}