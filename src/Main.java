import res.Hashtable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static String normalize(String s) {
        if (s == null || s.isEmpty()) return "";

        return s.replaceAll("^[^\\p{L}]+|[^\\p{L}]+$", "");
    }

    public static void main(String[] args) throws FileNotFoundException {
        String keywordsPath = "C:\\Users\\alunok10\\Desktop\\indice-remissivo-main\\src\\files\\chaves.txt";
        String textPath = "C:\\Users\\alunok10\\Desktop\\indice-remissivo-main\\src\\files\\texto.txt";
        String indicePath = "C:\\Users\\alunok10\\Desktop\\indice-remissivo-main\\src\\files\\indice.txt";

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

               if (!s.isEmpty()) {
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
