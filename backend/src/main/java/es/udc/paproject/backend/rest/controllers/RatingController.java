package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.RatingConversor.toRatingDto;
import static es.udc.paproject.backend.rest.dtos.RatingConversor.toRatingDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.services.PermissionException;
import es.udc.paproject.backend.model.services.ProgramService;
import es.udc.paproject.backend.model.services.RatingService;
import es.udc.paproject.backend.rest.dtos.RatingDto;
import es.udc.paproject.backend.rest.dtos.RatingParamsDto;
import es.udc.paproject.backend.rest.dtos.RatingParamsDto2;

@RestController
@RequestMapping("/rating")
public class RatingController {

	@Autowired
	private RatingService ratingService;

	@Autowired
	private ProgramService programService;

	@PostMapping("/create")
	public RatingDto createRating(@RequestAttribute Long userId,
			@RequestBody RatingParamsDto ratingParamsDto)
			throws InstanceNotFoundException, DuplicateInstanceException, PermissionException {

		if (!ratingParamsDto.getUserId().equals(userId)) {
		 	throw new PermissionException();
		}

		return toRatingDto(ratingService.createRating(ratingParamsDto.getUserId(),
				ratingParamsDto.getProgramId(), ratingParamsDto.getRating()));
	}

	@PostMapping("/delete")
	public void deleteRating(@RequestAttribute Long userId,
			@RequestBody RatingParamsDto2 ratingParamsDto2)
			throws InstanceNotFoundException, PermissionException {

		if (!ratingParamsDto2.getUserId().equals(userId)) {
		 	throw new PermissionException();
		}

		ratingService.deleteRating(ratingParamsDto2.getUserId(), ratingParamsDto2.getProgramId());
	}

	@GetMapping("/all/{programId}")
	public List<RatingDto> getProgramRatings(@RequestAttribute Long userId,
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

		return toRatingDtos(ratingService.getProgramRatings(programId));
	}
}
