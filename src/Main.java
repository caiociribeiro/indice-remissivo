import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

import res.Hashtable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    private TextField keywordsPathField;
    private TextField textPathField;
    private TextField indicePathField;
    private TextArea indiceTextArea;
    private Text alertMessage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gerador de Índice Remissivo");

        GridPane root = new GridPane();
        root.setVgap(10);
        root.setHgap(10);
        root.setPadding(new Insets(10));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(63);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(17);
        root.getColumnConstraints().addAll(column1, column2, column3);

        keywordsPathField = new TextField();
        keywordsPathField.setPromptText("Selecione o arquivo de chaves");
        Button keywordsBrowseButton = new Button("Selecionar");
        keywordsBrowseButton.setOnAction(e -> selectFile(primaryStage, keywordsPathField));
        root.add(new Label("Chaves:"), 0, 0);
        root.add(keywordsPathField, 1, 0);
        root.add(keywordsBrowseButton, 2, 0);

        textPathField = new TextField();
        textPathField.setPromptText("Selecione o arquivo de texto");
        Button textBrowseButton = new Button("Selecionar");
        textBrowseButton.setOnAction(e -> selectFile(primaryStage, textPathField));
        root.add(new Label("Texto:"), 0, 1);
        root.add(textPathField, 1, 1);
        root.add(textBrowseButton, 2, 1);

        indicePathField = new TextField();
        indicePathField.setPromptText("Selecione onde salvar o índice");
        Button indiceBrowseButton = new Button("Selecionar");
        indiceBrowseButton.setOnAction(e -> saveFile(primaryStage, indicePathField));
        root.add(new Label("Salvar índice em:"), 0, 2);
        root.add(indicePathField, 1, 2);
        root.add(indiceBrowseButton, 2, 2);

        Button generateButton = new Button("Gerar Índice");
        generateButton.setOnAction(e -> handleGenerateIndice(primaryStage));
        root.add(generateButton, 1, 3, 2, 1);

        indiceTextArea = new TextArea();
        indiceTextArea.setEditable(false);
        root.add(indiceTextArea, 0, 4, 3, 1);

        alertMessage = new Text();
        root.add(alertMessage, 0, 5, 3, 1);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectFile(Stage stage, TextField targetField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            targetField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void saveFile(Stage stage, TextField targetField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            targetField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void handleGenerateIndice(Stage stage) {
        String keywordsPath = keywordsPathField.getText();
        String textPath = textPathField.getText();
        String indicePath = indicePathField.getText();

        if (keywordsPath.isEmpty() || textPath.isEmpty() || indicePath.isEmpty()) {
            alertMessage.setText("Por favor, preencha todos os campos.");
            alertMessage.setStyle("-fx-fill: red;");
            return;
        }

        try {
            String indiceContent = generateIndice(keywordsPath, textPath, indicePath);
            indiceTextArea.setText(indiceContent);

            alertMessage.setText("Índice-remissivo gerado com sucesso! Arquivo salvo em: " + indicePath);
            alertMessage.setStyle("-fx-fill: green;");
        } catch (Exception ex) {
            alertMessage.setText("Erro ao gerar o índice: " + ex.getMessage());
            alertMessage.setStyle("-fx-fill: red;");
        }
    }

    private String generateIndice(String keywordsPath, String textPath, String indicePath) throws FileNotFoundException {
        Scanner keywordsScanner = new Scanner(new File(keywordsPath));

        Hashtable indice = new Hashtable();

        while (keywordsScanner.hasNextLine()) {
            String[] line = keywordsScanner.nextLine().split(" ");

            for (String s : line) {
                s = normalize(s);
                indice.put(s);
            }
        }

        Scanner textScanner = new Scanner(new File(textPath));

        int currentLine = 1;
        while (textScanner.hasNextLine()) {
            String[] line = textScanner.nextLine().split(" ");

            for (String s : line) {
                s = normalize(s);

                if (!s.isEmpty()) {
                    indice.update(s, currentLine);
                }
            }

            currentLine++;
        }

        indiceTextArea.setText(indice.toString());

        try (FileWriter writer = new FileWriter(indicePath)) {
            writer.write(indice.toString());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o índice: " + e.getMessage());
        }

        return indice.toString();
    }

    private static String normalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.replaceAll("^[^\\p{L}]+|[^\\p{L}]+$", "");
    }
}