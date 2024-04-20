package edu.psu.sweng888.activesync.dataAccessLayer.models;

import java.io.Serializable;

/**
 * Represents a unit for measuring weight.
 */
public enum WeightUnit implements Serializable {
    /**
     * Weights that use this unit are measured in imperial pounds (lbs).
     */
    Pounds,

    /**
     * Weights that use this unit are measured in metric kilograms (kg).
     */
    Kilograms
}
