package com.mac.marsrogue.game.map;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.MessageLog;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.map.decal.BloodMap;
import com.mac.marsrogue.game.map.decal.SootMap;
import com.mac.marsrogue.game.map.object.Door;
import com.mac.marsrogue.game.map.object.MapObject;
import com.mac.marsrogue.game.map.tile.EmptyTile;
import com.mac.marsrogue.game.map.tile.Tile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:46 PM.
 */
public class Map {
    
    private final long seed;
    private final int width, height, depth;
    
    private FieldOfView fov;
    
    private BloodMap bloodMap;
    private SootMap sootMap;
    
    private byte[][][] tiles;
    private HashMap<Integer, Point> stairsUp;
    private HashMap<Integer, Point> stairsDown;
    
    private HashMap<Integer, List<Creature>> creatureList;
    private HashMap<Integer, Creature[][]> creatureArray;
    private HashMap<Integer, List<Item>> itemList;
    private HashMap<Integer, Item[][]> itemArray;
    private HashMap<Integer, List<MapObject>> objectList;
    private HashMap<Integer, MapObject[][]> objectArray;
    
    private HashMap<Integer, boolean[][]> explored;
    private HashMap<Integer, boolean[][]> visible;
    
    private Creature player;

    public static boolean showDijkstra = false;
    public static boolean hideFov = true;
    
    //TODO: Move to HashMap<Integer, Integer>
    private int explorableTiles;
    private int exploredTiles;
    
    public Map(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        
        initArrays();
    }
    
    private void initArrays(){
        this.tiles = new byte[width][height][depth];
        this.stairsUp = new HashMap<Integer, Point>();
        this.stairsDown = new HashMap<Integer, Point>();
        this.creatureList = new HashMap<Integer, List<Creature>>();
        this.creatureArray = new HashMap<Integer, Creature[][]>();
        this.itemList = new HashMap<Integer, List<Item>>();
        this.itemArray = new HashMap<Integer, Item[][]>();
        this.objectList = new HashMap<Integer, List<MapObject>>();
        this.objectArray = new HashMap<Integer, MapObject[][]>();
        this.explored = new HashMap<Integer, boolean[][]>();
        this.visible = new HashMap<Integer, boolean[][]>();

        this.fov = new FieldOfView(this);
        
        for(int z = 0; z < depth; z++) {
            creatureList.put(z, new ArrayList<Creature>());
            creatureArray.put(z, new Creature[width][height]);
            itemList.put(z, new ArrayList<Item>());
            itemArray.put(z, new Item[width][height]);
            objectList.put(z, new ArrayList<MapObject>());
            objectArray.put(z, new MapObject[width][height]);
            explored.put(z, new boolean[width][height]);
            visible.put(z, new boolean[width][height]);            
        }
    }
    
