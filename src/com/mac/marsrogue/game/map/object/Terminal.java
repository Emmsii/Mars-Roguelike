package com.mac.marsrogue.game.map.object;

import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:35 PM.
 */
public class Terminal extends MapObject{

    private Door door;

    public Terminal(char glyph, Color foregroundColor, Door door, String name, String description) {
        this(glyph, foregroundColor, Colors.get("default_bg"), door, name, description);
    }

    public Terminal(char glyph, Color foregroundColor, Color backgroundColor, Door door, String name, String description) {
        super(glyph, foregroundColor, backgroundColor, name, description);
        this.door = door;
    }

    public Door door(){
        return door;
    }
}
