package com.mac.marsrogue.game.map.object;

import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:35 PM.
 */
public class DoorTerminal extends Terminal{

    private Door door;
    
    public DoorTerminal(char glyph, Color color, Door door, String name, String description) {
        super(glyph, color, name, description);
        this.door = door;
    }

    public DoorTerminal(char glyph, Color foregroundColor, Color backgroundColor, Door door, String name, String description) {
        super(glyph, foregroundColor, backgroundColor, name, description);
        this.door = door;
    }
}
