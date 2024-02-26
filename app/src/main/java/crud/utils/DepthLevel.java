package crud.utils;

/**
 * Enumeration representing different levels of depth for fetching entities.
 * The depth levels are categorized as SHALLOW, MEDIUM, and DEEP.
 */
public enum DepthLevel {
    SHALLOW("shallow"),
    MEDIUM("medium"),
    DEEP("deep");

    private final String value;

    /**
     * Constructor for DepthLevel enum.
     *
     * @param value The string representation of the depth level.
     */
    DepthLevel(String value) {
        this.value = value;
    }

    /**
     * Gets the string representation of the depth level.
     *
     * @return The string representation of the depth level.
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts a string value to the corresponding DepthLevel enum.
     *
     * @param value The string representation of the depth level.
     * @return The DepthLevel enum corresponding to the given string value.
     * @throws IllegalArgumentException if the provided value does not match any
     *                                  known depth level.
     */
    public static DepthLevel fromString(String value) {
        for (DepthLevel depthLevel : DepthLevel.values()) {
            if (depthLevel.value.equalsIgnoreCase(value)) {
                return depthLevel;
            }
        }
        throw new IllegalArgumentException("Unknown depth level: " + value);
    }

    /**
     * Checks if the enum value is equal to the provided string value, ignoring
     * case.
     *
     * @param value The string value to compare against.
     * @return true if the string value matches the enum value (ignoring case),
     *         false otherwise.
     */
    public boolean equalsString(String value) {
        return this.value.equalsIgnoreCase(value);
    }
}
