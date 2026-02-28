

public class Main {
    public static void main(String[] args) {
        // Create a new Matrix. The constructor automatically handles jagged arrays
        // by padding missing elements with zeros.
        Matrix m = new Matrix(new double[][]{{1,2,3,4},{5,6},{7,8},{9}});
        String matrix = m.toString();
        System.out.println(matrix);

        // Generate a random 2x3 matrix
        Matrix r = Matrix.random(2,3);
    }
}