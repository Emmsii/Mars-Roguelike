package com.mac.marsrogue.game.entity.item;

import com.mac.marsrogue.game.entity.Entity;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:57 PM.
 */
public class Item extends Entity{
    
    private int thrownAttackValue;
    
    public Item(char glyph, Color foregroundColor, String name, String description) {
        super(glyph, foregroundColor, name, description);
        this.thrownAttackValue = 1;
    }

    @Override
    public void update() {
        
    }
    
    public int thrownAttackValue(){
        return thrownAttackValue;
    }
}
