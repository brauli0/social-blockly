package es.udc.paproject.backend.model.services;

import java.util.List;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Rating;

public interface RatingService {
	Rating createRating(Long userId, Long programId, Float rating)
			throws InstanceNotFoundException, DuplicateInstanceException;

	void deleteRating(Long userId, Long programId) throws InstanceNotFoundException;

	List<Rating> getProgramRatings(Long programId) throws InstanceNotFoundException;
}
