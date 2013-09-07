import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private LinkedNode first;
    private LinkedNode last;
    
    public Deque() {
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return size;
    }
    
    // insert the item at the front
    public void addFirst(Item item) {
        if (item == null) {
           throw new NullPointerException();
        }
        LinkedNode node = new LinkedNode(item, first, null);
        first = node;
        if (last == null) {
            last = node;
        }
        ++size; 
    }
    
    // insert the item at the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        LinkedNode node = new LinkedNode(item, null, last);
        last = node;
        if (first == null) {
            first = node;
        }
        ++size;
    }
    
    // delete and return the item at the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item item = first.data;
        if (first.next != null) {
            first = first.next;
            first.prev = null;
        }
        else {
            first = null;
            last = null;
        }
        --size;    
        return item;
    }
    
    // delete and return the item at the end
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item item = last.data;
        if (last.prev != null) {
            last = last.prev;
            last.next = null;
        }
        else {
            first = null;
            last = null;
        }
        --size;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
       return new DequeIterator();
    }
    
    private class LinkedNode {
        private Item data;
        private LinkedNode next;
        private LinkedNode prev;
        
        public LinkedNode(Item data) {
            this.data = data;
        }
        
        public LinkedNode(Item data, LinkedNode nextNode, LinkedNode lastNode) {
            this(data);
            this.next = nextNode;
            this.prev = lastNode;
            if (nextNode != null) {
                nextNode.prev = this;
            }
            if (lastNode != null) {
                lastNode.next = this;
            }
        }
    }
    
    private class DequeIterator implements Iterator<Item> {
        private LinkedNode current;
        
        DequeIterator() {
            current = first; 
        }
        
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.data;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported to remove in Deque iterator.");
        }
        
    }

}