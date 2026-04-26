package com.example.treevisualizer;

public class BinarySearchTree {
    private Node root;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(Player player) {
        root = insert(root, player);
    }

    private Node insert(Node current, Player player) {
        if (current == null) {
            return new Node(player);
        }

        if (player.getRanking() < current.getPlayer().getRanking()) {
            current.setLeft(insert(current.getLeft(), player));
        } else {
            current.setRight(insert(current.getRight(), player));
        }

        return current;
    }

    public Node searchNode(String nickname) {
        return search(root, nickname);
    }

    public boolean search(String nickname) {
        return searchNode(nickname) != null;
    }

    private Node search(Node current, String nickname) {
        if (current == null) {
            return null;
        }

        if (current.getPlayer().getNickname().equals(nickname)) {
            return current;
        }

        Node leftResult = search(current.getLeft(), nickname);
        if (leftResult != null) {
            return leftResult;
        }

        return search(current.getRight(), nickname);
    }

    public Player remove(String nickname) {
        Node nodeToRemove = search(root, nickname);
        if (nodeToRemove != null) {
            Player removedPlayer = nodeToRemove.getPlayer();
            root = remove(root, nickname);
            return removedPlayer;
        }
        return null;
    }

    private Node remove(Node current, String nickname) {
        if (current == null) {
            return null;
        }

        if (current.getPlayer().getNickname().equals(nickname)) {
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }

            if (current.getLeft() == null) {
                return current.getRight();
            }

            if (current.getRight() == null) {
                return current.getLeft();
            }

            Node successor = findMin(current.getRight());
            current.setPlayer(successor.getPlayer());
            current.setRight(remove(current.getRight(), successor.getPlayer().getNickname()));
            return current;
        }

        current.setLeft(remove(current.getLeft(), nickname));
        current.setRight(remove(current.getRight(), nickname));

        return current;
    }

    private Node findMin(Node current) {
        if (current.getLeft() == null) {
            return current;
        }
        return findMin(current.getLeft());
    }

    public void inOrder() {
        inOrder(root);
        System.out.println();
    }

    private void inOrder(Node current) {
        if (current != null) {
            inOrder(current.getLeft());
            System.out.print(current.getPlayer().getNickname() + "(" + current.getPlayer().getRanking() + ") ");
            inOrder(current.getRight());
        }
    }

    public Node getRoot() {
        return root;
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }
}
