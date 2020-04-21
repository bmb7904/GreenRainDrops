package edu.bloomu.bmb56279.homework3;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This JavaFX application will present the user with a blank, black window. When the
 * user clicks anywhere in the black window, a circle (rain drop) will be created and
 * shown at the location of the mouse click. The radius of the circle will be randomly
 * generated, but within a range of [20,100]. As circles are created and shown, if the
 * user clicks on a currently shown circle, a new circle is not created, and the
 * clicked circle will be lit up with a change in opacity, while the others are
 * transparent. The user also has the ability click on a circle and drag it anywhere in
 * the window.
 *
 * @author Brett Bernardi
 */

public class RainDrops extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        final int width = 1000;
        final int height = 700;
        Scene scene = new Scene(root, width, height, Color.BLACK);

        // Using a lambda expression, pass an event handler to the root pane that
        // defines the behavior of the program when the user clicks in the pane
        root.setOnMouseClicked(event -> {
            // boolean flag. true if user clicked on a circle. Otherwise false.
            boolean clickedOnCircle = false;

            // Keeps track of which circle the user clicked on with a mouse click.
            // Must be initialized, so initialize it to null.
            Circle clickedCircle = null;

            // This loop will iterate through all of the root node's children, and
            // check to see if the position where the user clicked is empty space, or
            // it is currently occupied by a circle. Because these circles are not
            // individually named, and the number of them varies, we can iterate
            // through the children of the root to check all of the circles (if they
            // exist).
            for (Node n : root.getChildren()) {
                Circle c = (Circle) n;
                if (c.contains(event.getX(), event.getY())) {
                    clickedCircle = c;
                    clickedOnCircle = true;
                }
            }
            /*
             * At this point, there are two possibilities. If the point where the user
             * clicked is on a circle or not.
             */

            // If they did not click on a circle, the point of the mouse click is empty
            // space and a new circle should be added there, but first make all
            // previously created circles transparent (if they exist).
            if (!clickedOnCircle) {
                for(Node n: root.getChildren()) {
                    Circle c = (Circle) n;
                    c.setOpacity(0.25);
                }
                root.getChildren().add(getRaindrop(event.getX(), event.getY()));
            }
            // If the user did click on a circle, a new circle is not created. Instead,
            // the circle the user clicked on is highlighted and all other previously
            // created circles(if they exist) are made transparent.
            else {
                for(Node n: root.getChildren()) {
                    Circle c = (Circle) n;
                    if(c != clickedCircle) {
                        c.setOpacity(0.25);
                    }
                    else {
                        clickedCircle.setOpacity(1);
                    }
                }
            }
        });

        primaryStage.setTitle("Rain Drops");
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Helper method that creates and returns a raindrop (circle) based on the x and y
     * coordinates that are specified as parameters. The radius of the circle will be
     * randomly generated in the range of [20,100]. A radial gradient, using three
     * different shades of green, is added to the circle before it is returned. A mouse
     * dragged handler is passed into each newly created circle.
     *
     * @param xPos - x component of center of circle
     * @param yPos - y component of center of circle
     * @return - a new circle with the specified center coordinates and a radius
     * of a random size
     */
    private static Circle getRaindrop(double xPos, double yPos) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        double radius = rand.nextDouble(20.0, 100.0);
        Circle raindrop = new Circle(xPos, yPos, radius);
        raindrop.setFill(getRadialGradient());

        // Using a lambda expression, passes an event handler into each circle that will
        // define what happens when the circle is dragged
        raindrop.setOnMouseDragged(event -> {
            double differenceX = event.getSceneX() - raindrop.getCenterX();
            double differenceY = event.getSceneY() - raindrop.getCenterY();
            raindrop.setTranslateX(differenceX);
            raindrop.setTranslateY(differenceY);
            // Updates coordinates of the circle node. This is needed, otherwise the
            // circle node's x and y coordinates will not update as circle is moved
            // with the dragged setTranslate functions and thus will cause problems with
            // the mouseclick handler later on.
            raindrop.setCenterX(raindrop.getCenterX() + differenceX);
            raindrop.setCenterY(raindrop.getCenterY() + differenceY);
        });

        return raindrop;
    }

    /**
     * Helper method that returns a RadialGradient through three different shades of
     * green. Parameter values for the RadialGradient constructor were chosen through
     * trial and error, based on what I thought looked the best. Setting the
     * proportional parameter to true makes the other parameters interpreted as a
     * fraction of the circle. This is the only way I can keep the gradient on the
     * circle as it moves.
     *
     * @return a RadialGradient
     */
    private static RadialGradient getRadialGradient() {
        Stop[] stops = new Stop[3];
        stops[0] = new Stop(0, Color.SEAGREEN);
        stops[1] = new Stop(1, Color.GREEN);
        stops[2] = new Stop(2, Color.DARKSEAGREEN);
        return new RadialGradient(0, 0, 0.5, 0.5,
                0.25, true, CycleMethod.REFLECT, stops);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
