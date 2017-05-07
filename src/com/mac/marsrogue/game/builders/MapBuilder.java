package com.mac.marsrogue.game.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Maths;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.game.builders.chunks.Chunk;
import com.mac.marsrogue.game.builders.chunks.Corridor;
import com.mac.marsrogue.game.builders.chunks.Room;
import com.mac.marsrogue.game.map.tile.Tile;
import com.mac.marsrogue.game.map.Map;

import java.util.*;


/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:32 PM.
 */
public class MapBuilder {

    private final Random random;
    private final long seed;
    private final int width, height, depth;
    private final int chunkSize;

    private Map map;
    private HashMap<Integer, Chunk[][]> chunks;
    private HashMap<Integer, List<List<Point>>> rooms;
    private HashMap<Integer, List<ConnectionPoint>> roomConnectionPoints;

    private int corridorsPlaced = 0;

    public MapBuilder(int width, int height, int depth, int chunkSize){
        this(width, height, depth, chunkSize, new Random().nextLong());
    }

    public MapBuilder(int width, int height, int depth, int chunkSize, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.chunkSize = chunkSize;
        this.seed = seed;
        this.random = new Random();

        init();
    }

    /** Init Methods **/

    private void init(){
        this.map = new Map(width, height, depth, seed);
        this.chunks = new HashMap<Integer, Chunk[][]>();
        this.rooms = new HashMap<Integer, List<List<Point>>>();
        this.roomConnectionPoints = new HashMap<Integer, List<ConnectionPoint>>();

        Log.info("Generating " + width + "x" + height + "x" + depth +" map");
        Log.info("Seed: " + seed);
    }

    private void initLevel(int z){
        chunks.put(z, new Chunk[width / chunkSize][height / chunkSize]);
        rooms.put(z, new ArrayList<List<Point>>());
        roomConnectionPoints.put(z, new ArrayList<ConnectionPoint>());
    }

    /** Build Methods **/

    public Map build(){
        return map;
    }

    public MapBuilder generate(){
        int rejects = 0;
        double start = System.nanoTime();

        for(int z = 0; z < depth; z++){
            boolean isValid = false;
            do{
                initLevel(z);

                scatterRooms(z, 0.25f); //done
                placeCorridors(z, 3, 2); //done
                if(!checkCorridors(z)){
                    rejects++;
                    continue;
                }

                removeIsolatedRooms(z); //done
                placeRooms(z, 0.4f); //done
                growRooms(z, 0.15f); //done
                cleanupRooms(z, 7);

                isValid = levelValid(z);
                if(!isValid){
                    rejects++;
                    continue;
                }

                Log.debug("Generated level " + (z + 1));
            }while(!isValid);


            findConnectionPoints(z);
            removeDeadends(z);

            placeTiles(z);
            placeConnections(z);
            placeWalls(z);
            placeStairs(z);
        }

        Log.info("Generated map in " + ((System.nanoTime() - start) / 1000000) + "ms");
        Log.debug("Rejected " + rejects + " levels.");
        return this;
    }

    /** Generation Methods **/

    private boolean levelValid(int z){
        return true;
    }

    //Rooms -----------------------------------

    private void scatterRooms(int z, float frequency){
        for(int y = 1; y < (height / chunkSize) - 1; y++){
            for(int x = 1; x < (width / chunkSize) - 1; x++){
                if(x % 2 != 0 && y % 2 != 0){
                    if(random.nextFloat() < frequency) setChunk(x, y, z, new Room(x, y, z));
                }
            }
        }
    }

    public void placeRooms(int z, float frequency){
        int roomCount = 0;
        int roomsNeeded = (int) ((width / chunkSize) * (height / chunkSize) * 0.12);

        while(roomCount < roomsNeeded){
            int x = random.nextInt(width / chunkSize);
            int y = random.nextInt(height / chunkSize);
            if(x < 1 || y < 1 || x >= width / chunkSize - 1 || y >= height / chunkSize - 1) continue;
            if(chunk(x, y, z) != null) continue;

            boolean reduceChance = false;
            if(chunk(x - 1, y, z) instanceof Corridor && chunk(x + 1, y, z) instanceof Corridor || chunk(x, y - 1, z) instanceof Corridor && chunk(x, y + 1, z) instanceof Corridor) reduceChance = true;

            float chance = reduceChance ? frequency * 0.25f : frequency;
            if(random.nextFloat() <= chance){
                Room newRoom = new Room(x, y, z);
                //if(random.nextFloat() < 0.1f) newRoom = new StorageRoom(x, y, z);

                setChunk(x, y, z, newRoom);
                roomCount++;
            }
        }
    }

