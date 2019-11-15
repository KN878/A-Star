
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Map {
    private char map[][] = new char[9][9];
    private Point2D trueWoodcutter = new Point2D.Float();
    private int costMap[][] = new int[9][9];
    public Map(rrh girl){
        generateMap(girl);
    }

    //Get distance from each point to destination
    public int getHeuristic(Point2D point){
        return costMap[(int)point.getX()][(int)point.getY()];
    }


    private void generateMap(rrh girl){
        generateBear();
        generateWolf();
        girl.setGranny(generateGranny());
        girl.setWoodCutter(generateWoodCutter());
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                costMap[i][j] = Math.abs(i - (int)girl.getGranny().getX()) + Math.abs(j - (int)girl.getGranny().getY());
            }
        }
    }

    private void generateBear(){
        Random random = new Random();
        int x = random.nextInt(9);
        int y = random.nextInt(9);
        while((x==0 && y==0) || (x==0 && y==1) || (x==1 && y==0) || (x==1 && y==1)){
            x = random.nextInt(9);
            y = random.nextInt(9);
        }
        map[x][y] = 'B';
        //Range above bear
        for(int i=0; i<3; i++){
            if(x-1+i>=0 && y-1>=0){
                if(x-1+i<9) map[x-1+i][y-1] = 'S';
            }
        }
        //Range below bear
        for(int i=0; i<3; i++){
            if(x-1+i>=0 && y+1<9){
                if(x-1+i<9) map[x-1+i][y+1] = 'S';
            }
        }
        //Range on the right of bear
        if(x-1>=0) map[x-1][y] = 'S';
        //Range on the right of bear
        if(x+1<9) map[x+1][y] = 'S';
    }

    private void generateWolf(){
        Random random = new Random();
        int x = random.nextInt(9);
        int y = random.nextInt(9);
        while(((x==0 && y==0) || (x==1 && y==0) || (x==0 && y==1)) || map[x][y] =='B'){
            x = random.nextInt(9);
            y = random.nextInt(9);
        }
        map[x][y] = 'W';
        if(y+1<9) map[x][y+1] = 'W';
        if(y-1>=0) map[x][y-1] = 'W';
        if(x-1>=0) map[x-1][y] = 'W';
        if(x+1<9) map [x+1][y] = 'W';
    }

    private Point2D generateGranny(){
        Random random = new Random();
        int x = random.nextInt(9);
        int y = random.nextInt(9);
        while(map[x][y] != 0 ||(x==0 && y==0)){
            x = random.nextInt(9);
            y = random.nextInt(9);
        }
        map[x][y] = 'G';
        Point2D granny = new Point2D.Float(x,y);
        return granny;
    }

    private Point2D[] generateWoodCutter(){
        Point2D woodCutter[] = new Point2D[2];
        Random random = new Random();
        int x = random.nextInt(9);
        int y = random.nextInt(9);
        while(map[x][y] != 0 ||(x==0 && y==0)){
            x = random.nextInt(9);
            y = random.nextInt(9);
        }
        map[x][y] = 'Y';
        trueWoodcutter.setLocation(x,y);
        woodCutter[0] = new Point2D.Float(x,y);
        x = random.nextInt(9);
        y = random.nextInt(9);
        while(map[x][y] != 0 ||(x==0 && y==0)){
            x = random.nextInt(9);
            y = random.nextInt(9);
        }
        map[x][y] = 'N';
        woodCutter[1] = new Point2D.Float(x,y);
        x = random.nextInt(9)+1;
        Point2D tmp;
        for(int i=0; i<x; i++){
            tmp = woodCutter[0];
            woodCutter[0] = woodCutter[1];
            woodCutter[1] = tmp;
        }
        return woodCutter;
    }

    //Get char in the given cell
    public char getCell(Point2D point){
        if(point.getX()>=0 && point.getX()<=8 && point.getY()>=0 && point.getY()<=8){
            return map[(int)point.getX()][(int)point.getY()];
        }
        return '-';
    }

    public Point2D getTrueWoodcutter(){
        return trueWoodcutter;
    }

}
