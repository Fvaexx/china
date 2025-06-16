package org.example.china;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HelloController {
    @FXML private TextField matrixSizeField;
    @FXML private GridPane matrixAGrid;
    @FXML private GridPane matrixBGrid;
    @FXML private GridPane resultGrid;

    private Matrica matrixA;
    private Matrica matrixB;
    private final List<Matrica> matrices = new ArrayList<>();

    @FXML
    private void initialize() {
        // Ініціалізація за замовчуванням
        createMatrices(2);
    }

    private void createMatrices(int size) {
        matrixA = Matrica.createRandom(size);
        matrixB = Matrica.createRandom(size);

        updateMatrixGrid(matrixAGrid, matrixA);
        updateMatrixGrid(matrixBGrid, matrixB);

        matrices.clear();
        matrices.add(new Matrica(matrixA));
        matrices.add(new Matrica(matrixB));
    }

    @FXML
    private void handleAddMatrix() {
        try {
            int size = Integer.parseInt(matrixSizeField.getText());
            if (size <= 0 || size > 6) {
                showAlert("Помилка", "Розмір має бути від 1 до 6");
                return;
            }
            createMatrices(size);
        } catch (NumberFormatException e) {
            showAlert("Помилка вводу", "Будь ласка, введіть ціле число");
        }
    }

    @FXML
    private void handleEditMatrix() {
        try {
            if (matrixA == null || matrixB == null) {
                showAlert("Помилка", "Спочатку створіть матриці");
                return;
            }

            // Оновлення значень з інтерфейсу
            matrixA = readMatrixFromGrid(matrixAGrid);
            matrixB = readMatrixFromGrid(matrixBGrid);

            // Оновлення списку матриць
            matrices.clear();
            matrices.add(new Matrica(matrixA));
            matrices.add(new Matrica(matrixB));

            showAlert("Успіх", "Матриці оновлено успішно");

        } catch (NumberFormatException e) {
            showAlert("Помилка вводу", "Будь ласка, введіть коректні числа");
        }
    }

    @FXML
    private void handleSortMatrices() {
        if (matrices.size() < 2) {
            showAlert("Помилка", "Додайте щонайменш дві матриці");
            return;
        }

        // Оновлення значень перед сортуванням
        matrixA = readMatrixFromGrid(matrixAGrid);
        matrixB = readMatrixFromGrid(matrixBGrid);

        // Оновлення списку
        matrices.clear();
        matrices.add(new Matrica(matrixA));
        matrices.add(new Matrica(matrixB));

        // Сортування за детермінантом
        matrices.sort(Comparator.comparingDouble(Matrica::getDeterminant));

        // Оновлення інтерфейсу
        matrixA = matrices.get(0);
        matrixB = matrices.get(1);
        updateMatrixGrid(matrixAGrid, matrixA);
        updateMatrixGrid(matrixBGrid, matrixB);
    }

    @FXML
    private void handleAddMatrices() {
        try {
            // Оновлення значень перед операцією
            matrixA = readMatrixFromGrid(matrixAGrid);
            matrixB = readMatrixFromGrid(matrixBGrid);

            Matrica result = matrixA.add(matrixB);
            updateMatrixGrid(resultGrid, result);
        } catch (Exception e) {
            showAlert("Помилка операції", e.getMessage());
        }
    }

    @FXML
    private void handleMultiplyMatrices() {
        try {
            // Оновлення значень перед операцією
            matrixA = readMatrixFromGrid(matrixAGrid);
            matrixB = readMatrixFromGrid(matrixBGrid);

            Matrica result = matrixA.multiply(matrixB);
            updateMatrixGrid(resultGrid, result);
        } catch (Exception e) {
            showAlert("Помилка операції", e.getMessage());
        }
    }

    @FXML
    private void handleTransposeMatrixA() {
        try {
            // Оновлення значень перед операцією
            matrixA = readMatrixFromGrid(matrixAGrid);

            Matrica result = matrixA.transpose();
            updateMatrixGrid(resultGrid, result);
        } catch (Exception e) {
            showAlert("Помилка операції", e.getMessage());
        }
    }

    @FXML
    private void handleDeterminantMatrixA() {
        try {
            // Оновлення значень перед операцією
            matrixA = readMatrixFromGrid(matrixAGrid);

            double determinant = matrixA.getDeterminant();
            clearResultGrid();
            resultGrid.add(new Label(String.format("Детермінант: %.2f", determinant)), 0, 0);
        } catch (Exception e) {
            showAlert("Помилка операції", e.getMessage());
        }
    }

    // Допоміжні методи
    private void updateMatrixGrid(GridPane grid, Matrica matrix) {
        clearGrid(grid);

        int size = matrix.getSize();

        // Додаємо стовпці та рядки
        for (int i = 0; i < size; i++) {
            grid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(50));
            grid.getRowConstraints().add(new javafx.scene.layout.RowConstraints(30));
        }

        // Додаємо текстові поля
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                TextField field = new TextField(String.format("%.1f", matrix.getValue(i, j)));
                field.setPrefWidth(50);
                grid.add(field, j, i);
            }
        }
    }

    private Matrica readMatrixFromGrid(GridPane grid) {
        int childCount = grid.getChildren().size();
        int size = (int) Math.sqrt(childCount);
        Matrica matrix = new Matrica(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                TextField field = (TextField) grid.getChildren().get(i * size + j);
                double value = Double.parseDouble(field.getText());
                matrix.setValue(i, j, value);
            }
        }
        return matrix;
    }

    private void clearGrid(GridPane grid) {
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

    private void clearResultGrid() {
        clearGrid(resultGrid);
        resultGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(200));
        resultGrid.getRowConstraints().add(new javafx.scene.layout.RowConstraints(30));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}