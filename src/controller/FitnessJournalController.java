package controller;

import journal.Workout;
import journal.WorkoutJournal;
import journal.WorkoutType;
import display.ConsoleView;

import java.time.LocalDate;
import java.util.List;

public class FitnessJournalController {
    private final WorkoutJournal workoutJournal;
    private final ConsoleView consoleView;
    private boolean running;

    public FitnessJournalController(WorkoutJournal workoutJournal, ConsoleView consoleView) {
        this.workoutJournal = workoutJournal;
        this.consoleView = consoleView;
        this.running = false;
    }

    public void run() {
        running = true;
        while (running) {
            consoleView.displayMenu();
            int choice = consoleView.getMenuChoice();
            switch (choice) {
                case 1:
                    addWorkout();
                    break;
                case 2:
                    viewWorkouts();
                    break;
                case 3:
                    deleteWorkout();
                    break;
                case 4:
                    calculateWeeklyCalories();
                    break;
                case 5:
                    calculateMonthlyCalories();
                    break;
                case 6:
                    sortWorkouts();
                    break;
                case 7:
                    handleExit();
                    break;
                default:
                    consoleView.displayMessage("Invalid input. Please, try again");
                    break;
            }
        }
    }

    private void addWorkout() {
        try {
            Workout newWorkout = consoleView.getWorkoutData();
            workoutJournal.addWorkout(newWorkout);
            consoleView.displayMessage("Workout was added");
        } catch (IllegalArgumentException e) {
            consoleView.displayError("Failed to add workout: " + e.getMessage());
        }
    }

    private void viewWorkouts() {
        List<Workout> workouts = workoutJournal.getAllWorkouts();
        consoleView.displayWorkouts(workouts);
    }

    private void deleteWorkout() {
        List<Workout> allWorkouts = workoutJournal.getAllWorkouts();
        int numberOfWorkouts = consoleView.displayWorkouts(allWorkouts);
        if (numberOfWorkouts == 0) {
            return;
        }
        int criteriaChoice = consoleView.getDeleteCriteriaChoice();
        boolean deleted = false;
        String criteriaInfo = "";

        switch (criteriaChoice) {
            case 1:
                int index = consoleView.getWorkoutIndexToDelete(numberOfWorkouts);
                if (workoutJournal.deleteWorkout(index)) {
                    deleted = true;
                    criteriaInfo = "By index" + (index + 1);
                }
                break;
            case 2: // За датою
                LocalDate date = consoleView.getDateToDelete();
                int deletedCountDate = workoutJournal.deleteWorkoutsByDate(date);
                if (deletedCountDate > 0) {
                    deleted = true;
                    criteriaInfo = "By date (" + date + "), deleted workouts: " + deletedCountDate;
                }
                break;
            case 3: // За типом
                WorkoutType type = consoleView.getTypeToDelete();
                int deletedCountType = workoutJournal.deleteWorkoutsByType(type);
                if (deletedCountType > 0) {
                    deleted = true;
                    criteriaInfo = "By type (" + type.getDisplayName() + "), deleted workouts: " + deletedCountType;
                }
                break;
        }

        if (deleted) {
            consoleView.displayMessage("Workout successfully deleted " + criteriaInfo + ".");
        } else {
            consoleView.displayMessage("Failed to delete workout. Please check your input");
        }
    }


    private void calculateWeeklyCalories() {
        LocalDate startDateOfWeek = consoleView.getStartDateOfWeek();
        int totalCalories = workoutJournal.calculateWeeklyCalories(startDateOfWeek);
        consoleView.displayTotalCalories(totalCalories, "week at" + startDateOfWeek);
    }

    private void calculateMonthlyCalories() {
        int[] yearMonth = consoleView.getYearAndMonth();
        int year = yearMonth[0];
        int month = yearMonth[1];
        int totalCalories = workoutJournal.calculateMonthlyCalories(year, month);
        consoleView.displayTotalCalories(totalCalories, "month (" + month + "." + year + ")");
    }

    private void sortWorkouts() {
        if (workoutJournal.getAllWorkouts().isEmpty()) {
            consoleView.displayMessage("No workouts to sort");
            return;
        }

        int sortChoice = consoleView.getSortChoice();
        switch (sortChoice) {
            case 1:
                workoutJournal.sortWorkoutsByDate();
                consoleView.displayMessage("Workouts are sorted by date");
                break;
            case 2:
                workoutJournal.sortWorkoutsByType();
                consoleView.displayMessage("Workouts are sorted by type");
                break;
            default:
                // Не відбудеться через перевірки у ConsoleView
                break;
        }
        viewWorkouts();
    }

    private void handleExit() {
        consoleView.displayMessage("Saving data and ending program...");
        workoutJournal.saveWorkouts();
        running = false;
        consoleView.closeScanner();
    }
}