    private void growRooms(int z, float chanceToGrow){
        for(int y = 0; y < height / chunkSize; y++){
            for(int x = 0; x < width / chunkSize; x++){
                if(chunk(x, y, z) instanceof Room){
                    if(random.nextFloat() <= chanceToGrow){
                        List<Point> pointsToGrow = emptyChunksNextTo(x, y, z);
                        if(pointsToGrow.isEmpty()) continue;
                        int growBy = pointsToGrow.size() == 1 ? 1 : random.nextInt(pointsToGrow.size() - 1) + 1;
                        for(int i = 0; i < growBy; i++){
                            Point p = pointsToGrow.get(i);
                            if(p.x < 1 || p.y < 1 || p.x >= width / chunkSize - 1 || p.y >= height / chunkSize - 1) continue;
                            setChunk(p.x, p.y, p.z, new Room(p.x, p.y, p.z));
                        }
                    }
                }
            }
        }
    }

    private void removeIsolatedRooms(int z){
        for(int y = 1; y < (height / chunkSize) - 1; y++){
            for(int x = 1; x < (width / chunkSize) - 1; x++){
                Chunk c = chunk(x, y, z);
                if(c instanceof Room){
                    if(isRoomIsolated(x, y, z)) setChunk(x, y, z, null);
                }
            }
        }
    }

    private void cleanupRooms(int z, int maxRoomSize){
        for(int y = 0; y < height / chunkSize; y++){
            for(int x = 0; x < width / chunkSize; x++){
                if(chunk(x, y, z) instanceof Room){
                    List<Point> roomNextTo = pointsNextToRoom(x, y, z);
                    roomNextTo.add(new Point(x, y, z));
                }
            }
        }

        List<List<Point>> toRemove = new ArrayList<List<Point>>();

        for(List<Point> room : rooms.get(z)){
            if(!roomTouchesCorridor(room) || room.size() > maxRoomSize){
                for(Point p : room) setChunk(p.x, p.y, p.z, null);
                toRemove.add(room);
            }
        }

        rooms.get(z).removeAll(toRemove);
    }

    //Corridors -----------------------------------

    private void placeCorridors(int z, int corridorWidth, int lengthBeforeTurn){
        int x = 0;
        int y = 0;
        do{
            x = random.nextInt(width / chunkSize);
            while(x % 2 == 0) x = random.nextInt(width / chunkSize);
            y = random.nextInt(height / chunkSize);
            while(y % 2 == 0) y = random.nextInt(height / chunkSize);
        }while(chunk(x, y, z) != null);

        corridorsPlaced = 1;
        setChunk(x, y, z, new Corridor(x, y, z, corridorWidth));
        growCorridor(x, y, z, corridorWidth, lengthBeforeTurn);
    }

