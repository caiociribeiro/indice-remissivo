package res;

public class LinkedList {

    class Node {
        public int element;
        public Node next;
        public Node prev;

        public Node(int element) {
            this.element = element;
            this.next = null;
            this.prev = null;
        }
    }

    private Node first;
    private Node last;
    private int size;

    public LinkedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public int getLast() {
        if (isEmpty()) return -1;

        return this.last.element;
    }

    public void addLast(int element) {

        Node newNode = new Node(element);

        if (this.isEmpty()) {
            this.first = newNode;
        } else {
            this.last.next = newNode;
            newNode.prev = this.last;
        }

        this.last = newNode;

        this.size++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Node current = this.first;

        for (int i = 0; i < this.size; i++) {
            sb.append(current.element);
            if (i != this.size - 1) sb.append(" ");
            current = current.next;
        }

        return sb.toString();
    }
}