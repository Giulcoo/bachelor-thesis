package strategies;

import java.lang.Character;

public class Constants {
    //Data paths
    public static final String DATA_PATH = "data/";
    public static final String CHANGE_FILE = DATA_PATH + "changes";

    //Map Settings
    public static final float MAP_SIZE = 100;

    //Chunk Settings
    public static final int STATIC_CHUNK_AMOUNT = 5; //Real amount will be STATIC_CHUNK_AMOUNT * STATIC_CHUNK_AMOUNT
    public static final float STATIC_CHUNK_SIZE = MAP_SIZE/STATIC_CHUNK_AMOUNT;
    public static final int CHUNK_GROUP_MIN_ELEMENTS = 10;
    public static final int CHUNK_MAX_ELEMENTS = 100;
}
