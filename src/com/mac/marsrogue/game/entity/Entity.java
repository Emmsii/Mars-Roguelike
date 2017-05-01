package com.mac.marsrogue.game.entity;

import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.map.Map;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:48 PM.
 */
public abstract class Entity extends Drawable implements Cloneable{
    
    protected int id;
    
    protected Map map;
    public int x, y, z;
    
    protected String name;
    protected String description;
    
    protected Set<String> flags;
    
    public Entity(char glyph, Color foregroundColor, String name, String description) {
        this(glyph, foregroundColor, Colors.get("default_bg"), name, description);
    }

    public Entity(char glyph, Color foregroundColor, Color backgroundColor, String name, String description) {
        super(glyph, foregroundColor, backgroundColor);
        this.name = name;
        this.description = description;
        this.flags = new HashSet<String>();
    }
    
    public void init(Map map, int id){
        this.map = map;
        this.id = id;
    }
    
    public abstract void update();
    
    public int id(){
        return id;
    }
    
    public Map map(){
        return map;
    }
    
    public String name(){
        return name;
    }
    
    public String description(){
        return description;
    }
    
    public Set<String> flags(){
        return flags;
    }
    
    public boolean hasFlahs(String flag){
        return flag.contains(flag);
    }
    
    public boolean addFlag(String flag){
        return flags.add(flag);
    }
    
    public boolean removeFlag(String flag){
        return flags.remove(flag);
    }
    
    public void clearFlags(){
        flags.clear();
    }
    
    public Entity newInstance(){
        try {
            return (Entity) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
