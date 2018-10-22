package com.kuehne_nagel.model;

import com.kuehne_nagel.model.events.EventVisitor;

import java.util.Objects;

import static java.util.Objects.nonNull;

public class Contestant {

    private final String name;
    private final double _100m;
    private final double long_jump_height;
    private final double shot_put;
    private final double high_jump;
    private final double _400m;
    private final double _110m_hurdles;
    private final double discus_throw;
    private final double pole_vault;
    private final double javelin_throw;
    private final double _1500_meter;

    private Contestant(String name, double _100m, double long_jump_height, double shot_put, double high_jump, double _400m, double _110m_hurdles, double discus_throw, double pole_vault, double javelin_throw, double _1500_meter) {
        this.name = name;
        this._100m = _100m;
        this.long_jump_height = long_jump_height;
        this.shot_put = shot_put;
        this.high_jump = high_jump;
        this._400m = _400m;
        this._110m_hurdles = _110m_hurdles;
        this.discus_throw = discus_throw;
        this.pole_vault = pole_vault;
        this.javelin_throw = javelin_throw;
        this._1500_meter = _1500_meter;
    }

    public String getName() {
        return name;
    }

    public double get100m() {
        return _100m;
    }

    public double getLongJumpHeight() {
        return long_jump_height;
    }


    public double getShotPut() {
        return shot_put;
    }

    public double getHighJump() {
        return high_jump;
    }

    public double get400m() {
        return _400m;
    }

    public double get110mHurdles() {
        return _110m_hurdles;
    }

    public double getDiscusThrow() {
        return discus_throw;
    }


    public double getPoleVault() {
        return pole_vault;
    }

    public double getJavelinThrow() {
        return javelin_throw;
    }

    public double get1500m() {
        return _1500_meter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contestant that = (Contestant) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }


    public static class ContestantBuilder {
        private String name;
        private double _100m;
        private double _400m;
        private double long_jump_height;
        private double shot_put;
        private double high_jump;
        private double _110m_hurdles;
        private double discus_throw;
        private double pole_vault;
        private double javelin_throw;
        private long _1500m;
        private NonNegativePerformanceMetricConstraint constraint;

        public ContestantBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ContestantBuilder with100mPerformance(double seconds) {
            this._100m = seconds;
            return this;
        }

        public ContestantBuilder withLongJumpPerformance(double meters) {
            this.long_jump_height = meters;
            return this;
        }

        public ContestantBuilder withShotPutPerforamnce(double meters) {
            this.shot_put = meters;
            return this;
        }

        public ContestantBuilder withHighJumpPerformance(double meters) {
            this.high_jump = meters;
            return this;
        }

        public ContestantBuilder with400mPerformance(double seconds) {
            this._400m = seconds;
            return this;
        }

        public ContestantBuilder with110mHurdlesPerformance(double seconds) {
            this._110m_hurdles = seconds;
            return this;
        }

        public ContestantBuilder withDiscusThrowPerformance(double performance) {
            this.discus_throw = performance;
            return this;
        }

        public ContestantBuilder withPoleVaultPerformance(double meters) {
            this.pole_vault = meters;
            return this;
        }

        public ContestantBuilder withJavelinThrowPerformance(double performance) {
            this.javelin_throw = performance;
            return this;
        }

        public ContestantBuilder with1500mPerformance(long performanceInMillis) {
            this._1500m = performanceInMillis;
            return this;
        }

        public ContestantBuilder withConstraint(NonNegativePerformanceMetricConstraint constraint) {
            this.constraint = constraint;
            return this;
        }

        public Contestant build() {
            if (nonNull(constraint)) {
                if (!constraint.allNoneNegative(_100m, long_jump_height, shot_put, high_jump, _400m, _110m_hurdles, discus_throw, pole_vault, javelin_throw, _1500m)) {
                    throw new IllegalArgumentException("No Perforamce figure can be negative");
                }
            }
            return new Contestant(name, _100m, long_jump_height*100, shot_put, high_jump*100, _400m, _110m_hurdles, discus_throw, pole_vault*100, javelin_throw, _1500m/1000);
        }
    }

}
