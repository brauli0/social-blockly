package es.udc.paproject.backend.rest.dtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.Rating;

public class RatingConversor {

	private RatingConversor() {}

	public final static RatingDto toRatingDto(Rating rating) {
		return new RatingDto(rating.getUser().getId(), rating.getProgram().getId(), rating.getRating());
	}

	public final static List<RatingDto> toRatingDtos(List<Rating> ratings) {
		List<RatingDto> ratingDtos = new ArrayList<>();

		for (Rating rating : ratings) {
			ratingDtos.add(new RatingDto(rating.getUser().getId(),
					rating.getProgram().getId(), rating.getRating()));
		}

		return ratingDtos;
	}
}
