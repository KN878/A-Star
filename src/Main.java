//In this map W - wolf, B - Bear, S- bear's range, Y-real woodcutter, N-fake woodcutter, G-Granny



import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    static boolean isWrongWCVisited = false;
    static boolean isRightWCVisited = false;

    //Check if given point is within the map
    private static boolean isExistentPoint(Map map, Point2D point){
        return map.getCell(point)!='-';
    }

    //Check if given point is bear
    private static boolean isBear(Map map, Point2D point){
        return map.getCell(point)=='B' || map.getCell(point)=='S';
    }

    //Check if given point is wolf
    private static boolean isWolf(Map map, Point2D point){
        return map.getCell(point)=='W';
    }

    /**
     * Constructs a path using parent map and destination, then RRH goes through it
     * @param parent
     * @param girl
     * @param to
     */
    private static void goTo(HashMap parent, rrh girl, Point2D to){
        List<Point2D> reversePath = new ArrayList<>();
        Point2D tmp = (Point2D) parent.get(to);
        reversePath.add(tmp);
        while(!tmp.equals(girl.getPosition())){
            tmp = (Point2D) parent.get(tmp);
            reversePath.add(tmp);
        }
        for(int i=reversePath.size()-1; i>=0; i--){
            girl.changePosition(reversePath.get(i));
        }
    }

    /**
     * Main A* algorithm
     * @param map
     * @param girl
     * @param from
     * @param to
     * @return
     */
    private static boolean aStar(Map map, rrh girl, Point2D from, Point2D to){
        //Initializing parent, moveCost, totalCost and heuristicCost maps.Also creating closedList.
        HashMap<Point2D, Point2D> parent = new HashMap<>();
        HashMap<Point2D, Integer> moveCost = new HashMap<>();
        HashMap<Point2D, Integer> totalCost = new HashMap<>();
        HashMap<Point2D, Integer> heuristicCost = new HashMap<>();
        List<Point2D> closedList = new ArrayList<>();
        PriorityQueue<Point2D, Integer> queue = new PriorityQueue<>();
        //Setting current node as an input one, it's moveCost to 0 and adding it to the priority queue based on min-heap
        Point2D current = from;
        moveCost.put(current,0);
        queue.add(current,0);

        //Containers for a neighbour cells;
        Point2D aboveGirl = new Point2D.Double();
        Point2D belowGirl = new Point2D.Double();
        Point2D leftGirl = new Point2D.Double();
        Point2D rightGirl = new Point2D.Double();

        boolean isLost = false;

        while(true){
            if(isLost) break;
            //If there is elements in the queue - remove one with the lowest totalCost and add to the closedList
            //if queue is empty it means that either girl or granny is stuck
            if(!queue.isEmpty()) {
                current = queue.remove();
                closedList.add(current);
            }
            else {
                //Check if RRH is surrounded by Wolf
                if(isWolf(map,new Point2D.Double(girl.getPosition().getX()+1,girl.getPosition().getY())) &&
                        isWolf(map,new Point2D.Double(girl.getPosition().getX(),girl.getPosition().getY()+1))){
                    isLost=true;
                    continue;
                }
                //Check if real Woodcutter is visited. If so, it means that this map is unsolvable
                if(isRightWCVisited) {
                    isLost = true;
                    continue;
                }
                //flag stands for finding angle where RRH will be surrounded with Wolf and Bear
                boolean isFoundPoint = false;
                //Walking through closedList and trying to find point where RRH will be surrounded with Wolf and Bear
                //If there is no such a point, set flag to true
                for (Point2D point : closedList) {
                    if (map.getCell(point) == 'Y') isRightWCVisited = true;
                    if (map.getCell(point) == 'N') isWrongWCVisited = true;
                    //Below will different situations with the same aim to solve the stuck problem
                    //bear is below and wolf is on the right
                    if (map.getCell(new Point2D.Double(point.getX(), point.getY() + 1)) == 'W' &&
                            map.getCell(new Point2D.Double(point.getX() + 1, point.getY())) == 'S') {
                        if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                        isFoundPoint = true;
                        //Stepping into bears range
                        Point2D tmp = new Point2D.Double(point.getX() + 1, point.getY());
                        girl.changePosition(tmp);
                        girl.loseBerries();
                        while (true) {
                            //Go through the Bear's range trying to find exit
                            tmp = new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1);
                            if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                girl.changePosition(tmp);
                                moveCost.clear();
                                queue.add(tmp, 0);
                                moveCost.put(tmp, 0);
                                break;
                            }
                            if (isBear(map,point)) {
                                girl.changePosition(tmp);
                                girl.loseBerries();
                            }
                            if (map.getCell(tmp) == 'W') {
                                girl.changePosition(new Point2D.Double(girl.getPosition().getX() + 1, girl.getPosition().getY()));
                                girl.loseBerries();
                            }
                            //If while that walk RRH lost all berries - set flag of loss to true
                            if (girl.getBerries() == 0) {
                                isLost = true;
                                break;
                            }
                        }
                    }

                    //bear is on the right and wolf is below

                    if (map.getCell(new Point2D.Double(point.getX() + 1, point.getY())) == 'W' &&
                            map.getCell(new Point2D.Double(point.getX(), point.getY() + 1)) == 'S') {
                        if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                        isFoundPoint = true;
                        Point2D tmp = new Point2D.Double(point.getX(), point.getY() + 1);
                        girl.changePosition(tmp);
                        girl.loseBerries();
                        while (true) {
                            tmp = new Point2D.Double(girl.getPosition().getX() + 1, girl.getPosition().getY());
                            if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                girl.changePosition(tmp);
                                queue.add(tmp, 0);
                                moveCost.put(tmp, 0);
                                break;
                            }
                            if (isBear(map,point)) {
                                girl.changePosition(tmp);
                                girl.loseBerries();
                            }
                            if (map.getCell(tmp) == 'W') {
                                girl.changePosition(new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1));
                                girl.loseBerries();
                            }
                            if (girl.getBerries() == 0) {
                                isLost = true;
                                break;
                            }
                        }
                    }

                    //bear is on the right and wolf is above

                    if (map.getCell(new Point2D.Double(point.getX() - 1, point.getY())) == 'W' &&
                            map.getCell(new Point2D.Double(point.getX(), point.getY() + 1)) == 'S') {
                        if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                        isFoundPoint = true;
                        Point2D tmp = new Point2D.Double(point.getX(), point.getY() + 1);
                        girl.changePosition(tmp);
                        girl.loseBerries();
                        while (true) {
                            tmp = new Point2D.Double(girl.getPosition().getX() - 1, girl.getPosition().getY());
                            if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                girl.changePosition(tmp);
                                queue.add(tmp, 0);
                                moveCost.put(tmp, 0);
                                break;
                            }
                            if (isBear(map,point)) {
                                girl.changePosition(tmp);
                                girl.loseBerries();
                            }
                            if (map.getCell(tmp) == 'W') {
                                girl.changePosition(new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1));
                                girl.loseBerries();
                            }
                            if (girl.getBerries() == 0) {
                                isLost = true;
                                break;
                            }
                        }
                    }

                    //bear is above and wolf is on the right
                    if (map.getCell(new Point2D.Double(point.getX(), point.getY() + 1)) == 'W' &&
                            map.getCell(new Point2D.Double(point.getX() - 1, point.getY())) == 'S') {
                        if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                        isFoundPoint = true;
                        Point2D tmp = new Point2D.Double(point.getX() - 1, point.getY());
                        girl.changePosition(tmp);
                        girl.loseBerries();
                        while (true) {
                            tmp = new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1);
                            if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                girl.changePosition(tmp);
                                queue.add(tmp, 0);
                                moveCost.put(tmp, 0);
                                break;
                            }
                            if (isBear(map,point)) {
                                girl.changePosition(tmp);
                                girl.loseBerries();
                            }
                            if (map.getCell(tmp) == 'W') {
                                girl.changePosition(new Point2D.Double(girl.getPosition().getX() - 1, girl.getPosition().getY()));
                                girl.loseBerries();
                            }
                            if (girl.getBerries() == 0) {
                                isLost = true;
                                break;
                            }
                        }
                    }
                    //bear is below and wolf is on the left
                    if (map.getCell(new Point2D.Double(point.getX(), point.getY() - 1)) == 'W' &&
                            map.getCell(new Point2D.Double(point.getX() + 1, point.getY())) == 'S') {
                        if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                        isFoundPoint = true;
                        // System.out.println("here");
                        Point2D tmp = new Point2D.Double(point.getX() + 1, point.getY());
                        girl.changePosition(tmp);
                        girl.loseBerries();
                        while (true) {
                            tmp = new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() - 1);
                            if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                //       System.out.println("out");
                                girl.changePosition(tmp);
                                queue.add(tmp, 0);
                                moveCost.put(tmp, 0);
                                break;
                            }
                            if (isBear(map,point)) {
                                girl.changePosition(tmp);
                                girl.loseBerries();
                            }
                            if (map.getCell(tmp) == 'W') {
                                girl.changePosition(new Point2D.Double(girl.getPosition().getX() + 1, girl.getPosition().getY()));
                                girl.loseBerries();
                            }
                            if (girl.getBerries() == 0) {
                                isLost = true;
                                System.out.println("lost all berries");
                                break;
                            }
                        }
                    }
                    if (isLost) break;
                }
                //if such point hasnt been found, find a point in which Bear is in the next point to RRH and Wolf in 2 points away from her
                if (!isFoundPoint){
                    for (Point2D point : closedList) {
                        if (map.getCell(point) == 'Y') isRightWCVisited = true;
                        if (map.getCell(point) == 'N') isWrongWCVisited = true;
                        //bear is below and wolf is on the right in 2 cells
                        if (map.getCell(new Point2D.Double(point.getX(), point.getY() + 2)) == 'W' &&
                                map.getCell(new Point2D.Double(point.getX() + 1, point.getY())) == 'S') {
                            if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                            isFoundPoint = true;
                            Point2D tmp = new Point2D.Double(point.getX() + 1, point.getY());
                            girl.changePosition(tmp);
                            girl.loseBerries();
                            while (true) {
                                tmp = new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1);
                                if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                    girl.changePosition(tmp);
                                    queue.add(tmp, 0);
                                    moveCost.put(tmp, 0);
                                    break;
                                }
                                if (isBear(map,point)) {
                                    girl.changePosition(tmp);
                                    girl.loseBerries();
                                }
                                if (map.getCell(tmp) == 'W') {
                                    girl.changePosition(new Point2D.Double(girl.getPosition().getX() + 1, girl.getPosition().getY()));
                                    girl.loseBerries();
                                }
                                if (girl.getBerries() == 0) {
                                    isLost = true;
                                    break;
                                }
                            }
                        }

                        //bear is on the right and wolf is below in 2 cells

                        if (map.getCell(new Point2D.Double(point.getX() + 2, point.getY())) == 'W' &&
                                map.getCell(new Point2D.Double(point.getX(), point.getY() + 1)) == 'S') {
                            if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                            Point2D tmp = new Point2D.Double(point.getX(), point.getY() + 1);
                            girl.changePosition(tmp);
                            girl.loseBerries();
                            while (true) {
                                tmp = new Point2D.Double(girl.getPosition().getX()+1, girl.getPosition().getY());
                                if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                    girl.changePosition(tmp);
                                    queue.add(tmp, 0);
                                    moveCost.put(tmp, 0);
                                    break;
                                }
                                if (isBear(map,point)) {
                                    girl.changePosition(tmp);
                                    girl.loseBerries();
                                }
                                if (map.getCell(tmp) == 'W') {
                                    girl.changePosition(new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1));
                                    girl.loseBerries();
                                }
                                if (girl.getBerries() == 0) {
                                    isLost = true;
                                    break;
                                }
                            }
                        }

                        //bear is on the right and wolf is above in 2 cells

                        if (map.getCell(new Point2D.Double(point.getX() - 2, point.getY())) == 'W' &&
                                map.getCell(new Point2D.Double(point.getX(), point.getY() + 1)) == 'S') {
                            if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                            isFoundPoint = true;
                            Point2D tmp = new Point2D.Double(point.getX(), point.getY() + 1);
                            girl.changePosition(tmp);
                            girl.loseBerries();
                            while (true) {
                                tmp = new Point2D.Double(girl.getPosition().getX() - 1, girl.getPosition().getY());
                                if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                    girl.changePosition(tmp);
                                    queue.add(tmp, 0);
                                    moveCost.put(tmp, 0);
                                    break;
                                }
                                if (isBear(map,point)) {
                                    girl.changePosition(tmp);
                                    girl.loseBerries();
                                }
                                if (map.getCell(tmp) == 'W') {
                                    girl.changePosition(new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1));
                                    girl.loseBerries();
                                }
                                if (girl.getBerries() == 0) {
                                    isLost = true;
                                    break;
                                }
                            }
                        }

                        //bear is above and wolf is on the right in 2 cells
                        if (map.getCell(new Point2D.Double(point.getX(), point.getY() + 2)) == 'W' &&
                                map.getCell(new Point2D.Double(point.getX() - 1, point.getY())) == 'S') {
                            if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                            isFoundPoint = true;
                            Point2D tmp = new Point2D.Double(point.getX() - 1, point.getY());
                            girl.changePosition(tmp);
                            girl.loseBerries();
                            while (true) {
                                tmp = new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() + 1);
                                if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                    girl.changePosition(tmp);
                                    queue.add(tmp, 0);
                                    moveCost.put(tmp, 0);
                                    break;
                                }
                                if (isBear(map,point)) {
                                    girl.changePosition(tmp);
                                    girl.loseBerries();
                                }
                                if (map.getCell(tmp) == 'W') {
                                    girl.changePosition(new Point2D.Double(girl.getPosition().getX() - 1, girl.getPosition().getY()));
                                    girl.loseBerries();
                                }
                                if (girl.getBerries() == 0) {
                                    isLost = true;
                                    break;
                                }
                            }
                        }
                        //bear is below and wolf is on the left in two cells
                        if (map.getCell(new Point2D.Double(point.getX(), point.getY() - 2)) == 'W' &&
                                map.getCell(new Point2D.Double(point.getX() + 1, point.getY())) == 'S') {
                            if(!girl.getPosition().equals(point)) goTo(parent,girl,point);
                            isFoundPoint = true;
                            Point2D tmp = new Point2D.Double(point.getX() + 1, point.getY());
                            girl.changePosition(tmp);
                            girl.loseBerries();
                            while (true) {
                                tmp = new Point2D.Double(girl.getPosition().getX(), girl.getPosition().getY() - 1);
                                if (!isWolf(map,tmp) && !isBear(map,tmp)) {
                                    girl.changePosition(tmp);
                                    queue.add(tmp, 0);
                                    moveCost.put(tmp, 0);
                                    break;
                                }
                                if (isBear(map,point)) {
                                    girl.changePosition(tmp);
                                    girl.loseBerries();
                                }
                                if (map.getCell(tmp) == 'W') {
                                    girl.changePosition(new Point2D.Double(girl.getPosition().getX() + 1, girl.getPosition().getY()));
                                    girl.loseBerries();
                                }
                                if (girl.getBerries() == 0) {
                                    isLost = true;
                                    break;
                                }
                            }
                        }
                        if (isLost) break;
                    }
                }
                parent.clear();
                totalCost.clear();
                closedList.clear();
                continue;
            }

            //Mark real woodcutter as visited while going through it
            if(map.getCell(current)=='Y') {
                girl.refillBerries();
                isRightWCVisited=true;
            }

            //If we reached granny check if we have 6 berries, if not - go to woodcutters
            if(to.equals(girl.getGranny())){
                if(girl.getBerries()!=6){
                    if(!isRightWCVisited){
                        if(isWrongWCVisited){
                            aStar(map,girl,current,map.getTrueWoodcutter());
                            isRightWCVisited = true;
                            parent.clear();
                            closedList.clear();
                            moveCost.clear();
                            totalCost.clear();
                            queue.add(girl.getPosition(),0);
                            moveCost.put(girl.getPosition(),0);
                            continue;
                        }
                        aStar(map,girl,current,girl.getFirstWoodCutter());
                        if(girl.getBerries()==6){
                            isRightWCVisited = true;
                            //          System.out.println("got it");
                            parent.clear();
                            closedList.clear();
                            moveCost.clear();
                            totalCost.clear();
                            queue.add(girl.getPosition(),0);
                            moveCost.put(girl.getPosition(),0);
                            continue;
                        } else{
                            aStar(map,girl,current,girl.getSecondWoodCutter());
                            parent.clear();
                            closedList.clear();
                            moveCost.clear();
                            totalCost.clear();
                            queue.add(girl.getPosition(),0);
                            moveCost.put(girl.getPosition(),0);
                            continue;
                        }
                    } else{
                        return false;
                    }
                }
            }

            //If we reached destination point - go to it
            if(current.equals(to)){
                if(current.equals(girl.getGranny()) && girl.getBerries()==6){
                    goTo(parent,girl,girl.getGranny());
                    girl.changePosition(girl.getGranny());
                    return true;
                }
                goTo(parent,girl,to);
                girl.changePosition(to);
                return true;
            }

            //Get side cells
            aboveGirl = new Point2D.Double(current.getX(),current.getY()-1);
            belowGirl = new Point2D.Double(current.getX(),current.getY()+1);
            leftGirl = new Point2D.Double(current.getX()-1,current.getY());
            rightGirl = new Point2D.Double(current.getX()+1,current.getY());

            List<Point2D> neighbours = new ArrayList<>();
            //Check if such point exists  - calculate its distance to destination and it to heuristicCost
            if(isExistentPoint(map,aboveGirl)) {
                neighbours.add(aboveGirl);
                heuristicCost.put(aboveGirl,map.getHeuristic(aboveGirl));
            }
            if(isExistentPoint(map,belowGirl)) {
                neighbours.add(belowGirl);
                heuristicCost.put(belowGirl,map.getHeuristic(belowGirl));
            }
            if(isExistentPoint(map,leftGirl)) {
                neighbours.add(leftGirl);
                heuristicCost.put(leftGirl,map.getHeuristic(leftGirl));
            }
            if(isExistentPoint(map,rightGirl)) {
                neighbours.add(rightGirl);
                heuristicCost.put(rightGirl,map.getHeuristic(rightGirl));
            }

            //A* searching
            for(Point2D point : neighbours){
                //if neighbour is already in closedList or it is forbidden point - skip it
                if(closedList.contains(point)) continue;
                if(isWolf(map,point) || isBear(map, point)) continue;
                //if there is no such point in moveCost map - calculated its moveCost, totalCost,
                // add as parent to the current node and continue looping through neighbours
                if(!moveCost.containsKey(point)){
                    parent.put(point,current);
                    moveCost.put(point,moveCost.get(current)+1);
                    totalCost.put(point,moveCost.get(current)+1+heuristicCost.get(point));
                    queue.add(point,totalCost.get(point));
                    continue;
                }
                //If point moveCost is already in the map - check if its total cost is less than one in totalCost map
                //If so - recalculate and replace point
                if(totalCost.get(point)>moveCost.get(current)+1+heuristicCost.get(point)){
                    parent.put(point,current);
                    moveCost.put(point,moveCost.get(current)+1);
                    totalCost.put(point,moveCost.get(current)+1+heuristicCost.get(point));
                    queue.add(point,totalCost.get(point));
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ArrayList<Integer> steps = new ArrayList<>();
        int counter = 0;
        //Running aStar 100 times on random maps
        for(int i=0;i<100;i++){
            rrh girl = new rrh();
            Map map = new Map(girl);
            if(aStar(map,girl,girl.getPosition(),girl.getGranny())){
                steps.add(girl.stepsNumber());
                counter++;
            }
        }

        //Calculating average steps number
        double averageSteps = 0;
        int stepsSum = 0;
        for(int i=0; i<steps.size();i++) {
            stepsSum += steps.get(i);
        }
        averageSteps = stepsSum/steps.size();
        System.out.println("Victories "+counter);
        System.out.println("Average steps number"+averageSteps);
        System.out.println(System.nanoTime()+"ns");
    }
}
