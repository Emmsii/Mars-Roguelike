package com.mac.marsrogue.game.entity.creature.ai.behaviour;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.pathfinding.dijkstra.Dijkstra;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.CreatureAI;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 05:28 PM.
 */
public class TestBehaviour extends  Behaviour {
    
    public TestBehaviour(CreatureAI ai) {
        super(ai);
    }

    @Override
    public void update(Creature creature) {
        Log.trace("Testing Ai");
        Point next = creature.position();
        
        next = Dijkstra.rollDown(creature.position(), creature.map().dijkstraMaps().test, 0, 6);
         
        if(!next.equals(creature.position())){
            int mx = next.x - creature.x;
            int my = next.y - creature.y;
            creature.moveBy(mx, my, 0);
        }
    }
}
