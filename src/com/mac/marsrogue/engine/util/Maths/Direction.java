package com.mac.marsrogue.engine.util.Maths;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:17 PM.
 */
public enum Direction {

    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), UP_LEFT(-1, -1), UP_RIGHT(1, -1), DOWN_LEFT(-1, 1), DOWN_RIGHT(1, 1), NONE(0, 0);

    public static final Direction[] CARDINALS = {UP, DOWN, LEFT, RIGHT};
    public static final Direction[] DIAGONALS = {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    public static final Direction[] ALL = {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};

    public final int x, y;

    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Direction opposite(Direction dir){
        for(Direction d : ALL) if(dir.x * -1 == d.x && dir.y * -1 == d.y) return d;
        return null;
    }    
}
