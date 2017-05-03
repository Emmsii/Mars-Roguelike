package com.mac.marsrogue.ui.screen.game;

import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.game.Game;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.game.map.FieldOfView.FOVType;
import com.mac.marsrogue.game.map.tile.Tile;
import com.mac.marsrogue.ui.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:17 PM.
 */
public class MapScreen extends Screen{

    private Map map;
    private Creature player;
    private int xPos, yPos;

    public MapScreen(int x, int y, int width, int height, Game game) {
        super(x, y, width, height, "Level 1");
        this.map = game.map();
        this.player = game.player();
    }

    @Override
    public Screen input(KeyEvent e) {
        return this;
    }

    @Override
    public void render(AsciiPanel panel) {
        setTitle("Level " + (player.z + 1));
        clearWithBorder(panel, true);
        map.computeFov(player.x, player.y, player.z, player.awareness(), FOVType.SHADOWCAST);

        for(int ya = 1; ya < height - 1; ya++){
            int yp = ya + getScrollY() - 1;
            for(int xa = 1; xa < width - 1; xa++){
                int xp = xa + getScrollX() - 1;

                if(Map.hideFov) if(!map.isExplored(xp, yp, player.z)) continue;
                if(map.tile(xp, yp, player.z) == Tile.boundsTile) continue;

                panel.write(map.glyph(xp, yp, player.z), xa, ya, map.foregroundColor(xp, yp, player.z), map.backgroundColor(xp, yp, player.z));
            }
        }

        //		for(Creature c : map.creatures(player.z)){
        //			if(c.path != null){
        //				for(Point p : c.path){
        //					
        //					int xa = p.x - getScrollX();
        //					int ya = p.y - getScrollY();
        //					
        //					if(xa < 0 || ya < 0 || xa >= width || ya >= height) continue;
        //					if(map.creature(p.x, p.y, player.z) != null) continue;
        //					panel.write('*', xa, ya, Color.pink);
        //				}
        //			}
        //		}

        //		panel.write("Level " + (player.z + 1), 0, 0);
        //		panel.write("(" + player.x + ", " + player.y + ")", 0, 1);
        ////		panel.write(map.exploredTiles() + "/" + map.explorableTiles() + " (" + map.exploredPercentage() + "%)", 0, 2);
        //		panel.write("Explored " + map.exploredPercentage() + "%", 0, 2);
    }

    public void setCameraPosition(int x, int y){
        xPos = x;
        yPos = y;
    }

    public int getScrollX(){
        return Math.max(0, Math.min(xPos - (width - 1) / 2, map.width() - (width - 1)));
    }

    public int getScrollY(){
        return Math.max(0, Math.min(yPos - (height - 1) / 2, map.height() - (height - 1)));
    }
}
