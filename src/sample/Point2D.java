package sample;

public class Point2D implements Comparable<Point2D>{

    public Point2D(int num, float x, float y) {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "num=" + num +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean isEqual(Point2D p){
        return (this.x == p.x)&&(this.y == p.y);
    }

    int num;
    float x;
    float y;


    @Override
    public int compareTo(Point2D point2D) {
        return (this.x - point2D.x > 0)? 1 : -1;
    }
}
