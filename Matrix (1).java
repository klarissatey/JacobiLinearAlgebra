public class Matrix {

    private int row; // Number of rows in the matrix
    public double[][] matrix; // Main matrix for operations
    public double[][] L; // Lower triangular matrix for LU decomposition
    public double[][] U; // Upper triangular matrix for LU decomposition
    private int N; // Dimension of the nxn matrix where n = N^2
    public int operations; // Counter for tracking the number of operations performed

    // Constructor to initialize the matrix and its dimensions
    public Matrix(int N) {
        this.N = N;
        row = N * N;
        matrix = new double[N * N][N * N];
        L = new double[N * N][N * N];
        U = new double[N * N][N * N];
        for (int i = 0; i < row; i++) {
            L[i][i] = 1;
        }
    }

    // Method to set up a Poisson matrix
    public void makePoisson() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int index = i * N + j;
                matrix[index][index] = -4;
                if (i > 0) {
                    matrix[index][index - N] = 1;
                }
                if (i < N - 1) {
                    matrix[index][index + N] = 1;
                }
                if (j > 0) {
                    matrix[index][index - 1] = 1;
                }
                if (j < N - 1) {
                    matrix[index][index + 1] = 1;
                }
            }
        }
    }

    // Method to perform LU decomposition on the main matrix
    public void luDecomp() {
        U = copyMatrix(matrix);
        for (int currentRow = 0; currentRow < row; currentRow++) {
            for (int belowRows = currentRow + 1; belowRows < row; belowRows++) {
                double coefficient = U[belowRows][currentRow] / U[currentRow][currentRow];
                if (Math.abs(coefficient) < 1e-12) {
                    continue; // Skip if coefficient is basically zero
                }
                for (int rowTraverse = currentRow; rowTraverse < row; rowTraverse++) {
                    U[belowRows][rowTraverse] = U[belowRows][rowTraverse]
                            - U[currentRow][rowTraverse] * coefficient;
                }

                L[belowRows][currentRow] = coefficient;
                operations += 3;
            }
        }
    }

    // Count the number of non-zero entries in a matrix
    public int countSignificantNumbers(double[][] certainMatrix) {
        int count = 0;
        for (int i = 0; i < certainMatrix.length; i++) {
            for (int j = 0; j < certainMatrix[0].length; j++) {
                double num = certainMatrix[i][j];
                if (Math.abs(certainMatrix[i][j]) < 0.0000000001) {
                    // make sure to set numbers that are very close to zero equal to zero
                    num = 0.0;
                }
                if (num != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    // Prints the number of non-zero elements in L and U matrices
    public void printSignificantNumbers() {
        int LSig = countSignificantNumbers(L);
        int USig = countSignificantNumbers(U);
        System.out.println(LSig);
        System.out.println(USig);
    }

    // Method to print matrices with specific spacing and decimal precision
    public void printMatrix(double[][] matrix, int spacing, int decimal) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            sb.append("|");
            for (double val : row) {
                sb.append(String.format("%" + spacing + "." + decimal + "f", val));
            }
            sb.append("  |\n");
        }
        System.out.println(sb.toString());
    }

    // Helper method to copy matrices
    public double[][] copyMatrix(double[][] certainMatrix) {
        double[][] newArray = new double[certainMatrix.length][certainMatrix[0].length];
        for (int i = 0; i < certainMatrix.length; i++) {
            for (int j = 0; j < certainMatrix[0].length; j++) {
                newArray[i][j] = certainMatrix[i][j];
            }
        }
        return newArray;
    }

}
