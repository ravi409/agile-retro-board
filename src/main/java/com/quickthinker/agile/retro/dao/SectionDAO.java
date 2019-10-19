package com.quickthinker.agile.retro.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickthinker.agile.retro.model.Section;
import com.quickthinker.agile.retro.repository.SectionRepository;

@Service
public class SectionDAO {
	@Autowired
	SectionRepository sectionRepository;
	
	public List<Section> findAll(){
		List<Section> sections= new ArrayList<>();
		sectionRepository.findAll().forEach(sections::add);
		return sections;
	}
	
	public List<Section> findAllByRetroId(long retroId){
		return sectionRepository.findByRetroIdOrderByIdAsc(retroId);
	}
	
	public List<Section> findAllByRetroIdAndRowId(long retroId, long rowId){
		return sectionRepository.findByRetroIdAndRowIdOrderByIdAsc(retroId, rowId);
	}
	
	public List<Long> findDistinctRowIds(long retroId){
		return sectionRepository.findDistinctRowIds(retroId);
	}
	
	public List<Long> findDistinctSectionIds(long retroId){
		return sectionRepository.findDistinctSectionIds(retroId);
	}
	
	public Section save(Section section){
		section.setCreatedAt(LocalDate.now());
		section.setUpdatedAt(LocalDate.now());
		return sectionRepository.save(section);
	}
	
	public void delete(long id){
		sectionRepository.deleteById(id);
	}
	
	public void addSections(Map<String, String> body, long retroId, int numberOfSections){
		List<Map<String, Integer>> indexList=calculateSectionIndex(numberOfSections);
		for(Map<String, Integer> indexMap:indexList){
			Section section= new Section();
			if(body.containsKey("sectionname"+ indexMap.get("sectionIndex"))){
				String style=indexMap.get("sizeIndex").equals(3) ? "oneThird " :"half ";
				style += getColorStyle(indexMap.get("sectionIndex"));
				section.setName(body.get("sectionname"+ indexMap.get("sectionIndex")));
				section.setRetroId(retroId);
				section.setRowId((long)indexMap.get("rowIndex"));
				section.setStyle(style);
				this.save(section);
			}
		}
	}
	
	public String getColorStyle(int sectionIndex){
		String[] styles=new String[]{"orange","green","purple", "aqua","blue","yellow"};
		if(sectionIndex >= 6){
			sectionIndex -=6;
		}
		return styles[sectionIndex];
	}
	
	public List<Map<String,Integer>> calculateSectionIndex(int numberOfSections){
		List<Map<String,Integer>> indexList=new ArrayList<>();
		int halfSections=0, oneThirdSections =0, halfSecRowCnt =0, oneThirdSecRowCnt =0;
		
		oneThirdSections =numberOfSections;
		
		while(!(oneThirdSections%3 ==0)){
			halfSections += 2;
			oneThirdSections -=2;
		}
		
		if(oneThirdSections > 0 && oneThirdSections%3 ==0){
			oneThirdSecRowCnt = oneThirdSections/3;
			
			IntStream.range(0, oneThirdSecRowCnt).forEach(rowIndex -> {
				IntStream.range(0, 3).forEach(colIndex -> {
					Map<String,Integer> section= new HashMap<>();
					section.put("sectionIndex", ((rowIndex *3) + colIndex));
					section.put("rowIndex", rowIndex);
					section.put("sizeIndex", 3);
					indexList.add(section);
				});	
			});
		}
		final int previousSections=oneThirdSections;
		final int previousRows= oneThirdSecRowCnt;
		
		if(halfSections > 0 && halfSections%2 ==0){
			halfSecRowCnt = halfSections/2;
			
			IntStream.range(0,  halfSecRowCnt).forEach(rowIndex -> {
				IntStream.range(0,  2).forEach(colIndex -> {
					Map<String,Integer> section=new HashMap<>();
					section.put("sectionIndex", ((rowIndex *2 ) + previousSections + colIndex));
					section.put("rowIndex", rowIndex + previousRows);
					section.put("sizeIndex", 2);
					indexList.add(section);
				});
			});
		}
		return indexList;
		
	}

}
