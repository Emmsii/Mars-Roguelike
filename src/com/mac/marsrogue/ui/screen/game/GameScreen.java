package com.mac.marsrogue.ui.screen.game;

import com.mac.marsrogue.MarsRogue;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.Game;
import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.MessageLog.LogType;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.ui.screen.Screen;
import com.mac.marsrogue.ui.screen.game.subscreens.EquipScreen;


import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 05:18 PM.
 */
public class GameScreen extends Screen {
    
    private Game game;
    
    private MapScreen mapScreen;
    private LogScreen infoLogScreen;
    private LogScreen combatLogScreen;
    private InfoScreen infoScreen;
    
    private Screen subscreen;
    
    private Creature player;
    
    public GameScreen(Game game){
        this.game = game;

        infoLogScreen = new LogScreen(game.logs().get(LogType.MESSAGE), 11, "Message Log");
        combatLogScreen = new LogScreen(MarsRogue.width() / 2 - 1, MarsRogue.width() / 2 + 1, game.logs().get(LogType.COMBAT), 11, "Combat Log");
        mapScreen = new MapScreen(0, 0, (int) (MarsRogue.width() * 0.75), MarsRogue.height() - infoLogScreen.height() + 1, game);
        infoScreen = new InfoScreen(mapScreen.width() - 1, 0, MarsRogue.width() - mapScreen.width() + 1, MarsRogue.height() - infoLogScreen.height() + 1, game.player());
        
        game.logs().get(LogType.MESSAGE).add("Press ? for help.");
        game.logs().get(LogType.COMBAT).add("Combat Log Initialized");
        player = game.player();
    }

    @Override
    public Screen input(KeyEvent key) {
        boolean shouldUpdate = false;

        player.setMoved(false);
        player.setTerminal(null);
        player.setHasUsedEquipment(false);

        if(subscreen != null) subscreen = subscreen.input(key);
        else{
            switch(key.getKeyCode()){

                //Movement
                case KeyEvent.VK_UP: shouldUpdate = player.moveBy(0, -1, 0); break;
                case KeyEvent.VK_DOWN: shouldUpdate = player.moveBy(0, 1, 0); break;
                case KeyEvent.VK_LEFT: shouldUpdate = player.moveBy(-1, 0, 0); break;
                case KeyEvent.VK_RIGHT: shouldUpdate = player.moveBy(1, 0, 0); break;
                case KeyEvent.VK_PAGE_UP: shouldUpdate = game.map().moveCreatureUp(game.player()); break;
                case KeyEvent.VK_PAGE_DOWN: shouldUpdate = game.map().moveCreatureDown(game.player()); break;

                //Commands
//                case KeyEvent.VK_F:
//                    if(player.weapon() == null) player.notify(new ColoredString("You don't have a weapon equipped.", Colors.get("red")), LogType.MESSAGE);
//                    else subscreen = new FireWeaponScreen(player, player.x - mapScreen.getScrollX(), player.y - mapScreen.getScrollY(), true);
//                    break;
//                case KeyEvent.VK_B: subscreen = new BlueprintScreen(player); break;
                case KeyEvent.VK_E: subscreen = new EquipScreen(player); break;
//                case KeyEvent.VK_L: subscreen = new ExamineScreen(player, player.x - mapScreen.getScrollX(), player.y - mapScreen.getScrollY(), true); break;
//                case KeyEvent.VK_T: subscreen = new ThrowScreen(player, player.x - mapScreen.getScrollX(), player.y - mapScreen.getScrollY()); break;
                case KeyEvent.VK_P: player.pickup(); break;

                //Debug
                case KeyEvent.VK_N: Map.showDijkstra = !Map.showDijkstra; break;
                case KeyEvent.VK_M: Map.hideFov = !Map.hideFov; break;
                case KeyEvent.VK_SPACE: game.logs().get(LogType.MESSAGE).add(new ColoredString(Math.random() + "", new Color((float) Math.random(), (float) Math.random(), (float) Math.random()))); break;

                case KeyEvent.VK_ESCAPE: game.exit(); break;
            }

            switch(key.getKeyChar()){
                case ',': shouldUpdate = true; break;
//                case '?': subscreen = new HelpScreen(); break;
            }

//            if(game.player().terminal() != null) subscreen = new TerminalScreen(player.terminal(), player);
        }

        if(player.hasUsedEquipment()) shouldUpdate = true;
        if(subscreen == null && shouldUpdate) game.update();
//        if(player.hp() < 1) return new LooseScreen();

        return this;
    }

    @Override
    public void render(AsciiPanel panel) {
        mapScreen.setCameraPosition(player.x, player.y);

        mapScreen.render(panel);
        infoLogScreen.render(panel);
        combatLogScreen.render(panel);
        infoScreen.render(panel);

        if(subscreen != null) subscreen.render(panel);
    }
}
