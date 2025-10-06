package Module_6.Assignments;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

/**
 * Exercise34_1.java
 *
 * Write a program that views, inserts, and updates
 * staff information stored in a database, as shown in Figure 34.27a. The View button
 * displays a record with a specified ID. The Insert button inserts a new record. The
 * Update button updates the record for the specified ID.
 *
 * Users can:
 *  - View an existing staff record by entering an ID.
 *  - Insert a new staff record.
 *  - Update an existing staff record.
 *  - Clear the form fields.
 *
 * The Staff table should be created with this SQL statement:
 *
 * <pre>
 * create table Staff (
 *   id        char(9) not null,
 *   lastName  varchar(15),
 *   firstName varchar(15),
 *   mi        char(1),
 *   address   varchar(20),
 *   city      varchar(20),
 *   state     char(2),
 *   telephone char(10),
 *   email     varchar(40),
 *   primary key (id)
 * );
 * </pre>
 */
public class Exercise34_1 extends Application {

    // Database connection settings
    private static final String DB_URL  =
            "jdbc:mysql://localhost:3306/Exercise34_1?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // UI fields
    private final Label status = new Label("");
    private final TextField tfId        = new TextField();
    private final TextField tfLastName  = new TextField();
    private final TextField tfFirstName = new TextField();
    private final TextField tfMi        = new TextField();
    private final TextField tfAddress   = new TextField();
    private final TextField tfCity      = new TextField();
    private final TextField tfState     = new TextField();
    private final TextField tfTelephone = new TextField();

    @Override
    public void start(Stage stage) {

        // Form layout: VBox containing one HBox per row
        VBox formBox = new VBox(3);
        formBox.setPadding(new Insets(12));

        // Row 1: ID
        HBox row1 = new HBox(8);
        row1.getChildren().addAll(new Label("ID:"), tfId);

        // Row 2: Last Name | First Name | MI
        tfMi.setPrefColumnCount(1);         // sets the width of the Mi field to 1 character
        HBox row2 = new HBox(8);
        row2.getChildren().addAll(
                new Label("Last Name:"),  tfLastName,
                new Label("First Name:"), tfFirstName,
                new Label("MI:"),         tfMi
        );

        // Row 3: Address
        HBox row3 = new HBox(8);
        row3.getChildren().addAll(new Label("Address:"), tfAddress);

        // Row 4: City | State
        HBox row4 = new HBox(8);
        row4.getChildren().addAll(new Label("City:"), tfCity, new Label("State:"), tfState);

        // Row 5: Telephone
        HBox row5 = new HBox(8);
        row5.getChildren().addAll(new Label("Telephone:"), tfTelephone);

        formBox.getChildren().addAll(row1, row2, row3, row4, row5);

        // Buttons
        Button btView = new Button("View");
        Button btInsert = new Button("Insert");
        Button btUpdate = new Button("Update");
        Button btClear = new Button("Clear");

        HBox buttonBar = new HBox(10, btView, btInsert, btUpdate, btClear);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(0, 12, 12, 12));

        // Root layout
        BorderPane root = new BorderPane();
        root.setTop(status);
        root.setCenter(formBox);
        root.setBottom(buttonBar);

        // Button actions
        btView.setOnAction(e -> view());
        btInsert.setOnAction(e -> insert());
        btUpdate.setOnAction(e -> update());
        btClear.setOnAction(e -> clear());

        stage.setTitle("Exercise34_1");
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Displays a staff record for the given ID.
     * Uses a SELECT statement with a PreparedStatement to prevent SQL injection.
     * If the record exists, populates all text fields with database values.
     */
    private void view() {
        String id = tfId.getText().trim();
        if (id.isEmpty()) {
            status.setText("Enter an ID first.");
            return;
        }

        String sql = "SELECT id,lastName,firstName,mi,address,city,state,telephone FROM Staff WHERE id = ?";
        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tfLastName.setText(rs.getString("lastName"));
                    tfFirstName.setText(rs.getString("firstName"));
                    tfMi.setText(rs.getString("mi"));
                    tfAddress.setText(rs.getString("address"));
                    tfCity.setText(rs.getString("city"));
                    tfState.setText(rs.getString("state"));
                    tfTelephone.setText(rs.getString("telephone"));
                    status.setText("Record loaded.");
                } else {
                    status.setText("Record not found.");
                }
            }
        } catch (SQLException ex) {
            status.setText("View error: " + ex.getMessage());
        }
    }

    /**
     * Inserts a new staff record into the database.
     * Uses an INSERT statement with placeholders for all field values.
     * If a record with the same ID already exists, a SQL exception will occur.
     */
    private void insert() {
        String id = tfId.getText().trim();
        if (id.isEmpty()) {
            status.setText("ID is required to insert.");
            return;
        }

        String sql = "INSERT INTO Staff(id,lastName,firstName,mi,address,city,state,telephone) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, tfLastName.getText().trim());
            ps.setString(3, tfFirstName.getText().trim());
            ps.setString(4, tfMi.getText().trim());
            ps.setString(5, tfAddress.getText().trim());
            ps.setString(6, tfCity.getText().trim());
            ps.setString(7, tfState.getText().trim());
            ps.setString(8, tfTelephone.getText().trim());
            ps.executeUpdate();
            status.setText("Inserted.");
        } catch (SQLException ex) {
            status.setText("Insert error: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing staff record.
     * Uses an UPDATE statement with placeholders for all fields except ID,
     * which is used in the WHERE clause to identify the record.
     */
    private void update() {
        String id = tfId.getText().trim();
        if (id.isEmpty()) {
            status.setText("ID is required to update.");
            return;
        }

        String sql = "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, city=?, state=?, telephone=? "
                + "WHERE id=?";

        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tfLastName.getText().trim());
            ps.setString(2, tfFirstName.getText().trim());
            ps.setString(3, tfMi.getText().trim());
            ps.setString(4, tfAddress.getText().trim());
            ps.setString(5, tfCity.getText().trim());
            ps.setString(6, tfState.getText().trim());
            ps.setString(7, tfTelephone.getText().trim());
            ps.setString(8, id);
            int n = ps.executeUpdate();
            status.setText(n == 0 ? "No such ID to update." : "Updated.");
        } catch (SQLException ex) {
            status.setText("Update error: " + ex.getMessage());
        }
    }

    /**
     * Clears all text fields on the form and updates the status message.
     */
    private void clear() {
        tfId.clear();
        tfLastName.clear();
        tfFirstName.clear();
        tfMi.clear();
        tfAddress.clear();
        tfCity.clear();
        tfState.clear();
        tfTelephone.clear();
        status.setText("Cleared.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}