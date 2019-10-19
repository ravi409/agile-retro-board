package com.quickthinker.agile.retro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quickthinker.agile.retro.model.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
	@Modifying
	@Transactional
	@Query("update Point set message = ?1 where id= ?2")
	int updateMessageById(String message, long id);
	
	@Modifying
	@Transactional
	@Query("update Point set section_id = ?1 where id= ?2")
	int updateSectionById(long sectionId, long id);
	
	@Modifying
	@Transactional 
	@Query("update Point set votes_count = ?1 where id = ?2")
	int updateVotesById(long votesCount, long id);
	
	
	@Query("select p from Point p where p.section_id = ?1")
	List<Point> findBySectionId(long sectionId);
	
	
	@Query("select p from Point p where p.section_id in ?1")
	List<Point> findBySectionIdIn(List<Long> sectionIds);
	
	
}