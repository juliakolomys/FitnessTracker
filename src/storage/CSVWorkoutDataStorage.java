package storage;

import journal.Workout;
import journal.WorkoutType;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVWorkoutDataStorage implements WorkoutDataStorage {
    private final String filePath;
    private static final String CSV_HEADER = "type,date,durationMinutes,caloriesBurned";

    public CSVWorkoutDataStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Workout> loadWorkouts() throws IOException {
        List<Workout> workouts = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(CSV_HEADER);
                writer.newLine();
            }
            return workouts;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    if (!line.equals(CSV_HEADER)) {
                        System.err.println("The file header does not match the expected one. Expected: " + CSV_HEADER + ", Found one: " + line);
                    }
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        WorkoutType type = WorkoutType.valueOf(parts[0].trim().toUpperCase());
                        LocalDate date = LocalDate.parse(parts[1].trim());
                        int durationMinutes = Integer.parseInt(parts[2].trim());
                        int caloriesBurned = Integer.parseInt(parts[3].trim());
                        workouts.add(new Workout(type, date, durationMinutes, caloriesBurned));
                    } else {
                        System.err.println("Error reading CSV string: Incorrect number of fields: " + line);
                    }
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    System.err.println("Error parsing CSV string: " + line + " - " + e.getMessage());
                }
            }
        }
        return workouts;
    }

    @Override
    public void saveWorkouts(List<Workout> workouts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Workout workout : workouts) {
                writer.write(String.format("%s,%s,%d,%d",
                        workout.getType().name(),
                        workout.getDate().toString(),
                        workout.getDurationMinutes(),
                        workout.getCaloriesBurned()));
                writer.newLine();
            }
        }
    }
}