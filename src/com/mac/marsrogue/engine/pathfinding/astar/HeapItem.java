package com.mac.marsrogue.engine.pathfinding.astar;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:42 PM.
 */
public abstract class HeapItem<T> implements Comparable<T> {
    
    public int heapIndex;

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof HeapItem<?>)) return false;
        HeapItem<T> other = (HeapItem<T>) obj;
        if(other.heapIndex != heapIndex) return false;
        return true;
    }
}
