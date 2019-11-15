
import java.util.Arrays;

public class PriorityQueue<G,P extends Comparable<P>>
{
    //data for store
    private Item[] data;
    private int size;
    private static final int capacity = 10;

    /**
     * create new priority Queue
     */
    public PriorityQueue () {
        data = new Item[capacity];
        size = 0;
    }

    /**
     * add new element
     * @param value
     * @param priority
     */
    public void add(G value, P priority) {
        if (size >= data.length - 1) {
            data = this.resize();
        }
        size++;
        int index = size;
        data[index] = new Item(value,priority);
        upNode();
    }

    /**
     *
     * @return true if queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *
     * @return the greatest element
     */
    public G peek() {
        if (this.isEmpty()) {
            return null;
        }
        return (G) data[1].value;
    }

    /**
     * remove and return
     * @return the greatest element
     */
    public G remove() {
        G result = peek();
        data[1] = data[size];
        data[size] = null;
        size--;
        downNode();
        return result;
    }

    /**
     * move node down
     */
    protected void downNode() {
        int index = 1;
        while (indexLeft(index) <= size) {
            int smallerChild = indexLeft(index);

            if (rightIndex(index) <= size
                    && data[indexLeft(index)].priority.compareTo(data[rightIndex(index)].priority) > 0) {
                smallerChild = rightIndex(index);
            }

            if (data[index].priority.compareTo(data[smallerChild].priority) > 0) {
                swap(index, smallerChild);
            } else {
                break;
            }
            index = smallerChild;
        }
    }

    /**
     * move node up
     */
    protected void upNode() {
        int index = this.size;

        while (index > 1&& (data[parentIndex(index)].priority.compareTo(data[index].priority) > 0)) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }
    /**
     *
     * @return index left node
     */
    protected int indexLeft(int i) {return i * 2;}

    /**
     *
     * @return index right node
     */
    protected int rightIndex(int i) {
        return i * 2 + 1;
    }
    /**
     *
     * @return index parent node
     */
    protected int parentIndex(int i) {
        return i / 2;
    }

    /**
     *
     * @return new array for store heap
     */
    protected Item[] resize() {
        return Arrays.copyOf(data, data.length * 2);
    }
    /**
     * swap 2 node
     * @param index1
     * @param index2
     */
    protected void swap(int index1, int index2) {
        Item tmp = data[index1];
        data[index1] = data[index2];
        data[index2] = tmp;
    }
}