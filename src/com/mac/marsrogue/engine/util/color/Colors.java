package com.mac.marsrogue.engine.util.color;

import com.esotericsoftware.minlog.Log;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 05:07 PM.
 */
public class Colors {
    
    private static final HashMap<String, Color> colors = new HashMap<String, Color>();
    public static final Color INVALID = Color.PINK;
    
    public static Color get(String name){
        if(!colors.containsKey(name.toLowerCase().trim())){
            Log.warn("Unknown color: " + name.toLowerCase().trim(), new InvalidColorException());
            return INVALID;
        }
        return colors.get(name.toLowerCase().trim());
    }
    
    public static void add(String name, Color color){
        if(colors.containsKey(name.toLowerCase().trim())) return;
        colors.put(name.toLowerCase().trim(), color);
    }
    
    public static Color darken(Color color, float amount){
        int r = (int) (color.getRed() - (color.getRed() * amount));
        int g = (int) (color.getGreen() - (color.getGreen() * amount));
        int b = (int) (color.getBlue() - (color.getBlue() * amount));

        if(r < 0) r = 0;
        if(g < 0) g = 0;
        if(b < 0) b = 0;
        if(r > 255) r = 255;
        if(g > 255) g = 255;
        if(b > 255) b = 255;

        return new Color(r, g, b);
    }

    public static Color randomize(Color color, float amount, boolean uniform, Random random){
        float rand = random.nextFloat();

        int r = (int) (color.getRed() + (color.getRed() * amount) * (uniform ? rand : random.nextFloat()));
        int g = (int) (color.getGreen() + (color.getGreen() * amount) * (uniform ? rand : random.nextFloat()));
        int b = (int) (color.getBlue() + (color.getBlue() * amount) * (uniform ? rand : random.nextFloat()));

        if(r < 0) r = 0;
        if(g < 0) g = 0;
        if(b < 0) b = 0;
        if(r > 255) r = 255;
        if(g > 255) g = 255;
        if(b > 255) b = 255;

        return new Color(r, g, b);
    }

    public static Color blend( Color c1, Color c2, float ratio ) {
        if(c1 == null) return c2;
        if(c2 == null) return c1;

        if ( ratio > 1f ) ratio = 1f;
        else if ( ratio < 0f ) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int)((a1 * iRatio) + (a2 * ratio));
        int r = (int)((r1 * iRatio) + (r2 * ratio));
        int g = (int)((g1 * iRatio) + (g2 * ratio));
        int b = (int)((b1 * iRatio) + (b2 * ratio));

        return new Color( a << 24 | r << 16 | g << 8 | b );
    }

    public static Color blend(Color a, Color b){
        if(a == null) return b;
        if(b == null) return a;

        int rA = a.getRed();
        int gA = a.getGreen();
        int bA = a.getBlue();

        int rB = b.getRed();
        int gB = b.getGreen();
        int bB = b.getBlue();

        int rN = (rA + rB) / 2;
        int gN = (gA + gB) / 2;
        int bN = (bA + bB) / 2;

        if(rN < 0) rN = 0;
        if(gN < 0) gN = 0;
        if(bN < 0) bN = 0;
        if(rN > 255) rN = 255;
        if(gN > 255) gN = 255;
        if(bN > 255) bN = 255;

        return new Color(rN, bN, gN);
    }

    public static Color getColorFromGradient(Color[] colors, float[] positions, float v ){
        if(colors.length == 0 || colors.length != positions.length) throw new IllegalArgumentException();
        if(colors.length == 1) return colors[0];
        
        if(v <= positions[0]) return colors[0];
        if(v >= positions[positions.length-1]) return colors[positions.length-1];
        
        for( int i = 1; i < positions.length; ++i ){
            if( v <= positions[i] ){
                float t = (v - positions[i-1]) / (positions[i] - positions[i-1]);
                return lerpColor(colors[i-1], colors[i], t);
            }
        }

        //should never make it here
        throw new RuntimeException();
    }

    public static Color lerpColor( Color colorA, Color colorB, float t){
        int red = (int) Math.floor(colorA.getRed() * (1 - t) + colorB.getRed() * t);
        int green = (int) Math.floor(colorA.getGreen() * (1 - t) + colorB.getGreen() * t);
        int blue = (int) Math.floor(colorA.getBlue() * (1 - t) + colorB.getBlue() * t);
        return new Color(red, green, blue);
    }
    
    public static int count(){
        return colors.size();
    }
}
