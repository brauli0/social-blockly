package es.udc.paproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentDao extends PagingAndSortingRepository<Comment, Long> {

}