    private void growCorridor(int x, int y, int z, int corridorWidth, int lengthBeforeTurn){
        Integer[] randDirs = randomDirections();

        if(lengthBeforeTurn < 2) lengthBeforeTurn = 2;

        for(int i = 0; i < randDirs.length; i++){
            switch(randDirs[i]){
                case 1:
                    if(x - lengthBeforeTurn <= 1) continue;
                    if(chunk(x - lengthBeforeTurn, y, z) == null){
                        for(int j = 0; j <= lengthBeforeTurn; j++){
                            setChunk(x - j, y, z, new Corridor(x - j, y, z, corridorWidth));
                            if(chunk(x - j, y, z) != null) corridorsPlaced++;
                        }
                        growCorridor(x - lengthBeforeTurn, y, z, corridorWidth, lengthBeforeTurn);
                    }
                    break;
                case 2:
                    if(y - lengthBeforeTurn <= 1) continue;
                    if(chunk(x, y - lengthBeforeTurn, z) == null){
                        for(int j = 0; j <= lengthBeforeTurn; j++){
                            setChunk(x, y - j, z, new Corridor(x, y - j, z, corridorWidth));
                            if(chunk(x, y - j, z) != null) corridorsPlaced++;
                        }
                        growCorridor(x, y - lengthBeforeTurn, z, corridorWidth, lengthBeforeTurn);
                    }
                    break;
                case 3:
                    if(x + lengthBeforeTurn >= width / chunkSize - 1) continue;
                    if(chunk(x + lengthBeforeTurn, y, z) == null){
                        for(int j = 0; j <= lengthBeforeTurn; j++){
                            setChunk(x + j, y, z, new Corridor(x + j, y, z, corridorWidth));
                            if(chunk(x + j, y, z) != null) corridorsPlaced++;
                        }
                        growCorridor(x + lengthBeforeTurn, y, z, corridorWidth, lengthBeforeTurn);
                    }
                    break;
                case 4:
                    if(y + lengthBeforeTurn >= height / chunkSize - 1) continue;
                    if(chunk(x, y + lengthBeforeTurn, z) == null){
                        for(int j = 0; j <= lengthBeforeTurn; j++){
                            setChunk(x, y + j, z, new Corridor(x, y + j, z, corridorWidth));
                            if(chunk(x, y + j, z) != null) corridorsPlaced++;
                        }
                        growCorridor(x, y + lengthBeforeTurn, z, corridorWidth, lengthBeforeTurn);
                    }
                    break;
            }
        }
    }

    private boolean checkCorridors(int z){
        if(corridorsPlaced < (width / chunkSize) * (height / chunkSize) * 0.4) return false;
        return true;
    }

    private void removeDeadends(int z){
        boolean done = false;
        while(!done){
            done = true;

            for(int y = 0; y < height / chunkSize; y++){
                for(int x = 0; x < width / chunkSize; x++){
                    if(chunk(x, y, z) instanceof Corridor){
                        int exits = 0;

                        for(Point p : new Point(x, y, z).neighboursCardinal()){
                            if(chunk(p.x, p.y, p.z) instanceof Corridor || isConnectionPoint(new Point(x, y, z), p)) exits++;
                        }

                        if(exits != 1) continue;
                        done = false;
                        setChunk(x, y, z, null);
                    }
                }
            }
        }
    }

    //Tiles -----------------------------------

