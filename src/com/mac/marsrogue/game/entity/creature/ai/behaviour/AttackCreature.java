package com.mac.marsrogue.game.entity.creature.ai.behaviour;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.pathfinding.dijkstra.Dijkstra;
import com.mac.marsrogue.engine.util.Maths.Maths;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.entity.creature.CombatManager;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.CreatureAI;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 11:31 AM.
 */
public class AttackCreature extends Behaviour{
    
    private Creature target;
    
    public AttackCreature(CreatureAI ai, Creature target){
        super(ai);
        this.target = target;
    }
    
    @Override
    public void update(Creature creature) {

        /**
         * If creature does not have weapon
         *      set behaviour to FindWeapon 
         */
        if(creature.weapon() == null){
            ai.setBehaviour(new FindWeapon(ai));
            return;
        }
        
        if(creature.canSee(target)){


            /**
             * If creature can see target
             *      If creature is in range, fire weapon
             *      else move towards target
             */
            
            Gun gun = (Gun) creature.weapon();
            Point creaturePos = creature.position();
            Point next = creaturePos;

            int distanceToTarget = Maths.distance(creature.x, creature.y, target.x, target.y);
            
            float areaDanger = ai.dangerValueOfArea();
            float targetDanger = ai.dangerValueOfCreature(target);
            float selfDanger = ai.dangerValueOfCreature(creature);
            float distanceDanger = 3f / (distanceToTarget * 1.25f) - 0.2f;
            
            float totalDanger = (targetDanger - selfDanger) + areaDanger + distanceDanger;
            
            if(creature.id() == 1) {
                Log.trace("Area Danger: " + areaDanger);
                Log.trace("Target Danger: " + targetDanger);
                Log.trace("Self Danger: " + selfDanger);
                Log.trace("Distance Danger: " + distanceDanger);
                Log.trace("Total Danger: " + totalDanger);
            }

            /**
             * Danger < 0, creature feels pretty safe, will be aggressive
             * Danger > 0, creature will be more cautious
             * Danger += ai confidence value min -0.5 0.5
             */

            int range = gun.gunClass().recommendedRange();
            
            
            if(distanceToTarget <= range){
                
                if(distanceToTarget <= range * 0.45f){
                    next = Dijkstra.rollDown(creaturePos, creature.map().dijkstraMaps().approach(), -range, 0);    
                }else{
                    new CombatManager(creature, null).fireWeapon(target.x, target.y);    
                }
            }else{
                next = Dijkstra.rollDown(creaturePos, creature.map().dijkstraMaps().approach(), -range, 0);
            }
            
            if(!next.equals(creaturePos)){
                int mx = next.x - creature.x;
                int my = next.y - creature.y;
                creature.moveBy(mx, my, 0);
            }
        }else{
            wander(0.25f, creature);
        }
        
    }

    private void wander(float frequency, Creature creature){
        if(Math.random() < frequency){
            int x = (int) Math.round(Math.random() * 2 - 1);
            int y = (int) Math.round(Math.random() * 2 - 1);
            creature.moveBy(x, y, 0);
        }
    }

}
