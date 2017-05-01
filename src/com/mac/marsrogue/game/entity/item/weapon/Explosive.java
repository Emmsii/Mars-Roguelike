package com.mac.marsrogue.game.entity.item.weapon;

import com.mac.marsrogue.game.entity.item.Item;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:01 PM.
 */
public class Explosive extends Item {
    
    private int power;
    private int range;

    private int armTime;
    private boolean armed;

    public Explosive(char glyph, Color color, String name, String description) {
        super(glyph, color, name, description);
        this.power = 22;
        this.range = 6;
        this.armTime = 3;
        this.armed = false;
    }

    @Override
    public void update() {
        if(!armed) return;
        armTime--;
        if(armTime <= 0) explode();
    }

    public void arm(){
        armed = true;
    }

    public void explode(){
        map.remove(this);
        //map.explosion(x, y, z, power, range);
        System.out.println("There was an explosion!");
    }
    
}
