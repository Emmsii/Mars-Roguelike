package com.mac.marsrogue.game.entity.item.weapon;

import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.item.Equipable;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.MessageLog.LogType;

import java.awt.Color;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:58 PM.
 */
public class Weapon extends Item implements Equipable{

    private boolean equipped;

    public Weapon(char glyph, Color color, String name, String description) {
        super(glyph, color, name, description);
    }

    @Override
    public void equip(Creature creature) {
        if(creature.weapon() != null && creature.weapon() != this) creature.unequip(creature.weapon());

        if(isEquipped()){
            unequip(creature);
            return;
        }

        creature.setWeapon(this);
        creature.doAction(new ColoredString("equip a %s"), LogType.MESSAGE, name);
        equipped = true;
    }

    @Override
    public void unequip(Creature creature) {
        if(creature.weapon() != null) creature.doAction(new ColoredString("unequip a %s"), LogType.MESSAGE, name);
        creature.setWeapon(null);
        equipped = false;
    }

    @Override
    public boolean isEquipped() {
        return equipped;
    }

    public float score(){
        return 0;
    }
}
