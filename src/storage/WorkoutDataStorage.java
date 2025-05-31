package storage;

import journal.Workout;
import java.io.IOException;
import java.util.List;

public interface WorkoutDataStorage {
    List<Workout> loadWorkouts() throws IOException;
    void saveWorkouts(List<Workout> workouts) throws IOException;
}