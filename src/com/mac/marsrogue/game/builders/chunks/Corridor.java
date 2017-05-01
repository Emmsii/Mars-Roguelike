package com.mac.marsrogue.game.builders.chunks;

import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.builders.MapBuilder;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.game.map.object.Door;
import com.mac.marsrogue.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:35 PM.
 */
public class Corridor extends Chunk{
    private final int width;
    private final float CORRIDOR_DOOR_FREQUENCY = 0.045f;

    public Corridor(int x, int y, int z, int width) {
        super(x, y, z);
        this.width = width;
    }

    @Override
    public void place(MapBuilder mapBuilder, Map map, int size, Random random) {
        int yp0 = 0;
        int yp1 = width;
        int xp0 = 0;
        int xp1 = width;

        if(mapBuilder.chunk(x, y - 1, z) instanceof Corridor) yp0 = -(size - width);
        if(mapBuilder.chunk(x - 1, y, z) instanceof Corridor) xp0 = -(size - width);

        for(int y = yp0; y < yp1; y++){
            int yp = this.y * size + y + (size / 2) - 1;
            for(int x = 0; x < width; x++){
                int xp = this.x * size + x + (size / 2) - 1;
                map.setTile(xp, yp, z, Tile.floorTile);
            }
        }

        for(int y = 0; y < width; y++){
            int yp = this.y * size + y + (size / 2) - 1;
            for(int x = xp0; x < xp1; x++){
                int xp = this.x * size + x + (size / 2) - 1;
                map.setTile(xp, yp, z, Tile.floorTile);
            }
        }

        if(random.nextFloat() < CORRIDOR_DOOR_FREQUENCY){

            for(int i = 0; i < width; i++){
                int xp = this.x * size + (size / 2);
                int yp = this.y * size + (size / 2);
                if(yp0 != 0){
                    xp -= 1 - i;
                    yp -= 2;
                    map.setTile(xp, yp, z, Tile.wallCenter);
                    if(i == width / 2){
                        //						Door linkedDoor = map.placeDoor(xp, yp, z);
                        map.setTile(xp, yp, z, Tile.floorTile);
                        new Door(Colors.get("door_fg"), "Door", "Its a door").place(map, new Point(xp, yp, z));
                        //						map.placeTerminal(xp - 1, yp - 1, z, linkedDoor);
                        //						map.placeTerminal(xp - 1, yp + 1, z, linkedDoor);
                    }

                }else if(xp0 != 0){
                    xp -= 2;
                    yp -= 1 - i;
                    map.setTile(xp, yp, z, Tile.wallCenter);
                    if(i == width / 2){
                        //						Door linkedDoor = map.placeDoor(xp, yp, z);
                        map.setTile(xp, yp, z, Tile.floorTile);
                        map.add(new Door(Colors.get("door_fg"), "Door", "Its a door"), new Point(xp, yp, z));
                        //						map.placeTerminal(xp - 1, yp - 1, z, linkedDoor);
                        //						map.placeTerminal(xp + 1, yp - 1, z, linkedDoor);	
                    }
                }
            }

        }

    }
    
}
