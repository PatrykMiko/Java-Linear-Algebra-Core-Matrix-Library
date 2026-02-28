import java.util.Random;

/**
 * A custom Matrix library implementation.
 * Underlying data is stored in a flattened 1D array for contiguous memory allocation
 * and better cache performance.
 */
public class Matrix {
    double[] data;
    int rows;
    int cols;

    // Tracks row swaps during Gaussian Elimination to accurately calculate the determinant sign
    public int swaps = 0;

    /**
     * Constructs an empty matrix with specified dimensions.
     * @param rows Number of rows
     * @param cols Number of columns
     */
    Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        data = new double[rows * cols];
    }

    /**
     * Constructs a matrix from a 2D array.
     * Automatically handles jagged arrays by padding them with zeros to form a perfect rectangle.
     * @param d 2D array of doubles
     */
    Matrix(double[][] d){
        rows = d.length;
        cols = d[0].length;
        // Find the maximum column length to handle jagged arrays
        for(int i = 1; i < rows; i++){
            if(d[i].length > cols){
                cols = d[i].length;
            }
        }
        data = new double[rows * cols];

        // Flatten the 2D array into the 1D data array
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if (j < d[i].length) {
                    data[i * cols + j] = d[i][j];
                } else {
                    data[i * cols + j] = 0; // Padding
                }
            }
        }
    }

    /**
     * Converts the 1D internal representation back to a 2D array.
     */
    double[][] asArray(){
        double[][] matrix = new double[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                matrix[i][j] = data[i * cols + j];
            }
        }
        return matrix;
    }

    /**
     * Retrieves a value from the matrix safely.
     */
    double get(int r, int c){
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new RuntimeException(String.format("Outside bounds for row %d and col %d", r, c));
        }
        return data[r * cols + c];
    }

    /**
     * Sets a value in the matrix safely.
     */
    void set(int r, int c, double value){
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new RuntimeException(String.format("Outside bounds for row %d and col %d", r, c));
        }
        data[r * cols + c] = value;
    }

    /**
     * Returns a string representation of the matrix.
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[\n");
        for (int i = 0; i < rows; i++) {
            buf.append(" [");
            for (int j = 0; j < cols; j++) {
                buf.append(data[i * cols + j]);
                if (j < cols - 1) buf.append(" ");
            }
            buf.append("]\n");
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * Reshapes the matrix without changing its data.
     * Total number of elements must remain the same.
     */
    void reshape(int newRows, int newCols){
        if(rows * cols != newRows * newCols) {
            throw new RuntimeException(String.format("%d x %d matrix can't be reshaped to %d x %d", rows, cols, newRows, newCols));
        }
        rows = newRows;
        cols = newCols;
    }

    /**
     * Returns the dimensions of the matrix.
     */
    int[] shape() {
        return new int[]{rows, cols};
    }

    // --- ELEMENT-WISE OPERATIONS WITH ANOTHER MATRIX ---

    Matrix add(Matrix m){
        if(rows != m.rows || cols != m.cols) {
            throw new RuntimeException("The matrices must have the same parameters");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) + m.get(i, j));
            }
        }
        return result;
    }

    Matrix sub(Matrix m){
        if(rows != m.rows || cols != m.cols) {
            throw new RuntimeException("The matrices must have the same parameters");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) - m.get(i, j));
            }
        }
        return result;
    }

    Matrix mul(Matrix m){
        if(rows != m.rows || cols != m.cols) {
            throw new RuntimeException("The matrices must have the same parameters");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) * m.get(i, j));
            }
        }
        return result;
    }

    Matrix div(Matrix m){
        if(rows != m.rows || cols != m.cols) {
            throw new RuntimeException("The matrices must have the same parameters");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) / m.get(i, j));
            }
        }
        return result;
    }

    // --- SCALAR OPERATIONS ---

    Matrix add(double w){
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) + w);
            }
        }
        return result;
    }

    Matrix sub(double w){
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) - w);
            }
        }
        return result;
    }

    Matrix mul(double w){
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) * w);
            }
        }
        return result;
    }

    Matrix div(double w){
        if(w == 0) {
            throw new RuntimeException("Division by zero");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result.set(i, j, this.get(i, j) / w);
            }
        }
        return result;
    }

    // --- ALGEBRAIC OPERATIONS ---

    /**
     * Performs matrix multiplication (dot product).
     */
    Matrix dot(Matrix m){
        if(this.cols != m.rows) {
            throw new RuntimeException("Incompatible shapes");
        }
        Matrix result = new Matrix(this.rows, m.cols);

        for(int i=0; i<result.rows; i++){
            for(int j=0; j<result.cols; j++){
                double value = 0;
                for (int k=0; k<this.cols; k++){
                    value += this.get(i, k) * m.get(k, j);
                }
                result.set(i, j, value);
            }
        }
        return result;
    }

    /**
     * Calculates the Frobenius norm (Euclidean norm) of the matrix.
     */
    double frobenius(){
        double result = 0;
        for(int i=0; i<rows*cols; i++){
            result += data[i]*data[i];
        }
        result = Math.sqrt(result);
        return result;
    }

    /**
     * Generates a matrix filled with random values.
     */
    public static Matrix random(int rows, int cols){
        Matrix m = new Matrix(rows, cols);
        Random r = new Random();
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                m.set(i, j, r.nextDouble());
            }
        }
        return m;
    }

    /**
     * Generates an identity matrix of size n x n.
     */
    public static Matrix eye(int n){
        Matrix m = new Matrix(n, n);
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(i == j){
                    m.set(i, j, 1);
                } else {
                    m.set(i, j, 0);
                }
            }
        }
        return m;
    }

    /**
     * Performs Gaussian elimination with partial pivoting.
     * Transforms the matrix into row echelon form.
     */
    Matrix GaussianElimination(){
        Matrix result = new Matrix(this.asArray());

        if(result.data.length == 0){
            throw new RuntimeException("Empty matrix");
        }

        int m = result.rows;
        int n = result.cols;
        int h = 0;
        int k = 0;

        while(h < m && k < n){
            // Find pivot for column k
            int i_max = h;
            for(int i = h + 1; i < m; i++){
                if(Math.abs(result.get(i, k)) > Math.abs(result.get(i_max, k))){
                    i_max = i;
                }
            }
            if(result.get(i_max, k) == 0){
                // No pivot in this column, pass to next column
                k++;
            } else {
                // Swap rows if necessary
                if (i_max != h) {
                    for(int j = 0; j < n; j++){
                        double tmp = result.get(h, j);
                        result.set(h, j, result.get(i_max, j));
                        result.set(i_max, j, tmp);
                    }
                    result.swaps++; // Track swaps for determinant calculation
                }

                // Eliminate subsequent rows
                for(int i = h + 1; i < m; i++){
                    double f = result.get(i, k) / result.get(h, k);
                    result.set(i, k, 0.0);
                    for(int j = k + 1; j < n; j++){
                        result.set(i, j, result.get(i, j) - result.get(h, j) * f);
                    }
                }
                h++;
                k++;
            }
        }
        return result;
    }

    /**
     * Calculates the determinant of a square matrix using Gaussian Elimination.
     */
    public double determinant(){
        if(rows != cols){
            throw new RuntimeException("It has to be a square matrix");
        }

        Matrix m = GaussianElimination();
        double result = 1.0;

        // Multiply diagonal elements
        for(int i=0; i<m.rows; i++){
            for(int j=0; j<m.cols; j++){
                if(i == j){
                    result *= m.get(i, j);
                }
            }
        }

        // Adjust sign based on the number of row swaps during elimination
        if (m.swaps % 2 != 0) {
            result = -result;
        }

        return result;
    }
}
