import controller.FitnessJournalController;
import journal.WorkoutJournal;
import storage.CSVWorkoutDataStorage;
import storage.WorkoutDataStorage;
import display.ConsoleView;

import java.io.File;
import java.nio.file.Paths;

public class Dispatcher {
    public static void main(String[] args) {
        String currentDir = Paths.get("").toAbsolutePath().toString();

        String dataFilePath = currentDir + File.separator + "workouts.csv";
        System.out.println("Шлях до файлу даних: " + dataFilePath);

        WorkoutDataStorage dataStorage = new CSVWorkoutDataStorage(dataFilePath);
        WorkoutJournal workoutJournal = new WorkoutJournal(dataStorage);
        ConsoleView consoleView = new ConsoleView();
        FitnessJournalController controller = new FitnessJournalController(workoutJournal, consoleView);
        controller.run();
        controller.run();
    }
}