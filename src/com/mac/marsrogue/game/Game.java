package com.mac.marsrogue.game;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.pathfinding.astar.AStar;
import com.mac.marsrogue.game.builders.CreatureBuilder;
import com.mac.marsrogue.game.builders.ItemBuilder;
import com.mac.marsrogue.game.builders.MapBuilder;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.item.blueprint.Blueprint;
import com.mac.marsrogue.game.entity.item.blueprint.GunBlueprint;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.game.MessageLog.LogType;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 05:03 PM.
 */
public class Game {
    
    public static final Random random = new Random();

    private Map map;
    private Creature player;
    private HashMap<LogType, MessageLog> logs;

    public void init(){
        logs = new HashMap<LogType, MessageLog>();
        logs.put(LogType.COMBAT, new MessageLog());
        logs.put(LogType.MESSAGE, new MessageLog());

        map = new MapBuilder(136, 136, 30, 8).generate().build();

        ItemBuilder.createGunBlueprints(map.depth(), 3, 12, 0.8f, random);

        //		map.showTileImage();

        //		map = new MapDecorator(map).decorate().simulate(0, 0).finish();
        //		map = new MapBuilder(32, 32, 1, 8, 1234).generateEmptyWalledMap().randomScatterTile(0.01f, Tile.wallCenter).build();
        //		FileHandler.saveMap(map);

        //		Pathfinder.instance().init(map);
        AStar.instance().init(map);

        player = CreatureBuilder.newPlayer(logs);

        for(int i = 0; i < 10; i++){
            Gun gun = ItemBuilder.newGun(0, random);
            Blueprint gunBp = new GunBlueprint(gun.name() + " Blueprint", "Its a blueprint", gun);
            player.inventory().add(gunBp);
        }

        player.inventory().add(ItemBuilder.newGun(1, random));
        player.inventory().add(ItemBuilder.newExplosive(1, random));
        player.inventory().add(ItemBuilder.newExplosive(1, random));
        player.inventory().add(ItemBuilder.newExplosive(1, random));
        player.inventory().add(ItemBuilder.newExplosive(1, random));

        player.equip(ItemBuilder.newArmor(0, random));

        map.add(player, map.randomEmptyPoint(0));

        for(int y = 0; y < map.height(); y++){
            for(int x = 0; x < map.width(); x++){
                //				if(random.nextFloat() < 0.15f) map.sootMap().addSoot(x, y, 0, 1f);
            }
        }

        for(int i = 0; i < 10; i++){
            //			map.add(ItemBuilder.newGun(0, random), map.randomEmptyPoint(0));
            //			map.add(ItemBuilder.newRawMatter(0), map.randomEmptyPoint(0));

            Creature mob = CreatureBuilder.newTestEnemy();
            mob.inventory().add(ItemBuilder.newGun(0, random));
            //			for(int j = 0; j < random.nextInt(4); j++) mob.inventory().add(ItemBuilder.newGun(0, random));
            Gun g = ItemBuilder.newGun(0, random);
            g.equip(mob);
            map.add(mob, map.randomEmptyPoint(0));
        }

        //		for(int i = 0; i < 30; i++){
        //			int score = 0;
        //			for(int j = 0; j < 100; j++){
        //				Gun g = ItemBuilder.newGun(i, random);
        //				score += g.score();
        //			}
        //			
        //			System.out.println("Level: " + i + " Average Score: " + (score / 100));
        //		}
    }

    public void update(){
        map.update(player.z);
    }

    public void exit(){
        System.out.println("Bye!");
        //Save the game...
        System.exit(0);
    }

    public Map map(){
        return map;
    }

    public Creature player(){
        return player;
    }

    public HashMap<LogType, MessageLog> logs(){
        return logs;
    }

    public MessageLog getLog(LogType type){
        if(!logs.containsKey(type)){
            Log.error("Unknown log type: " + type);
            return null;
        }
        return logs.get(type);
    }
}
