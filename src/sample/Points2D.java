package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.Comparator;

public class Points2D {
    private static Points2D points2D;

    public ObservableList<Point2D> point2DS;

    public static synchronized Points2D getInstance(){
        if(points2D == null){
            points2D = new Points2D();
        }
        return points2D;
    }

    public void setPoint2DS(ObservableList<Point2D> point2DS) {
        this.point2DS = point2DS;
    }

    public double x_max_dif(){
        Collections.sort(point2DS);
        return Math.abs(point2DS.get(point2DS.size()-1).x - point2DS.get(0).x);
    }

    public double min_x(){
        Collections.sort(point2DS);
        return point2DS.get(0).x;
    }

    public double y_max_dif(){
        Collections.sort(point2DS, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                return (o1.y - o2.y > 0) ? 1 : -1;
            }
        });
        return Math.abs(point2DS.get(point2DS.size()-1).y - point2DS.get(0).y);
    }

    public double min_y(){
        Collections.sort(point2DS, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                return (o1.y - o2.y > 0) ? 1 : -1;
            }
        });
        return point2DS.get(0).y;
    }
}
