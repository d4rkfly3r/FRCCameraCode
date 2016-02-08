package net.d4.aiir

/**
 * Created by d4rkfly3r (Joshua F.) on 2/7/16.
 */
class TrainSelfOrganizingMap(val selfOrganizingMap: SelfOrganizingMap, val train: Array<DoubleArray>, val learningMethod: TrainSelfOrganizingMap.LearningMethod, var learnRate: Double) {
    enum class LearningMethod {
        ADDITIVE,
        SUBTRACTIVE
    }

    protected var reduction: Double = 0.99
    protected var globalError: Double = 0.0
    protected val outputCount: Int = selfOrganizingMap.outputCount
    protected val inputCount: Int = selfOrganizingMap.inputCount
    protected val bestNetwork: SelfOrganizingMap = SelfOrganizingMap(inputCount, outputCount, selfOrganizingMap.normalizationType)
    protected val won: IntArray = IntArray(outputCount)
    protected val correctionMatrix: Matrix = Matrix(outputCount, inputCount + 1)
    protected var workMatrix: Matrix? = if (learningMethod == LearningMethod.ADDITIVE) Matrix(1, inputCount + 1) else null
    public var totalError: Double = 1.0
        private set
    public var bestError: Double = 0.0
        private set

    init {
        for (tset in 0..train.size - 1) {
            val dptr = Matrix.createColumnMatrix(train[tset])
            if (MatrixMath.vectorLength(dptr) < SelfOrganizingMap.VERYSMALL) {
                throw RuntimeException("Multiplicative normalization has null training case")
            }
        }

        initialize()

        bestError = Double.MAX_VALUE
    }

    fun initialize() {
        selfOrganizingMap.outputWeights.ramdomize(-1.0, 1.0)

        for (i in 0..outputCount - 1) {
            normalizeWeight(selfOrganizingMap.outputWeights, i)
        }
    }

    protected fun normalizeWeight(matrix: Matrix, row: Int) {
        var len: Double = MatrixMath.vectorLength(matrix.getRow(row))
        len = Math.max(len, SelfOrganizingMap.VERYSMALL)

        len = 1.0 / len
        for (i in 0..inputCount - 1) {
            matrix.set(row, i, matrix[row, i] * len)
        }
        matrix.set(row, inputCount, 0.0)
    }

    fun adjustWeights() {
        for (i in 0..outputCount - 1) {
            if (won[i] == 0) continue

            var f: Double = 1.0 / won[i]
            if (learningMethod == LearningMethod.SUBTRACTIVE) f *= learnRate

            var length: Double = 0.0
            for (j in 0..inputCount - 1) {
                val correction: Double = f * correctionMatrix[i, j]
                selfOrganizingMap.outputWeights.add(i, j, correction)
                length += correction * correction
            }
        }
    }

    private fun copyWeights(source: SelfOrganizingMap, target: SelfOrganizingMap) {
        MatrixMath.copy(source.outputWeights, target.outputWeights)
    }

    fun evaluateErrors() {
        this.correctionMatrix.clear()

        for (i in 0..won.size - 1) {
            won[i] = 0
        }

        globalError = 0.0
        for (tset in 0..train.size - 1) {
            val input: NormalizeInput = NormalizeInput(train[tset], selfOrganizingMap.normalizationType)
            val best: Int = selfOrganizingMap.winner(input)

            won[best]++

            val wptr: Matrix = selfOrganizingMap.outputWeights.getRow(best)

            var length: Double = 0.0
            var diff: Double

            for (i in 0..inputCount - 1) {
                diff = train[tset][i] * input.normalizationFactor - wptr[0, i]
                length += diff * diff

                if (learningMethod == LearningMethod.SUBTRACTIVE) {
                    correctionMatrix.add(best, i, diff)
                } else {
                    workMatrix!!.set(0, i, learnRate * train[tset][i] * input.normalizationFactor + wptr[0, i])
                }
            }

            diff = input.synth - wptr[0, inputCount]
            length += diff * diff

            if (learningMethod == LearningMethod.SUBTRACTIVE) {
                correctionMatrix.add(best, inputCount, diff)
            } else {
                workMatrix!!.set(0, inputCount, learnRate * input.synth + wptr[0, inputCount])
            }

            if (length > globalError) {
                globalError = length
            }

            if (learningMethod == LearningMethod.ADDITIVE) {
                normalizeWeight(workMatrix!!, 0)
                for (i in 0..inputCount) {
                    correctionMatrix.add(best, i, workMatrix!![0, i] - wptr[0, i])
                }
            }

            globalError = Math.sqrt(globalError)
        }
    }

    protected fun forceWin() {
        var best: Int
        var which: Int = 0

        val outputWeights: Matrix = selfOrganizingMap.outputWeights

        // Loop over all training sets.  Find the training set with
        // the least output.
        var dist: Double = Double.MAX_VALUE
        for (tset in 0..this.train.size - 1) {
            best = selfOrganizingMap.winner(this.train[tset])
            val output = selfOrganizingMap.output

            if (output[best] < dist) {
                dist = output[best]
                which = tset
            }
        }

        val input: NormalizeInput = NormalizeInput(this.train[which], selfOrganizingMap.normalizationType)
        best = selfOrganizingMap.winner(input)
        val output: DoubleArray = selfOrganizingMap.output

        dist = Double.MIN_VALUE
        var i: Int = this.outputCount
        while ((i--) > 0) {
            if (this.won[i] !== 0) {
                continue
            }
            if (output[i] > dist) {
                dist = output[i]
                which = i
            }
        }

        for (j in 0..input.inputMatrix.getCols() - 1) {
            outputWeights.set(which, j, input.inputMatrix[0, j])
        }

        normalizeWeight(outputWeights, which)
    }

    fun iteration() {
        evaluateErrors()

        totalError = globalError

        if (totalError < bestError) {
            bestError = totalError
            copyWeights(selfOrganizingMap, bestNetwork)
        }

        var winners: Int = 0
        for (i in 0..won.size - 1) {
            if (won[i] != 0) {
                winners++
            }
        }
        if (winners < outputCount && winners < train.size) {
            forceWin()
            return
        }

        adjustWeights()

        if (learnRate > 0.01) {
            learnRate *= reduction
        }
    }

}