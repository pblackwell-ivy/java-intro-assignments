package Module_5.Practice.chapter15;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal demo: load ONLY Idaho from usmap.txt,
 * turn its (lat,lon) pairs into a JavaFX Polygon,
 * and scale to fit the window while keeping aspect.
 */
public class IdahoOnlyApp extends Application {

    private static final String USMAP_URL = "https://liveexample.pearsoncmg.com/data/usmap.txt";

    @Override
    public void start(Stage stage) throws Exception {
        // Load (lat,lon) pairs for Idaho
        List<double[]> idahoCoords = loadIdaho(java.net.URI.create(USMAP_URL).toURL());
        if (idahoCoords.isEmpty()) {
            throw new IllegalStateException("Could not find Idaho data in usmap.txt");
        }

        // Compute bounds just from Idaho data
        double minLat = Double.POSITIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
        double minLon = Double.POSITIVE_INFINITY, maxLon = Double.NEGATIVE_INFINITY;
        for (double[] ll : idahoCoords) {
            double lat = ll[0], lon = ll[1];
            if (lat < minLat) minLat = lat; if (lat > maxLat) maxLat = lat;
            if (lon < minLon) minLon = lon; if (lon > maxLon) maxLon = lon;
        }
        double worldW = maxLon - minLon;
        double worldH = maxLat - minLat;

        // Build the polygon in "world" coords:
        // X = lon - minLon,  Y = maxLat - lat  (flip Y so north is up)
        Polygon idaho = new Polygon();
        for (double[] ll : idahoCoords) {
            double lat = ll[0], lon = ll[1];
            double x = lon - minLon;
            double y = maxLat - lat;
            idaho.getPoints().addAll(x, y);
        }
        idaho.setFill(Color.WHITE);
        idaho.setStroke(Color.GRAY);
        idaho.setStrokeWidth(0.1);           // thin, hairline-like border
        idaho.setStrokeLineJoin(StrokeLineJoin.ROUND);

        // Put polygon in a Group so we can scale the whole thing
        Group mapGroup = new Group(idaho);

        // Center it and add some padding
        StackPane root = new StackPane(mapGroup);
        root.setPadding(new Insets(12));

        // Bind scale to fit the window while preserving aspect ratio
        var fitScale = Bindings.createDoubleBinding(() -> {
            double availW = Math.max(1, root.getWidth()  - root.getPadding().getLeft() - root.getPadding().getRight());
            double availH = Math.max(1, root.getHeight() - root.getPadding().getTop()  - root.getPadding().getBottom());
            double sx = availW / worldW;
            double sy = availH / worldH;
            return Math.min(sx, sy);
        }, root.widthProperty(), root.heightProperty());

        mapGroup.scaleXProperty().bind(fitScale);
        mapGroup.scaleYProperty().bind(fitScale);

        // Scene + Stage
        Scene scene = new Scene(root, 640, 480, Color.WHITE);
        stage.setTitle("Idaho Outline (from usmap.txt)");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Reads usmap.txt and returns only Idaho's (lat, lon) pairs.
     * File format (book version): a state name line, then one or more lines of "lat lon";
     * next state begins when a line's first token is not numeric.
     * Also normalizes en/em dashes to ASCII '-' so parsing negatives works.
     */
    private static List<double[]> loadIdaho(URL url) throws Exception {
        List<double[]> coords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            boolean inIdaho = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Normalize en dash / em dash to '-' (important for negatives like –117.03)
                line = line.replace('\u2013', '-').replace('\u2014', '-');

                String[] parts = line.split("\\s+");
                boolean firstIsNumber;
                try {
                    Double.parseDouble(parts[0]);
                    firstIsNumber = true;
                } catch (NumberFormatException ex) {
                    firstIsNumber = false;
                }

                if (!firstIsNumber) {
                    // It's a state name line
                    inIdaho = "Idaho".equalsIgnoreCase(line); // exact match; change if file includes variations
                } else if (inIdaho) {
                    // Collect all doubles on this line as lat/lon pairs
                    for (int i = 0; i + 1 < parts.length; i += 2) {
                        double lat = Double.parseDouble(parts[i]);
                        double lon = Double.parseDouble(parts[i + 1]);
                        coords.add(new double[]{lat, lon});
                    }
                }
            }
        }
        return coords;
    }

    public static void main(String[] args) {
        launch(args);
    }
}