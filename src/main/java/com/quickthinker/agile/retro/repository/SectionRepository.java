package com.quickthinker.agile.retro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quickthinker.agile.retro.model.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long>{

	<T> List<T> findByRetroIdAndRowIdOrderByIdAsc(long retroId, long rowId);

	<T> List<T>  findByRetroIdOrderByIdAsc(long retroId);
	
	@Query("select distinct rowId from Section where retroId= ?1 order by rowId")
	List<Long> findDistinctRowIds(long retroId);
	
	@Query("select distinct id from Section where retroId = ?1")
	List<Long> findDistinctSectionIds(long retroId);
}
