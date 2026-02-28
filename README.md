# Java Matrix Library

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-Tested-25A162?style=for-the-badge&logo=junit5&logoColor=white)

A lightweight, dependency-free Java library for matrix manipulation and linear algebra operations. 

## Overview
This project provides a custom `Matrix` class designed for mathematical accuracy and memory efficiency. It handles fundamental mathematical operations, matrix transformations, and more complex algebraic computations like Gaussian Elimination and Determinants.

## Features
* **Standard Operations:** Addition, subtraction, multiplication, and division (both scalar and element-wise).
* **Linear Algebra:** Dot product (matrix multiplication), Frobenius norm.
* **Advanced Math:** Gaussian Elimination (Row Echelon Form) and Determinant calculation.
* **Utility:** Generation of Identity (`eye`) and Random matrices.
* **Robust Initialization:** Safely parses 2D arrays, automatically padding jagged arrays with zeros to ensure a perfect rectangular shape.

## Technical Details: Memory Optimization
Instead of using a traditional array of arrays (`double[][]`), the underlying matrix data is stored in a **flattened 1D array (`double[]`)**. 

This approach ensures contiguous memory allocation, which significantly improves CPU cache spatial locality and overall performance during heavy mathematical operations. Indexing is handled transparently via the formula `i * cols + j`.

## Usage

### Initialization
```java
// From a 2D array
Matrix a = new Matrix(new double[][]{{1, 2}, {3, 4}});

// Generate a 3x3 Identity matrix
Matrix b = Matrix.eye(3);

// Generate a 2x4 Random matrix
Matrix c = Matrix.random(2, 4);
