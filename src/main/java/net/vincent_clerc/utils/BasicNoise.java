package net.vincent_clerc.utils;

import java.util.Random;

public class BasicNoise {

    private int[][] tableau = new int[25][25];

    private Float sensitivity;
    private Random rand;

    public BasicNoise(Integer seed, Float sensitivity) {

        this.rand = new Random(seed);
        this.sensitivity = sensitivity;

        this.build();

    }

    private void build() {

        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                tableau[i][j] = rand.nextInt(256); // Valeurs entre 0 et 255
            }
        }
    }

    public Integer noise(Integer x, Integer y) {
        return this.tableau[x][y];
    }

    public Float reducedNoise(Integer x, Integer y) {
        return this.tableau[x][y] * this.sensitivity;
    }

}

