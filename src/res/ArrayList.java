package res;

@SuppressWarnings("unchecked")
public class ArrayList<E extends Comparable<E>> {
    private E[] array;
    private int size;

    public ArrayList() {
        this.array = (E[]) new Comparable[10];
        this.size = 0;
    }

    public ArrayList(int capacity) {
        this.array = (E[]) new Comparable[capacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        return array[index];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void grow() {
        int newSize = array.length * 2;
        array = copyOf(array, newSize);
    }

    private E[] copyOf(E[] array, int size) {
        E[] newArray = (E[]) new Comparable[size];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
        }

        if (size == array.length) grow();

        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }

        this.array[index] = element;
        size++;
    }

    public void addFirst(E element) {
        add(0, element);
    }

    public void addLast(E element) {
        add(size, element);
    }

    public void addOrdered(E element) {
        if (size == array.length) grow();

        E e;
        int l = 0, r = size - 1;

        for (; l <= r; l++, r--) {
            e = get(l);
            if (element.compareTo(e) < 0) {
                add(l, element);
                return;
            }
            e = get(r);
            if (element.compareTo(e) > 0) {
                add(r + 1, element);
                return;
            }
        }

        add(l, element);
    }

    public E remove(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
        }

        E removed = get(index);
        for (int i = index; i < size; i++) {
            array[i] = array[i + 1];
        }

        array[size] = null;
        size--;

        return removed;
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        E removed = get(size - 1);

        array[size - 1] = null;
        size--;

        return removed;
    }

    public int indexOf(E element) {
        if (isEmpty()) return -1;

        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) return i;
        }
        return -1;
    }

    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }

    public boolean binarySearch(E element) {
        int left = 0;
        int right = size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            int comparison = get(mid).compareTo(element);

            if (comparison == 0) {
                return true;
            }

            if (comparison < 0) {
                left = mid + 1;
            }
            else {
                right = mid - 1;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i != size - 1) sb.append(" ");
        }
        return sb.toString();
    }
}