package net.d4.aiir

/**
 * Created by d4rkfly3r (Joshua F.) on 2/7/16.
 */
public class SelfOrganizingMap(val inputCount: Int, val outputCount: Int, val normalizationType: NormalizeInput.NormalizationType) {
    companion object {
        public val VERYSMALL: Double = Math.pow(1.0, -30.0);
    }


}