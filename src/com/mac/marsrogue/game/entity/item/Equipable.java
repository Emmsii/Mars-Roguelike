package com.mac.marsrogue.game.entity.item;

import com.mac.marsrogue.game.entity.creature.Creature;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:57 PM.
 */
public interface Equipable {
    
    void equip(Creature creature);
    void unequip(Creature creature);
    boolean isEquipped();
}
