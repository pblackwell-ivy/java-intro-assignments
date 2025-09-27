package Module_5.Practice.chapter15;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
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
 * Case Study 15.13 – US Map (48 contiguous states)
 * - Reads state polygons from usmap.txt (book format: no count line)
 * - Mouse coloring: PRIMARY=red, MIDDLE=white, SECONDARY=blue
 * - Resizes to fit; zoom with Up/Down keys
 */
public class USMapApp extends Application {

    private static final String USMAP_URL = "https://liveexample.pearsoncmg.com/data/usmap.txt";

    /** Simple model: state name + raw latitude/longitude arrays */
    private static class StateShape {
        final String name;
        final double[] lats; // length = n
        final double[] lons; // length = n
        StateShape(String name, double[] lats, double[] lons) {
            this.name = name;
            this.lats = lats;
            this.lons = lons;
        }
    }

    // Container for all polygons; we bind its scale to fit the window
    private final Group mapGroup = new Group();

    // User zoom multiplier (applied on top of fit-to-window)
    private final DoubleProperty zoom = new SimpleDoubleProperty(1.0);

    // Bounds in “world” (lat/lon) space
    private double minLat = Double.POSITIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
    private double minLon = Double.POSITIVE_INFINITY, maxLon = Double.NEGATIVE_INFINITY;

    @Override
    public void start(Stage stage) throws Exception {
        // Build URL without deprecation warning
        URL url = java.net.URI.create(USMAP_URL).toURL();

        // 1) Parse the file (book format)
        List<StateShape> states = parseUsMap(url);

        // 2) Compute world bounds
        for (StateShape s : states) {
            for (double v : s.lats) { if (v < minLat) minLat = v; if (v > maxLat) maxLat = v; }
            for (double v : s.lons) { if (v < minLon) minLon = v; if (v > maxLon) maxLon = v; }
        }

        // 3) Build polygons (points are set in recomputePolygons())
        for (StateShape s : states) {
            Polygon p = new Polygon();
            p.setFill(Color.WHITE);
            p.setStroke(Color.BLACK);
            p.setStrokeWidth(0.25);
            p.setStrokeLineJoin(StrokeLineJoin.ROUND);
            p.setUserData(s);

            p.setOnMouseClicked(me -> {
                if (me.getButton() == MouseButton.PRIMARY)       p.setFill(Color.RED);
                else if (me.getButton() == MouseButton.SECONDARY) p.setFill(Color.BLUE);
                else if (me.getButton() == MouseButton.MIDDLE)    p.setFill(Color.WHITE);
            });

            mapGroup.getChildren().add(p);
        }

        // 4) Layout
        StackPane center = new StackPane(mapGroup);
        center.setPadding(new Insets(10));
        BorderPane root = new BorderPane(center);

        Scene scene = new Scene(root, 900, 600, Color.WHITE);
        stage.setTitle("US Map – Case Study 15.13");
        stage.setScene(scene);
        stage.show();

        // 5) Fit scale (preserve aspect) multiplied by zoom
        var fitScale = Bindings.createDoubleBinding(() -> {
            double availW = Math.max(1, center.getWidth()  - center.getPadding().getLeft() - center.getPadding().getRight());
            double availH = Math.max(1, center.getHeight() - center.getPadding().getTop()  - center.getPadding().getBottom());
            double worldW = maxLon - minLon;
            double worldH = maxLat - minLat;
            double scale = Math.min(availW / worldW, availH / worldH);
            return scale * zoom.get();
        }, center.widthProperty(), center.heightProperty(), zoom);

        mapGroup.scaleXProperty().bind(fitScale);
        mapGroup.scaleYProperty().bind(fitScale);

        // Recompute polygon points when size changes
        center.widthProperty().addListener((o, ov, nv) -> recomputePolygons());
        center.heightProperty().addListener((o, ov, nv) -> recomputePolygons());
        recomputePolygons(); // initial

        // 6) Keyboard zoom
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP)   zoom.set(Math.min(zoom.get() * 1.15, 20));
            if (e.getCode() == KeyCode.DOWN) zoom.set(Math.max(zoom.get() / 1.15, 0.2));
        });
    }

    /** Rebuild each polygon’s point list in “world” coordinates (X=lon shift, Y=lat flipped). */
    private void recomputePolygons() {
        for (var node : mapGroup.getChildren()) {
            Polygon poly = (Polygon) node;
            StateShape s = (StateShape) poly.getUserData();
            List<Double> pts = new ArrayList<>(s.lats.length * 2);
            for (int i = 0; i < s.lats.length; i++) {
                double x = (s.lons[i] - minLon); // shift longitudes so min lon maps to 0
                double y = (maxLat - s.lats[i]); // flip latitude so north is up
                pts.add(x);
                pts.add(y);
            }
            poly.getPoints().setAll(pts);
        }
        // Anchor at (0,0); StackPane will center and the scale binding will fit
        mapGroup.setLayoutX(0);
        mapGroup.setLayoutY(0);
    }

    /**
     * Parser for the book’s usmap.txt format:
     *   - A state name line (may contain spaces)
     *   - Followed by one or more lines of "lat lon" pairs
     *   - Next state starts when a line’s first token is NOT a number
     *
     * Also normalizes en/em dashes to ASCII minus so Double.parseDouble works.
     */
    private static List<StateShape> parseUsMap(URL url) throws Exception {
        List<StateShape> out = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            String currentState = null;
            List<Double> coordBuffer = new ArrayList<>(); // lat lon lat lon ...

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // normalize en/em dash to regular minus
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
                    // New state; flush previous
                    flushState(out, currentState, coordBuffer);
                    currentState = line; // keep full name
                } else {
                    // Coordinates on this line
                    for (String tok : parts) {
                        if (!tok.isEmpty()) coordBuffer.add(Double.parseDouble(tok));
                    }
                }
            }
            // Flush last state
            flushState(out, currentState, coordBuffer);
        }

        return out;
    }

    /** Convert buffered coords into a StateShape and clear the buffer. */
    private static void flushState(List<StateShape> out, String name, List<Double> coordBuffer) {
        if (name == null || coordBuffer.isEmpty()) return;
        int nPairs = coordBuffer.size() / 2;

        double[] lats = new double[nPairs];
        double[] lons = new double[nPairs];

        // Book data is LAT then LON; if map appears mirrored, swap the two assignments below.
        for (int i = 0, j = 0; i < nPairs; i++) {
            lats[i] = coordBuffer.get(j++);
            lons[i] = coordBuffer.get(j++);
        }

        out.add(new StateShape(name, lats, lons));
        coordBuffer.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}