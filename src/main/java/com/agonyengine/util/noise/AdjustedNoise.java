package com.agonyengine.util.noise;

class AdjustedNoise implements NoiseGenerator {
    private static final double NOISE_MULT_3D = 1.154;  // ~1/0.866 because -0.866 to 0.866 is the actual
                                                        // effective range of this noise. This multiplier
                                                        // will scale it to approximately -1 to 1.

    private final OpenSimplexNoise noise;

    public AdjustedNoise(long seed) {
        noise = new OpenSimplexNoise(seed);
    }

    /**
     * Use the raw noise as a base, but adjust it to a range of approximately 0 to 1.0.
     *
     * @param x The position along the X axis to sample noise from.
     * @param y The position along the Y axis to sample noise from.
     * @return The adjusted noise value.
     */
    @Override
    public double eval(double x, double y) {
        return ((NOISE_MULT_3D * noise.eval(x, y)) / 2) + 0.5;
    }
}
