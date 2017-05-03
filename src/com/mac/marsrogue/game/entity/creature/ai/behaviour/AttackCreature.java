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
    private boolean canSee;
    private Point lastSeen;
    private int agroCooldown;
    
    public AttackCreature(CreatureAI ai, Creature target){
        super(ai);
        this.target = target;
    }
    
    @Override
    public void update(Creature creature) {
        
        if(creature.weapon() == null){
            ai.setBehaviour(new FindWeapon(ai));
        }
        
        if(creature.canSee(target)){
            canSee = true;
            lastSeen = target.position();
            agroCooldown = 4;
        }

        Point creaturePos = creature.position();
        Point next = creaturePos;
        
        if(canSee){
            Log.trace("Creature can see target!");
            Gun gun = (Gun) creature.weapon();
            
            float generalDanger = creature.ai().calculateDangerValue();
            float targetDanger = creature.ai().dangerValueOfCreature(target);
            float selfDanger = creature.ai().dangerValueOfCreature(creature);

            int distance = Maths.distance(creature.x, creature.y, target.x, target.y);
            float distDanger = 3f / (distance * 1.25f) - 0.25f;
            
            float totalDanger = (targetDanger - selfDanger) + generalDanger + distDanger;
            
//            int r = Math.min(creature.awareness(), gun.gunClass().recommendedRange());
////            int range = (int) (r + (r * (totalDanger / 2)));
//            int range = r;
            
            int range = (int) (creature.awareness() * 0.75f);
            
            Log.trace("General Danger: " + generalDanger);
            Log.trace("Target Danger: " + targetDanger + " Self Danger: " + selfDanger + " Total: " + totalDanger);
            Log.trace("Range: " + range + " (Recommended: " + gun.gunClass().recommendedRange() + ")");
            
            if(inRange(creature.x, creature.y, target.x, target.y, range)){
                Log.trace("Creature is in range!");                
                //If creature is in range of target
                //Creature should move back IF DANGER VALUE IS HIGH ENOUGH

                if(totalDanger * 0.25 >= (1f - ai.aggressivenessWeight())){
                    Log.trace("Creature moving back");
                    next = Dijkstra.rollUp(creaturePos, creature.map().dijkstraMaps().approach(), -range, 0);
                }else{
                    //FIRE WEAPON
                    Log.info("PEW PEW");
                    new CombatManager(creature, null).fireWeapon(target.x, target.y);
                }
            }else{
                //Run away?
                Log.trace(Maths.distance(creature.x, creature.y, target.x, target.y) + " DISTANCE");
                Log.info("Not in range. Moving towards!");
                next = Dijkstra.rollDown(creaturePos, creature.map().dijkstraMaps().approach(), range, 0);
            }
        }else{

            wander(0.25f, creature);                
            return;
        }
        Log.trace("Creature Pos " + creaturePos + " Next " + next);
        if(!next.equals(creaturePos)){
            int mx = next.x - creature.x;
            int my = next.y - creature.y;
            creature.moveBy(mx, my, 0);
        }else{
            if(!creature.canSee(target)) canSee = false;
            agroCooldown--;
        }
    }

    private void wander(float frequency, Creature creature){
        if(Math.random() < frequency){
            int x = (int) Math.round(Math.random() * 2 - 1);
            int y = (int) Math.round(Math.random() * 2 - 1);
            creature.moveBy(x, y, 0);
        }
    }
    
    private boolean inRange(int x, int y, int xt, int yt, int range){
        return Maths.distance(x, y, xt, yt) <= range;
    }
}