    private void placeTiles(int z){
        if(z == 0){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(random.nextBoolean()) map.setTile(x, y, z, Tile.sandyGround1);
                    else map.setTile(x, y, z, Tile.sandyGround2);
                }
            }
        }

        for(int y = 0; y < height / chunkSize; y++){
            for(int x = 0; x < width / chunkSize; x++){
                if(chunk(x, y, z) == null) continue;
                chunk(x, y, z).place(this, map, chunkSize, random);
            }
        }
    }

    private void placeConnections(int z){
        for(ConnectionPoint c : roomConnectionPoints.get(z)){
            Room r = (Room) chunk(c.roomConnection.x, c.roomConnection.y, c.roomConnection.z);
            r.connectToCorridor(this, map, c.corridorConnection, chunkSize, random);
        }
    }

    private void placeWalls(int z){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(map.tile(x, y, z) != Tile.floorTile && map.tile(x, y, z) != Tile.glassHori && map.tile(x, y, z) != Tile.glassVert){
                    for(Point p : new Point(x, y, z).neighboursAll()){
                        Tile t = map.tile(p.x, p.y, p.z);
                        if(t == Tile.floorTile){
                            map.setTile(x, y, z, Tile.wallCenter);
                            break;
                        }
                    }
                }
            }
        }

        Tile[][] newWalls = new Tile[width][height];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(Tile.isWall(map.tile(x, y, z).id)){
                    newWalls[x][y] = Tile.wallCenter;

                    if(Tile.isWall(map.tile(x + 1, y, z).id) || Tile.isWall(map.tile(x - 1, y, z).id)) newWalls[x][y] = Tile.wallHori;
                    if(Tile.isWall(map.tile(x, y + 1, z).id) || Tile.isWall(map.tile(x, y - 1, z).id)) newWalls[x][y] = Tile.wallVert;

                    if(Tile.isWall(map.tile(x + 1, y, z).id) && Tile.isWall(map.tile(x, y - 1 , z).id)) newWalls[x][y] = Tile.wallSW;
                    if(Tile.isWall(map.tile(x - 1, y, z).id) && Tile.isWall(map.tile(x, y - 1, z).id)) newWalls[x][y] = Tile.wallSE;

                    if(Tile.isWall(map.tile(x + 1, y, z).id) && Tile.isWall(map.tile(x, y + 1, z).id)) newWalls[x][y] = Tile.wallNW;
                    if(Tile.isWall(map.tile(x - 1, y, z).id) && Tile.isWall(map.tile(x, y + 1, z).id)) newWalls[x][y] = Tile.wallNE;

                    if(Tile.isWall(map.tile(x + 1, y, z).id) && Tile.isWall(map.tile(x - 1, y, z).id) && Tile.isWall(map.tile(x, y - 1, z).id)) newWalls[x][y] = Tile.wallHoriN;
                    if(Tile.isWall(map.tile(x + 1, y, z).id) && Tile.isWall(map.tile(x - 1, y, z).id) && Tile.isWall(map.tile(x, y + 1, z).id)) newWalls[x][y] = Tile.wallHoriS;

                    if(Tile.isWall(map.tile(x, y + 1, z).id) && Tile.isWall(map.tile(x, y - 1, z).id) && Tile.isWall(map.tile(x - 1, y, z).id)) newWalls[x][y] = Tile.wallVertW;
                    if(Tile.isWall(map.tile(x, y + 1, z).id) && Tile.isWall(map.tile(x, y - 1, z).id) && Tile.isWall(map.tile(x + 1, y, z).id)) newWalls[x][y] = Tile.wallVertE;

                    if(Tile.isWall(map.tile(x + 1, y, z).id) && Tile.isWall(map.tile(x - 1, y, z).id) && Tile.isWall(map.tile(x, y - 1, z).id) && Tile.isWall(map.tile(x, y + 1, z).id)) newWalls[x][y] = Tile.wallCenter;

                    if(Tile.isWall(map.tile(x, y - 1, z).id) && !Tile.isWall(map.tile(x, y + 1, z).id) && !Tile.isWall(map.tile(x - 1, y, z).id) && !Tile.isWall(map.tile(x + 1, y, z).id)) newWalls[x][y] = Tile.wallEndS;
                    if(Tile.isWall(map.tile(x, y + 1, z).id) && !Tile.isWall(map.tile(x, y - 1, z).id) && !Tile.isWall(map.tile(x - 1, y, z).id) && !Tile.isWall(map.tile(x + 1, y, z).id)) newWalls[x][y] = Tile.wallEndN;

                    if(Tile.isWall(map.tile(x + 1, y, z).id) && !Tile.isWall(map.tile(x - 1, y, z).id) && !Tile.isWall(map.tile(x, y - 1, z).id) && !Tile.isWall(map.tile(x, y + 1, z).id)) newWalls[x][y] = Tile.wallEndE;
                    if(Tile.isWall(map.tile(x - 1, y, z).id) && !Tile.isWall(map.tile(x + 1, y, z).id) && !Tile.isWall(map.tile(x, y - 1, z).id) && !Tile.isWall(map.tile(x, y + 1, z).id)) newWalls[x][y] = Tile.wallEndW;
                }
            }
        }

        for(int y = 0; y < height; y++) for(int x = 0; x < width; x++) if(newWalls[x][y] != null) map.setTile(x, y, z, newWalls[x][y]);
    }

    private void placeStairs(int z){
        Point up, down;
        int distance = 0;

        do{
            up = new Point(random.nextInt(width), random.nextInt(height), z);
            down = new Point(random.nextInt(width), random.nextInt(height), z);
            if(map.tile(up.x, up.y, z).solid || map.tile(down.x, down.y, z).solid) continue;
            if(!(chunk(up.x / chunkSize, up.y / chunkSize, z) instanceof Room) || !(chunk(down.x / chunkSize, down.y / chunkSize, z) instanceof Room)) continue;

            distance = Maths.distance(up.x, up.y, down.x, down.y);
        }while(distance < width * 0.5);

        if(z != 0){
            map.setUpStair(z, up);
            map.setTile(up.x, up.y, z, Tile.stairUp);
        }

        if(z != depth - 1){
            map.setDownStair(z, down);
            map.setTile(down.x, down.y, z, Tile.stairDown);
        }
    }

    /** Helper Methods **/

    public boolean isConnectionPoint(Point corridor, Point toCheck){
        for(ConnectionPoint c : roomConnectionPoints.get(corridor.z)) if(c.corridorConnection.equals(corridor) && c.roomConnection.equals(toCheck)) return true;
        return false;
    }

    private List<Point> pointsNextToRoom(int x, int y, int z){
        Point check = new Point(x, y, z);
        for(List<Point> room : rooms.get(z)) for(Point p : room) if(p.neighboursCardinal().contains(check)) return room;
        List<Point> newRoom = new ArrayList<Point>();
        rooms.get(z).add(newRoom);
        return newRoom;
    }

    private boolean roomTouchesCorridor(List<Point> room){
        for(Point p : room) for(Point n : p.neighboursCardinal()) if(chunk(n.x, n.y, n.z) instanceof Corridor) return true;
        return false;
    }

    private void findConnectionPoints(int z){
        for(List<Point> room : rooms.get(z)){
            List<Point> nextToCorridor = pointsNextToCorridor(room);
            if(!nextToCorridor.isEmpty()){
                Collections.shuffle(nextToCorridor);

                Point roomConnection = nextToCorridor.get(random.nextInt(nextToCorridor.size()));
                Point corridorConnection = corridorNextToRoom(roomConnection);
                if(corridorConnection == null) Log.warn("Room could not find adjacent corridor to connect to.");

                roomConnectionPoints.get(z).add(new ConnectionPoint(roomConnection, corridorConnection));
            }
        }
    }

    private List<Point> pointsNextToCorridor(List<Point> room){
        List<Point> result = new ArrayList<Point>();
        for(Point p : room) {
            for(Point n : p.neighboursCardinal()){
                if(chunk(n.x, n.y, n.z) instanceof Corridor){
                    result.add(p);
                    break;
                }
            }
        }
        return result;
    }

    private Point corridorNextToRoom(Point p){
        for(Point n : p.neighboursCardinal()) if(chunk(n.x, n.y, n.z) instanceof Corridor) return n;
        return null;
    }

    private List<Point> emptyChunksNextTo(int x, int y, int z){
        List<Point> result = new ArrayList<Point>();
        for(Point p : new Point(x, y, z).neighboursCardinal()) if(chunk(p.x, p.y, p.z) == null) result.add(p);
        return result;
    }

    private boolean isRoomIsolated(int x, int y, int z){
        for(Point p : new Point(x, y, z).neighboursCardinal()){
            Chunk c = chunk(p.x, p.y, z);
            if(c != null) return false;
        }
        return true;
    }

    /** Util Methods **/



    public void setChunk(int x, int y, int z, Chunk chunk){
        if(!inChunkBounds(x, y, z)) return;
        chunks.get(z)[x][y] = chunk;
    }

    public Chunk chunk(int x, int y, int z){
        if(!inChunkBounds(x, y, z)) return null;
        return chunks.get(z)[x][y];
    }

    private boolean inChunkBounds(int x, int y, int z){
        return x >= 0 && y >= 0 && z >= 0 && x < width / chunkSize && y < height / chunkSize && z < depth;
    }

    private Integer[] randomDirections(){
        List<Integer> r = new ArrayList<Integer>();
        for(int i = 0; i < 4; i++) r.add(i + 1);
        Collections.shuffle(r);
        return r.toArray(new Integer[4]);
    }

    public MapBuilder generateEmptyWalledMap(){
        initLevel(0);
        Log.debug("Generating empty walled map");
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(x == 0 || y == 0 || x == width - 1 || y == height - 1) map.setTile(x, y, z, Tile.wallCenter);
                    else map.setTile(x, y, z, Tile.floorTile);
                }
            }
        }

        return this;
    }

    public MapBuilder randomScatterTile(float density, Tile tile){
        Log.debug("Scattering tile " + tile.id + " " + density + "/1");
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(random.nextFloat() < density) map.setTile(x, y, z, tile);
                }
            }
        }

        return this;
    }

    public class ConnectionPoint{
        public Point roomConnection;
        public Point corridorConnection;

        public ConnectionPoint(Point roomConnection, Point corridorConnection) {
            this.roomConnection = roomConnection;
            this.corridorConnection = corridorConnection;
        }
    }
}
