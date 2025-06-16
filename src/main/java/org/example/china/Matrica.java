package org.example.china;

import java.util.Random;

public class Matrica {
    private double[][] data;
    private final int size;

    public Matrica(int size) {
        this.size = size;
        this.data = new double[size][size];
    }

    public Matrica(Matrica other) {
        this.size = other.size;
        this.data = new double[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(other.data[i], 0, this.data[i], 0, size);
        }
    }

    public static Matrica createRandom(int size) {
        Matrica matrix = new Matrica(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.data[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    // Додавання матриць
    public Matrica add(Matrica other) {
        if (this.size != other.size) {
            throw new IllegalArgumentException("Matrices must have the same size");
        }
        Matrica result = new Matrica(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.data[i][j] = this.data[i][j] + other.data[i][j];
            }
        }
        return result;
    }

    // Множення матриць
    public Matrica multiply(Matrica other) {
        if (this.size != other.size) {
            throw new IllegalArgumentException("Matrices must have the same size");
        }
        Matrica result = new Matrica(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result.data[i][j] += this.data[i][k] * other.data[k][j];
                }
            }
        }
        return result;
    }

    // Транспонування матриці
    public Matrica transpose() {
        Matrica result = new Matrica(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    // Обчислення детермінанта (рекурсивно)
    public double getDeterminant() {
        return calculateDeterminant(data, size);
    }

    private double calculateDeterminant(double[][] matrix, int n) {
        if (n == 1) {
            return matrix[0][0];
        }
        double determinant = 0;
        double[][] temp = new double[n][n];
        int sign = 1;
        for (int f = 0; f < n; f++) {
            getCofactor(matrix, temp, 0, f, n);
            determinant += sign * matrix[0][f] * calculateDeterminant(temp, n - 1);
            sign = -sign;
        }
        return determinant;
    }

    private void getCofactor(double[][] matrix, double[][] temp, int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    public int getSize() {
        return size;
    }

    public double getValue(int row, int col) {
        return data[row][col];
    }

    public void setValue(int row, int col, double value) {
        data[row][col] = value;
    }
}