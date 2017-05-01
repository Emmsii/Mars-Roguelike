package com.mac.marsrogue.game.entity.item.blueprint;

import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.item.Item;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:53 PM.
 */
public abstract class Blueprint extends Item{
    
    protected Item item;
    
    public Blueprint(String name, String description, Item item) {
        super((char) 19, Colors.get("blueprint"), name, description);
        this.item = item;
    }
    
    public abstract void render(AsciiPanel panel, int x, int y, Blueprint compareTo);
    
    public Item get(){
        return (Item) item.newInstance();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof Blueprint)) return false;
        Blueprint other = (Blueprint) obj;
        if(item.id() != other.item.id()) return false;
        return true;
    }
}
