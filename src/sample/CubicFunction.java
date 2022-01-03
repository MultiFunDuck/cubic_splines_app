package sample;

public class CubicFunction {

    //Кубическая функция вида А(x-start)^3 + B(x-start)^2 + C(x-start) + D

    double start;
    double end;
    double A,B,C,D;

    public double value(double x){
        if(start <= x && x <= end){
            double x_s = x - start;
            return A * x_s * x_s * x_s  +  B * x_s * x_s  +  C * x_s  +  D;
        }
        else{
            return 0;
        }
    }

    public CubicFunction(double start, double end, double a, double b, double c, double d) {
        this.start = start;
        this.end = end;
        A = a;
        B = b;
        C = c;
        D = d;
    }

    public double first_derivative(double x){
        if(start <= x && x <= end){
            double x_s = x - start;
            return 3*A * x_s * x_s  +  2*B * x_s  +  C;
        }
        else{
            return 0;
        }
    }

    public double second_derivative(double x){
        if(start <= x && x <= end){
            double x_s = x - start;
            return 6*A * x_s  +  2*B;
        }
        else{
            return 0;
        }
    }
}
