package darwin.util;

import darwin.model.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {
    public boolean contains(Vector2d position){
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }
    public int size(){
        return (((upperRight.getX() - lowerLeft().getX()) + 1) * ((upperRight().getY() - lowerLeft().getY()) + 1));
    }
    public HashSet<Vector2d> generateSpaces(){
        HashSet<Vector2d> result = new HashSet<>();
        for(int x = lowerLeft.getX(); x <= upperRight.getX(); x++){
            for(int y = lowerLeft.getY(); y <= upperRight.getY(); y++){
               result.add(new Vector2d(x, y));
            }
        }
        return result;
    }
    public ArrayList<Vector2d> generateArraySpaces(){
        ArrayList<Vector2d> result = new ArrayList<>();
        for(int x = lowerLeft.getX(); x <= upperRight.getX(); x++){
            for(int y =lowerLeft.getY(); y <= upperRight.getY(); y++){
                result.add(new Vector2d(x, y));
            }
        }
        return result;

    }

    public Vector2d upperRight(){
        return upperRight;
    }

    public Vector2d lowerLeft(){
        return lowerLeft;
    }
}
