package display;

import journal.Workout;
import journal.WorkoutType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.lang.StringBuilder;

public class ConsoleView {
    private Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\n----- Menu -----");
        System.out.println("1. Add workout");
        System.out.println("2. View all workouts");
        System.out.println("3. Delete workout");
        System.out.println("4. Calculate total calories for the week");
        System.out.println("5. Calculate total calories for the month");
        System.out.println("6. Sort workouts");
        System.out.println("7. Exit");
        System.out.print("Enter a number of your option");
    }

    public int getMenuChoice() {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.nextLine();
                System.out.print("Choose option: ");
            }
        }
    }

    public Workout getWorkoutData() {
        System.out.println("\n--- Add training ---");

        WorkoutType type = null;
        LocalDate date = null;
        int durationMinutes = -1;
        int caloriesBurned = 1;
        String typeStr;
        String dateStr;

        StringBuilder sb = new StringBuilder();
        WorkoutType[] allWorkoutTypes = WorkoutType.values();

        for (int i = 0; i < allWorkoutTypes.length; i++) {
            sb.append(allWorkoutTypes[i].getDisplayName());
            if (i < allWorkoutTypes.length - 1) {
                sb.append(", ");
            }
        }
        String availableWorkoutTypes = sb.toString();

        //тип тренування
        while (type == null) {
            System.out.println("Available training types: " + availableWorkoutTypes);
            System.out.print("Enter the type of training: ");
            typeStr = scanner.nextLine().trim();
            try {
                type = WorkoutType.fromString(typeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid workout type. Please try again.");
            }
        }

        // дата тренування
        while (date == null) {
            System.out.print("Enter the date of the training (РРРР-ММ-ДД): ");
            dateStr = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD");
            }
        }

        while (durationMinutes < 0) {
            System.out.print("Enter the duration of the workout in minutes: ");
            try {
                durationMinutes = scanner.nextInt();
                if (durationMinutes < 1) {
                    System.out.println("Duration must be a positive number bigger than 0");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number");
            } finally {
                scanner.nextLine();
            }
        }

        // кількість спалених калорій
        if (type == WorkoutType.OTHER_TYPE) {
            while (caloriesBurned < 1) {
                System.out.print("Since it's an 'Other' option, please enter the number of burned calories in a minute: ");
                try {
                    caloriesBurned = scanner.nextInt();
                    if (caloriesBurned < 1) {
                        System.out.println("The number of calories must be a positive number bigger than 0");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a whole number");
                } finally {
                    scanner.nextLine();
                }
            }
        }

        return new Workout(type, date, durationMinutes, caloriesBurned);
    }

    public int displayWorkouts(List<Workout> workouts) {
        System.out.println("\n----- Your workouts -----");
        if (workouts.isEmpty()) {
            System.out.println("You have no added workouts");
            return 0;
        } else {
            for (int i = 0; i < workouts.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, workouts.get(i).toString()); // Нумерація з 1 для користувача
            }
            return workouts.size();
        }
    }


    public int getDeleteCriteriaChoice() {
        System.out.println("\n--- Delete workout ---");
        System.out.println("1. By index");
        System.out.println("2. By date");
        System.out.println("3. By type");
        System.out.print("Choose option: ");
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice > 0 && choice < 4) {
                    return choice;
                } else {
                    System.out.println("Invalid input. Please enter 1, 2 or 3");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.nextLine();
            }
        }
    }

    public int getWorkoutIndexToDelete(int maxIndex) {
        System.out.print("Enter the number of the training to delete: ");
        while (true) {
            try {
                int index = scanner.nextInt();
                scanner.nextLine();
                if (index > 0 && index < maxIndex + 1) {
                    return index - 1;
                } else {
                    System.out.println("Invalid number. Please enter a number between 1 and " + maxIndex + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number");
                scanner.nextLine();
            }
        }
    }

    public LocalDate getDateToDelete() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter the date of the training (РРРР-ММ-ДД): ");
            String dateStr = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD");
            }
        }
        return date;
    }

    public WorkoutType getTypeToDelete() {
        WorkoutType type = null;
        String typeStr;

        StringBuilder sb = new StringBuilder();
        WorkoutType[] allWorkoutTypes = WorkoutType.values();

        for (int i = 0; i < allWorkoutTypes.length; i++) {
            sb.append(allWorkoutTypes[i].getDisplayName());
            if (i < allWorkoutTypes.length - 1) {
                sb.append(", ");
            }
        }
        String availableWorkoutTypes = sb.toString();

        while (type == null) {
            System.out.println("Available training types: " + availableWorkoutTypes);
            System.out.print("Enter the type of workout to delete: ");
            typeStr = scanner.nextLine().trim();
            try {
                type = WorkoutType.fromString(typeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong workout type. Please try again.");
            }
        }
        return type;
    }

    public LocalDate getStartDateOfWeek() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter the start date of the week (YYYY-MM-DD) to calculate calories: ");
            String dateStr = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD");
            }
        }
        return date;
    }

    public int[] getYearAndMonth() {
        int year = -1;
        int month = -1;

        while (year < 1) {
            System.out.print("Enter the year to calculate calories: ");
            try {
                year = scanner.nextInt();
                if (year < 0) {
                    System.out.println("Year must be a positive number");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            } finally {
                scanner.nextLine();
            }
        }

        while (month < 1 || month > 12) {
            System.out.print("Enter the month (1-12) to calculate calories: ");
            try {
                month = scanner.nextInt();
                if (month < 1 || month > 12) {
                    System.out.println("Month must be in the range 1 to 12");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            } finally {
                scanner.nextLine();
            }
        }
        return new int[]{year, month};
    }

    public void displayTotalCalories(int calories, String period) {
        System.out.printf("Amount of calories burned during %s: %d%n", period, calories);
    }

    public int getSortChoice() {
        System.out.println("\n--- Sort workouts ---");
        System.out.println("1. By date");
        System.out.println("2. By type");
        System.out.print("Choose sorting otion: ");
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice > 0 && choice < 3) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.nextLine();
            }
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String errorMessage) {
        System.err.println("Error: " + errorMessage);
    }

    public void closeScanner() {
        scanner.close();
    }
}
