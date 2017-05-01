package com.mac.marsrogue.ui.screen.game;

import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;
import com.mac.marsrogue.game.entity.item.weapon.gun.GunProjectile;
import com.mac.marsrogue.ui.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:30 PM.
 */
public class InfoScreen extends Screen{

    private Creature creature;

    private int paddingX = 2;
    private int paddingY = 2;

    public InfoScreen(int x, int y, int width, int height, Creature creature) {
        super(x, y, width, height, "Stats");
        this.creature = creature;
    }

    @Override
    public Screen input(KeyEvent key) {
        return null;
    }

    @Override
    public void render(AsciiPanel panel) {
        clearWithBorder(panel, true);

        int xp = this.x + paddingX;
        int yp = this.y + paddingY;

        panel.write(creature.name(), this.x + (width / 2) - (creature.name().length() / 2), yp++);

        yp++;

        writeStat(panel, "Health", creature.hp() + "/" + creature.maxHp(), xp, yp++, null, null, 1);
        writeStat(panel, "Armor", "1000/1000", xp, yp++, null, null, 1);
        writeStat(panel, "Armor Charge", "87%", xp, yp++, null, null, 1);

        if(creature.weapon() != null){
            yp++;
            if(creature.weapon() instanceof Gun){
                Gun gun = (Gun) creature.weapon();

                String gunTitle = " " + gun.rarity() + " " + gun.gunClass().name().toUpperCase() + " ";
                panel.write(gunTitle, this.x + (width / 2) - (gunTitle.length() / 2), yp++, Colors.get("default_bg"), gun.rarity().color);

                yp++;
                panel.write(gun.name(), this.x + (width / 2) - (gun.name().length() / 2), yp++);

                //				drawStringWrapped(panel, "Bla bla bla, here does some description", xp, yp, Colors.get("gray"), this.width - paddingX);

                yp++;
                if(gun.projectile() == GunProjectile.SLUG) writeStat(panel, "AMMO", "[3/12]", xp, yp++, Colors.get("default_bg"), Colors.get("orange"), 4);
                else writeStat(panel, "CHARGE", (int) (gun.charge() * 100) + "%", xp, yp++, Colors.get("default_bg"), Colors.get("blue"), 4);

                yp++;
                writeStat(panel, "Damage", "[" + gun.damage() + "]", xp, yp++, null, null, 2);
                writeStat(panel, "Accuracy", "[" + gun.accuracy() + "%]", xp, yp++, null, null, 2);
                writeStat(panel, "Projectile", " " + gun.projectile() + " ", xp, yp++, Colors.get("default_bg"), gun.projectile().color, 2);
                writeStat(panel, "Type", gun.type() + "", xp, yp++, null, null, 2);
                writeStat(panel, "Fire Rate", "[" + gun.fireRate() + "]", xp, yp++, null, null, 2);
                if(gun.projectile() == GunProjectile.SLUG) writeStat(panel, "Clip Size", "[" + gun.clipSize() + "]", xp, yp++, null, null, 2);
            }
        }

        yp++;
        if(creature.head() != null){
            panel.write(creature.head().name(), xp, yp++);
            panel.write("MORE STATS GO HERE", xp, yp++);
        }

        if(creature.chest() != null){
            panel.write(creature.chest().name(), xp, yp++);
            panel.write("MORE STATS GO HERE", xp, yp++);
        }

        if(creature.legs() != null){
            panel.write(creature.legs().name(), xp, yp++);
            panel.write("MORE STATS GO HERE", xp, yp++);
        }


        yp += 10;
        panel.write("Scanner Data", xp, yp++);

        yp++;
        panel.write("HOSTILES DETECTED", xp, yp++, Colors.get("default_bg"), Colors.get("red"));
    }

    private void writeStat(AsciiPanel panel, String name, String stat, int xp, int yp, Color statForeground, Color statBackground, int padding){
        if(statForeground == null) statForeground = Colors.get("default_fg");
        if(statBackground == null) statBackground = Colors.get("default_bg");
        int xa = xp + (this.width - (stat.length() + 4)) - padding;
        panel.write(stat, xa, yp, statForeground, statBackground);
        panel.write(name, xp + padding, yp);

    }
}
