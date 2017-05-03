package com.mac.marsrogue.game.entity.creature.ai;

import com.mac.marsrogue.engine.util.Maths.Line;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.MessageLog.LogType;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.behaviour.Behaviour;
import com.mac.marsrogue.game.map.object.Door;
import com.mac.marsrogue.game.map.object.DoorTerminal;
import com.mac.marsrogue.game.map.object.MapObject;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:50 PM.
 */
public abstract class CreatureAI {
    
    protected Creature creature;
    protected Behaviour behaviour;
    
    public CreatureAI(Creature creature){
        this.creature = creature;
        creature.setAi(this);
    }
    
    public abstract void init();
    
    public void update(){
        creature.setMoved(false);
        if(behaviour != null) behaviour.update(creature);
    }

    public boolean tryMove(int xp, int yp, int zp){
        if(!canEnter(xp, yp, zp)) return false;

        //		MapObject obj = creature.map().object(xp, yp, zp);
        //		if(obj instanceof Door){
        //			Door d = (Door) obj;
        //			if(d != null){
        //				if(!d.isOpen()){
        //					if(d.locked()){
        //						//if player has key for door, unlock
        //						return false;
        //					}
        //					d.setOpen(true);
        //					creature.doAction(new ColoredString("open the door"));
        //					return true;
        //				}
        //			}
        //		}
        MapObject obj = creature.map().object(xp, yp, zp);
        if(obj != null && !processMapObject(obj)) return false;
        //		if(!processMapObject(obj)) return false;

        creature.map().moveCreature(creature, xp, yp, zp);
        return true;
    }

    private boolean processMapObject(MapObject obj){
        //		if(obj == null) return false;

        if(obj instanceof Door){
            Door d = (Door) obj;
            if(!d.isOpen()){
                if(d.locked()){
                    //if player has key, unlock
                    creature.notify(new ColoredString("The door is locked", Colors.get("ORANGE")), LogType.MESSAGE);
                    return false;
                }
                d.setOpen(true);
                creature.doAction(new ColoredString("open the door"), LogType.MESSAGE);
                return true;
            }
            return true;
        } else if(obj instanceof DoorTerminal){
            DoorTerminal t = (DoorTerminal) obj;
            creature.setTerminal(t);
        }

        return false;
    }

    public boolean canSee(int xp, int yp, int zp){
        if(creature.z != zp) return false;
        if((creature.x - xp) * (creature.x - xp) + (creature.y - yp) * (creature.y - yp) > creature.awareness() * creature.awareness()) return false;
        for(Point p : new Line(creature.x, creature.y, xp, yp)){
            if(creature.map().transparent(p.x, p.y, zp) || p.x == xp && p.y == yp) continue;
            return false;
        }
        return true;
    }

    public boolean canEnter(int xp, int yp, int zp){
        return !creature.map().tile(xp, yp, zp).solid && creature.map().creature(xp, yp, zp) == null;
    }

    public void notify(ColoredString message, LogType type){

    }

    public void setBehaviour(Behaviour behaviour){
        this.behaviour = behaviour;
    }
}
