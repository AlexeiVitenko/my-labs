package by.bsuir.avdb;

import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {
    
    private int mSize;
    
    public LimitedQueue(int size){
        mSize = size;
    }
    
    public boolean offer(E e) {
        if (size() >= mSize) {
            poll();
        }
        return super.offer(e);
    };
    
    public int getLimit(){
        return mSize;
    }
}
