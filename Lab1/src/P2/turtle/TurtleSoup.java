/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package src.P2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TurtleSoup {
    /**
     * 利用Turtle接口的成员函数画一个正方形
     * <p>
     * Draw a square.
     *
     * @param turtle     the Lab_1.turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle,int sideLength) {
        for(int i = 0; i < 4; i++) {
            turtle.forward(sideLength);
            turtle.turn(90);
        }
    }

    /**
     * 根据边数sides计算多边形的内角度数
     * <p>
     * Determine inside angles of a regular polygon.
     * <p>
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     *
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        if(sides >= 2)
            return (sides - 2) * 180 / (double)sides;
        else {
            System.out.println("不存在" + sides + "条边的多边形");
            return - 1;
        }
    }

    /**
     * 根据内角度数angle计算多边形的边数sides
     * <p>
     * Determine number of sides given the size of interior angles of a regular polygon.
     * <p>
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     *
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return (int) Math.round((360 / (180 - angle)));
    }

    /**
     * 给出正多边形的边数和边长,用turtle画出这个正多边形
     * <p>
     * Given the number of sides, draw a regular polygon.
     * <p>
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     *
     * @param turtle     the Lab_1.turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle,int sides,int sideLength) {
        if(sides <= 2) {
            System.out.println("不存在" + sides + "条边的多边形");
            return;
        }
        for(int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(180 - calculateRegularPolygonAngle(sides));
        }
        turtle.draw();
    }

    /**
     * 给出一个角度值和两个点,求上一个点到下一个点的向量与该角度值的差
     * <p>
     * 特别注意:起始角是按y正半轴方向算,转动方向是顺!时!针!
     * <p>
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * <p>
     * The return value is the angle input to turn() that would point the Lab_1.turtle in the direction of
     * the target point (targetX,targetY), given that the Lab_1.turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360.
     * <p>
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     *
     * @param currentBearing current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     * must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing,int currentX,int currentY,int targetX,int targetY) {
        double targetBearing; // 移动时的方向角度
        double changeBearing; // 需要转动的角度
        if(currentX == targetX) { // 起点与终点的横坐标相等,看起点和终点的纵坐标大小比较
            if(targetY >= currentY) // 终点的纵坐标大,转过的角度是0
                targetBearing = 0;
            else// 起点与终点的横坐标相等,看起点和终点的纵坐标大小比较
                targetBearing = 180; // 终点的纵坐标大,转过的角度是0
        }else if(currentY == targetY) {
            if(targetX > currentX)
                targetBearing = 90;
            else
                targetBearing = 270;
        }else { // 否则,最终的角度可以根据反正切函数得出
            double detX = targetX - currentX, detY = targetY - currentY;
            targetBearing = Math.abs(Math.atan(detX / detY)) / Math.PI * 180; //这个角只是向量与y轴成的锐角
            if(detX < 0 && detY > 0) // 第二象限
                targetBearing = 360 - targetX;
            else if(detX < 0 && detY < 0) // 第三象限
                targetBearing += 180;
            else if(detX > 0 && detY < 0) // 第四象限
                targetBearing = 180 - targetBearing;
        }
        changeBearing = targetBearing - currentBearing;
        if(changeBearing < 0)
            changeBearing += 360;
        return changeBearing;
    }

    /**
     * 分别计算一组点中一个点到下一个点时,还需转过的角度值(BearingsToPoint)
     * <p>
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * <p>
     * Assumes that the Lab_1.turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the Lab_1.turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     *
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     * otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords,List<Integer> yCoords) {
        ArrayList<Double> Bearings = new ArrayList<>();
        double currentBearing = 0;
        for(int i = 0; i < xCoords.size() - 1; i++) {
            currentBearing = calculateBearingToPoint(currentBearing,xCoords.get(i),yCoords.get(i),xCoords.get(i + 1),yCoords.get(i + 1));
            Bearings.add(currentBearing);
        }
        return Bearings;
    }

    /**
     * 计算一组输入点中所有点的最小凸包,就是给定平面点集,找出该点集中最外圈的点构成凸多边形,使得该平面点集中所有的点都在该凸多边形内或该多边形边上
     *
     * 礼品包装算法基本思想:
     * 1.先找出最左下的点,记为开始点
     * 2.找出第2个点,以开始点为起点,该点为终点的向量,与y正半轴的夹角(y轴正半轴顺时针旋转到该向量所需要的角度)
     * 3.再找下一个点,以第2个点为起点,该点为终点的向量,与步骤2中的向量的夹角最小(步骤2中的向量顺时针旋转到该向量所需要的角度)
     * 4.以此类推,直到下一个点为开始点,程序结束
     * <p>
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and
     * there are other algorithms too.
     *
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        ArrayList<Point> pList = new ArrayList<>(points); // 把参数中的集合元素放入有序的数组中
        Set<Point> selectedPoints = new HashSet<>(); // 用于存储所有最终满足条件的点
        for(Point p: pList) System.out.println(p); // 打印一下pList数组,看看新顺序是怎么样的
        // 特别注意HashSet放进ArrayList时,顺序全被打乱了
        if(points.size() <= 2) //少于两个点,就选中全部的点
            return points;
        int i, current = 0, next, n = points.size();
        for(i = 0; i < n - 1; i++) {
            if(pList.get(i).x() == pList.get(i + 1).x() && pList.get(i).y() == pList.get(i + 1).y()) {
                System.out.println("存在两个相同的点");
                System.exit(0);
            }
        }
        final int first; // 第一次取的点
        int second;// 第二次取的点
        int selectNum = 1; // 已选择的点的数量
        double currentBearing = 0; // 当前向量与y正半轴的夹角
        boolean[] select = new boolean[n]; // 标记已经选中的点
        for(i = 0; i < n; i++) { // 第一次选中最左下的点作为起点
            if(pList.get(i).x() > pList.get(current).x()) // 选最右的点
                current = i;
            else if(pList.get(i).x() == pList.get(current).x()) // 有横坐标相同的点,选纵坐标大的点
                current = pList.get(i).x() > pList.get(current).x() ? i : current;
        }
        select[current] = true;
        next = (current + 1) % n;
        second = first = current;
        while(true) {
            // 选出与当前点极角最小的点作为下一个点next
            for(i = 0; i < n; i++) {
                //System.out.println(calculateBearingToPoint(currentBearing, (int) pList.get(current).x(), (int) pList.get(current).y(), (int) pList.get(i).x(), (int) pList.get(i).y()));
                //System.out.println(calculateBearingToPoint(currentBearing, (int) pList.get(current).x(), (int) pList.get(current).y(), (int) pList.get(next).x(), (int) pList.get(next).y()));
                if(
                        ! select[i] && next != i &&
                        calculateBearingToPoint(currentBearing,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(i).x(),(int)pList.get(i).y()) <
                        calculateBearingToPoint(currentBearing,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(next).x(),(int)pList.get(next).y())
                )
                    next = i;
                else if(
                        ! select[i] && next != i &&
                        calculateBearingToPoint(currentBearing,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(i).x(),(int)pList.get(i).y()) ==
                        calculateBearingToPoint(currentBearing,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(next).x(),(int)pList.get(next).y())
                )
                    // 夹角相等的情况,取最远点,可以取到较少的点
                    next = Math.abs(pList.get(i).x() - pList.get(current).x()) + Math.abs(pList.get(i).y() - pList.get(current).y()) >
                            Math.abs(pList.get(next).x() - pList.get(current).x()) + Math.abs(pList.get(next).y() - pList.get(current).y()) ? i : next;
            }
            if(
                    current != first && current != second &&
                    calculateBearingToPoint(currentBearing,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(next).x(),(int)pList.get(next).y()) >=
                    calculateBearingToPoint(currentBearing,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(first).x(),(int)pList.get(first).y())
            )
                // 如果current到next的极角比begin到next的极角还小,那么这个next就是多余的直接把current连到begin即可,所以直接结束循环
                // 当然前提是current不是开始点和第二个点(因为凸集至少需要三个点)
                break;
            else {
                currentBearing = calculateBearingToPoint(0,(int)pList.get(current).x(),(int)pList.get(current).y(),(int)pList.get(next).x(),(int)pList.get(next).y());
                //当前的向量与y轴正轴的夹角
                select[next] = true;
                selectNum++;
                if(selectNum == n) // 已经把所有的点都选中了,也结束循环
                    break;
                if(current == first)
                    second = next;
                current = next;
                while(select[next])
                    next = (next + 1) % n;
            }
        }
        for(i = 0; i < n; i++)
            if(select[i])
                selectedPoints.add(pList.get(i));
        return selectedPoints;
    }

    /**
     * Draw your personal, custom art.
     * <p>
     * Many interesting images can be drawn using the simple implementation of a Lab_1.turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * <p>
     * 想不出啥有创意的东西,给大家表演一个彩色多边形旋涡吧,可以变换边数
     *
     * @param turtle the Lab_1.turtle context
     */
    public static void drawPersonalArt(Turtle turtle, int sides, int sidesLength, int num) {
        int i;
        if(sides <= 2){
            System.out.println("sides不能小于3");
            return;
        }
        PenColor[] color = new PenColor[]{
                PenColor.BLACK,
                PenColor.GRAY,
                PenColor.RED,
                PenColor.PINK,
                PenColor.ORANGE,
                PenColor.YELLOW,
                PenColor.GREEN,
                PenColor.CYAN,
                PenColor.BLUE,
                PenColor.MAGENTA};
        double bearing = 180 - calculateRegularPolygonAngle(sides);
        for(i = 1; i < num; i++) {
            turtle.forward(sidesLength * i);
            turtle.turn(bearing);
            turtle.forward(sidesLength * i);
            turtle.turn(bearing);
            turtle.color(color[i % 10]);
        }
        turtle.forward(sidesLength * i);
        turtle.turn(bearing);
        turtle.forward(sidesLength * (i - 1));
        turtle.turn(bearing);
        turtle.forward(sidesLength);
        turtle.draw();
    }

    /**
     * Main method.
     * <p>
     * This is the method that runs when you run "java TurtleSoup".
     *
     * @param args unused
     */
    public static void main(String[] args) {
        DrawableTurtle turtle = new DrawableTurtle();
        //drawRegularPolygon(Lab_1.turtle, 1, 100);
        Point p11 = new Point(1,1);
        Point p1010 = new Point(10,10);
        Point p110 = new Point(1,10);
        Point p12 = new Point(1,2);
        Point p23 = new Point(2,3);
        Point p32 = new Point(3,2);
        //System.out.println(calculateBearingToPoint(0,(int) 10.0,(int) 10.0,(int) 3,(int) 2));
        //System.out.println(calculateBearingToPoint(0,10,10,1,2));
        HashSet<Point> points = new HashSet<>();
        points.add(p11);
        points.add(p1010);
        points.add(p110);
        points.add(p12);
        points.add(p23);
        points.add(p32);
        Set<Point> selectedPoints = convexHull(points);
        for(Point point: selectedPoints) System.out.println(point);
        drawPersonalArt(turtle, 6, 10,25);
        //drawPersonalArt(Lab_1.turtle);
    }
}