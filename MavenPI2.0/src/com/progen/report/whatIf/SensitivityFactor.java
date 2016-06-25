package com.progen.report.whatIf;

import com.google.common.base.Predicate;

public class SensitivityFactor {

    private String dimValue;
    private double sensitivity;

    public SensitivityFactor(String dimValue, double sensitivity) {
        this.dimValue = dimValue;
        this.sensitivity = sensitivity;
    }

    public String getDimensionValue() {
        return dimValue;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void updateSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public static Predicate<SensitivityFactor> getSensitivityPredicateForValue(final String dimValue) {
        Predicate<SensitivityFactor> predicate = new Predicate<SensitivityFactor>() {

            @Override
            public boolean apply(SensitivityFactor input) {
                if (dimValue.equals(input.getDimensionValue())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public static Predicate<SensitivityFactor> getSensitivityPredicateForSensitivity(final double sensitivity) {
        Predicate<SensitivityFactor> predicate = new Predicate<SensitivityFactor>() {

            @Override
            public boolean apply(SensitivityFactor input) {
                if (sensitivity == input.getSensitivity()) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SensitivityFactor other = (SensitivityFactor) obj;
        if ((this.dimValue == null) ? (other.dimValue != null) : !this.dimValue.equals(other.dimValue)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.dimValue != null ? this.dimValue.hashCode() : 0);
        return hash;
    }
}
