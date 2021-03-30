package csci2020u.lab09;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
    private Canvas canvas;
    private Double maximum = 0.0;
    ArrayList<Double> close1 = new ArrayList<>();
    ArrayList<Double> close2 = new ArrayList<>();
    Group root = new Group();

    private InputStream downloadStockPrices(String ticker) throws IOException {
        URL url = new URL("https://query1.finance.yahoo.com/v7/finance/download/" + ticker
                + "?period1=1262322000&period2=1451538000&interval=1mo&events=history&includeAdjustedClose=true");
        return url.openConnection().getInputStream();
    }



    private void drawLinePlot(ArrayList<Double> stock1, ArrayList<Double> stock2){

        for (int i = 0 ; i < stock1.size(); i++){
            if (stock1.get(i) >= maximum){
                maximum = stock1.get(i);
            }
        }
        for (int i = 0 ; i < stock1.size(); i++){
            if (stock2.get(i) >= maximum){
                maximum = stock2.get(i);
            }
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeLine(10,550,10,30);
        gc.strokeLine(10,550,770, 550);
        gc.setStroke(Color.RED);
        plotLine(stock1);
        gc.setStroke(Color.BLUE);
        plotLine(stock2);



    }




    public  void  plotLine(ArrayList<Double> prices){
        double ratio = (canvas.getWidth()-50)/prices.size();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i =1; i<prices.size(); i++){
            gc.strokeLine(30 + ratio*(i-1),500 - (prices.get(i-1)*500/maximum), 30 + ratio*i,500 - (prices.get(i)*500/maximum));
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        canvas = new Canvas();
        InputStream stock1 = downloadStockPrices("GOOG");
        Scanner s1 = new Scanner(stock1);
        String line;
        while(s1.hasNext()){
            line = s1.nextLine();
            String[] columns = line.split(",");
            if (!columns[0].equals("Date")){
                close1.add(Double.parseDouble((columns[4])));
            }

        }

        InputStream stock2 = downloadStockPrices("AAPL");
        Scanner s2 = new Scanner(stock2);
        while(s2.hasNext()){
            line = s2.nextLine();
            String[] columns = line.split(",");
            if (!columns[0].equals("Date")){
                close2.add(Double.parseDouble((columns[4])));
            }

        }

        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800 ,800);
        primaryStage.setScene(scene);
        primaryStage.show();
        drawLinePlot(close1, close2);

    }





    public static void main(String[] args) {
        launch(args);
    }
}

