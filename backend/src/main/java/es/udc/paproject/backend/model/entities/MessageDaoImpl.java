package es.udc.paproject.backend.model.entities;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class MessageDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<ChatMessage> getGroupMessages(Long groupId) {
		String queryString = "SELECT m FROM ChatMessage m "
				+ "WHERE m.group.id = :groupId order by m.messageDate";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("groupId", groupId);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ChatMessage> getGroupMessagesByDate(Long groupId, Calendar begin, Calendar end) {
		String queryString = "SELECT m FROM ChatMessage m "
				+ "WHERE m.group.id = :groupId and "
				+ "m.messageDate > :begin and m.messageDate < :end "
				+ "order by m.messageDate";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("groupId", groupId);
		query.setParameter("begin", begin);
		query.setParameter("end", end);

		return query.getResultList();
	}
}