    public void init(){
        for(int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    setExplored(x, y, z, false);
                    setVisible(x, y, z, true);
                }
            }
        }
    }
    
    public void update(int z){
        
    }

    /* FOV Methods */

    public void computeFov(int x, int y, int z, int radius, FieldOfView.FOVType type){
        fov.clearFov();
        fov.compute(x, y, z, radius, type);
    }

    public void setExplored(int x, int y, int z, boolean explored){
        if(!inBounds(x, y, z)) return;
        if(!this.explored.get(z)[x][y] && explored && Tile.getTile(tile(x, y, z).id) instanceof EmptyTile) exploredTiles++;
        this.explored.get(z)[x][y] = explored;
    }

    public void setVisible(int x, int y, int z, boolean visible){
        if(!inBounds(x, y, z)) return;
        this.visible.get(z)[x][y] = visible;
    }

    public boolean isExplored(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        if(!hideFov) return true;
        return explored.get(z)[x][y];
    }

    public boolean isVisible(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        return visible.get(z)[x][y];
    }

    public boolean inFov(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        if(!hideFov) return true;
        return fov.inFov(x, y, z);
    }
    
    /* Util Methods */

    public Point randomEmptyPoint(int z){
        if(z > depth - 1){
            Log.warn("Cannot find point deeper than [" + (depth() - 1) + "] Input: " + z);
        }
        int x = 0, y = 0;

        do{
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }while(solid(x, y, z) || creature(x, y, z) != null);

        return new Point(x, y, z);
    }

    public boolean inBounds(int x, int y, int z){
        return x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth;
    }

    /* Entity Methods */

    //Entity Adders --------------------

    public void add(Creature c, Point spawn){
        c.x = spawn.x;
        c.y = spawn.y;
        c.z = spawn.z;
        c.init(this, creatures().size());
        creatureList.get(spawn.z).add(c);
        creatureArray.get(spawn.z)[spawn.x][spawn.y] = c;
        if(c.glyph() == '@') player = c;
    }

    public void add(Item i, Point spawn){
        i.x = spawn.x;
        i.y = spawn.y;
        i.z = spawn.z;
        i.init(this, items().size());
        itemList.get(spawn.z).add(i);
        itemArray.get(spawn.z)[spawn.x][spawn.y] = i;
    }

    public void add(MapObject o, Point spawn){
        o.x = spawn.x;
        o.y = spawn.y;
        o.z = spawn.z;
        o.init(this, objects().size());
        objectList.get(spawn.z).add(o);
        objectArray.get(spawn.z)[spawn.x][spawn.y] = o;
    }
    
    //Entity Removers --------------------

    public void remove(Creature creature){
        creatureArray.get(creature.z)[creature.x][creature.y] = null;
        creatureList.get(creature.z).remove(creature);
    }

    public void remove(Item item){
        itemArray.get(item.z)[item.x][item.y] = null;
        itemList.get(item.z).remove(item);
    }

    public void remove(MapObject object){
        objectArray.get(object.z)[object.x][object.y] = null;
        objectList.get(object.z).remove(object);
    }
    
    //Entity Movement --------------------

    public void moveCreature(Creature creature, int x, int y, int z){
        creatureArray.get(creature.z)[creature.x][creature.y] = null;
        if(z != creature.z) switchFloorArray(creature, creature.z, z);
        creature.x = x;
        creature.y = y;
        creature.z = z;
        creatureArray.get(creature.z)[creature.x][creature.y] = creature;
    }

    public boolean moveCreatureDown(Creature creature){
        if(tile(creature.x, creature.y, creature.z) != Tile.stairDown) return false;
        Point stairs = stairUp(creature.z + 1);
        if(stairs != null) return creature.moveTo(stairs.x, stairs.y, stairs.z);
        return false;
    }

    public boolean moveCreatureUp(Creature creature){
        if(tile(creature.x, creature.y, creature.z) != Tile.stairUp) return false;
        Point stairs = stairDown(creature.z - 1);
        if(stairs != null) return creature.moveTo(stairs.x, stairs.y, stairs.z);
        return false;
    }

    public void switchFloorArray(Creature creature, int oldZ, int newZ){
        creatureList.get(newZ).add(creature);
        creatureList.get(oldZ).remove(creature);
    }
    
    //Entity Getters --------------------

    public List<Creature> creatures(int z){
        return creatureList.get(z);
    }

    public List<Creature> creatures(){
        List<Creature> result = new ArrayList<Creature>();
        for(List<Creature> list : creatureList.values()) result.addAll(list);
        return result;
    }

    public List<Item> items(int z){
        return itemList.get(z);
    }

    public List<Item> items(){
        List<Item> result = new ArrayList<Item>();
        for(List<Item> list : itemList.values()) result.addAll(list);
        return result;
    }

    public Creature creature(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;
        return creatureArray.get(z)[x][y];
    }

    public Item item(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;
        return itemArray.get(z)[x][y];
    }

    public List<MapObject> objects(int z){
        return objectList.get(z);
    }

    public List<MapObject> objects(){
        List<MapObject> result = new ArrayList<MapObject>();
        for(List<MapObject> list : objectList.values()) result.addAll(list);
        return result;
    }

    public MapObject object(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;
        return objectArray.get(z)[x][y];
    }

    public List<Creature> creaturesInRange(int xp, int yp, int zp, int r){
        List<Creature> result = new ArrayList<Creature>();
        for(int y = -r; y < r + 1; y++){
            for(int x = -r; x < r + 1; x++){
                if(x * x + y * y > r * r) continue;
                Creature c = creature(xp + x, yp + y, zp);
                if(c != null) result.add(c);
            }
        }
        return result;
    }

    public Creature player(){
        return player;
    }
    
    //Entity Utils ------------------

    public boolean addAtEmptyPoint(Item item, int x, int y, int z){
        if(!inBounds(x, y, z)) return false;

        List<Point> points = new ArrayList<Point>();
        List<Point> checked = new ArrayList<Point>();

        points.add(new Point(x, y, z));

        while(!points.isEmpty()){
            Point p = points.remove(0);
            checked.add(p);

            if(solid(p.x, p.y, p.z)) continue;
            if(item(p.x, p.y, p.z) == null){
                add(item, p);
                Creature c = creature(p.x, p.y, p.z);
                if(c != null) c.notify(new ColoredString("A %s lands between your feet."), MessageLog.LogType.MESSAGE, item.name());
                return true;
            }else{
                List<Point> neighbours = p.neighboursCardinal();
                neighbours.removeAll(checked);
                points.addAll(neighbours);
            }
        }
        return false;
    }

    public boolean closedDoor(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        MapObject obj = object(x, y, z);
        if(obj == null || !(obj instanceof Door)) return false;
        Door d = (Door) obj;
        return !d.isOpen();
    }
    
    /* Map Methods */

    //Stairs Setters --------------------

    public void setUpStair(int z, Point point){
        stairsUp.put(z, point);
    }

    public void setDownStair(int z, Point point){
        stairsDown.put(z, point);
    }
    
    //Stairs Getters --------------------

    public Point stairUp(int z){
        if(z < 0 || z >= depth) return null;
        return stairsUp.get(z);
    }

    public Point stairDown(int z){
        if(z < 0 || z >= depth) return null;
        return stairsDown.get(z);
    }
    
    //Tile Getters --------------------

    public Tile tile(int x, int y, int z){
        if(!inBounds(x, y, z)) return Tile.boundsTile;
        return Tile.getTile(tiles[x][y][z]);
    }

    public boolean solid(int x, int y, int z){
        if(!inBounds(x, y, z)) return true;
        MapObject obj = object(x, y, z);
        if(object(x, y, z) != null){
            if(obj instanceof Door){
                Door d = (Door) obj;
                if(d.locked()) return true;
            }
            return false;
        }
        return tile(x, y, z).solid;
    }

    public boolean transparent(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        MapObject obj = object(x, y, z);
        if(object(x, y, z) != null){
            if(obj instanceof Door){
                Door d = (Door) obj;
                if(d.isOpen()) return true;
            }
            return false;
        }
        return tile(x, y, z).transparent;
    }
    
    //Tile Setters --------------------

    public void setTile(int x, int y, int z, Tile tile){
        if(!inBounds(x, y, z)) return;
        tiles[x][y][z] = tile.id;
    }
    
    //Tile Graphics Getters --------------------

    public char glyph(int x, int y, int z){
        if(inFov(x, y, z)){
            Creature c = creature(x, y, z);
            if(c != null) return c.glyph();

            Item i = item(x, y, z);
            if(i != null) return i.glyph();
        }

        MapObject o = object(x, y, z);
        if(o != null) return o.glyph();

        return tile(x, y, z).glyph;
    }

    public Color foregroundColor(int x, int y, int z){
        if(inFov(x, y, z)){
            Creature c = creature(x, y, z);
            if(c != null) return c.foregroundColor();

            Item i = item(x, y, z);
            if(i != null) return i.foregroundColor();

            MapObject o = object(x, y, z);
            if(o != null) return o.foregroundColor();

            Color decal = decalColor(x, y, z);
            if(decal != null) return decal.brighter();

//            if(Map.showDijkstra){
//                if(!solid(x, y, z) && inBounds(x, y, z) && dijkstraMaps().approach() != null)
//                    return Colors.getColorFromGradient(colors, positions, dijkstraMaps.approach()[x][y]);
//            }

            return tile(x, y, z).foregroundColor;
        }

        MapObject o = object(x, y, z);
        if(o != null) return o.foregroundColor().darker().darker().darker();

        return tile(x, y, z).foregroundColor.darker().darker().darker();
    }

    public Color backgroundColor(int x, int y, int z){
        if(inFov(x, y, z)){

//            if(Map.showDijkstra){
//                if(!solid(x, y, z) && inBounds(x, y, z) && dijkstraMaps().approach() != null){
//                    return Colors.getColorFromGradient(colors, positions, dijkstraMaps.approach()[x][y]);
//                }
//            }

            Color decal = decalColor(x, y, z);
            if(decal != null) return decal;

            MapObject o = object(x, y, z);
            if(o != null) return o.backgroundColor();

            return tile(x, y, z).backgroundColor;
        }
        return tile(x, y, z).backgroundColor.darker().darker().darker();
    }

    public Color decalColor(int x, int y, int z){
        if(bloodMap().color(x, y, z) == null && sootMap.color(x, y, z) == null) return null;
        Color sootAndFloor = Colors.blend(tile(x, y, z).backgroundColor, sootMap.color(x, y, z), sootMap.alpha(x, y, z));
        return Colors.blend(sootAndFloor, bloodMap.color(x, y, z), bloodMap.alpha(x, y, z));
    }
    
    //Map Getters --------------------

    public int width(){
        return width;
    }

    public int height(){
        return height;
    }

    public int depth(){
        return depth;
    }

    public long seed(){
        return seed;
    }

    public BloodMap bloodMap(){
        return bloodMap;
    }

    public SootMap sootMap(){
        return sootMap;
    }

    public int exploredPercentage(){
        return (int) (((float) exploredTiles / (float) explorableTiles) * 100);
    }

    public int explorableTiles(){
        return explorableTiles;
    }

    public int exploredTiles(){
        return exploredTiles;
    }
}
