package journal;

import storage.WorkoutDataStorage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorkoutJournal {
    private List<Workout> workouts;
    private final WorkoutDataStorage dataStorage;

    public WorkoutJournal(WorkoutDataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.workouts = new ArrayList<>();
        loadWorkouts();
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
        saveWorkouts();
    }

    public List<Workout> getAllWorkouts() {
        return new ArrayList<>(workouts);
    }

    public boolean deleteWorkout(int index) {
        if (index > -1 && index < workouts.size()) {
            workouts.remove(index);
            saveWorkouts();
            return true;
        }
        return false;
    }

    public int deleteWorkoutsByDate(LocalDate date) {
        int initialSize = workouts.size();
        workouts.removeIf(w -> w.getDate().equals(date));
        if (workouts.size() < initialSize) {
            saveWorkouts();
        }
        return initialSize - workouts.size(); // Кількість видалених
    }

    public int deleteWorkoutsByType(WorkoutType type) {
        int initialSize = workouts.size();
        workouts.removeIf(w -> w.getType().equals(type));
        if (workouts.size() < initialSize) {
            saveWorkouts();
        }
        return initialSize - workouts.size();
    }

    public int calculateWeeklyCalories(LocalDate startDateOfWeek) { // якщо вважати понеділок першим днем тижня
        LocalDate actualStartOfWeek = startDateOfWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = actualStartOfWeek.plusDays(6);
        int totalCalories = 0;

        for (Workout w : workouts) {
            LocalDate workoutDate = w.getDate();
            if (!workoutDate.isBefore(actualStartOfWeek) && !workoutDate.isAfter(endOfWeek)) { // дата тренування не раніше понеділка, не пізніше неділі
                totalCalories += w.getCaloriesBurned();
            }
        }
        return totalCalories;
    }

    public int calculateMonthlyCalories(int year, int month) {
        int totalCalories = 0;
        for (Workout w : workouts) {
            LocalDate workoutDate = w.getDate();
            if (workoutDate.getYear() == year && workoutDate.getMonthValue() == month) {
                totalCalories += w.getCaloriesBurned();
            }
        }
        return totalCalories;
    }

    public void sortWorkoutsByDate() {
        workouts.sort(Comparator.comparing(Workout::getDate));
    }

    public void sortWorkoutsByType() {
        workouts.sort(Comparator.comparing(w -> w.getType().getDisplayName()));
    }

    private void loadWorkouts() {
        try {
            this.workouts = dataStorage.loadWorkouts();
        } catch (IOException e) {
            System.err.println("Error loading workouts: " + e.getMessage());
            this.workouts = new ArrayList<>();
        }
    }

    public void saveWorkouts() {
        try {
            dataStorage.saveWorkouts(workouts);
        } catch (IOException e) {
            System.err.println("Error saving workouts: " + e.getMessage());
        }
    }
}