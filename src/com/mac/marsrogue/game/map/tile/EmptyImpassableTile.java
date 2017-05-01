package com.mac.marsrogue.game.map.tile;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:20 PM.
 */
public class EmptyImpassableTile extends Tile{

    public EmptyImpassableTile(int id, char glyph, Color foregroundColor, Color backgroundColor) {
        super(id, glyph, foregroundColor, backgroundColor);
        this.solid = true;
        this.transparent = true;
    }
}
