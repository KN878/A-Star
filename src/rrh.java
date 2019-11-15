import java.awt.geom.Point2D;
import java.util.Stack;

public class rrh {
    //Berries
    private int berries;

    //Current position
    private Point2D position;

    //Step-counter
    private int stepCounter;

    private Point2D granny;

    private Point2D[] woodCutter = new Point2D[2];

    private Point2D prevPos = new Point2D.Float(0,0);

    //Stack to save path
    public Stack<Point2D> path = new Stack<>();

    public rrh(){
        berries = 6;
        stepCounter = 0;
        position = new Point2D.Float(0,0);
    }

    public int stepsNumber(){
        return stepCounter;
    }

    public int getBerries(){
        return berries;
    }

    public void refillBerries(){ this.berries = 6; }

    public void loseBerries(){
        this.berries -=2;
    }

    public Point2D getGranny() {
        return granny;
    }

    public void setGranny(Point2D granny){
        this.granny = granny;
    }

    public Point2D getFirstWoodCutter() { return woodCutter[0]; }

    public Point2D getSecondWoodCutter() { return woodCutter[1]; }

    public Point2D getPosition(){ return position; }


    public void setWoodCutter(Point2D[] woodCutter){
        this.woodCutter[0] = woodCutter[0];
        this.woodCutter[1] = woodCutter[1];
    }

    //Move to the next position
    public void changePosition(Point2D point){
        prevPos.setLocation(position.getX(), position.getY());
        position = point;
        path.add(point);
        stepCounter++;
    }

}
