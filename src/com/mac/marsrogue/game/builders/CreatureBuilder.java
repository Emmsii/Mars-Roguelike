package com.mac.marsrogue.game.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.MessageLog;
import com.mac.marsrogue.game.MessageLog.LogType;
import com.mac.marsrogue.game.codex.Codex;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.ai.ColonistAI;
import com.mac.marsrogue.game.entity.creature.ai.EnemyAI;
import com.mac.marsrogue.game.entity.creature.ai.PlayerAI;
import com.mac.marsrogue.game.entity.creature.limbs.Limb;

import java.util.HashMap;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:45 PM.
 */
public class CreatureBuilder {

    public static Creature newColonist(){
        Creature colonist = (Creature) Codex.creatures.get("colonist").newInstance();
        new ColonistAI(colonist);
        return colonist;
    }

    public static Creature newAndroid(){
        Creature enemy = (Creature) Codex.creatures.get("android").newInstance();
        new EnemyAI(enemy);
        return enemy;
    }

    public static Creature newPlayer(HashMap<LogType, MessageLog> logs){
        Creature player = new Creature('@', Colors.get("player"), "Player", "Its you!", Colors.get("human_blood"), Codex.factions.get("neutral"));
        player.limbController().addLimb(Limb.ALL);
        player.setStats(10000, 20);
        new PlayerAI(player, logs);
        return player;
    }
}
