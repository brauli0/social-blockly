package es.udc.paproject.backend.model.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Exercise;
import es.udc.paproject.backend.model.entities.ExerciseDao;
import es.udc.paproject.backend.model.entities.ExerciseDaoImpl;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.entities.UsersGroupDao;

@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

	@Autowired
	private ExerciseDao exerciseDao;

	@Autowired
	private ExerciseDaoImpl exerciseDaoImpl;

	@Autowired
	private UsersGroupDao usersGroupDao;

	@Autowired
	private UserDao userDao;

	@Override
	public Exercise createExercise(String statementText, Long groupId, Long userId,
			Calendar expirationDate, String blocks) throws InstanceNotFoundException {

		Optional<UsersGroup> opGroup = usersGroupDao.findById(groupId);
		if (!opGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		Exercise exercise = new Exercise(statementText, opGroup.get(), opUser.get(), expirationDate, blocks);

		return exerciseDao.save(exercise);
	}

	@Override
	public void deleteExercise(Long exerciseId) throws InstanceNotFoundException {
		Optional<Exercise> opExercise = exerciseDao.findById(exerciseId);
		if (!opExercise.isPresent()) {
			throw new InstanceNotFoundException("project.entities.exercise", exerciseId);
		}

		exerciseDao.delete(opExercise.get());
	}

	@Override
	public Exercise getExercise(Long exerciseId) throws InstanceNotFoundException {
		Optional<Exercise> opExercise = exerciseDao.findById(exerciseId);
		if (!opExercise.isPresent()) {
			throw new InstanceNotFoundException("project.entities.exercise", exerciseId);
		}

		return opExercise.get();
	}

	@Override
	public Exercise updateExercise(Long exerciseId, String statementText, Calendar expirationDate)
			throws InstanceNotFoundException {

		Optional<Exercise> opExercise = exerciseDao.findById(exerciseId);
		if (!opExercise.isPresent()) {
			throw new InstanceNotFoundException("project.entities.exercise", exerciseId);
		}

		Exercise exercise =  opExercise.get();

		exercise.setStatementText(statementText);
		exercise.setExpirationDate(expirationDate);

		return exercise;
	}

	@Override
	public List<Exercise> getExercisesByGroup(Long groupId) throws InstanceNotFoundException {
		Optional<UsersGroup> opGroup = usersGroupDao.findById(groupId);
		if (!opGroup.isPresent()) {
			throw new InstanceNotFoundException("project.entities.usersGroup", groupId);
		}

		return exerciseDaoImpl.getExercisesByGroup(groupId);
	}

}
