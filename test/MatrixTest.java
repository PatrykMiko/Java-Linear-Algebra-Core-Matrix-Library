import org.junit.Test;

import static org.junit.Assert.*;
import java.lang.Math;
import java.util.Random;

/**
 * Unit tests for the Matrix class.
 * Validates initialization, mathematical operations, scalar operations,
 * and advanced algorithms like Gaussian Elimination and Determinants.
 */

public class MatrixTest {

    @org.junit.Test
    public void testConstructorNxM() {
        Matrix m = new Matrix(2,2);
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(4, m.data.length);
        Matrix m2 = new Matrix(3,4);
        assertEquals(3, m2.rows);
        assertEquals(4, m2.cols);
        assertEquals(12, m2.data.length);
    }

    @org.junit.Test
    public void testConstructorArray2D() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        assertEquals(2, m.rows);
        assertEquals(2, m.cols);
        assertEquals(4, m.data.length);
        assertArrayEquals(new double[]{1,2,3,4}, m.data, 0.001);
        double[][] expected= m.asArray();
        assertEquals(2, expected[0].length);
        assertEquals(2, expected[1].length);
        assertArrayEquals(new double[]{1,2}, expected[0], 0.001);
        assertArrayEquals(new double[]{3,4}, expected[1], 0.001);
        Matrix m2 = new Matrix(new double[][]{{1,2,5},{3,4}, {6}});
        assertEquals(3, m2.rows);
        assertEquals(3, m2.cols);
        assertEquals(9, m2.data.length);
        assertArrayEquals(new double[]{1,2,5,3,4,0,6,0,0}, m2.data, 0.001);
        double[][] expected2= m2.asArray();
        assertEquals(3, expected2[0].length);
        assertEquals(3, expected2[1].length);
        assertEquals(3, expected2[2].length);
        assertArrayEquals(new double[]{1,2,5}, expected2[0], 0.001);
        assertArrayEquals(new double[]{3,4,0}, expected2[1], 0.001);
        assertArrayEquals(new double[]{6,0,0}, expected2[2], 0.001);
    }

    @org.junit.Test
    public void asArray() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        double[][] expected= m.asArray();
        assertEquals(m.rows, expected.length);
        assertEquals(m.cols, expected[0].length);
        assertArrayEquals(new double[]{1,2}, expected[0], 0.001);
        assertArrayEquals(new double[]{3,4}, expected[1], 0.001);
        Matrix m2 = new Matrix(new double[][]{{1,2,5},{3,4}, {6}});
        double[][] expected2= m2.asArray();
        assertEquals(m2.rows, expected2.length);
        assertEquals(m2.cols, expected2[0].length);
        assertArrayEquals(new double[]{1,2,5}, expected2[0], 0.001);
        assertArrayEquals(new double[]{3,4,0}, expected2[1], 0.001);
        assertArrayEquals(new double[]{6,0,0}, expected2[2], 0.001);
    }

    @org.junit.Test
    public void get() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        assertEquals(4, m.get(1, 1), 0.001);
        assertEquals(1, m.get(0, 0), 0.001);
        Exception ex = assertThrows( RuntimeException.class, () -> m.get(2, 2) );
        assertTrue(ex.getMessage().contains(String.format("Outside bounds for row %d and col %d", 2, 2)));
    }

    @org.junit.Test
    public void set() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        m.set(1,1,3);
        assertEquals(3, m.get(1, 1), 0.001);
        m.set(0,1,5);
        assertEquals(5, m.get(0, 1), 0.001);
        Exception ex = assertThrows( RuntimeException.class, () -> m.set(2, 2, 1) );
        assertTrue(ex.getMessage().contains(String.format("Outside bounds for row %d and col %d", 2, 2)));
    }

    @Test
    public void testToString() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        String matrix = m.toString();
        long count = matrix.chars()
                .filter(ch -> ch == ' ')
                .count();
        assertEquals(4, count);
        long count2 = matrix.chars()
                .filter(ch -> ch == '[')
                .count();
        assertEquals(3, count2);
        long count3 = matrix.chars()
                .filter(ch -> ch == ']')
                .count();
        assertEquals(3, count3);
        long count4 = matrix.chars()
                .filter(ch -> ch == '\n')
                .count();
        assertEquals(3, count4);
        Matrix m2 = new Matrix(new double[][]{{1,2,5},{3,4}, {6}});
        String matrix2 = m2.toString();
        long count_ = matrix2.chars()
                .filter(ch -> ch == ' ')
                .count();
        assertEquals(9, count_);
        long count_2 = matrix2.chars()
                .filter(ch -> ch == '[')
                .count();
        assertEquals(4, count_2);
        long count_3 = matrix2.chars()
                .filter(ch -> ch == ']')
                .count();
        assertEquals(4, count_3);
        long count_4 = matrix2.chars()
                .filter(ch -> ch == '\n')
                .count();
        assertEquals(4, count_4);
    }

    @org.junit.Test
    public void reshape() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Exception ex = assertThrows( RuntimeException.class, () -> m.reshape(4, 4) );
        assertTrue(ex.getMessage().contains(String.format("%d x %d matrix can't be reshaped to %d x %d", 2, 2, 4, 4)));
        m.reshape(4, 1);
        assertEquals(4, m.rows);
        assertEquals(1, m.cols);
        assertEquals(4, m.data.length);
        assertArrayEquals(new double[]{1,2,3,4}, m.data, 0.001);
    }

    @org.junit.Test
    public void shape() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        int[] expected = m.shape();
        assertArrayEquals(new int[] {2,2}, expected);
        Matrix m2 = new Matrix(new double[][]{{1,2,5},{3,4}, {6}});
        int[] expected2 = m2.shape();
        assertArrayEquals(new int[] {3,3}, expected2);
    }

    @org.junit.Test
    public void add() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.add(m.mul(-1));
        assertArrayEquals(new double[]{0,0,0,0}, expected.data, 0.001);
        Matrix m2 = new Matrix(new double[][]{{0,1},{2,3}});
        Matrix expected2 = m.add(m2);
        assertArrayEquals(new double[]{1,3,5,7}, expected2.data, 0.001);
        Matrix m3 = new Matrix(new double[][]{{0,1, 4},{2,3}});
        Exception ex = assertThrows( RuntimeException.class, () -> m.add(m3));
        assertTrue(ex.getMessage().contains("The matrices must have the same parameters"));
    }

    @org.junit.Test
    public void sub() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.sub(m);
        double expect = expected.frobenius();
        assertEquals(0, expect, 0.001);
        Matrix m2 = new Matrix(new double[][]{{0,1},{2,3}});
        Matrix expected2 = m.sub(m2);
        assertArrayEquals(new double[]{1,1,1,1}, expected2.data, 0.001);
        Matrix m3 = new Matrix(new double[][]{{0,1, 4},{2,3}});
        Exception ex = assertThrows( RuntimeException.class, () -> m.sub(m3));
        assertTrue(ex.getMessage().contains("The matrices must have the same parameters"));
    }

    @org.junit.Test
    public void mul() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.mul(m.add(m.mul(-1)));
        assertArrayEquals(new double[]{0,0,0,0}, expected.data, 0.001);
        Matrix m2 = new Matrix(new double[][]{{0,1},{2,3}});
        Matrix expected2 = m.mul(m2);
        assertArrayEquals(new double[]{0,2,6,12}, expected2.data, 0.001);
        Matrix m3 = new Matrix(new double[][]{{0,1, 4},{2,3}});
        Exception ex = assertThrows( RuntimeException.class, () -> m.mul(m3));
        assertTrue(ex.getMessage().contains("The matrices must have the same parameters"));
    }

    @org.junit.Test
    public void div() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.div(m);
        double expect = expected.frobenius();
        assertEquals(Math.sqrt(m.rows * m.cols), expect, 0.001);
        Matrix m2 = new Matrix(new double[][]{{1,1},{2,2}});
        Matrix expected2 = m.div(m2);
        assertArrayEquals(new double[]{1,2,1.5,2}, expected2.data, 0.001);
        Matrix m3 = new Matrix(new double[][]{{0,1, 4},{2,3}});
        Exception ex = assertThrows( RuntimeException.class, () -> m.div(m3));
        assertTrue(ex.getMessage().contains("The matrices must have the same parameters"));
    }

    @org.junit.Test
    public void testAdd() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.add(2);
        assertArrayEquals(new double[]{3,4,5,6}, expected.data, 0.001);
        Matrix expected2 = m.add(4);
        assertArrayEquals(new double[]{5,6,7,8}, expected2.data, 0.001);
    }

    @org.junit.Test
    public void testSub() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.sub(2);
        assertArrayEquals(new double[]{-1,0,1,2}, expected.data, 0.001);
        Matrix expected2 = m.sub(4);
        assertArrayEquals(new double[]{-3,-2,-1,0}, expected2.data, 0.001);
    }

    @org.junit.Test
    public void testMul() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.mul(2);
        assertArrayEquals(new double[]{2,4,6,8}, expected.data, 0.001);
        Matrix expected2 = m.mul(4);
        assertArrayEquals(new double[]{4,8,12,16}, expected2.data, 0.001);
    }

    @org.junit.Test
    public void testDiv() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        Matrix expected = m.div(2);
        assertArrayEquals(new double[]{0.5,1,1.5,2}, expected.data, 0.001);
        Matrix expected2 = m.div(4);
        assertArrayEquals(new double[]{0.25,0.5,0.75,1}, expected2.data, 0.001);
        Exception ex = assertThrows( RuntimeException.class, () -> m.div(0));
        assertTrue(ex.getMessage().contains("Division by zero"));
    }

    @org.junit.Test
    public void dot() {
        Matrix m1 = new Matrix(new double[][]{{3,1},{2,1},{1,0}});
        Matrix m2 = new Matrix(new double[][]{{1,0,2},{-1,3,1}});
        Matrix expected = m1.dot(m2);
        assertArrayEquals(new double[]{2,3,7,1,3,5,1,0,2}, expected.data, 0.001);
        Matrix m3 = new Matrix(new double[][]{{1,0,2},{-1,3,1},{1,3,2}});
        Exception ex = assertThrows( RuntimeException.class, () -> m1.dot(m3));
        assertTrue(ex.getMessage().contains("Incompatible shapes"));
    }

    @org.junit.Test
    public void frobenius() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        double expect = m.frobenius();
        assertEquals(Math.sqrt(30), expect, 0.001);
        Matrix m2 = new Matrix(new double[][]{{1,3,0,-1},{0,2,1,3},{3,1,2,1}});
        double expect2 = m2.frobenius();
        assertEquals(Math.sqrt(40), expect2, 0.001);
    }

    @org.junit.Test
    public void random() {
        Matrix m = Matrix.random(3,3);
        assertEquals(3, m.rows);
        assertEquals(3, m.cols);
        assertEquals(9, m.data.length);
        Matrix m2 = Matrix.random(2,3);
        assertEquals(2, m2.rows);
        assertEquals(3, m2.cols);
        assertEquals(6, m2.data.length);
    }

    @org.junit.Test
    public void eye() {
        Random rand = new Random();
        int x = rand.nextInt(10) + 1;
        Matrix m = Matrix.eye(x);
        double expect = m.frobenius();
        assertEquals(Math.sqrt(x), expect, 0.001);

    }

    @org.junit.Test
    public void determinant() {
        Matrix m = new Matrix(new double[][]{{1,2},{3,4}});
        double expect = m.determinant();
        assertEquals(-2, expect, 0.001);
        Matrix m2 = new Matrix(new double[][]{{1,3,0,-1},{0,2,1,3},{3,1,2,1},{-1,2,0,3}});
        double expect2 = m2.determinant();
        assertEquals(14, expect2, 0.001);
        Matrix m3 = new Matrix(new double[][]{{1,3,0,-1},{0,2,1,3},{3,1,2,1}});
        Exception ex = assertThrows( RuntimeException.class, m3::determinant);
        assertTrue(ex.getMessage().contains("It has to be a square matrix"));
    }
}