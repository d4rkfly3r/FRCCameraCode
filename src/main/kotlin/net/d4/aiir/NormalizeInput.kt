package net.d4.aiir

import kotlin.collections.indices

/**
 * Created by d4rkfly3r (Joshua F.) on 2/7/16.
 */
class NormalizeInput(val input: DoubleArray, val type: NormalizeInput.NormalizationType) {
    enum class NormalizationType {
        Z_AXIS, MULTIPLICATIVE, OTHER, ZY_AXIS
    }

    /**
     * The normalization factor.
     */
    public var normalizationFactor: Double = 0.0
        private set

    /**
     * The synthetic input.
     */
    public var synth: Double = 0.0
        private set

    /**
     * The input expressed as a matrix.
     */
    public var inputMatrix: Matrix = createInputMatrix(input, synth)
        private set


    init {
        calculateFactors(input)
    }

    protected fun createInputMatrix(pattern: DoubleArray, extra: Double): Matrix {
        val result = Matrix(1, pattern.size + 1)
        for (i in pattern.indices) {
            result.set(0, i, pattern[i])
        }

        result.set(0, pattern.size, extra)

        return result
    }

    protected fun calculateFactors(input: DoubleArray) {
        val inputMatrix = Matrix.createColumnMatrix(input)
        var len = MatrixMath.vectorLength(inputMatrix)
        len = Math.max(len, SelfOrganizingMap.VERYSMALL)
        val numInputs = input.size

        if (this.type === NormalizationType.MULTIPLICATIVE) {
            this.normalizationFactor = 1.0 / len
            this.synth = 0.0
        } else {
            this.normalizationFactor = 1.0 / Math.sqrt(numInputs.toDouble())
            val d = numInputs - Math.pow(len, 2.0)
            if (d > 0.0) {
                this.synth = Math.sqrt(d) * this.normalizationFactor
            } else {
                this.synth = 0.0
            }
        }

    }
}


