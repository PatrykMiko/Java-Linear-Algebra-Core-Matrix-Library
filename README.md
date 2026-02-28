# Java Linear Algebra: Core Matrix Library

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-Tested-25A162?style=for-the-badge&logo=junit5&logoColor=white)

A custom, dependency-free Java library for Matrix representation and linear algebra operations. I built this project to demonstrate a deep understanding of Object-Oriented Programming, algorithm design, and memory-efficient data structures in Java.

## 🚀 Why This Project? 

When applying for internships, I wanted to showcase more than just basic CRUD operations. This project demonstrates my ability to:
* **Optimize Data Structures:** Instead of using a standard `double[][]` array which scatters data in memory (array of arrays), the matrix data is flattened into a single contiguous 1D `double[]` array. This provides better spatial locality and CPU cache performance.
* **Implement Complex Algorithms:** Features mathematical algorithms written from scratch, including **Gaussian Elimination with partial pivoting** and **Determinant calculation**.
* **Practice Test-Driven Development (TDD):** The core logic is backed by extensive JUnit testing covering edge cases, bounds checking, and math correctness.
* **Handle Edge Cases Gracefully:** Custom exception handling prevents invalid mathematical operations (e.g., matrix dimension mismatches, division by zero), and the constructor elegantly pads jagged arrays with zeros.

## ⚙️ Features

* **Initialization:** Create matrices of specific shapes, identity matrices (`eye`), random matrices, or parse 2D jagged arrays.
* **Element-wise Operations:** Addition, Subtraction, Multiplication, and Division (matrix-matrix and matrix-scalar).
* **Matrix Operations:**
  * Dot Product (Matrix Multiplication)
  * Reshaping dimensions
  * Frobenius Norm calculation
  * Gaussian Elimination (row echelon form)
  * Determinant calculation ($O(N^3)$ complexity using Gaussian elimination)

## 🛠️ Usage Example

```java
// Initialize matrices
Matrix a = new Matrix(new double[][]{{1, 2}, {3, 4}});
Matrix b = Matrix.eye(2); // 2x2 Identity Matrix

// Perform element-wise operations
Matrix added = a.add(b); 
Matrix scalarMul = a.mul(2.5);

// Matrix Multiplication (Dot Product)
Matrix product = a.dot(b);

// Advanced Math
double det = a.determinant(); // Returns -2.0
Matrix echelonForm = a.GaussianElimination();
