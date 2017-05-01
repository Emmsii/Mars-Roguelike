package com.mac.marsrogue.engine.pathfinding.astar;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.map.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:39 PM.
 */
public class AStar {
    private static AStar instance = null;

    private Map map;
    private boolean init = false;

    protected AStar(){

    }

    public void init(Map map){
        this.map = map;
        init = true;
        Log.debug("Pathfinder initialized.");
    }

    public List<Point> findPath(Point startPos, Point endPos){
        if(!init){
            Log.error("Pathfinder not initialized.");
            System.exit(-1);
        }

        if(map.solid(startPos.x, startPos.y, startPos.z) || map.solid(endPos.x, endPos.y, endPos.z)){
            Log.warn("Cannot find path, start and end tiles bust be empty.");
            return null;
        }

        Node startNode = new Node(true, startPos);
        Node targetNode = new Node(true, endPos);

        Heap<Node> openSet = new Heap<Node>(map.width() * map.height());
        HashSet<Node> closedSet = new HashSet<Node>();
        openSet.add(startNode);
        int tries = 0;
        while(openSet.size() > 0 && tries++ < map.width() * map.height()){
            Node node = openSet.remove();
            closedSet.add(node);

            if(node.pos.equals(targetNode.pos)) return getPath(startNode, node);

            for(Node neighbour : getNeighbours(node)) {
                if(!neighbour.walkable || closedSet.contains(neighbour)) continue;

                if(map.creature(neighbour.pos.x, neighbour.pos.y, neighbour.pos.z) != null){
                    if(!neighbour.pos.equals(targetNode.pos)) continue;
                }

                int newCostToNeighbour = node.gCost + distance(node, neighbour);

                Creature c = map.creature(neighbour.pos.x, neighbour.pos.y, neighbour.pos.z);
                if(c != null) if(!c.isPlayer()) newCostToNeighbour += (c.timeStationary() * 2) + 1;

                if(newCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)){
                    neighbour.gCost = newCostToNeighbour;
                    neighbour.hCost = distance(neighbour, targetNode);
                    neighbour.parent = node;
                    if(!openSet.contains(neighbour)) openSet.add(neighbour);
                }
            }
        }

        List<Point> result = new ArrayList<Point>();
        result.add(startPos);
        return result;
    }

    public List<Point> getPath(Node startNode, Node endNode){
        List<Node> path = new ArrayList<Node>();
        List<Point> result = new ArrayList<Point>();

        Node currentNode = endNode;

        while(!currentNode.pos.equals(startNode.pos)){
            path.add(currentNode);
            result.add(currentNode.pos);
            currentNode = currentNode.parent;
        }

        Collections.reverse(result);
        return result;
    }

    private int distance(Node a, Node b){
        int dstX = Math.abs(a.pos.x - b.pos.x);
        int dstY = Math.abs(a.pos.y - b.pos.y);
        if (dstX > dstY) return 14 * dstY + 10 * (dstX - dstY);
        return 14 * dstX + 10 * (dstY - dstX);
    }

    private List<Node> getNeighbours(Node from){
        List<Node> result = new ArrayList<Node>();
        int z = from.pos.z;
        for(int y = -1; y <= 1; y++){
            for(int x = -1; x <= 1; x++){
                if(x == 0 && y == 0) continue;

                int checkX = from.pos.x + x;
                int checkY = from.pos.y + y;

                if(map.inBounds(checkX, checkY, z) && !map.solid(checkX, checkY, z)){
                    result.add(new Node(!map.solid(checkX, checkY, z), new Point(checkX, checkY, z)));
                }
            }
        }
        return result;
    }

    public static AStar instance(){
        if(instance == null) instance = new AStar();
        return instance;
    }
    
}
