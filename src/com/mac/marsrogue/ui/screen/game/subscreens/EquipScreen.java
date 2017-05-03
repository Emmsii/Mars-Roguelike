package com.mac.marsrogue.ui.screen.game.subscreens;

import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.item.Equipable;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.entity.item.weapon.Explosive;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;
import com.mac.marsrogue.ui.screen.Screen;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 09:30 AM.
 */
public class EquipScreen extends InventoryBasedScreen{
    
    public EquipScreen(Creature player) {
        super(player);
    }

    @Override
    protected String getVerb() {
        return "equip/unequip";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item instanceof Equipable && !(item instanceof Explosive);
    }

    @Override
    protected Screen use(Item item) {
        player.equip(item);
        return this;
    }

    @Override
    protected String getItemName(Item item) {
        if(item instanceof Gun){
            Gun gun = (Gun) item;
            return gun.name() + " " + gun.gunClass().name();
        }
        return super.getItemName(item);
    }
}
