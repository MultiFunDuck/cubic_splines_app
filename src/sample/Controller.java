package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Controller {


    Points2D points2D = Points2D.getInstance();

    ObservableList<Point2D> point2DS = FXCollections.observableArrayList();

    @FXML
    private TableView<Point2D> Points_table;

    @FXML
    private Button add_point_button;

    @FXML
    private Button create_spline_button;

    @FXML
    private Button delete_point_button;

    @FXML
    private TextField point_num;

    @FXML
    private TableColumn<Point2D, String> point_num_col;

    @FXML
    private TableColumn<Point2D, String> point_x_col;

    @FXML
    private TableColumn<Point2D, String> point_y_col;

    @FXML
    private TextField x_coord_field;

    @FXML
    private TextField y_coord_field;

    @FXML
    private Canvas canvas_pane;

    @FXML
    private Label x_max_coord;

    @FXML
    private Label x_min_coord;

    @FXML
    private Label y_min_coord;

    @FXML
    private Label y_max_coord;

    @FXML
    private Label y_mid_coord;

    @FXML
    private Label x_mid_coord;

    @FXML
    private TextField left_slope_angle;

    @FXML
    private TextField right_slope_angle;


    @FXML
    void add_point(MouseEvent event) {



        float x = Float.parseFloat(x_coord_field.getText());
        float y = Float.parseFloat(y_coord_field.getText());
        int id = point2DS.size() + 1;
        boolean is_there_same = false;

        Point2D p = new Point2D(id,x,y);

        for(int i = 0; i < point2DS.size();i++){
            if(p.isEqual(point2DS.get(i))){
                is_there_same = true;
            }
        }

        if(!is_there_same){
            point_num_col.setCellValueFactory(new PropertyValueFactory<>("num"));
            point_x_col.setCellValueFactory(new PropertyValueFactory<>("x"));
            point_y_col.setCellValueFactory(new PropertyValueFactory<>("y"));

            point2DS.add(p);
            Collections.sort(point2DS, new Comparator<Point2D>() {
                @Override
                public int compare(Point2D o1, Point2D o2) {
                    return (o1.x - o2.x > 0)? 1 : -1;
                }
            });

            for(int i = 0; i < point2DS.size();i++){
                point2DS.get(i).num = i + 1;
            }


            Points_table.setItems(point2DS);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Точка с такими координатами уже есть");

            alert.showAndWait();
        }
        points2D.setPoint2DS(point2DS);
    }



    @FXML
    void delete_point(MouseEvent event) {

        ObservableList<Point2D> point2DS = Points2D.getInstance().point2DS;
        int num = Integer.parseInt(point_num.getText());
        for(int i = 0; i < point2DS.size(); i++){
            if(num == point2DS.get(i).num){
                point2DS.remove(i);
            }
        }
        Collections.sort(point2DS);

        for(int i = 0; i < point2DS.size();i++){
            point2DS.get(i).num = i + 1;
        }
        Points_table.setItems(point2DS);
        points2D.setPoint2DS(point2DS);
    }

    @FXML
    void build_spline(MouseEvent event) {

        ObservableList<Point2D> pointsList = points2D.point2DS;
        double right_slope = Math.tan(Math.PI*(Double.parseDouble(right_slope_angle.getText()))/180);
        double left_slope = Math.tan(Math.PI*(Double.parseDouble(left_slope_angle.getText()))/180);
        Collections.sort(point2DS, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                return (o1.x - o2.x > 0)? 1 : -1;
            }
        });
        CubicSpline cubicSpline = new CubicSpline(left_slope,right_slope,pointsList);


        drawPoints(cubicSpline.values(1000));


    }

    public void draw_points(MouseEvent mouseEvent) {

        ObservableList<Point2D> point2DS = Points2D.getInstance().point2DS;
        Collections.sort(point2DS, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                return (o1.x - o2.x > 0)? 1 : -1;
            }
        });
        drawPoints(point2DS);

    }

    public void drawPoints(List<Point2D> pointsList){


        GraphicsContext context = canvas_pane.getGraphicsContext2D();
        Canvas canvas = context.getCanvas();
        double width = canvas.getWidth();
        double height = canvas.getHeight();



        double max_x_diff = points2D.x_max_dif();
        double max_y_diff = points2D.y_max_dif();

        double y_scale = (max_y_diff == 0)? height/2 : (2*height/(3*max_y_diff));
        double x_scale = (max_x_diff == 0)? width/2 : (2*width/(3*max_x_diff));
        double xy_scale = (max_y_diff == 0)? 1 : max_x_diff/max_y_diff;

        double min_x = points2D.min_x();
        double min_y = points2D.min_y();

        context.clearRect(0,0,width,height);



        for(int i = 0; i < pointsList.size(); i++){
            context.fillRoundRect(width/6 + (pointsList.get(i).x - min_x) * x_scale, height - (height/6 + (pointsList.get(i).y - min_y) * y_scale), 10,10,100,100);
        }

        x_max_coord.setText(Double.toString(min_x + max_x_diff));
        x_min_coord.setText(Double.toString(min_x));
        y_max_coord.setText(Double.toString(min_y + max_y_diff));
        y_min_coord.setText(Double.toString(min_y));
        x_mid_coord.setText(Double.toString(max_x_diff/2));
        y_mid_coord.setText(Double.toString(max_y_diff/2));

        drawLines(context,width,height,xy_scale);
    }

    public void drawLines(GraphicsContext context, double width, double height, double xy_scale){
        int num_of_squares = 64;
        double square_length = Math.sqrt(height*width/(num_of_squares*(1+xy_scale*xy_scale)));

        context.beginPath();
        context.moveTo(0,height);
        context.lineTo(width,height);
        context.setLineWidth(2);
        context.setStroke(Color.valueOf("#E90000"));
        context.stroke();

        context.beginPath();
        context.moveTo(0,height);
        context.lineTo(0,0);
        context.setLineWidth(2);
        context.setStroke(Color.valueOf("#E90000"));
        context.stroke();


        for(int i = 1; i*square_length < width; i++){
            context.beginPath();
            context.moveTo(i*square_length,0);
            context.lineTo(i*square_length,height);
            context.setLineWidth(1);
            context.setStroke(Color.valueOf("#6B6B6B"));
            context.stroke();
        }

        for(int i = 1; i*square_length*xy_scale < height; i++){
            context.beginPath();
            context.moveTo(0,i*square_length*xy_scale);
            context.lineTo(width,i*square_length*xy_scale);
            context.setLineWidth(1);
            context.stroke();
        }
    }
}

