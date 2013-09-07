import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] elementData;
    
    private int size;
   
    private final static int DEFAULT_SIZE = 4;
    
    private final static int EXPLODE_FACTOR = 2;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        elementData = new Object[DEFAULT_SIZE];
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // return the number of items on the queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        elementData[size] = item;
        ++size;
    }

    // delete and return a random item
    @SuppressWarnings("unchecked")
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int index = getRandomIndex();
        Item item = (Item)elementData[index];
        elementData[index] = elementData[size - 1];
        elementData[size - 1] = null;
        --size;
        
        shrinkCapacity();
        return item;
    }
    
    // return (but do not delete) a random item
    @SuppressWarnings("unchecked")
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int index = getRandomIndex();
        return (Item)elementData[index];
    }
    
    private void ensureCapacity() {
        if (size == elementData.length) {
            Object[] newElements = new Object[size * EXPLODE_FACTOR];
            for (int i = 0; i < size; ++i) {
                newElements[i] = elementData[i];
                elementData[i] = null;
            }
            elementData = newElements;
        }
    }
    
    private void shrinkCapacity() {
        if (size <= elementData.length / EXPLODE_FACTOR) {
            int newSize = size > DEFAULT_SIZE ? size : DEFAULT_SIZE;
            Object[] newElements = new Object[newSize];
            for(int i = 0; i < size; ++i) {
                newElements[i] = elementData[i];
            }
            elementData = newElements;
        }
    }
    
    private int getRandomIndex() {
        return StdRandom.uniform(size);
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RanQueueIterator();
    }

    private class RanQueueIterator implements Iterator<Item> {
        Object[] elements;
        int mySize;
        
        RanQueueIterator() {
           elements = new Object[size];
           mySize = size;
           for (int i = 0; i < size; ++i) {
               elements[i] = elementData[i];
           }
        }
        
        @Override
        public boolean hasNext() {
            return mySize != 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Item next() {
            if (mySize == 0) {
                throw new NoSuchElementException();
            }
            int index = StdRandom.uniform(mySize);
            Item item = (Item)elements[index];
            elements[index] = elements[mySize - 1];
            elements[mySize - 1] = null;
            --mySize;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
        int times = 640;
        while (times-- > 0) {
            d.enqueue(StdRandom.uniform(200));
        }
        while (times++ <= 638) {
            d.dequeue();
        }
        
        StdOut.println("");
    }
}