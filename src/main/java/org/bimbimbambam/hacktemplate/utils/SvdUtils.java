package org.bimbimbambam.hacktemplate.utils;

public class SvdUtils {
    public static class SVDResult {
        public final double[][] U;
        public final double[] S;
        public final double[][] V;

        public SVDResult(double[][] U, double[] S, double[][] V) {
            this.U = U;
            this.S = S;
            this.V = V;
        }
    }

    public static SVDResult computeSVD(double[][] A) {
        int rows = A.length;
        int cols = A[0].length;

        // Step 1: Compute A^T * A
        double[][] ATA = multiply(transpose(A), A);

        // Step 2: Compute eigenvalues and eigenvectors of A^T * A
        EigenDecompositionResult eigResult = computeEigenDecomposition(ATA);
        double[] eigenValues = eigResult.eigenValues;
        double[][] V = eigResult.eigenVectors;

        // Step 3: Compute singular values (sqrt of eigenvalues)
        double[] singularValues = new double[eigenValues.length];
        for (int i = 0; i < eigenValues.length; i++) {
            singularValues[i] = Math.sqrt(Math.max(eigenValues[i], 0));
        }

        // Step 4: Compute U = A * V * Sigma^-1
        double[][] U = new double[rows][cols];
        for (int i = 0; i < singularValues.length; i++) {
            if (singularValues[i] > 1e-10) { // Avoid division by zero
                double[] vColumn = getColumn(V, i);
                double[] uColumn = multiplyVectorByMatrix(A, vColumn);
                double norm = vectorNorm(uColumn);
                for (int j = 0; j < rows; j++) {
                    U[j][i] = uColumn[j] / norm;
                }
            }
        }

        return new SVDResult(U, singularValues, V);
    }

    private static double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposed = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    private static double[][] multiply(double[][] A, double[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    private static double[] multiplyVectorByMatrix(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    private static double vectorNorm(double[] vector) {
        double norm = 0.0;
        for (double v : vector) {
            norm += v * v;
        }
        return Math.sqrt(norm);
    }

    private static double[] getColumn(double[][] matrix, int colIndex) {
        int rows = matrix.length;
        double[] column = new double[rows];
        for (int i = 0; i < rows; i++) {
            column[i] = matrix[i][colIndex];
        }
        return column;
    }

    private static class EigenDecompositionResult {
        public final double[] eigenValues;
        public final double[][] eigenVectors;

        public EigenDecompositionResult(double[] eigenValues, double[][] eigenVectors) {
            this.eigenValues = eigenValues;
            this.eigenVectors = eigenVectors;
        }
    }

    private static EigenDecompositionResult computeEigenDecomposition(double[][] matrix) {
        // Use a simple power iteration or other eigenvalue algorithms here
        // This is a placeholder, as the implementation is complex
        return new EigenDecompositionResult(new double[]{}, new double[][]{});
    }
}

