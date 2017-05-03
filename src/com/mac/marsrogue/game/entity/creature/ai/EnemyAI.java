package com.mac.marsrogue.game.entity.creature.ai;

import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.behaviour.AttackCreature;
import com.mac.marsrogue.game.entity.creature.ai.behaviour.TestBehaviour;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:45 PM.
 */
public class EnemyAI extends CreatureAI{
    
    public EnemyAI(Creature creature) {
        super(creature);
    }

    @Override
    public void init() {
//        setBehaviour(new AttackCreature(this, creature.map().player()));
        setBehaviour(new TestBehaviour(this));
    }
}
