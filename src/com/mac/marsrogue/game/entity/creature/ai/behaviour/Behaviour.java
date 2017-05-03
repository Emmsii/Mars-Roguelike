package com.mac.marsrogue.game.entity.creature.ai.behaviour;

import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.CreatureAI;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:46 PM.
 */
public abstract class Behaviour {
    
    protected CreatureAI ai;
    
    public Behaviour(CreatureAI ai){
        this.ai = ai;
    }
    
    public abstract void update(Creature creature);
}
