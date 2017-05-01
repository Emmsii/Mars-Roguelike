package com.mac.marsrogue.game.entity.item;

import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:03 PM.
 */
public enum Rarity {
    
    COMMON(1, 100, Colors.get("common")), UNCOMMON(1.25f, 30, Colors.get("uncommon")), RARE(1.75f, 10, Colors.get("rare")), VERY_RARE(2f, 2, Colors.get("very_rare"));

    public float multiplier;
    public int value;
    public Color color;

    Rarity(float multiplier, int value, Color color){
        this.multiplier = multiplier;
        this.value = value;
        this.color = color;
    }
    
}
