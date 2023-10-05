package strategies;

import java.nio.file.Path;

public class Constants {
    //Scenario settings
    public static final int SEED = 1;
    public static final boolean VERBOSE = false;
    public static final int MAX_PLAYER_PRINT = 10;
    public static final String PRINT_TAB = "    ";

    //Data paths
    public static final String DATA_PATH = "data/";
    public static final Path CHUNK_FOLDER = Path.of(DATA_PATH + "chunks");
    public static final String CHUNK_PATH = DATA_PATH + "chunks/";
    public static final String CHANGE_FILE = DATA_PATH + "changes";
    public static final String INFO_FILE = DATA_PATH + "info";

    //Map Settings
    public static final float MAP_SIZE = 1000;
    public static final float PLAYER_MOVE_MAGNITUDE = 1;
    public static final float BOT_SPAWN_DISTANCE_FROM_PLAYER = MAP_SIZE/10;

    //Chunk Settings
    public static boolean DYNAMIC_CHUNK_SIZE = false;
    public static boolean USE_CHANGE_FILE = true;
    public static boolean USE_GZIP = true;
    public static int STATIC_CHUNK_AMOUNT = 2; //Real amount will be STATIC_CHUNK_AMOUNT * STATIC_CHUNK_AMOUNT
    public static float STATIC_CHUNK_SIZE = MAP_SIZE/STATIC_CHUNK_AMOUNT;
    public static int CHUNK_GROUP_MIN_ELEMENTS = 100;
    public static int CHUNK_MAX_ELEMENTS = 1000;
}
