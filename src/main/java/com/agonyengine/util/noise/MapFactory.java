package com.agonyengine.util.noise;

import org.springframework.stereotype.Component;

@Component
public class MapFactory {
    private NoiseGenerator maxNoise = new MaxNoise();

    public double generateCell(final FbmParameters parameters, long x, long y) {
        final NoiseGenerator noiseGenerator = new AdjustedNoise(parameters.getSeed());
        final double maxValue = fbmNoise(parameters, 0, 0, maxNoise); // the max value of the FBM noise

        return rescale(
            fbmNoise(
                parameters,
                x - parameters.getWidth(),
                y - parameters.getHeight(),
                noiseGenerator
            ),
            0,
            maxValue,
            0,
            1.0);
    }

    private double fbmNoise(FbmParameters parameters, double x, double y, NoiseGenerator noise) {
        double total = 0.0;
        double frequency = parameters.getBaseFrequency();
        double amplitude = parameters.getBaseAmplitude();

        for (int i = 0; i < parameters.getOctaves(); i++) {
            total += amplitude * noise.eval(x * frequency, y * frequency);

            frequency *= parameters.getLacunarity();
            amplitude *= parameters.getPersistence();
        }

        return total;
    }

    private double rescale(final double value, final double min, final double max, final double scaledMin, final double scaledMax) {
        return ((value - min) / (max - min)) * (scaledMax - scaledMin) + scaledMin;
    }
}
