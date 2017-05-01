package com.mac.marsrogue.game.codex;

import com.esotericsoftware.minlog.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:07 PM.
 */
public class CodexItem<T> {

    private Map<String, T> items = new HashMap<String, T>();

    protected String name;
    protected T invalidResult;

    public CodexItem(String name){
        this(name, null);
    }

    public CodexItem(String name, T invalidResult){
        this.name = name;
        this.invalidResult = invalidResult;
    }

    public T get(String name){
        if(!items.containsKey(name.toLowerCase().trim())){
            Log.warn("Unknown key: " + name + " for " + this.name);
            return invalidResult;
        }else return items.get(name.toLowerCase().trim());
    }

    public void put(String name, T item){
        if(items.containsKey(name.toLowerCase().trim())) return;
        items.put(name.toLowerCase().trim(), item);
    }

    public boolean has(String name){
        return items.containsKey(name.toLowerCase().trim());
    }

    public int count(){
        return items.size();
    }
}
