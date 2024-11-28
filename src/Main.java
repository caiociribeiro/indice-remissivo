import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.stage.DirectoryChooser;
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
    private TextArea indiceTextArea;
    private Text alertMessage;
    private File lastDirectory = null;

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
        column1.setPercentWidth(10);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(100);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(17);
        root.getColumnConstraints().addAll(column1, column2, column3);

        keywordsPathField = new TextField();
        keywordsPathField.setPromptText("Selecione o arquivo de chaves");
        keywordsPathField.setEditable(false);
        Button keywordsBrowseButton = new Button("Selecionar");
        keywordsBrowseButton.setOnAction(e -> selectFile(primaryStage, keywordsPathField));
        root.add(new Label("Chaves:"), 0, 0);
        root.add(keywordsPathField, 1, 0);
        root.add(keywordsBrowseButton, 2, 0);

        textPathField = new TextField();
        textPathField.setPromptText("Selecione o arquivo de texto");
        textPathField.setEditable(false);
        Button textBrowseButton = new Button("Selecionar");
        textBrowseButton.setOnAction(e -> selectFile(primaryStage, textPathField));
        root.add(new Label("Texto:"), 0, 1);
        root.add(textPathField, 1, 1);
        root.add(textBrowseButton, 2, 1);

        GridPane.setHalignment(textBrowseButton, HPos.RIGHT);
        GridPane.setHalignment(keywordsBrowseButton, HPos.RIGHT);

        Button generateButton = new Button("Gerar Índice");
        generateButton.setOnAction(e -> handleGenerateIndice(primaryStage));
        root.add(generateButton, 0, 3, 2, 1);
        GridPane.setHalignment(generateButton, HPos.LEFT);

        indiceTextArea = new TextArea();
        indiceTextArea.setEditable(false);
        indiceTextArea.setPrefHeight(300);
        root.add(indiceTextArea, 0, 4, 3, 1);

        alertMessage = new Text();
        root.add(alertMessage, 0, 5, 3, 1);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectFile(Stage stage, TextField targetField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");

        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            targetField.setText(selectedFile.getAbsolutePath());
            lastDirectory = selectedFile.getParentFile();
        }
    }

    private String selectFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo");

        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        File directory = lastDirectory != null ? lastDirectory : new File(System.getProperty("user.home"));
        String suggestedFileName = getUniqueFileName(directory);

        fileChooser.setInitialFileName(suggestedFileName);

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            lastDirectory = file.getParentFile();
            return file.getAbsolutePath();
        }

        return null;
    }

    private String getUniqueFileName(File directory) {
        String base = "indice";
        int c = 1;
        String fileName = base;

        File file = new File(directory, fileName + ".txt");
        while (file.exists()) {
            fileName = base + " (" + c + ")";
            file = new File(directory, fileName + ".txt");
            c++;
        }

        return fileName;
    }

    private void handleGenerateIndice(Stage stage) {
        String indicePath = selectFile(stage);

        if (indicePath == null) return;

        String keywordsPath = keywordsPathField.getText();
        String textPath = textPathField.getText();


        if (keywordsPath.isEmpty() || textPath.isEmpty() || indicePath.isEmpty()) {
            alertMessage.setText("Preencha todos os campos.");
            alertMessage.setStyle("-fx-fill: red;");
            return;
        }

        try {
            String indiceContent = generateIndice(keywordsPath, textPath, indicePath);
            indiceTextArea.setText(indiceContent);

            alertMessage.setText("Índice-remissivo gerado com sucesso! Arquivo salvo.");
            alertMessage.setStyle("-fx-fill: green;");

            File file = new File(indicePath);
            if (file.exists()) {
                new ProcessBuilder("explorer.exe", "/select,", file.getAbsolutePath()).start();
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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