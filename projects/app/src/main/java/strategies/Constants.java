package strategies;

import java.lang.Character;

public class Constants {
    // Data Path
    public static final String DATA_PATH = "data/";

    // Types
    public static final Character CHARACTER_TYPE = 'c';
    public static final Character ITEM_TYPE = 'i';
    public static final Character OBSTACLE_TYPE = 'o';

    // Equipment
    public static final String WEAPON_TYPE = "Weapon";
    public static final String HEAD_ARMOR_TYPE = "Head Armor";
    public static final String BODY_ARMOR_TYPE = "Body Armor";
    public static final String LEG_ARMOR_TYPE = "Leg Armor";
    public static final String FOOT_ARMOR_TYPE = "Foot Armor";

    public static final String[] WEAPONS = {"Sword", "Axe", "Bow", "Spear", "Dagger"};
    public static final String[] HEAD_ARMORS = {"Helmet", "Hood", "Cap", "Crown", "Hat"};
    public static final String[] BODY_ARMORS = {"Cuirass", "Robe", "Vest", "Shirt", "Jacket"};
    public static final String[] LEG_ARMORS = {"Greaves", "Pants", "Leggings", "Shorts", "Skirt"};
    public static final String[] FOOT_ARMORS = {"Boots", "Shoes", "Slippers", "Socks", "Sandals"};

    // Map
    public static final int MAP_SIZE = 100;
    public static final double RENDER_DISTANCE = 23.0878*2;
}
