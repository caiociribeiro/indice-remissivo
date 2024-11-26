package res;
public class Normalizer {
    public static String normalize(String s) {
        if (s == null || s.isEmpty()) return "";

        s = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        s = s.replaceAll("[^\\p{ASCII}]", "");
        s = s.replaceAll("(?i)['s]\\b", "");
        s = s.toLowerCase();

        return s;
    }
}
