package com.mac.marsrogue.game.map;

import com.mac.marsrogue.engine.pathfinding.dijkstra.Dijkstra;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.entity.creature.Creature;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 11:40 AM.
 */
public class DijkstraMaps {
    
    private Dijkstra dijkstra;
    
    private int[][] approach;
    
    public DijkstraMaps(Map map){
        this.dijkstra = new Dijkstra(map);
    }
    
    public void updateApproach(Creature player){
        approach = dijkstra.generate(player.position(), 1);
    }
    
    public int[][] approach(){
        return approach;
    }
}
