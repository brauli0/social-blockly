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
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.ProgramDao;
import es.udc.paproject.backend.model.entities.ProgramDaoImpl;
import es.udc.paproject.backend.model.entities.Shared;
import es.udc.paproject.backend.model.entities.SharedDao;
import es.udc.paproject.backend.model.entities.SharedId;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;;

@Service
@Transactional
public class ProgramServiceImpl implements ProgramService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private ProgramDao programDao;

	@Autowired
	private ProgramDaoImpl programDaoImpl;

	@Autowired
	private SharedDao sharedDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ExerciseDao exerciseDao;

	@Override
	public Program createProgram(Long userId, Long exerciseId, String programName, String programDesc,
			String code, Boolean privateProgram) throws InstanceNotFoundException {
		// Check that user exists
		permissionChecker.checkUserExists(userId);
		Optional<User> opUser = userDao.findById(userId);

		Exercise exercise = null;
		if (exerciseId != null) {
			Optional<Exercise> opExercise = exerciseDao.findById(exerciseId);
			if (!opExercise.isPresent()) {
				throw new InstanceNotFoundException("project.entities.exercise", exerciseId);
			}
			exercise = opExercise.get();
		}

		Program program = new Program(opUser.get(), exercise, programName, programDesc,
				code, privateProgram);

		return programDao.save(program);
	}

	@Override
	public void deleteProgram(Long id) throws InstanceNotFoundException {
		Optional<Program> program = programDao.findById(id);
		if (program.isPresent())
			programDao.delete(program.get());
		else
			throw new InstanceNotFoundException("project.entities.program", id);
	}

	@Override
	@Transactional(readOnly=true)
	public Program getProgram(Long id) throws InstanceNotFoundException {
		Optional<Program> program = programDao.findById(id);

		if (!program.isPresent())
			throw new InstanceNotFoundException("project.entities.program", id);

		return program.get();
	}

	@Override
	public Program updateProgram(Long id, String programName, String programDesc, String code)
			throws InstanceNotFoundException {
		Optional<Program> optionalProgram = programDao.findById(id);

		if (!optionalProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", id);
		}

		Program program = optionalProgram.get();

		program.setProgramName(programName);
		program.setProgramDesc(programDesc);
		program.setCode(code);
		program.setUpdateDate(Calendar.getInstance());

		return program;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Program> getProgramsByUser(Long userId) throws InstanceNotFoundException {
		// Check that user exists
		permissionChecker.checkUserExists(userId);

		return programDaoImpl.findByUserId(userId);
	}

	@Override
	public Program setPublic(Long id) throws InstanceNotFoundException {
		Optional<Program> optionalProgram = programDao.findById(id);
		if (!optionalProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", id);
		}
		Program program = optionalProgram.get();

		// Delete all shared users
		programDaoImpl.deleteSharedUsers(id);

		program.setPrivateProgram(false);

		return program;
	}

	@Override
	public Program setPrivate(Long id) throws InstanceNotFoundException {
		Optional<Program> optionalProgram = programDao.findById(id);
		if (!optionalProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", id);
		}
		Program program = optionalProgram.get();

		// Delete all shared users
		programDaoImpl.deleteSharedUsers(id);

		program.setPrivateProgram(true);

		return program;
	}

	@Override
	public List<Program> getPublicProgramsByUser(Long userId) throws InstanceNotFoundException {
		// Check that user exists
		permissionChecker.checkUserExists(userId);

		return programDaoImpl.findPublicByUserId(userId);
	}

	@Override
	public void shareProgram(Long userId, Long programId) throws InstanceNotFoundException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		User user = opUser.get();

		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}
		Program program = opProgram.get();

		Shared shared = new Shared(user, program);
		sharedDao.save(shared);
	}

	@Override
	public void unshareProgram(Long userId, Long programId) throws InstanceNotFoundException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}

		SharedId sharedId = new SharedId(userId, programId);
		Optional<Shared> opShared = sharedDao.findById(sharedId);
		if (!opShared.isPresent()) {
			throw new InstanceNotFoundException("project.entities.shared", sharedId);
		}
		Shared shared = opShared.get();

		sharedDao.delete(shared);
	}

	@Override
	public List<Program> getSharedProgramsWithMe(Long userId) throws InstanceNotFoundException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		return programDaoImpl.findSharedPrograms(userId);
	}

	@Override
	public List<User> getSharedUsersByProgram(Long programId) throws InstanceNotFoundException {

		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}

		return programDaoImpl.findSharedUsers(programId);
	}

	@Override
	public List<Program> getSharedProgramsWithMeByUser(Long myUserId, Long otherUserId)
			throws InstanceNotFoundException {

		Optional<User> opUser = userDao.findById(myUserId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", myUserId);
		}

		opUser = userDao.findById(otherUserId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", otherUserId);
		}

		return programDaoImpl.findProgramsSharedWithMeByUser(myUserId, otherUserId);
	}

	@Override
	public List<Program> getProgramsByKeyword(String keyword) {
		return programDaoImpl.findByKeyword(keyword);
	}

	@Override
	public List<Program> getProgramsByExercise(Long exerciseId) throws InstanceNotFoundException {
		Optional<Exercise> opExercise = exerciseDao.findById(exerciseId);
		if (!opExercise.isPresent()) {
			throw new InstanceNotFoundException("project.entities.exercise", exerciseId);
		}

		return programDaoImpl.findByExercise(exerciseId);
	}
}
