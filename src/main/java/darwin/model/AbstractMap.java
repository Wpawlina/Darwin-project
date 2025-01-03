package darwin.model;

import darwin.model.animal.AbstractAnimal;
import darwin.util.Boundary;

import java.util.ArrayList;
import java.util.HashMap;


abstract  public class AbstractMap implements   WorldMap{
    private final Boundary mapBoundery;
    private final Boundary jungleBoundery;
    private final  HashMap<Vector2d, ArrayList<AbstractAnimal>> animals=new HashMap<>();
    private final  HashMap<Vector2d, Plant> plants=new HashMap<>();
    private int plantCount=0;
    private int emptyPositionsCount=0;
    private final int  spawnPlantPerDay;
    private final int  initialPlantSpawn;
    private final int plantEnergy;










}
