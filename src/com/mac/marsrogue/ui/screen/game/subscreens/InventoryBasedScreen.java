package com.mac.marsrogue.ui.screen.game.subscreens;

import com.mac.marsrogue.MarsRogue;
import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.item.Equipable;
import com.mac.marsrogue.game.entity.item.Inventory;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.ui.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 09:27 AM.
 */
public abstract class InventoryBasedScreen extends Screen{
    
    protected Creature player;
    
    public InventoryBasedScreen(Creature player){
        super((int) ((MarsRogue.width() / 2) - ((MarsRogue.width() * 0.7) / 2)), (int) ((MarsRogue.height() / 2) - ((MarsRogue.height() * 0.7) / 2)), (int) (MarsRogue.width() * 0.7), (int) (MarsRogue.height() * 0.7), "");
        this.player = player;
    }

    @Override
    public Screen input(KeyEvent key) {
        char index = key.getKeyChar();
        List<Item> list = getList();
        if(letters.indexOf(index) > -1 && list.size() > letters.indexOf(index) && isAcceptable(list.get(letters.indexOf(index)))) return use(list.get(letters.indexOf(index)));
        else if(key.getKeyCode() == KeyEvent.VK_ESCAPE) return null;
        else return this;
    }

    @Override
    public void render(AsciiPanel panel) {
        clearWithBorder(panel, true);
        int xp = this.x + 4;
        int yp = this.y + 4;

        List<Item> lines = getList();
        panel.write("What do you want to " + getVerb() + "?", xp + 2, yp++);

        yp += 2;
        for(int i = 0; i < lines.size(); i++){
            Item item = lines.get(i);
            yp++;
            drawItem(panel, letters.charAt(i), item, xp, yp);
        }

        panel.write("Press [escape] to return.", this.x + 2, this.y + this.height - 2, Colors.get("dark_text"));
    }

    private void drawItem(AsciiPanel panel, char index, Item item, int xp, int yp){
        String itemName = getItemName(item);

        panel.write("[" + index + "]", xp, yp);
        panel.write(item.glyph(), xp + 4, yp, item.foregroundColor());
        panel.write(itemName, xp + 6, yp);
        if(item instanceof Equipable){
            Equipable e = (Equipable) item;
            if(e.isEquipped()) panel.write("[EQUIPPED]", xp + 7 + itemName.length(), yp, Colors.get("gray"));
        }
        yp++;
        //		panel.write(item.description(), xp + 6, yp++, Colors.defaultForeground.darker());
    }
    
    private List<Item> getList(){
        List<Item> newItems = new ArrayList<Item>();
        List<Item> items = player.inventory().items();
        
        for(Item i : items) if(isAcceptable(i)) newItems.add(i);
        return newItems;
    }
    
    protected String getItemName(Item item){
        return item.name();
    }
    
    protected abstract String getVerb();
    protected abstract boolean isAcceptable(Item item);
    protected abstract Screen use(Item item);
}
