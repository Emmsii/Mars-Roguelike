package com.mac.marsrogue.game.entity.creature.ai.behaviour;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.CreatureAI;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 12:14 PM.
 */
public class FindWeapon extends Behaviour{
    
    public FindWeapon(CreatureAI ai) {
        super(ai);
    }

    @Override
    public void update(Creature creature) {
        Log.trace("Creature is finding weapon");

        /**
         * look in inventory for best weapon.
         *      if weapon found, equip, change behaviour
         *      else look around?? (a*)
         */
    }
}
