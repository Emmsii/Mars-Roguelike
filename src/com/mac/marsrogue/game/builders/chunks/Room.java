package com.mac.marsrogue.game.builders.chunks;

import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.builders.MapBuilder;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.game.map.object.Door;
import com.mac.marsrogue.game.map.object.DoorTerminal;
import com.mac.marsrogue.game.map.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:35 PM.
 */
public class Room extends Chunk{

    private final int CORRIDOR_WIDTH = 3;
    private final float WINDOW_CHANCE = 0.25f;
    private final float CHANCE_TO_LOCK = 0.25f;

    private List<Point> points;

    public Room(int x, int y, int z) {
        super(x, y, z);
        this.points = new ArrayList<Point>();
    }

    public Point getRandomPointInRoom(){
        return points.get((int) Math.random() * points.size());
    }

    @Override
    public void place(MapBuilder mapBuilder, Map map, int size, Random random) {

        int yp0 = 1;
        int yp1 = size;
        int xp0 = 1;
        int xp1 = size;

        if(mapBuilder.chunk(x, y - 1, z) instanceof Room) yp0 = -1;
        if(mapBuilder.chunk(x - 1, y, z) instanceof Room) xp0 = -1;

        for(int y = yp0; y < yp1; y++){
            int yp = this.y * size + y ;
            for(int x = 1; x < size; x++){
                int xp = this.x * size + x ;
                map.setTile(xp, yp, z, Tile.floorTile);
            }
        }

        for(int y = 1; y < size; y++){
            int yp = this.y * size + y ;
            for(int x = xp0; x < xp1; x++){
                int xp = this.x * size + x;
                map.setTile(xp, yp, z, Tile.floorTile);
            }
        }

        boolean emptyN = mapBuilder.chunk(x, y - 1, z) == null && random.nextFloat() <= WINDOW_CHANCE;
        boolean emptyS = mapBuilder.chunk(x, y + 1, z) == null && random.nextFloat() <= WINDOW_CHANCE;
        boolean emptyE = mapBuilder.chunk(x + 1, y, z) == null && random.nextFloat() <= WINDOW_CHANCE;
        boolean emptyW = mapBuilder.chunk(x - 1, y, z) == null && random.nextFloat() <= WINDOW_CHANCE;

        if(z == 0){
            for(int y = 1; y <= size; y++){
                for(int x = 1; x <= size; x++){
                    if(y == 0 && x > 2 && x < size - 2 && emptyN) map.setTile(x + this.x * size, y + this.y * size, z, Tile.glassVert);
                    if(y == size && x > 2 && x < size - 2 && emptyS) map.setTile(x + this.x * size, y + this.y * size, z, Tile.glassVert);
                    if(x == 0 && y > 2 && y < size - 2 && emptyW) map.setTile(x + this.x * size, y + this.y * size, z, Tile.glassHori);
                    if(x == size && y > 2 && y < size - 2 && emptyE) map.setTile(x + this.x * size, y + this.y * size, z, Tile.glassHori);
                }
            }
        }

    }

    public void connectToCorridor(MapBuilder mapBuilder, Map map, Point corridor, int size, Random random){
        int yp0 = 0;
        int yp1 = CORRIDOR_WIDTH;
        int xp0 = 0;
        int xp1 = CORRIDOR_WIDTH;

        if(corridor.y < y) yp0 = -(size - CORRIDOR_WIDTH);
        if(corridor.y > y) yp1 = (size + CORRIDOR_WIDTH);
        if(corridor.x < x) xp0 = -(size - CORRIDOR_WIDTH);
        if(corridor.x > x) xp1 = (size + CORRIDOR_WIDTH);

        for(int y = yp0; y < yp1; y++){
            int yp = this.y * size + y + (size / 2) - 1;
            for(int x = 0; x < CORRIDOR_WIDTH; x++){
                int xp = this.x * size + x + (size / 2) - 1;
                map.setTile(xp, yp, z, Tile.floorTile);
            }
        }

        for(int y = 0; y < CORRIDOR_WIDTH; y++){
            int yp = this.y * size + y + (size / 2) - 1;
            for(int x = xp0; x < xp1; x++){
                int xp = this.x * size + x + (size / 2) - 1;
                map.setTile(xp, yp, z, Tile.floorTile);
            }
        }

        for(int i = 0; i < CORRIDOR_WIDTH; i++){
            int xp = this.x * size + (size / 2);
            int yp = this.y * size + (size / 2);
            if(yp0 != 0){
                xp -= 1 - i;
                yp -= 5;
            }else if(yp1 != CORRIDOR_WIDTH){
                xp -= 1 - i;
                yp += 5;
            }else if(xp0 != 0){
                xp -= 5;
                yp -= 1 - i;
            }else if(xp1 != CORRIDOR_WIDTH){
                xp += 5;
                yp -= 1 - i;
            }

            map.setTile(xp, yp, z, Tile.wallCenter);
            if(i == CORRIDOR_WIDTH / 2){
                map.setTile(xp, yp, z, Tile.floorTile);
                Door linkedDoor = new Door(Colors.get("door_fg"), "Door", "Its a door");
                //				map.add(linkedDoor, new Point(xp, yp, z));
                linkedDoor.place(map, new Point(xp, yp, z));

                if(random.nextFloat() <= CHANCE_TO_LOCK){
                    linkedDoor.setLocked(true);
                    int cx = xp;
                    int cy = yp;
                    if(xp0 != 0) cx += 1;
                    else cx -= 1;
                    if(yp0 != 0) cy += 1;
                    else cy -= 1;

                    //					map.placeTerminal(cx, cy, z, linkedDoor);
                    //					map.add(new DoorTerminal((char) 241, new Color(76, 140, 83), linkedDoor, "DoorTerminal", "Used to lock and unlock doors"), new Point(cx, cy, z));
                    new DoorTerminal((char) 241, Colors.get("terminal_fg"), linkedDoor, "DoorTerminal", "Used to lock and unlock doors").place(map, new Point(cx, cy, z));

                    if(xp0 != 0) cx -= 2;
                    else cx += 2;
                    if(yp0 != 0) cy -= 2;
                    else cy += 2;

                    //					map.placeTerminal(cx, cy, z, linkedDoor);
                    //					map.add(new DoorTerminal((char) 241, new Color(76, 140, 83), linkedDoor, "DoorTerminal", "Used to lock and unlock doors"), new Point(cx, cy, z));
                    new DoorTerminal((char) 241, Colors.get("terminal_fg"), linkedDoor, "DoorTerminal", "Used to lock and unlock doors").place(map, new Point(cx, cy, z));
                }
            }
        }


    }

    public List<Point> points(){
        return points;
    }
}
