package es.udc.paproject.backend.rest.dtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.Exercise;

public class ExerciseConversor {

	private ExerciseConversor() {}

	public final static ExerciseDto toExerciseDto(Exercise exercise) {
		return new ExerciseDto(exercise.getId(), exercise.getStatementText(),
				exercise.getGroup().getId(), exercise.getUser().getId(),
				exercise.getCreationDate(), exercise.getExpirationDate(), exercise.getBlocks());
	}

	public final static List<ExerciseDto> toExerciseDtos(List<Exercise> exercises) {
		List<ExerciseDto> exerciseDtos = new ArrayList<>();

		for (Exercise exercise : exercises) {
			exerciseDtos.add(new ExerciseDto(exercise.getId(), exercise.getStatementText(),
					exercise.getGroup().getId(), exercise.getUser().getId(),
					exercise.getCreationDate(), exercise.getExpirationDate(), exercise.getBlocks()));
		}

		return exerciseDtos;
	}
}
