package edu.psu.sweng888.activesync.dataAccessLayer.models;

/**
 * Represents a measurement of weight in a particular unit.
 */
public class Weight {

    /**
     * The amount of weight that this instance represents.
     */
    public double amount;

    /**
     * The unit in which this instance's amount is measured.
     */
    public WeightUnit unit;

    /**
     * Returns a human-readable string representation of this instance.
     */
    public String toString() {
        return weightUnitAsSuffix(this.unit, this.amount) + " " + this.amount;
    }

    /**
     * Returns the string suffix of the given weight unit when describing the given amount
     * of weight.
     * @param unit The unit for which a suffix should be returned.
     * @param amount The amount of weight being described by the given unit.
     * @return A suffix that can be appended to the amount to render a human-readable measurement.
     */
    private static String weightUnitAsSuffix(WeightUnit unit, double amount) {
        switch (unit) {
            case Kilograms:
                return "kg";
            case Pounds:
                if (amount == 1) return "lb";
                return "lbs";
            default:
                throw new IllegalArgumentException(
                    "Unrecognized or unsupported weight unit \"" + unit + "\"."
                );
        }
    }
}
