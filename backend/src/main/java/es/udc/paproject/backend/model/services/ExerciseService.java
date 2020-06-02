package es.udc.paproject.backend.model.services;

import java.util.Calendar;
import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Exercise;

public interface ExerciseService {

	Exercise createExercise(String statementText, Long groupId, Long userId, Calendar expirationDate,
			String blocks) throws InstanceNotFoundException;

	void deleteExercise(Long exerciseId) throws InstanceNotFoundException;

	Exercise getExercise(Long exerciseId) throws InstanceNotFoundException;

	Exercise updateExercise(Long exerciseId, String statementText, Calendar expirationDate)
			throws InstanceNotFoundException;

	List<Exercise> getExercisesByGroup(Long groupId) throws InstanceNotFoundException;
}
