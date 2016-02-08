package net.d4.aiir

import java.io.Serializable

/**
 * Created by d4rkfly3r (Joshua F.) on 2/7/16.
 */
public class Matrix : Serializable, Cloneable {

    var matrix: Array<DoubleArray> = Array(1) { DoubleArray(1) }

    constructor(sourceMatrix: Array<DoubleArray>) {
        this.matrix = Array(sourceMatrix.size) { DoubleArray(sourceMatrix[0].size) }
        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                this.set(r, c, sourceMatrix[r][c])
            }
        }

    }

    constructor(rows: Int, cols: Int) {
        this.matrix = Array(rows) { DoubleArray(cols) }
    }

    companion object {
        fun createColumnMatrix(input: DoubleArray): Matrix {
            val d = Array(input.size) { DoubleArray(1) }
            for (row in 0..d.size - 1) {
                d[row][0] = input[row]
            }
            return Matrix(d)
        }

        fun createRowMatrix(input: DoubleArray): Matrix {
            val d = Array(1) { DoubleArray(input.size) }
            System.arraycopy(input, 0, d[0], 0, input.size)
            return Matrix(d)
        }

        public class MatrixError : RuntimeException {

            /**
             * Construct this exception with a message.
             * @param message The message.
             */
            constructor(message: String) : super(message) {
            }

            /**
             * Construct this exception with another exception.
             * @param t The other exception.
             */
            constructor(t: Throwable) : super(t) {
            }

        }

    }

    operator fun set(row: Int, col: Int, value: Double) {
        validate(row, col)
        if (java.lang.Double.isInfinite(value) || java.lang.Double.isNaN(value)) {
            throw MatrixError("Trying to assign invalid number to matrix: $value")
        }
        this.matrix[row][col] = value
    }


    fun add(row: Int, col: Int, value: Double) {
        validate(row, col)
        val newValue = get(row, col) + value
        set(row, col, newValue)
    }

    fun clear() {
        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                set(r, c, 0.0)
            }
        }
    }


    override fun clone(): Matrix = Matrix(this.matrix)

    fun equals(matrix: Matrix): Boolean = equals(matrix, 10.0)

    fun equals(matrix: Matrix, precision: Double): Boolean {
        if (precision < 0) {
            throw MatrixError("Precision can't be negative: $precision")
        }

        val nPrex: Double = Math.pow(10.0, precision)
        if (nPrex.isInfinite() || nPrex > Long.MAX_VALUE) {
            throw MatrixError("Precision of $precision decimal places is not supported.")
        }

        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                if ((get(r, c) * nPrex) != (matrix[r, c] * nPrex)) {
                    return false
                }
            }
        }

        return true
    }


    /**

     * @param array
     * *
     * @param index
     * *
     * @return The new index after this matrix has been read.
     */
    fun fromPackedArray(array: Array<Double>, index: Int): Int {
        var inx = index

        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                this.matrix[r][c] = array[inx++]
            }
        }

        return inx
    }

    operator fun get(row: Int, col: Int): Double {
        validate(row, col)
        return this.matrix[row][col]
    }

    fun getCol(col: Int): Matrix {
        if (col > getCols()) {
            throw MatrixError("Can't get column #$col because it does not exist.")
        }

        val newMatrix = Array(getRows()) { DoubleArray(1) }

        for (row in 0..getRows() - 1) {
            newMatrix[row][0] = this.matrix[row][col]
        }

        return Matrix(newMatrix)
    }

    fun getCols(): Int = this.matrix[0].size

    fun getRow(row: Int): Matrix {
        if (row > getRows()) {
            throw MatrixError("Can't get row #$row because it does not exist.")
        }

        val newMatrix = Array(1) { DoubleArray(getCols()) }

        for (col in 0..getCols() - 1) {
            newMatrix[0][col] = this.matrix[row][col]
        }

        return Matrix(newMatrix)
    }

    fun getRows(): Int = this.matrix.size

    private fun validate(row: Int, col: Int) {
        if ((row >= getRows()) || (row < 0)) {
            throw MatrixError("The row: $row is out of range: ${getRows()}")
        }

        if ((col >= getCols()) || (col < 0)) {
            throw MatrixError("The col: $col is out of range: ${getCols()}")
        }
    }

    fun isVector(): Boolean {
        if (getRows() === 1) {
            return true
        } else {
            return getCols() === 1
        }
    }

    fun isZero(): Boolean {
        for (row in 0..getRows() - 1) {
            for (col in 0..getCols() - 1) {
                if (this.matrix[row][col] !== 0.0) {
                    return false
                }
            }
        }
        return true
    }

    fun ramdomize(min: Double, max: Double) {
        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                this.matrix[r][c] = (Math.random() * (max - min)) + min
            }
        }
    }

    fun size(): Int {
        return this.matrix[0].size * this.matrix.size
    }

    fun sum(): Double {
        var result = 0.0
        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                result += this.matrix[r][c]
            }
        }
        return result
    }

    fun toPackedArray(): Array<Double?> {
        val result = arrayOfNulls<Double>(getRows() * getCols())

        var index = 0
        for (r in 0..getRows() - 1) {
            for (c in 0..getCols() - 1) {
                result[index++] = this.matrix[r][c]
            }
        }

        return result
    }

}