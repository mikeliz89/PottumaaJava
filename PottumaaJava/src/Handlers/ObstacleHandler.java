package Handlers;

import Entity.Obstacles.Obstacle;

import java.awt.*;
import java.util.ArrayList;

public class ObstacleHandler extends BaseHandler {

    private ArrayList<Obstacle> obstacles;

    public ObstacleHandler() {
        obstacles = new ArrayList<>();
    }

    @Override
    protected void update() {

    }

    public void draw(Graphics2D g) {
        for(Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void add(Obstacle obstacle) {
        obstacles.add(obstacle);
    }
}
