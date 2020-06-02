package es.udc.paproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class UserDaoSearchImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<User> getUsersByKeyword(String keyword) {
		String queryString = "select u from User u where u.userName like :keyword "
				+ "or u.firstName like :keyword or u.lastName like :keyword";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("keyword", "%"+keyword+"%");

		return query.setMaxResults(10).getResultList();
	}

	public User getUserInfo(String username) throws NoResultException {
		String queryString = "select u from User u where u.userName like :username";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("username", username);

		return (User) query.getSingleResult();
	}
}