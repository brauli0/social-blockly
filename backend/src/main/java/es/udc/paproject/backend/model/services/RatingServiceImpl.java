package es.udc.paproject.backend.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.common.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.ProgramDao;
import es.udc.paproject.backend.model.entities.Rating;
import es.udc.paproject.backend.model.entities.RatingDao;
import es.udc.paproject.backend.model.entities.RatingDaoImpl;
import es.udc.paproject.backend.model.entities.RatingId;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private RatingDaoImpl ratingDaoImpl;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ProgramDao programDao;

	@Override
	public Rating createRating(Long userId, Long programId, Float rating)
			throws InstanceNotFoundException, DuplicateInstanceException {
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

		RatingId ratingId = new RatingId(userId, programId);
		Optional<Rating> opRating = ratingDao.findById(ratingId);
		if (opRating.isPresent()) {
			throw new DuplicateInstanceException("project.entities.rating", user.getUserName());
		}

		Rating ratingEntity = new Rating(user, program, rating);

		return ratingDao.save(ratingEntity);
	}

	@Override
	public void deleteRating(Long userId, Long programId) throws InstanceNotFoundException {
		Optional<User> opUser = userDao.findById(userId);
		if (!opUser.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}

		RatingId ratingId = new RatingId(userId, programId);

		Optional<Rating> opRating = ratingDao.findById(ratingId);
		if (!opRating.isPresent()) {
			throw new InstanceNotFoundException("project.entities.rating", ratingId);
		}
		Rating rating = opRating.get();

		ratingDao.delete(rating);
	}

	@Override
	public List<Rating> getProgramRatings(Long programId) throws InstanceNotFoundException {
		Optional<Program> opProgram = programDao.findById(programId);
		if (!opProgram.isPresent()) {
			throw new InstanceNotFoundException("project.entities.program", programId);
		}

		return ratingDaoImpl.getProgramRatings(programId);
	}

}
