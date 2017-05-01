package com.mac.marsrogue.game.map.tile;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:19 PM.
 */
public class EmptyTile extends Tile{

    public EmptyTile(int id, char glyph, Color foregroundColor, Color backgroundColor) {
        super(id, glyph, foregroundColor, backgroundColor);
        this.solid = false;
        this.transparent = true;
    }
}
