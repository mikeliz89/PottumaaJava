package Handlers;

import Entity.Player.Player;
import MapPoint.MapPoint;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public class MapPointHandler extends BaseHandler {

    private Player player;
    private ArrayList<MapPoint> mapPoints;
    private boolean playerIsInMapPoint = false;
    private TileMap groundTileMap;

    public MapPointHandler(Player player, TileMap groundTileMap) {
        this.player = player;
        this.groundTileMap = groundTileMap;
        mapPoints = new ArrayList<>();
    }

    public boolean playerIsInMapPoint() {
        return playerIsInMapPoint;
    }

    public void add(MapPoint mapPoint) {
        mapPoints.add(mapPoint);
    }

    public void update() {
        isPlayerInMapPoint();
    }

    public void draw(Graphics2D g) {
        for (MapPoint mapPoint : mapPoints) {
            mapPoint.setMapPosition((int) groundTileMap.getX(), (int) groundTileMap.getY());
            mapPoint.draw(g);
        }
    }

    private void isPlayerInMapPoint() {
        MapPoint mapPointToChangeTo = getMapPointToChangeTo();
        player.setMapPointForLevelChange(mapPointToChangeTo);
        if(mapPointToChangeTo != null) {
            playerIsInMapPoint = true;
            return;
        }
        playerIsInMapPoint = false;
    }

    private MapPoint getMapPointToChangeTo() {
        MapPoint mapPointToChangeTo = null;
        for(MapPoint mapPoint : mapPoints) {
            Rectangle mapPointRectangle = mapPoint.getRectangle();
            Rectangle playerRectangle = player.getRectangle();
            if(playerRectangle.intersects(mapPointRectangle)) {
                mapPointToChangeTo = mapPoint;
            }
        }
        return mapPointToChangeTo;
    }
}
