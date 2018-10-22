package com.kuehne_nagel.model.scoring;

/**
 * A       B        C
 * 100 m	     25.4347	18	    1.81
 * Long jump	 0.14354	220	    1.4
 * Shot put	     51.39	    1.5	    1.05
 * High jump	 0.8465	    75	    1.42
 * 400 m	     1.53775	82	    1.81
 * 110 m hurdles 5.74352	28.5	1.92
 * Discus throw	 12.91	    4	    1.1
 * Pole vault	 0.2797	    100	    1.35
 * Javelin throw 10.14	    7	    1.08
 * 1500 m	     0.03768	480	    1.85
 */
/*values should ideally be placed in properties
* And if used within Spring, let the container manage singleton instead of having static methods
* */
public class ScoringProfile {

    private double aFactor;
    private double bFactor;
    private double cFactor;

    private ScoringProfile(double aFactor, double bFactor, double cFactor) {
        this.aFactor = aFactor;
        this.bFactor = bFactor;
        this.cFactor = cFactor;
    }

    public static ScoringProfile for100m() {
        return new ScoringProfile(25.4347, 18, 1.81);
    }

    public static ScoringProfile forLongJump() {
        return new ScoringProfile(0.14354, 220, 1.4);
    }

    public static ScoringProfile forShotPut() {
        return new ScoringProfile(51.39, 1.5, 1.05);
    }

    public static ScoringProfile forHighJump() {
        return new ScoringProfile(0.8465, 75, 1.42);
    }

    public static ScoringProfile for400m() {
        return new ScoringProfile(1.53775, 82, 1.81);
    }

    public static ScoringProfile for110mHurdles() {
        return new ScoringProfile(5.74352, 28.5, 1.92);
    }

    public static ScoringProfile forDiscusThrow() {
        return new ScoringProfile(12.91, 4, 1.1);
    }

    public static ScoringProfile forPoleVault() {
        return new ScoringProfile(0.2797, 100, 1.35);
    }

    public static ScoringProfile forJavelinThrow() {
        return new ScoringProfile(10.14, 7, 1.08);
    }

    public static ScoringProfile for1500m() {
        return new ScoringProfile(0.03768, 480, 1.85);
    }

    public double getAFactor() {
        return aFactor;
    }

    public double getBFactor() {
        return bFactor;
    }

    public double getCFactor() {
        return cFactor;
    }
}
