package es.udc.paproject.backend.model.services;

import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;

public interface ProgramService {
	Program createProgram(Long userId, Long exerciseId, String programName, String programDesc,
			String code, Boolean privateProgram) throws InstanceNotFoundException;

	void deleteProgram(Long id) throws InstanceNotFoundException;

	Program getProgram(Long id) throws InstanceNotFoundException;

	Program updateProgram(Long id, String programName, String programDesc, String code)
			throws InstanceNotFoundException;

	List<Program> getProgramsByUser(Long userId) throws InstanceNotFoundException;

	Program setPublic(Long id) throws InstanceNotFoundException;

	Program setPrivate(Long id) throws InstanceNotFoundException;

	List<Program> getPublicProgramsByUser(Long userId) throws InstanceNotFoundException;

	void shareProgram(Long userId, Long programId) throws InstanceNotFoundException;

	void unshareProgram(Long userId, Long programId) throws InstanceNotFoundException;

	List<Program> getSharedProgramsWithMe(Long userId) throws InstanceNotFoundException;

	List<User> getSharedUsersByProgram(Long programId) throws InstanceNotFoundException;

	List<Program> getSharedProgramsWithMeByUser(Long myUserId, Long otherUserId)
			throws InstanceNotFoundException;

	List<Program> getProgramsByKeyword(String keyword);

	List<Program> getProgramsByExercise(Long exerciseId) throws InstanceNotFoundException;
}
