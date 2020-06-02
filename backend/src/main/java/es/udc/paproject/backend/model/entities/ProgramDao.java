package es.udc.paproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProgramDao extends PagingAndSortingRepository<Program, Long> {

	public List<Program> findByUserId(Long userId);
}
