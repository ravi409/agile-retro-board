package com.quickthinker.agile.retro.repository;

import org.springframework.stereotype.Repository;

import com.quickthinker.agile.retro.model.Retro;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RetroRepository extends JpaRepository<Retro, Long>{
	
	List<Retro> findByCreatedByOrderByIdDesc(String createdBy);
	Optional<Retro> findByUuid(String uuid);

}
