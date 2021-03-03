package Handlers;

import Entity.Explosion;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public class ExplosionHandler extends BaseHandler {

    private ArrayList<Explosion> explosions;
    private TileMap groundTileMap;

    public ExplosionHandler(TileMap groundTileMap) {
        explosions = new ArrayList<>();
        this.groundTileMap = groundTileMap;
    }

    public void draw(Graphics2D g) {
        for (Explosion explosion : explosions) {
            explosion.setMapPosition((int) groundTileMap.getX(), (int) groundTileMap.getY());
            explosion.draw(g);
        }
    }

    public void update() {
        updateExplosions();
    }

    private void updateExplosions() {
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }
}
