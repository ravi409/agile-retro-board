package com.quickthinker.agile.retro.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickthinker.agile.retro.model.Point;
import com.quickthinker.agile.retro.repository.PointRepository;

@Service
public class PointDAO {
	@Autowired
	PointRepository pointRepository;
	
	public List<Point> findAll(){
		List<Point> points=new ArrayList<>();
		pointRepository.findAll().forEach(points::add);
		return points;
	}
	
	public List<Point> findBySectionId(long sectionId){
		return pointRepository.findBySectionId(sectionId);
	}
	
	public List<Point>	findBySectionIdIn(List<Long> sectionIds){
		return pointRepository.findBySectionIdIn(sectionIds);
	}
	
	public Point save(Point point){
		point.setCreatedAt(LocalDate.now());
		point.setUpdatedAt(LocalDate.now());
		point.setVotes_count((long)0);
		return pointRepository.save(point);
	}
	
	public int updateMessage(String message,long id){
		return pointRepository.updateMessageById(message, id);
	}
	
	public int updateSectionId(long sectionId,long id){
		return pointRepository.updateSectionById(sectionId, id);
	}
	
	public int addVote(long id){
		int result=0;
		Point point=pointRepository.findById(id).orElse(null);
		if(point !=null){
			result=pointRepository.updateVotesById(point.getVotes_count() +1, id);
		}
		return result;
	}
	
	public void delete(long id){
		pointRepository.deleteById(id);
	}

}
