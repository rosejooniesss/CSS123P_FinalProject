package css123p_finalproject.model;

/**
 * Geometry of the dungeon hall. 25 x 18 tiles of 32px = 800 x 576.
 *
 *   y   0 - 128   back wall (pillars, banners, portraits)
 *   y 128 - 512   walkable stone floor (carpets + centre slab)
 *   y 512 - 544   balustrade
 *   y 544 - 576   void below the balcony
 *
 * Entities are 64x64 and use their top-left corner as (x, y).
 * MIN_Y lets the head overlap the wall a little so it looks like
 * the character is standing in front of it.
 */
public final class MapLayout {
    private MapLayout() {}

    public static final int TILE = 32;
    public static final int COLS = 25;
    public static final int ROWS = 18;
    public static final int WIDTH = COLS * TILE;   // 800
    public static final int HEIGHT = ROWS * TILE;  // 576

    public static final int WALL_BOTTOM = 128;
    public static final int FLOOR_BOTTOM = 512;
    public static final int SIDE = 32;

    public static final int ENTITY_SIZE = 64;

    public static final int MIN_X = SIDE;
    public static final int MAX_X = WIDTH - SIDE - ENTITY_SIZE;
    public static final int MIN_Y = WALL_BOTTOM - 42;
    public static final int MAX_Y = FLOOR_BOTTOM - ENTITY_SIZE;

    public static int clampX(int x) { return Math.max(MIN_X, Math.min(MAX_X, x)); }
    public static int clampY(int y) { return Math.max(MIN_Y, Math.min(MAX_Y, y)); }
}
