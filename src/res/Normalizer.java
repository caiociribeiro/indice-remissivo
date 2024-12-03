package res;

public class Normalizer {
    public static String normalize(String s) {
        // evitar nullPointer
        if (s == null || s.isEmpty()) return "";

        // divide letras com acento em letra + acento (á -> a +  ́)
        s = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);

        // remove todos os caracteres nao ASCII (no caso, os acentos)
        s = s.replaceAll("[^\\p{ASCII}]", "");

        // converte tudo para minusculo
        s = s.toLowerCase();

        return s;
    }
}
