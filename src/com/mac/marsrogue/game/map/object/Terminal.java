package com.mac.marsrogue.game.map.object;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 02/05/2017 at 03:51 PM.
 */
public abstract class Terminal extends MapObject{
    
    public Terminal(char glyph, Color color, String name, String description) {
        super(glyph, color, name, description);
    }

    public Terminal(char glyph, Color foregroundColor, Color backgroundColor, String name, String description) {
        super(glyph, foregroundColor, backgroundColor, name, description);
    }
}
