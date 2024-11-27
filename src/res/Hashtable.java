package res;

public class Hashtable {
    private BinarySearchTree[] table;
    private int size = 0;

    public Hashtable() {
        table = new BinarySearchTree[26];
        for (int i = 0; i < table.length; i++) {
            table[i] = new BinarySearchTree();
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int getIndex(String word) {
        word = Normalizer.normalize(word);

        if (word.isEmpty()) return -1;

        return word.charAt(0) - 'a';
    }

    public void put(String word) {
        int index = getIndex(word);

        if (index == -1) {
            return;
        }

        table[index].insert(word);

        size++;
    }

    public void update(String word, int line) {
        int index = getIndex(word);

        table[index].updateIfExists(word, line);
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (BinarySearchTree bst : table) {
            if (!bst.isEmpty()) {
                sb.append(bst);
            }
        }

        return sb.toString();
    }
}

