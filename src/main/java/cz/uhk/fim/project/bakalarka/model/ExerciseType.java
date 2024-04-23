package cz.uhk.fim.project.bakalarka.model;
/**
 * This interface predefines methods that are shared for exercises - Run, GymWorkout and Swimming.
 *
 * @author Alex Zamastil
 */
public interface ExerciseType {
    Exercise getExercise();

    String getType();
}
