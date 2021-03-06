package com.mac.marsrogue.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:52 PM.
 */
public class Pool<T> {

    private List<PoolItem<T>> poolItems;
    private final Random random;
    private int totalWeight;

    public Pool() {
        this(new Random());
    }

    public Pool(Random random){
        this.poolItems = new ArrayList<PoolItem<T>>();
        this.random = random;
    }

    public T get(){
        int runningWeight = 0;
        int roll = random.nextInt(totalWeight - 1) + 1;

        for(PoolItem<T> poolItem : poolItems){
            runningWeight += poolItem.weight;
            if(roll <= runningWeight){
                remove(poolItem);
                return poolItem.item;
            }
        }

        throw new RuntimeException("Cannot get item from pool.");
    }

    public void add(T item, int weight){
        poolItems.add(new PoolItem<T>(weight, item));
        totalWeight += weight;
    }

    public void remove(PoolItem<T> poolItem){
        poolItems.remove(poolItem);
        totalWeight -= poolItem.weight;
    }

    public int size(){
        return poolItems.size();
    }

    public boolean isEmpty(){
        return poolItems.isEmpty();
    }

    @SuppressWarnings("hiding")
    class PoolItem<T>{
        public int weight;
        public T item;

        public PoolItem(int weight, T item){
            this.weight = weight;
            this.item = item;
        }
    }
}
