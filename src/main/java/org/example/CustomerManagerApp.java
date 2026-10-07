package org.example;

import org.example.model.Customer;
import org.example.service.CustomerService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {

    private final CustomerService service = new CustomerService();
    private TextField nameField;
    private ComboBox<String> provinceBox;
    private TableView<Customer> table;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {

        Menu fileMenu = new Menu("File");
        MenuItem closeItem = new MenuItem("Close");
        closeItem.setOnAction(e -> stage.close());
        fileMenu.getItems().add(closeItem);
        MenuBar menuBar = new MenuBar(fileMenu);

        Label nameLabel = new Label("Customer name");
        nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        nameLabel.setLabelFor(nameField);

        Label provinceLabel = new Label("Province");
        provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll(
                "Central", "Lusaka", "Copperbelt", "Eastern",
                "Northern", "Southern", "Western", "North-Western", "Muchinga");
        provinceBox.setPromptText("Choose a province");
        provinceLabel.setLabelFor(provinceBox);

        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true);
        Button deleteButton = new Button("Delete selected");
        saveButton.setOnAction(e -> handleSave());
        deleteButton.setOnAction(e -> handleDelete());

        table = new TableView<>();
        table.setItems(service.getCustomers());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);

        statusLabel = new Label("Ready.");
        statusLabel.setStyle("-fx-text-fill: #1b5e20;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(provinceLabel, 0, 1);
        form.add(provinceBox, 1, 1);
        form.add(saveButton, 1, 2);
        form.add(deleteButton, 1, 3);
        form.add(statusLabel, 0, 4, 2, 1);

        VBox root = new VBox(menuBar, form, table);
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 720, 560);
        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();

        nameField.requestFocus();
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: #b00020;");
            statusLabel.setText("Enter the customer name.");
            nameField.requestFocus();
            return;
        }

        String province = provinceBox.getValue();
        if (province == null) {
            statusLabel.setStyle("-fx-text-fill: #b00020;");
            statusLabel.setText("Choose a province.");
            provinceBox.requestFocus();
            return;
        }

        service.save(name, province);
        statusLabel.setStyle("-fx-text-fill: #1b5e20;");
        statusLabel.setText("Customer saved.");
        nameField.clear();
        provinceBox.getSelectionModel().clearSelection();
        nameField.requestFocus();
    }

    private void handleDelete() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setStyle("-fx-text-fill: #b00020;");
            statusLabel.setText("Select a customer before deleting.");
            return;
        }

        ButtonType delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete the selected customer?",
                delete, ButtonType.CANCEL);
        ask.setHeaderText("Confirm deletion");

        if (ask.showAndWait().orElse(ButtonType.CANCEL) == delete) {
            service.delete(selected);
            statusLabel.setStyle("-fx-text-fill: #1b5e20;");
            statusLabel.setText("Customer deleted.");
        } else {
            statusLabel.setStyle("-fx-text-fill: #1b5e20;");
            statusLabel.setText("Deletion cancelled.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
