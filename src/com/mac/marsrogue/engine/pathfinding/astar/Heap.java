package com.mac.marsrogue.engine.pathfinding.astar;

import java.util.Arrays;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:43 PM.
 */
public class Heap<T extends HeapItem<T>> {

    protected T[] items;
    protected int count;

    @SuppressWarnings("unchecked")
    public Heap(int maxHeapSize){
        items = (T[]) new HeapItem[maxHeapSize];
    }

    public void add(T item) {
        if(size() >= items.length - 1) items = this.resize();
        item.heapIndex = count;
        items[count] = item;
        sortUp(item);
        count++;
    }

    public T remove() {
        T firstItem = items[0];
        count--;
        items[0] = items[count];
        items[0].heapIndex = 0;
        sortDown(items[0]);
        return firstItem;
    }

    public boolean isEmpty(){
        return count == 0;
    }

    public void updateItem(T item) {
        sortUp(item);
    }

    public int size(){
        return count;
    }

    public boolean contains(T item) {
        return items[item.heapIndex].equals(item);
    }

    protected T[] resize(){
        return Arrays.copyOf(items, items.length * 2);
    }

    protected void sortDown(T item) {
        while (true) {
            int childIndexLeft = item.heapIndex * 2 + 1;
            int childIndexRight = item.heapIndex * 2 + 2;
            int swapIndex = 0;

            if(childIndexLeft < count) {
                swapIndex = childIndexLeft;

                if(childIndexRight < count) if(items[childIndexLeft].compareTo(items[childIndexRight]) < 0) swapIndex = childIndexRight;

                if (item.compareTo(items[swapIndex]) < 0) swap(item,items[swapIndex]);
                else return;

            }else return;
        }
    }

    protected void sortUp(T item) {
        int parentIndex = (item.heapIndex - 1) / 2;

        while(true) {
            T parentItem = items[parentIndex];
            if (item.compareTo(parentItem) > 0) swap(item,parentItem);
            else break;
            parentIndex = (item.heapIndex - 1) / 2;
        }
    }

    protected void swap(T itemA, T itemB) {
        items[itemA.heapIndex] = itemB;
        items[itemB.heapIndex] = itemA;
        int itemAIndex = itemA.heapIndex;
        itemA.heapIndex = itemB.heapIndex;
        itemB.heapIndex = itemAIndex;
    }
}
