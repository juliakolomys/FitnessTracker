package journal;

public enum WorkoutType {
    RUNNING("Running", 11),
    WEIGHTLIFTING("Weightlifting", 5),
    YOGA("Yoga", 3),
    CARDIO("Cardio", 12),
    SWIMMING("Swimming", 13),
    CYCLING("Cycling", 8),
    HIKING("Hiking", 7),
    OTHER_TYPE("Other", 0);

    private final String displayName;
    private final int defaultCaloriesPerMinute;

    WorkoutType(String displayName, int defaultCaloriesPerMinute) {
        this.displayName = displayName;
        this.defaultCaloriesPerMinute = defaultCaloriesPerMinute;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultCaloriesPerMinute() {
        return defaultCaloriesPerMinute;
    }

    public static WorkoutType fromString(String text) {
        for (WorkoutType type : WorkoutType.values()) {
            if (type.displayName.equalsIgnoreCase(text) || type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown workout type: " + text);
    }
}

