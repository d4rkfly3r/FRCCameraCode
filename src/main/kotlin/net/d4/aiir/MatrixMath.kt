package net.d4.aiir

import kotlin.collections.indices

/**
 * Created by d4rkfly3r (Joshua F.) on 2/7/16.
 */
object MatrixMath {

    fun add(a: Matrix, b: Matrix): Matrix {
        if (a.getRows() !== b.getRows()) {
            throw Matrix.Companion.MatrixError(
                    "To add the matrices they must have the same number of rows and columns.  Matrix a has " + a.getRows() + " rows and matrix b has " + b.getRows() + " rows.")
        }

        if (a.getCols() !== b.getCols()) {
            throw Matrix.Companion.MatrixError(
                    "To add the matrices they must have the same number of rows and columns.  Matrix a has " + a.getCols() + " cols and matrix b has " + b.getCols() + " cols.")
        }

        val result = Array(a.getRows()) { DoubleArray(a.getCols()) }

        for (resultRow in 0..a.getRows() - 1) {
            for (resultCol in 0..a.getCols() - 1) {
                result[resultRow][resultCol] = a[resultRow, resultCol] + b[resultRow, resultCol]
            }
        }

        return Matrix(result)
    }

    fun copy(source: Matrix, target: Matrix) {
        for (row in 0..source.getRows() - 1) {
            for (col in 0..source.getCols() - 1) {
                target.set(row, col, source[row, col])
            }
        }

    }

    fun copy(bestnet: SelfOrganizingMap, selfOrganizingMap: SelfOrganizingMap) {

    }

    fun deleteCol(matrix: Matrix, deleted: Int): Matrix {
        if (deleted >= matrix.getCols()) {
            throw Matrix.Companion.MatrixError("Can't delete column " + deleted + " from matrix, it only has " + matrix.getCols() + " columns.")
        }
        val newMatrix = Array(matrix.getRows()) { DoubleArray(matrix.getCols() - 1) }

        for (row in 0..matrix.getRows() - 1) {
            var targetCol = 0

            for (col in 0..matrix.getCols() - 1) {
                if (col != deleted) {
                    newMatrix[row][targetCol] = matrix[row, col]
                    targetCol++
                }

            }

        }
        return Matrix(newMatrix)
    }

    fun deleteRow(matrix: Matrix, deleted: Int): Matrix {
        if (deleted >= matrix.getRows()) {
            throw Matrix.Companion.MatrixError("Can't delete row " + deleted + " from matrix, it only has " + matrix.getRows() + " rows.")
        }
        val newMatrix = Array(matrix.getRows() - 1) { DoubleArray(matrix.getCols()) }
        var targetRow = 0
        for (row in 0..matrix.getRows() - 1) {
            if (row != deleted) {
                for (col in 0..matrix.getCols() - 1) {
                    newMatrix[targetRow][col] = matrix[row, col]
                }
                targetRow++
            }
        }
        return Matrix(newMatrix)
    }

    fun divide(a: Matrix, b: Double): Matrix {
        val result = Array(a.getRows()) { DoubleArray(a.getCols()) }
        for (row in 0..a.getRows() - 1) {
            for (col in 0..a.getCols() - 1) {
                result[row][col] = a[row, col] / b
            }
        }
        return Matrix(result)
    }

    fun dotProduct(a: Matrix, b: Matrix): Double {
        if (!a.isVector() || !b.isVector()) {
            throw Matrix.Companion.MatrixError(
                    "To take the dot product, both matrices must be vectors.")
        }

        val aArray = a.toPackedArray()
        val bArray = b.toPackedArray()

        if (aArray.size != bArray.size) {
            throw Matrix.Companion.MatrixError(
                    "To take the dot product, both matrices must be of the same length.")
        }

        var result = 0.0
        val length = aArray.size

        for (i in 0..length - 1) {
            result += aArray[i]!! * bArray[i]!!
        }

        return result
    }

    fun identity(size: Int): Matrix {
        if (size < 1) {
            throw Matrix.Companion.MatrixError("Identity matrix must be at least of size 1.")
        }

        val result = Matrix(size, size)

        for (i in 0..size - 1) {
            result.set(i, i, 1.0)
        }

        return result
    }

    fun multiply(a: Matrix, b: Double): Matrix {
        val result = Array(a.getRows()) { DoubleArray(a.getCols()) }
        for (row in 0..a.getRows() - 1) {
            for (col in 0..a.getCols() - 1) {
                result[row][col] = a[row, col] * b
            }
        }
        return Matrix(result)
    }

    fun multiply(a: Matrix, b: Matrix): Matrix {
        if (a.getCols() !== b.getRows()) {
            throw Matrix.Companion.MatrixError(
                    "To use ordinary matrix multiplication the number of columns on the first matrix must mat the number of rows on the second.")
        }

        val result = Array(a.getRows()) { DoubleArray(b.getCols()) }

        for (resultRow in 0..a.getRows() - 1) {
            for (resultCol in 0..b.getCols() - 1) {
                var value = 0.0

                for (i in 0..a.getCols() - 1) {

                    value += a[resultRow, i] * b[i, resultCol]
                }
                result[resultRow][resultCol] = value
            }
        }

        return Matrix(result)
    }

    fun subtract(a: Matrix, b: Matrix): Matrix {
        if (a.getRows() !== b.getRows()) {
            throw Matrix.Companion.MatrixError(
                    "To subtract the matrices they must have the same number of rows and columns.  Matrix a has " + a.getRows() + " rows and matrix b has " + b.getRows() + " rows.")
        }

        if (a.getCols() !== b.getCols()) {
            throw Matrix.Companion.MatrixError(
                    "To subtract the matrices they must have the same number of rows and columns.  Matrix a has " + a.getCols() + " cols and matrix b has " + b.getCols() + " cols.")
        }

        val result = Array(a.getRows()) { DoubleArray(a.getCols()) }

        for (resultRow in 0..a.getRows() - 1) {
            for (resultCol in 0..a.getCols() - 1) {
                result[resultRow][resultCol] = a[resultRow, resultCol] - b[resultRow, resultCol]
            }
        }

        return Matrix(result)
    }

    fun transpose(input: Matrix): Matrix {
        val inverseMatrix = Array(input.getCols()) { DoubleArray(input.getRows()) }

        for (r in 0..input.getRows() - 1) {
            for (c in 0..input.getCols() - 1) {
                inverseMatrix[c][r] = input[r, c]
            }
        }

        return Matrix(inverseMatrix)
    }

    fun vectorLength(input: Matrix): Double {
        if (!input.isVector()) {
            throw Matrix.Companion.MatrixError(
                    "Can only take the vector length of a vector.")
        }
        val v = input.toPackedArray()
        var rtn = 0.0
        for (i in v.indices) {
            rtn += Math.pow(v[i]!!, 2.0)
        }
        return Math.sqrt(rtn)
    }

}
