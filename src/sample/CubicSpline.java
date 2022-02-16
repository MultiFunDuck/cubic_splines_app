package sample;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class CubicSpline {

    ArrayList<CubicFunction> functions;

    public CubicSpline(double left_angle, double right_angle, ObservableList<Point2D> points) {
        build_spline(left_angle,right_angle, points);
    }

    public void build_spline(double left_angle, double right_angle, ObservableList<Point2D> points){


        double left_curv_0 = 0;
        double left_curv_1 = 1;
        double right_angle_0 = right_slope_angle(left_curv_0,left_angle,points);
        double right_angle_1 = right_slope_angle(left_curv_1,left_angle,points);

        double left_curvature = left_curv_0 + (right_angle - right_angle_0)*(left_curv_1 - left_curv_0)/(right_angle_1 - right_angle_0);

        this.functions = drive_spline(left_curvature,left_angle, points);
    }


    public double right_slope_angle(double left_curvature, double left_angle,ObservableList<Point2D> points){
        ArrayList<CubicFunction> functions = drive_spline(left_curvature,left_angle, points);
        CubicFunction last = functions.get(functions.size()-1);
        double x = last.end - last.start;
        double right_slope = 3*last.A*x*x + 2*last.B*x + last.C;
        return right_slope;
    }

    public ArrayList<CubicFunction> drive_spline(double left_curvature, double left_angle, ObservableList<Point2D> points){

        functions = new ArrayList<>();

        double first_x = points.get(0).x;
        double second_x = points.get(1).x;
        double first_dif = second_x - first_x;
        double first_D = points.get(0).y;
        double first_C = left_angle;
        double first_B = left_curvature;
        double first_S = points.get(1).y;
        double first_A = A_calc_from_BCDS(first_B, first_C, first_D, first_S, first_dif);


        CubicFunction firstCubic = new CubicFunction(first_x,second_x,first_A,first_B,first_C,first_D);

        functions.add(firstCubic);
        for(int i = 1; i < points.size() - 1; i++){
            double cur_x = points.get(i).x;
            double next_x = points.get(i+1).x;
            double dif = next_x - cur_x;

            double D_next = functions.get(i-1).value(cur_x);
            double C_next = functions.get(i-1).first_derivative(cur_x);
            double B_next = (1/2)*functions.get(i-1).second_derivative(cur_x);
            double S_next = points.get(i+1).y;
            double A_next = A_calc_from_BCDS(B_next,C_next,D_next,S_next,dif);

            CubicFunction next_cubic = new CubicFunction(cur_x, next_x, A_next, B_next, C_next, D_next);
            functions.add(next_cubic);
        }
        return functions;
    }

    public double A_calc_from_BCDS(double B, double C, double D, double S, double dif){
        double l = 0.0;
        return (S/(Math.pow(dif,3)) - B*(1+6*l)/(Math.pow(dif,1)) - C/(Math.pow(dif,2)) - D/(Math.pow(dif,3)))/(1+12*l);
    }

    public double value(double x){
        for(int i = 0; i < functions.size(); i++){
            if(functions.get(i).start <= x && x <= functions.get(i).end){
                return functions.get(i).value(x);
            }
        }
        return 0;
    }

    public ArrayList<Point2D> values(int num_of_splits){
        double start = functions.get(0).start;
        double end = functions.get(functions.size()-1).end;
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for(int i = 0; i <= num_of_splits; i++){
            double x = start + i * (end - start) / num_of_splits;
            points.add(new Point2D(i+1,(float)x,(float)(value(x))));
        }
        return points;
    }


    public ArrayList<CubicFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<CubicFunction> functions) {
        this.functions = functions;
    }

}
