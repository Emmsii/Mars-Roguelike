package com.mac.marsrogue.engine.pathfinding;

import com.mac.marsrogue.engine.pathfinding.astar.AStar;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.entity.creature.Creature;

import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:39 PM.
 */
public class Path {

    private List<Point> points;

    public Path(Creature creature, int x, int y) {
        points = AStar.instance().findPath(new Point(creature.x, creature.y, creature.z), new Point(x, y, creature.z));
    }

    public boolean hasNext(){
        return points.size() > 0;
    }

    public Point getNext(){
        Point p = points.get(0);
        points.remove(0);
        return p;
    }

    public List<Point> points(){
        return points;
    }

    public int length(){
        if(points == null) return 0;
        return points.size();
    }
}
