package com.mac.marsrogue.game.map;

import com.mac.marsrogue.engine.pathfinding.dijkstra.Dijkstra;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.entity.creature.Creature;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 11:40 AM.
 */
public class DijkstraMaps {
    
//    private Dijkstra dijkstra;
    
    private int[][] approach;
    private int[][] flee;
    
    private Map map;
    
    public DijkstraMaps(Map map){
        this.map = map;
    }
    
    public void updateApproach(Creature player){
        approach = Dijkstra.calculate(player.position(), 0, 1, null, map);

    }
    
    public void updateFlee(Creature player){
        flee = Dijkstra.calculate(Dijkstra.highestPoint(approach), 0, -1.2f, approach, map);
//        Dijkstra.print(approach, "Approach");
//        Dijkstra.print(flee, "Flee");

    }
    
    public int[][] approach(){
        return approach;
    }
    
    public int[][] flee(){
        return flee;
    }
}
