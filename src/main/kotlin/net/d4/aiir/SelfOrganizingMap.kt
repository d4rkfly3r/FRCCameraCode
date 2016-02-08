package net.d4.aiir

/**
 * Created by d4rkfly3r (Joshua F.) on 2/7/16.
 */
public class SelfOrganizingMap(val inputCount: Int, val outputCount: Int, val normalizationType: NormalizeInput.NormalizationType) {
    companion object {
        public val VERYSMALL: Double = 1.0e-30
    }

    var outputWeights: Matrix = Matrix(outputCount, inputCount + 1)
    var output: DoubleArray = DoubleArray(outputCount)

    fun winner(input: DoubleArray): Int {
        val normalizeInput: NormalizeInput = NormalizeInput(input, normalizationType)
        return winner(normalizeInput)
    }

    fun winner(normalizeInput: NormalizeInput): Int {
        var win: Int = 0
        var biggest: Double = Double.MIN_VALUE
        for (i in 0..outputCount) {
            val optr: Matrix = outputWeights.getRow(i)
            this.output[i] = MatrixMath.dotProduct(normalizeInput.inputMatrix, optr) * normalizeInput.normalizationFactor

            this.output[i] = (this.output[i] + 1.0) / 2.0

            if (this.output[i] > biggest) {
                biggest = this.output[i]
                win = i
            }

            if (this.output[i] < 0) {
                this.output[i] = 0.0
            }

            if (this.output[i] > 1) {
                this.output[i] = 1.0
            }

        }
        return win
    }

}