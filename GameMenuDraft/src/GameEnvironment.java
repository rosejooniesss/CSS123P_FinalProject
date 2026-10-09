package gamemenudraft;

import java.util.ArrayList;
import java.util.List;

public class GameEnvironment {
    private Player player = new Player();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private MapData mapData = new MapData();

    public void update() {
        // game loop logic goes here
    }

    public Player getPlayer() { return player; }
    public MapData getMapData() { return mapData; }
}
