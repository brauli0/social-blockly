package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.ProgramConversor.toFullProgramDto;
import static es.udc.paproject.backend.rest.dtos.ProgramConversor.toProgramDto;
import static es.udc.paproject.backend.rest.dtos.ProgramConversor.toProgramDtos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Exercise;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.ExerciseService;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.ProgramService;
import es.udc.paproject.backend.rest.dtos.FullProgramDto;
import es.udc.paproject.backend.rest.dtos.ProgramDto;
import es.udc.paproject.backend.rest.dtos.ProgramIdDto;
import es.udc.paproject.backend.rest.dtos.ProgramParamsDto;
import es.udc.paproject.backend.rest.dtos.ProgramParamsDto2;
import es.udc.paproject.backend.rest.dtos.ProgramParamsDto3;

@RestController
@RequestMapping("/programs")
public class ProgramController {

	@Autowired
	private ProgramService programService;

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	private GroupService groupService;

	@PostMapping("/create")
	public ProgramDto createProgram(@RequestAttribute Long userId,
			@RequestBody ProgramParamsDto programParamsDto)
			throws InstanceNotFoundException, PermissionException {

		if (!programParamsDto.getUserId().equals(userId)) {
			throw new PermissionException();
		}

		return toProgramDto(programService.createProgram(programParamsDto.getUserId(),
				programParamsDto.getExerciseId(), programParamsDto.getProgramName(),
				programParamsDto.getProgramDesc(), programParamsDto.getCode(),
				programParamsDto.getPrivateProgram()));
	}

	@PostMapping("/delete")
	public void deleteProgram(@RequestAttribute Long userId,
			@RequestBody ProgramIdDto programIdDto)
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programIdDto.getProgramId());
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		programService.deleteProgram(programIdDto.getProgramId());
	}

	@GetMapping("/{programId}")
	public ProgramDto getProgramInfo(@RequestAttribute Long userId,
			@PathVariable Long programId)
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programId);
		if (program.getPrivateProgram()) {
			if (program.getUser().getId() != userId) {
				List<User> users = programService.getSharedUsersByProgram(programId);

				boolean shared = false;
				if (users.size() == 0) {
					throw new PermissionException();
				}

				for (User user : users) {
					if (user.getId() == userId) {
						shared = true;
					}
				}

				if (!shared) {
					throw new PermissionException();
				}
			}
		}

		return toProgramDto(program);
	}

	@GetMapping("/example")
	public ProgramDto getExampleProgram() throws InstanceNotFoundException {
		Program program = programService.getProgram((long)1);

		return toProgramDto(program);
	}

	@GetMapping("/fullinfo/{programId}")
	public FullProgramDto getProgramFullInfo(@RequestAttribute Long userId,
			@PathVariable Long programId)
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programId);
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		return toFullProgramDto(program,
				programService.getSharedUsersByProgram(programId));
	}

	@PutMapping("/update")
	public ProgramDto updateProgram(@RequestAttribute Long userId,
			@RequestBody ProgramParamsDto2 programParamsDto2)
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programParamsDto2.getProgramId());
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		return toProgramDto(programService.updateProgram(programParamsDto2.getProgramId(),
				programParamsDto2.getProgramName(), programParamsDto2.getProgramDesc(),
				programParamsDto2.getCode()));
	}

	@GetMapping("/all/{id}")
	public List<FullProgramDto> getAllProgramsByUser(@RequestAttribute Long userId,
			@PathVariable Long id)
			throws InstanceNotFoundException, PermissionException {

		if (!id.equals(userId)) {
			throw new PermissionException();
		}

		List<Program> programs = programService.getProgramsByUser(id);
		List<FullProgramDto> fullProgramDtos = new ArrayList<>();
		for (Program p : programs) {
			fullProgramDtos.add(toFullProgramDto(p, programService.getSharedUsersByProgram(p.getId())));
		}

		return fullProgramDtos;
	}

	@PutMapping("/setpublic")
	public FullProgramDto setPublic(@RequestAttribute Long userId,
			@RequestBody ProgramIdDto programIdDto)
					throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programIdDto.getProgramId());
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		return toFullProgramDto(programService.setPublic(programIdDto.getProgramId()),
				programService.getSharedUsersByProgram(programIdDto.getProgramId()));
	}

	@PutMapping("/setprivate")
	public FullProgramDto setPrivate(@RequestAttribute Long userId,
			@RequestBody ProgramIdDto programIdDto)
					throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programIdDto.getProgramId());
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		return toFullProgramDto(programService.setPrivate(programIdDto.getProgramId()),
				programService.getSharedUsersByProgram(programIdDto.getProgramId()));
	}

	@GetMapping("/public/{userId}")
	public List<ProgramDto> getPublicProgramsByUser(@PathVariable Long userId)
			throws InstanceNotFoundException {

		return toProgramDtos(programService.getPublicProgramsByUser(userId));
	}

	@PutMapping("/share")
	public void shareProgram(@RequestAttribute Long userId,
			@RequestBody ProgramParamsDto3 programParamsDto3 )
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programParamsDto3.getProgramId());
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		programService.shareProgram(programParamsDto3.getUserId(), programParamsDto3.getProgramId());
	}

	@PutMapping("/unshare")
	public void unshareProgram(@RequestAttribute Long userId,
			@RequestBody ProgramParamsDto3 programParamsDto3 )
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Program program = programService.getProgram(programParamsDto3.getProgramId());
		if (program.getUser().getId() != userId) {
			throw new PermissionException();
		}

		programService.unshareProgram(programParamsDto3.getUserId(), programParamsDto3.getProgramId());
	}

	@GetMapping("/shared/{id}")
	public List<ProgramDto> getSharedProgramsByUser(@RequestAttribute Long userId,
			@PathVariable Long id)
			throws InstanceNotFoundException, PermissionException {

		if (!id.equals(userId)) {
			throw new PermissionException();
		}

		return toProgramDtos(programService.getSharedProgramsWithMe(id));
	}

	@GetMapping("/shared/{myUserId}/{otherUserId}")
	public List<ProgramDto> getSharedProgramsWithMeByUser(@RequestAttribute Long userId,
			@PathVariable Long myUserId, @PathVariable Long otherUserId)
			throws InstanceNotFoundException, PermissionException {

		if (!myUserId.equals(userId)) {
			throw new PermissionException();
		}

		return toProgramDtos(programService.getSharedProgramsWithMeByUser(myUserId, otherUserId));
	}

	@GetMapping("/search/{keyword}")
	public List<ProgramDto> getProgramsByKeyword(@PathVariable String keyword) {

		// Public programs only
		return toProgramDtos(programService.getProgramsByKeyword(keyword));
	}

	@GetMapping("/exercise/{exerciseId}")
	public List<ProgramDto> getProgramsByExercise(@RequestAttribute Long userId,
			@PathVariable Long exerciseId)
			throws InstanceNotFoundException, PermissionException {

		// Check permissions
		Exercise exercise = exerciseService.getExercise(exerciseId);
		List<User> members = groupService.getAllUsersByGroup(exercise.getGroup().getId());
		boolean isMember = false;
		for (User user : members) {
			if (user.getId() == userId) {
				isMember = true;
			}
		}
		if (!isMember) {
			throw new PermissionException();
		}

		return toProgramDtos(programService.getProgramsByExercise(exerciseId));
	}
}
