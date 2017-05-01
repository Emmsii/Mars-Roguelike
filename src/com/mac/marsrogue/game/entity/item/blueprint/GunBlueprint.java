package com.mac.marsrogue.game.entity.item.blueprint;

import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.engine.util.StringUtil;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;
import com.mac.marsrogue.game.entity.item.weapon.gun.GunProjectile;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:53 PM.
 */
public class GunBlueprint extends Blueprint{

    public GunBlueprint(String name, String description, Gun item) {
        super(name, description, item);
    }

    @Override
    public void render(AsciiPanel panel, int x, int y, Blueprint compareTo) {
        Gun gun = (Gun) item;
        Gun compare = compareTo == null ? null : (Gun) compareTo.item;

        String nameText = gun.name() + " " + gun.gunClass().name();
        panel.write(nameText + " - ", x, y);
        panel.write(gun.rarity() + "", x + nameText.length() + 3, y++, Colors.get("default_bg"), gun.rarity().color);

        y++;
        panel.write("Gun description goes here", x, y++, Colors.get("gray"));

        y++;
        x++;

        String damageText = "Damage: ";
        panel.write(damageText, x, y);
        Color c = Colors.get("default_fg");
        if(compare != null) c = StringUtil.totalValue(gun.damage()) == StringUtil.totalValue(compare.damage()) ? Colors.get("cyan") : StringUtil.totalValue(gun.damage()) > StringUtil.totalValue(compare.damage()) ? Colors.get("green") : Colors.get("red");
        panel.write(gun.damage(), x + damageText.length(), y++, c);

        String accuracyText = "Accuracy: ";
        panel.write(accuracyText, x, y);
        c = Colors.get("default_fg");
        if(compare != null) c = gun.accuracy() == compare.accuracy() ? Colors.get("cyan") : gun.accuracy() > compare.accuracy() ? Colors.get("green") : Colors.get("red");
        panel.write(gun.accuracy() + "%", x + accuracyText.length(), y++, c);

        panel.write("Projectile:", x, y);
        panel.write(gun.projectile() + "", x + 12, y++, Colors.get("default_bg"), gun.projectile().color);
        panel.write("Type: " + gun.type(), x, y++);

        String fireRateText = "Fire Rate: ";
        panel.write(fireRateText, x, y);
        c = Colors.get("default_fg");
        if(compare != null) c = StringUtil.totalValue(gun.fireRate()) == StringUtil.totalValue(compare.fireRate()) ? Colors.get("cyan") : StringUtil.totalValue(gun.fireRate()) > StringUtil.totalValue(compare.fireRate()) ? Colors.get("green") : Colors.get("red");
        panel.write(gun.fireRate(), x + fireRateText.length(), y++, c);

        if(gun.projectile() == GunProjectile.SLUG){
            String clipSizeText = "Clip Size: ";
            panel.write(clipSizeText, x, y);
            c = Colors.get("default_fg");
            if(compare != null && compare.projectile() == GunProjectile.SLUG) c = gun.clipSize() > compare.clipSize() ? Colors.get("green") : Colors.get("red");
            panel.write(gun.clipSize() + "", x + clipSizeText.length(), y++, c);
        }
    }

    @Override
    public String name() {
        Gun gun = (Gun) item;
        return gun.name() + " " + gun.gunClass().name() + " Blueprint";
    }
}
