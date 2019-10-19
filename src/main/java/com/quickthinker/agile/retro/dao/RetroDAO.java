package com.quickthinker.agile.retro.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.quickthinker.agile.retro.model.Retro;
import com.quickthinker.agile.retro.repository.RetroRepository;

@Service
public class RetroDAO {
	@Autowired
	RetroRepository retroRepository;
	
	public List<Retro> findAll(){
		List<Retro> retros=new ArrayList<>();
		retroRepository.findAll().forEach(retros::add);
		return retros;
	}
	
	public Retro findByUuid(String uuid){
		return retroRepository.findByUuid(uuid).orElse(new Retro());
	}
	
	public List<Retro> findByCreatedBy(String createdBy){
		return retroRepository.findByCreatedByOrderByIdDesc(createdBy);
	}
	
	public Retro save(Retro retro){
		Retro retroCreated=null;
		retro.setCreatedAt(LocalDate.now());
		retro.setUpdatedAt(LocalDate.now());
		try{
			retro.setUuid(UUID.randomUUID().toString());
			retroCreated = retroRepository.save(retro);
		}catch(DataIntegrityViolationException e){
			retro.setUuid(UUID.randomUUID().toString());
			retroCreated = retroRepository.save(retro);
		}
		return retroCreated;
	}
	
	public void delete(long id){
		retroRepository.deleteById(id);
	}

}
