package com.mac.marsrogue.game.map.tile;

import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:19 PM.
 */
public class Tile {

    public static Tile[] tiles = new Tile[256];

    public static final Tile boundsTile = new SolidTile(0, ' ', Color.RED, Colors.get("default_bg"));

    //public static final Tile floorTile = new EmptyTile(1, (char) 197, Colors.floorForeground, Colors.floorBackground);
    //	
    //	public static final Tile wallCenter = new WallTile(2, (char) 206, new Color(42, 63, 74), new Color(66, 92, 105));
    //	public static final Tile wallHori = new WallTile(3, (char) 205, new Color(42, 63, 74), new Color(66, 92, 105));
    //	public static final Tile wallVert = new WallTile(4, (char) 186, new Color(42, 63, 74), new Color(66, 92, 105));
    //	
    //	public static final Tile wallNE = new WallTile(5, (char) 187, new Color(42, 63, 74), new Color(66, 92, 105));
    //	public static final Tile wallNW = new WallTile(6, (char) 201, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallSE = new WallTile(7, (char) 188, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallSW = new WallTile(8, (char) 200, Colors.wallForeground, Colors.wallBackground);
    //	
    //	public static final Tile wallHoriN = new WallTile(9, (char) 202, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallHoriS = new WallTile(10, (char) 203, Colors.wallForeground, Colors.wallBackground);
    //	
    //	public static final Tile wallVertE = new WallTile(11, (char) 204, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallVertW = new WallTile(12, (char) 185, Colors.wallForeground, Colors.wallBackground);
    //		
    //	public static final Tile wallEndN = new WallTile(13, (char) 210, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallEndS = new WallTile(14, (char) 208, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallEndE = new WallTile(15, (char) 198, Colors.wallForeground, Colors.wallBackground);
    //	public static final Tile wallEndW = new WallTile(16, (char) 181, Colors.wallForeground, Colors.wallBackground);
    //	
    //	public static final Tile stairUp = new EmptyTile(17, (char) 24, Color.CYAN, Colors.wallBackground);
    //	public static final Tile stairDown = new EmptyTile(18, (char) 25, Color.CYAN, Colors.wallBackground);
    //	
    //	public static final Tile sandyGround1 = new EmptyImpassableTile(19, ',', new Color(186, 95, 56), new Color(201, 101, 46));
    //	public static final Tile sandyGround2 = new EmptyImpassableTile(20, '.', new Color(181, 101, 58), new Color(204, 104, 49));
    //	
    //	public static final Tile glassHori = new EmptyImpassableTile(22, (char) 179, new Color(180, 215, 224), Colors.wallBackground);
    //	public static final Tile glassVert = new EmptyImpassableTile(23, (char) 196, new Color(180, 215, 224), Colors.wallBackground);

    public static final Tile floorTile = new EmptyTile(1, '.', Colors.get("wall_fg"), Colors.get("default_bg"));

    public static final Tile wallCenter = new WallTile(2, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallHori = new WallTile(3, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallVert = new WallTile(4, '#', Colors.get("wall_fg"), Colors.get("default_bg"));

    public static final Tile wallNE = new WallTile(5, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallNW = new WallTile(6, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallSE = new WallTile(7, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallSW = new WallTile(8, '#', Colors.get("wall_fg"), Colors.get("default_bg"));

    public static final Tile wallHoriN = new WallTile(9, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallHoriS = new WallTile(10, '#', Colors.get("wall_fg"), Colors.get("default_bg"));

    public static final Tile wallVertE = new WallTile(11, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallVertW = new WallTile(12, '#', Colors.get("wall_fg"), Colors.get("default_bg"));

    public static final Tile wallEndN = new WallTile(13, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallEndS = new WallTile(14, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallEndE = new WallTile(15, '#', Colors.get("wall_fg"), Colors.get("default_bg"));
    public static final Tile wallEndW = new WallTile(16, '#', Colors.get("wall_fg"), Colors.get("default_bg"));

    public static final Tile stairUp = new EmptyTile(17, (char) 24, Color.CYAN, Colors.get("default_bg"));
    public static final Tile stairDown = new EmptyTile(18, (char) 25, Color.CYAN, Colors.get("default_bg"));

    public static final Tile sandyGround1 = new EmptyImpassableTile(19, ',', new Color(186, 95, 56), Colors.get("default_bg"));
    public static final Tile sandyGround2 = new EmptyImpassableTile(20, '.', new Color(181, 101, 58),Colors.get("default_bg"));

    public static final Tile glassHori = new EmptyImpassableTile(22, (char) 179, new Color(180, 215, 224), Colors.get("default_bg"));
    public static final Tile glassVert = new EmptyImpassableTile(23, (char) 196, new Color(180, 215, 224), Colors.get("default_bg"));

    public final byte id;
    public char glyph;
    public Color foregroundColor;
    public Color backgroundColor;
    public boolean solid;
    public boolean transparent;

    public Tile(int id, char glyph, Color foregroundColor, Color backgroundColor) {
        this.id = (byte) id;
        this.glyph = glyph;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;

        if(tiles[id] != null) throw new RuntimeException("Duplicate tile id [" + id + "]");
        tiles[id] = this;
    }

    public static Tile getTile(int id){
        return tiles[id];
    }

    public static boolean isWall(int id){
        return getTile(id) instanceof WallTile;
    }
}
