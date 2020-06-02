package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.ExerciseConversor.toExerciseDto;
import static es.udc.paproject.backend.rest.dtos.ExerciseConversor.toExerciseDtos;

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
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UsersGroup;
import es.udc.paproject.backend.model.entities.UsersGroupRel;
import es.udc.paproject.backend.model.services.ExerciseService;
import es.udc.paproject.backend.model.services.GroupService;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.rest.dtos.ExerciseDto;
import es.udc.paproject.backend.rest.dtos.ExerciseParamsDto;
import es.udc.paproject.backend.rest.dtos.ExerciseParamsDto2;
import es.udc.paproject.backend.rest.dtos.IdDto;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	private GroupService groupService;

	private void checkAdmin(Long exerciseId, Long userId)
			throws InstanceNotFoundException, PermissionException {
		Exercise exercise = exerciseService.getExercise(exerciseId);
		boolean isAdmin = false;
		UsersGroup group = groupService.getGroup(exercise.getGroup().getId());
		List<UsersGroupRel> rels = group.getUsersRel();
		for (UsersGroupRel rel : rels) {
			if (rel.getUser().getId() == userId && rel.getIsAdmin()) {
				isAdmin = true;
			}
		}
		if (!isAdmin) {
			throw new PermissionException();
		}
	}

	@PostMapping("/create")
	public ExerciseDto createExercise(@RequestAttribute Long userId,
			@RequestBody ExerciseParamsDto exerciseParamsDto)
			throws InstanceNotFoundException, PermissionException {

		if (!exerciseParamsDto.getUserId().equals(userId)) {
			throw new PermissionException();
		}

		boolean isAdmin = false;
		UsersGroup group = groupService.getGroup(exerciseParamsDto.getGroupId());
		List<UsersGroupRel> rels = group.getUsersRel();
		for (UsersGroupRel rel : rels) {
			if (rel.getUser().getId() == userId && rel.getIsAdmin()) {
				isAdmin = true;
			}
		}
		if (!isAdmin) {
			throw new PermissionException();
		}

		return toExerciseDto(exerciseService.createExercise(exerciseParamsDto.getStatementText(),
				exerciseParamsDto.getGroupId(), exerciseParamsDto.getUserId(),
				exerciseParamsDto.getExpirationDate(), exerciseParamsDto.getBlocks()));
	}

	@PostMapping("/delete")
	public void deleteExercise(@RequestAttribute Long userId,
			@RequestBody IdDto idDto) throws InstanceNotFoundException, PermissionException {

		checkAdmin(idDto.getId(), userId);

		exerciseService.deleteExercise(idDto.getId());
	}

	@GetMapping("/{exerciseId}")
	public ExerciseDto getExercise(@RequestAttribute Long userId,
			@PathVariable Long exerciseId) throws InstanceNotFoundException, PermissionException {

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

		return toExerciseDto(exerciseService.getExercise(exerciseId));
	}

	@PutMapping("/update")
	public ExerciseDto updateExercise(@RequestAttribute Long userId,
			@RequestBody ExerciseParamsDto2 exerciseParamsDto2)
			throws InstanceNotFoundException, PermissionException {

		checkAdmin(exerciseParamsDto2.getExerciseId(), userId);

		return toExerciseDto(exerciseService.updateExercise(exerciseParamsDto2.getExerciseId(),
				exerciseParamsDto2.getStatementText(), exerciseParamsDto2.getExpirationDate()));
	}

	@GetMapping("/group/{groupId}")
	public List<ExerciseDto> getExercisesByGroup(@RequestAttribute Long userId,
			@PathVariable Long groupId)
			throws InstanceNotFoundException, PermissionException {

		List<User> members = groupService.getAllUsersByGroup(groupId);
		boolean isMember = false;
		for (User user : members) {
			if (user.getId() == userId) {
				isMember = true;
			}
		}
		if (!isMember) {
			throw new PermissionException();
		}

		return toExerciseDtos(exerciseService.getExercisesByGroup(groupId));
	}
}
