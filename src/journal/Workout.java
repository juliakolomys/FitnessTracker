package journal;

import java.time.LocalDate;
import java.util.Objects;

public class Workout implements Comparable<Workout> {
    private WorkoutType type;
    private LocalDate date;
    private int durationMinutes;
    private int caloriesBurned;

    public Workout(WorkoutType type, LocalDate date, int durationMinutes, int caloriesBurned) {
        if (durationMinutes < 1) {
            throw new IllegalArgumentException("Duration must be a positive number bigger than 0.");
        }
        if (caloriesBurned < 0) {
            throw new IllegalArgumentException("Calories burned cannot be negative.");
        }
        this.type = type;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
    }

    public WorkoutType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getCaloriesBurned() {
        if (this.type == WorkoutType.OTHER_TYPE) {
            return this.caloriesBurned;
        } else {
            return type.getDefaultCaloriesPerMinute() * durationMinutes;
        }
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes < 1) {
            throw new IllegalArgumentException("Duration must be a positive number bigger than 0.");
        }
        this.durationMinutes = durationMinutes;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        if (caloriesBurned < 1) {
            throw new IllegalArgumentException("Burned calories must be positive number bigger than 0.");
        }
        this.caloriesBurned = caloriesBurned;
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Date: %s, Duration: %d хв, Calories: %d",
                type.getDisplayName(), date, durationMinutes, getCaloriesBurned());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return durationMinutes == workout.durationMinutes &&
                caloriesBurned == workout.caloriesBurned &&
                type == workout.type &&
                Objects.equals(date, workout.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, date, durationMinutes, caloriesBurned);
    }

    @Override
    public int compareTo(Workout other) {
        return this.date.compareTo(other.date);
    }
}