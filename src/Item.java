
public class Item<G,P extends Comparable<P>> implements Comparable<Item>{
    // store element
    G value;
    P priority;

    /**
     * create new
     * @param value
     * @param priority
     */
    public Item(G value, P priority){
        this.value = value;
        this.priority = priority;
    }

    /**
     * compare 2 items
     * @param
     * @return
     */
    @Override
    public int compareTo(Item o) {
        return this.priority.compareTo((P) o.priority);
    }
}
