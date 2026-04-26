package com.example.treevisualizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TreeVisualizer extends Application {
    private BinarySearchTree tree;
    private Canvas canvas;
    private TextField totalPlayersLabel;
    private Node highlightedNode = null;

    private static final double NODE_RADIUS = 25;
    private static final double VERTICAL_SPACING = 90;
    private static final Color NODE_FILL = Color.web("#e3f2fd");
    private static final Color NODE_STROKE = Color.web("#0066cc");
    private static final Color HIGHLIGHT_FILL = Color.web("#fff59d");
    private static final Color HIGHLIGHT_STROKE = Color.web("#ff6f00");
    private static final Color LINE_COLOR = Color.web("#666666");

    @Override
    public void start(Stage stage) {
        tree = CSVReader.loadPlayersFromCSV("/com/example/treevisualizer/players.csv");

        BorderPane mainRoot = new BorderPane();
        mainRoot.setPadding(new Insets(10));
        mainRoot.setTop(buildTopPanel());

        HBox centerLayout = new HBox(10);
        centerLayout.setPadding(new Insets(10));

        canvas = new Canvas(1400, 800);
        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setStyle("-fx-control-inner-background: #f5f5f5;");
        
        VBox controlPanel = buildControlPanel();
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        centerLayout.getChildren().addAll(scrollPane, controlPanel);

        mainRoot.setCenter(centerLayout);
        
        drawTree();
        Scene scene = new Scene(mainRoot, 1600, 900);
        stage.setTitle("🎮 Player Ranking Tree Visualizer");
        stage.setScene(scene);
        stage.show();

        updateTotalPlayers();
    }

    private VBox buildTopPanel() {
        VBox topPanel = new VBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-border-color: #0066cc; -fx-border-width: 0 0 2 0; -fx-background-color: #f0f8ff;");

        Label titleLabel = new Label("🌳 Player Ranking Binary Search Tree");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #0066cc;");

        HBox infoBox = new HBox(20);
        infoBox.setPadding(new Insets(5));
        
        totalPlayersLabel = new TextField();
        totalPlayersLabel.setEditable(false);
        totalPlayersLabel.setStyle("-fx-font-size: 12;");
        totalPlayersLabel.setPrefWidth(200);

        infoBox.getChildren().addAll(
            totalPlayersLabel,
            createReloadButton(),
            createRefreshButton()
        );

        topPanel.getChildren().addAll(titleLabel, infoBox);
        return topPanel;
    }

    private Button createReloadButton() {
        Button btn = new Button("🔄 Recarregar CSV");
        btn.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btn.setOnAction(e -> {
            tree = CSVReader.loadPlayersFromCSV("/com/example/treevisualizer/players.csv");
            highlightedNode = null;
            drawTree();
            updateTotalPlayers();
            showAlert("Sucesso", "Árvore recarregada do CSV!");
        });
        return btn;
    }

    private Button createRefreshButton() {
        Button btn = new Button("🎨 Atualizar Visualização");
        btn.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btn.setOnAction(e -> {
            highlightedNode = null;
            drawTree();
        });
        return btn;
    }

    private VBox buildControlPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(12));
        panel.setPrefWidth(280);
        panel.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #fafafa;");

        Label titleLabel = new Label("⚙️ Operações");
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #0066cc;");

        panel.getChildren().addAll(
            titleLabel,
            new Separator(),
            buildInsertSection(),
            new Separator(),
            buildSearchSection(),
            new Separator(),
            buildRemoveSection(),
            new Separator(),
            buildInOrderSection(),
            new Separator(),
            new Region()
        );

        VBox.setVgrow(panel.getChildren().get(panel.getChildren().size() - 1), Priority.ALWAYS);
        return panel;
    }

    private VBox buildInsertSection() {
        VBox section = new VBox(8);
        
        Label label = new Label("➕ Inserir Jogador");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");
        
        TextField rankingField = new TextField();
        rankingField.setPromptText("Ranking");
        
        Button btn = new Button("Inserir");
        btn.setStyle("-fx-padding: 6; -fx-font-size: 11;");
        btn.setPrefWidth(260);
        btn.setOnAction(e -> handleInsert(nicknameField, rankingField));
        
        section.getChildren().addAll(label, nicknameField, rankingField, btn);
        return section;
    }

    private void handleInsert(TextField nicknameField, TextField rankingField) {
        String nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            showAlert("Erro", "Nickname não pode estar vazio!");
            return;
        }

        try {
            int ranking = Integer.parseInt(rankingField.getText().trim());
            if (ranking <= 0) {
                showAlert("Erro", "Ranking deve ser um número positivo!");
                return;
            }
            
            Player player = new Player(nickname, ranking);
            tree.insert(player);
            highlightedNode = null;
            drawTree();
            updateTotalPlayers();
            nicknameField.clear();
            rankingField.clear();
            showAlert("✅ Sucesso", "Jogador " + nickname + " inserido!");
        } catch (NumberFormatException ex) {
            showAlert("❌ Erro", "Ranking deve ser um número válido!");
        }
    }

    private VBox buildSearchSection() {
        VBox section = new VBox(8);
        
        Label label = new Label("🔍 Buscar Jogador");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");
        
        Button btn = new Button("Buscar");
        btn.setStyle("-fx-padding: 6; -fx-font-size: 11;");
        btn.setPrefWidth(260);
        btn.setOnAction(e -> handleSearch(nicknameField));
        
        section.getChildren().addAll(label, nicknameField, btn);
        return section;
    }

    private void handleSearch(TextField nicknameField) {
        String nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            showAlert("Erro", "Insira um nickname para buscar!");
            return;
        }

        Node found = tree.searchNode(nickname);
        if (found != null) {
            highlightedNode = found;
            drawTree();
            showAlert("✅ Encontrado", "Jogador '" + nickname + "' encontrado!");
        } else {
            highlightedNode = null;
            drawTree();
            showAlert("❌ Não encontrado", "Jogador '" + nickname + "' não existe!");
        }
    }

    private VBox buildRemoveSection() {
        VBox section = new VBox(8);
        
        Label label = new Label("🗑️ Remover Jogador");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");
        
        Button btn = new Button("Remover");
        btn.setStyle("-fx-padding: 6; -fx-font-size: 11;");
        btn.setPrefWidth(260);
        btn.setOnAction(e -> handleRemove(nicknameField));
        
        section.getChildren().addAll(label, nicknameField, btn);
        return section;
    }

    private void handleRemove(TextField nicknameField) {
        String nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            showAlert("Erro", "Insira um nickname para remover!");
            return;
        }

        Player removed = tree.remove(nickname);
        if (removed != null) {
            highlightedNode = null;
            drawTree();
            updateTotalPlayers();
            nicknameField.clear();
            showAlert("✅ Sucesso", "Jogador '" + nickname + "' removido!");
        } else {
            showAlert("❌ Erro", "Jogador '" + nickname + "' não encontrado!");
        }
    }

    private VBox buildInOrderSection() {
        VBox section = new VBox(8);
        
        Label label = new Label("📋 Ordem In-Order");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        Button btn = new Button("Exibir no Console");
        btn.setStyle("-fx-padding: 6; -fx-font-size: 11;");
        btn.setPrefWidth(260);
        btn.setOnAction(e -> {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("PERCURSO IN-ORDER DA ÁRVORE");
            System.out.println("=".repeat(60));
            tree.inOrder();
            System.out.println("=".repeat(60) + "\n");
        });
        
        section.getChildren().addAll(label, btn);
        return section;
    }

    private void drawTree() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        if (tree.getRoot() != null) {
            drawNode(gc, tree.getRoot(), canvas.getWidth() / 2, 50, canvas.getWidth() / 3);
        } else {
            gc.setFont(Font.font(16));
            gc.setFill(Color.GRAY);
            gc.fillText("Árvore vazia. Carregue o CSV ou insira jogadores.", 50, canvas.getHeight() / 2);
        }
    }

    private void drawNode(GraphicsContext gc, Node node, double x, double y, double xOffset) {
        if (node == null) {
            return;
        }

        drawLines(gc, node, x, y, xOffset);
        drawNodeCircle(gc, node, x, y);
        drawNodeText(gc, node, x, y);

        drawNode(gc, node.getLeft(), x - xOffset, y + VERTICAL_SPACING, xOffset / 2);
        drawNode(gc, node.getRight(), x + xOffset, y + VERTICAL_SPACING, xOffset / 2);
    }

    private void drawLines(GraphicsContext gc, Node node, double x, double y, double xOffset) {
        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(2);

        if (node.getLeft() != null) {
            double leftX = x - xOffset;
            double leftY = y + VERTICAL_SPACING;
            gc.strokeLine(x, y, leftX, leftY);
        }

        if (node.getRight() != null) {
            double rightX = x + xOffset;
            double rightY = y + VERTICAL_SPACING;
            gc.strokeLine(x, y, rightX, rightY);
        }
    }

    private void drawNodeCircle(GraphicsContext gc, Node node, double x, double y) {
        boolean isHighlighted = (highlightedNode != null && highlightedNode == node);

        if (isHighlighted) {
            gc.setFill(Color.web("#ffeb3b"));
            double expandedRadius = NODE_RADIUS + 8;
            gc.fillOval(x - expandedRadius, y - expandedRadius, expandedRadius * 2, expandedRadius * 2);
            gc.setStroke(HIGHLIGHT_STROKE);
            gc.setLineWidth(3);
            gc.strokeOval(x - expandedRadius, y - expandedRadius, expandedRadius * 2, expandedRadius * 2);
        }

        Color fillColor = isHighlighted ? HIGHLIGHT_FILL : NODE_FILL;
        Color strokeColor = isHighlighted ? HIGHLIGHT_STROKE : NODE_STROKE;
        double lineWidth = isHighlighted ? 3 : 2.5;

        gc.setFill(fillColor);
        gc.fillOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        gc.setStroke(strokeColor);
        gc.setLineWidth(lineWidth);
        gc.strokeOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
    }

    private void drawNodeText(GraphicsContext gc, Node node, double x, double y) {
        boolean isHighlighted = (highlightedNode != null && highlightedNode == node);
        
        String displayText = node.getPlayer().toString();
        gc.setFont(Font.font("Arial", 10));
        gc.setFill(isHighlighted ? HIGHLIGHT_STROKE : NODE_STROKE);

        double textWidth = gc.getFont().getSize() * displayText.length() * 0.6;
        double textX = x - textWidth / 2;
        double textY = y + 4;

        gc.fillText(displayText, textX, textY);
    }

    private void updateTotalPlayers() {
        int count = countNodes(tree.getRoot());
        totalPlayersLabel.setText("Total de Jogadores: " + count);
    }

    private int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}