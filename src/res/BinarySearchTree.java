package res;

public class BinarySearchTree {
    private Keyword root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private Keyword findMin(Keyword Keyword) {
        while (Keyword.left != null) {
            Keyword = Keyword.left;
        }

        return Keyword;
    }

    private Keyword findMax(Keyword keyword) {
        while (keyword.right != null) {
            keyword = keyword.right;
        }

        return keyword;
    }

    public void insert(String word) {
        size--;
        insert(word, root);
    }

    private void insert(String word, Keyword keyword) {
        Keyword newKeyword = new Keyword(word);

        if (root == null) {
            root = newKeyword;
            return;
        }

        if (Normalizer.normalize(word).compareTo(Normalizer.normalize(keyword.word)) < 0) {
            if (keyword.left == null) {
                keyword.left = newKeyword;
                return;
            }

            insert(word, keyword.left);
        }

        if (Normalizer.normalize(word).compareTo(Normalizer.normalize(keyword.word)) > 0) {
            if (keyword.right == null) {
                keyword.right = newKeyword;
                return;
            }

            insert(word, keyword.right);
        }

    }

    public void updateIfExists(String word, int line) {
        updateIfExists(word, line, root);
    }

    private void updateIfExists(String word, int line, Keyword keyword) {
        if (keyword == null) {
            return;
        }

        if (Normalizer.normalize(word).equals(Normalizer.normalize(keyword.word))) {
            keyword.addOccurrence(line);
            return;
        }

        if (Normalizer.normalize(word).compareTo(Normalizer.normalize(keyword.word)) < 0) {
            if (keyword.left == null) {
                return;
            }
            updateIfExists(word, line, keyword.left);
        } else if (Normalizer.normalize(word).compareTo(Normalizer.normalize(keyword.word)) > 0) {
            if (keyword.right == null) {
                return;
            }
            updateIfExists(word, line, keyword.right);
        }

    }


    @Override
    public String toString() {
        return inOrderToString(new StringBuilder(), root).toString();
    }

    private StringBuilder inOrderToString(StringBuilder sb, Keyword keyword) {
        if (keyword == null) {
            return sb;
        }

        sb = inOrderToString(sb, keyword.left);
        sb.append(keyword).append('\n');
        sb = inOrderToString(sb, keyword.right);

        return sb;
    }

    private static class Keyword {
        String word;
        ArrayList<Integer> occurrences;
        Keyword left, right;

        Keyword(String word) {
            this.word = word;
            occurrences = new ArrayList<>();
            left = right = null;
        }

        public void addOccurrence(int occurrence) {
            if (!occurrences.binarySearch(occurrence)) {
                occurrences.addOrdered(occurrence);
            }
        }

        @Override
        public String toString() {
            return word + " " + occurrences;
        }
    }
}
