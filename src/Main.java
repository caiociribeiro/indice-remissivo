import res.BinarySearchTree;
import res.Hashtable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;

public class Main {

    public static String normalize(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[^a-zA-Z]", "").toLowerCase();

        return s;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String keywordsPath = "C:\\Users\\caio\\unifor\\ed\\indice-remissivo\\src\\files\\chaves.txt";
        String textPath = "C:\\Users\\caio\\unifor\\ed\\indice-remissivo\\src\\files\\texto.txt";
        String indicePath = "C:\\Users\\caio\\unifor\\ed\\indice-remissivo\\src\\files\\indice.txt";

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
        while(textScanner.hasNextLine()) {
           String[] line = textScanner.nextLine().split(" ");

           for (String s : line) {
               s = normalize(s);

               if (!s.isBlank()) {
                   indice.update(s, currentLine);
               }
           }

           currentLine++;
        }

        try (FileWriter writer = new FileWriter(indicePath)) {
            writer.write(indice.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
