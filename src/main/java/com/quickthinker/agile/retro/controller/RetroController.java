package com.quickthinker.agile.retro.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quickthinker.agile.retro.dao.PointDAO;
import com.quickthinker.agile.retro.dao.RetroDAO;
import com.quickthinker.agile.retro.dao.SectionDAO;
import com.quickthinker.agile.retro.model.Point;
import com.quickthinker.agile.retro.model.Retro;
import com.quickthinker.agile.retro.model.Section;

@RestController
public class RetroController {
	
	@Autowired
	RetroDAO retroDAO;
	
	@Autowired
	SectionDAO sectionDAO;
	
	@Autowired
	PointDAO pointDAO;
	
	@RequestMapping(value="/hello", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public String test(){
		return "Hello World";
	}
	
	@RequestMapping(value="/retros", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public List<Retro> getAllRetros(){
		return retroDAO.findAll();
	}
	
	@RequestMapping(value="/retros/{uuid}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public Retro getRetro(@PathVariable("uuid") String uuid){
		return retroDAO.findByUuid(uuid);
	}
	
	
	@RequestMapping(value="/retros/{uuid}/points", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public List<Point> getRetroPoints(@PathVariable("uuid") String uuid){
		List<Point> points =null;
		try{
			Retro retro=retroDAO.findByUuid(uuid);
			points= pointDAO.findBySectionIdIn(sectionDAO.findDistinctSectionIds(retro.getId()));
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return points;
	}
	
	@RequestMapping(value="/section/{uuid}/{rowId}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public List<Section> getSectionByRetroId(@PathVariable("uuid") String uuid,@PathVariable("rowId") long rowId){
		Retro retro = retroDAO.findByUuid(uuid);
		return sectionDAO.findAllByRetroIdAndRowId(retro.getId(), rowId);
	}

	@RequestMapping(value="/section/distinct/{uuid}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public List<Long> getSectionDistinctRowId(@PathVariable("uuid") String uuid){
		Retro retro = retroDAO.findByUuid(uuid);
		return sectionDAO.findDistinctRowIds(retro.getId());
	}
	
	@RequestMapping(value="/points/{id}", method = RequestMethod.POST,consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE,  produces= MediaType.APPLICATION_JSON_VALUE)
	public int updatePoint(@PathVariable("id") long id, @RequestParam Map<String,String> body){
		int result =0;
		if(body.containsKey("point[message]")){
			result =pointDAO.updateMessage(body.get("point[message]"), id);
		}else if(body.containsKey("point[section_id]")){
			result= pointDAO.updateSectionId(Long.parseLong(body.get("point[section_id]")), id);
		}
		return result;
	}
	
	@RequestMapping(value="/points}", method = RequestMethod.POST,consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE,  produces= MediaType.APPLICATION_JSON_VALUE)
	public Object savePoint(@RequestParam Map<String,String> body){
		Point point =new Point();
		point.setSection_id(Long.parseLong(body.get("point[section_id]")));
		point.setMessage(body.get("point[message]"));
		return pointDAO.save(point);
	}
	
	@RequestMapping(value="/points/{id}/votes}", method = RequestMethod.POST,  produces= MediaType.APPLICATION_JSON_VALUE)
	public int addVote(@PathVariable("id") long id){
		return pointDAO.addVote(id);
	}
	
	@RequestMapping(value="/points/delete/{id}}", method = RequestMethod.GET,  produces= MediaType.APPLICATION_JSON_VALUE)
	public int deletePoint(@PathVariable("id") long id,@RequestParam("message") String message){
		return pointDAO.addVote(id);
	}
	
	
	
}
