package com.mac.marsrogue.engine.pathfinding.astar;

import com.mac.marsrogue.engine.util.Maths.Point;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:42 PM.
 */
public class Node extends HeapItem<Node> {

    public boolean walkable;
    public Point pos;
    public int gCost;
    public int hCost;
    public Node parent;

    public Node(boolean walkable, Point pos) {
        this.walkable = walkable;
        this.pos = pos;
    }

    public int fCost(){
        return gCost + hCost;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        if(!pos.equals(other.pos)) return false;
        return true;
    }

    @Override
    public int hashCode(){
        return pos.hashCode();
    }

    @Override
    public int compareTo(Node o) {
        if(o.fCost() > this.fCost()) return 1;
        else if(o.fCost() < this.fCost()) return -1;
        else{
            if(o.hCost > this.hCost) return 1;
            else if(o.hCost < this.hCost) return -1;
        }
        return 0;
    }
}
