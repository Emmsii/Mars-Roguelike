package com.mac.marsrogue.game.map.object;

import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.game.map.tile.Tile;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:48 PM.
 */
public class Door extends MapObject{

    private boolean open;
    private boolean locked;

    public Door(Color color, String name, String description) {
        super('+', color, name, description);
        this.open = false;
        this.locked = false;
    }

    @Override
    public void update(){
        boolean creatureNearby = false;

        if(open){
            boolean upDown = false;
            if(Tile.isWall(map.tile(x - 1, y, z).id) && Tile.isWall(map.tile(x + 1, y, z).id)) upDown = true;
            else if(Tile.isWall(map.tile(x, y - 1, z).id) && Tile.isWall(map.tile(x, y + 1, z).id)) upDown = false;

            if(upDown){
                if(map.creature(x, y - 1, z) != null || map.creature(x, y + 1, z) != null) creatureNearby = true;
            }else{
                if(map.creature(x - 1, y, z) != null || map.creature(x + 1, y, z) != null) creatureNearby = true;
            }

            if(map.creature(x, y, z) != null) creatureNearby = true;

            if(!creatureNearby){
                //				Item i = map.item(x, y, z);
                //				if(i != null){
                //					map.remove(i);
                //					//notify creatures in range item has been crushed by door
                //				}
                setOpen(false);
            }
        }

    }

    @Override
    public void place(Map map, Point spawn) {
        if(!map.inBounds(spawn.x, spawn.y, spawn.z)) return;
        char glyph = '+';
        //		if(Tile.isWall(map.tile(spawn.x, spawn.y - 1, spawn.z).id)) glyph = (char) 179;
        //		if(Tile.isWall(map.tile(spawn.x - 1, spawn.y, spawn.z).id)) glyph = (char) 196;
        this.glyph = glyph;
        map.add(this, spawn);
    }

    @Override
    public char glyph() {
        if(open) return ' ';
        else return super.glyph();
    }

    @Override
    public Color foregroundColor() {
        if(open) return map.tile(x, y, z).foregroundColor;
        else return super.foregroundColor();
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public boolean locked(){
        return locked;
    }

    public void setOpen(boolean open){
        this.open = open;
    }

    public boolean isOpen(){
        return open;
    }
}
