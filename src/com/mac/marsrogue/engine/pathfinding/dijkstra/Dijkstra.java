package com.mac.marsrogue.engine.pathfinding.dijkstra;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.map.Map;

import java.util.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:44 PM.
 */
public class Dijkstra {

    private final int FILL = Integer.MAX_VALUE;

    private Map map;
    private int width, height;
    private int[][] result;

    int maxValue;

    public Dijkstra(Map map, int maxValue) {
        this.map = map;
        this.width = map.width();
        this.height = map.height();
        this.maxValue = maxValue == 0 ? FILL : maxValue;
        this.result = new int[width][height];

        for(int y = 0; y < height; y++) for(int x = 0; x < width; x++) result[x][y] = FILL;
    }

    public int[][] generate(HashMap<Point, Integer> goals, int weightChange, float multiplier){
        for(int y = 0; y < height; y++) for(int x = 0; x < width; x++) result[x][y] = FILL;

        List<Point> open = new ArrayList<Point>();
        Set<Point> closed = new HashSet<Point>();

        for(Point p : goals.keySet()){
            if(map.solid(p.x, p.y, p.z)){
                Log.warn("Goal must be an empty tile: " + p + " Tile: " + map.tile(p.x, p.y, p.z));
                continue;
            }
            result[p.x][p.y] = (int) ((goals.get(p) - weightChange) * multiplier);
            open.add(p);
        }

        while(!open.isEmpty()){
            Point current = open.remove(0);

            int lowestNeighbour = FILL;

            List<Point> neighbours = current.neighboursAll();

            for(Point p : neighbours){
                if(map.solid(p.x, p.y, p.z)) continue;
                if(!map.inBounds(p.x, p.y, p.z)) continue;
                if(result[p.x][p.y] >= maxValue && result[p.x][p.y] != FILL) continue;
                if(result[p.x][p.y] < lowestNeighbour) lowestNeighbour = result[p.x][p.y];
                if(!open.contains(p) && !closed.contains(p)) open.add(p);
            }

            if(lowestNeighbour < result[current.x][current.y]) result[current.x][current.y] = (int) ((lowestNeighbour + 1) * multiplier);
            closed.add(current);
        }

        return result;
    }

    public int[][] generate(HashMap<Point, Integer> goals, float multiplier){
        return generate(goals, 0, multiplier);
    }

    public int[][] generate(Point goal, int cost, float multiplier){
        HashMap<Point, Integer> goals = new HashMap<Point, Integer>();
        goals.put(goal, cost);
        return generate(goals, multiplier);
    }

    public int[][] generate(Point goal, float multiplier){
        return generate(goal, 0, multiplier);
    }

    public static Point rollDown(Point start, int[][] input, int offset, int stopAt){

        if(start.x < 0 || start.y < 0 || start.x >= input.length || start.y >= input[0].length){
            Log.warn("Cannot roll down dijkstra map, input point out of bounds: " + start);
            return start;
        }

        if(input[start.x][start.y] + offset == stopAt) return start;
        else if(input[start.x][start.y] + offset < stopAt) return rollUp(start, input, offset, stopAt);

        Point pointCardinal = start;
        int lowestCardinal = Integer.MAX_VALUE;
        for(Point n : start.neighboursCardinal()){
            int in = input[n.x][n.y] + offset;
            if(in < lowestCardinal && in >= stopAt){
                lowestCardinal = in;
                pointCardinal = n;
            }
        }

        Point pointDiagonal = start;
        int lowestDiagonal = Integer.MAX_VALUE;
        for(Point n : start.neighboursDiagonal()){
            int in = input[n.x][n.y] + offset;
            if(in < lowestDiagonal && in >= stopAt){
                lowestDiagonal = in;
                pointDiagonal = n;
            }
        }

        if(lowestCardinal <= lowestDiagonal) return pointCardinal;
        else return pointDiagonal;
    }

    public static Point rollUp(Point start, int[][] input, int offset, int stopAt){
        if(start.x < 0 || start.y < 0 || start.x >= input.length || start.y >= input[0].length){
            Log.warn("Cannot roll down dijkstra map, input point out of bounds: " + start);
            return start;
        }

        if(input[start.x][start.y] + offset == stopAt) return start;
        else if(input[start.x][start.y] + offset > stopAt) return rollDown(start, input, offset, stopAt);

        Point pointCardinal = start;
        int highestCardinal= Integer.MIN_VALUE;
        for(Point n : start.neighboursCardinal()){
            int in = input[n.x][n.y] + offset;
            if(in > highestCardinal && in <= stopAt){
                highestCardinal = in;
                pointCardinal = n;
            }
        }

        Point pointDiagonal = start;
        int highestDiagonal = Integer.MIN_VALUE;
        for(Point n : start.neighboursDiagonal()){
            int in = input[n.x][n.y] + offset;
            if(in > highestDiagonal && in <= stopAt){
                highestDiagonal = in;
                pointDiagonal = n;
            }
        }

        if(highestCardinal >= highestDiagonal) return pointCardinal;
        else return pointDiagonal;
    }
}
