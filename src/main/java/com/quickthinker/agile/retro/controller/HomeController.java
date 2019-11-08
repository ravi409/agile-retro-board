package com.quickthinker.agile.retro.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quickthinker.agile.retro.dao.RetroDAO;
import com.quickthinker.agile.retro.dao.SectionDAO;
import com.quickthinker.agile.retro.model.Retro;
import com.quickthinker.agile.retro.model.Section;

@Controller
public class HomeController {
	
	@Autowired
	RetroDAO retroDAO;
	
	@Autowired
	SectionDAO sectionDAO;
	
	@GetMapping("/")
	public String homePage(Model model){
		model.addAttribute("retros",retroDAO.findByCreatedBy("GUEST"));
		return "index";
	}
	
	@RequestMapping(value="/", method = RequestMethod.POST,consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE,  produces= MediaType.APPLICATION_JSON_VALUE)
	public String saveRetroBoard( @RequestParam Map<String,String> body){
		long numberOfSections = 0;
		Retro retroCreated = null;
		Retro retro = new Retro();
		if(body.containsKey("name")){
			retro.setName(body.get("name"));
		}
		if(body.containsKey("description")){
			retro.setDescr(body.get("description"));
		}
		if(body.containsKey("numberOfSections")){
			numberOfSections = Long.parseLong(body.get("numberOfSections"));
			retro.setNumOfSections(numberOfSections);
		}
		retroCreated = retroDAO.save(retro);
		if(retroCreated.getId() !=null){
			sectionDAO.addSections(body, retroCreated.getId(), (int)numberOfSections);	
		}
		return "redirect:/" + retroCreated.getUuid();
	}
	
	@GetMapping("/{uuid}")
	public String retroBoard(Model model, @PathVariable("uuid") String uuid){
		List<Map<String,Object>> sectionRows = new ArrayList<>();
		List<Section> sections=new ArrayList<>();
		Retro retro = retroDAO.findByUuid(uuid);
		
		if(retro.getId() !=null){
			List<Long> rows= sectionDAO.findDistinctRowIds(retro.getId());
			for(long rowId:rows){
				Map<String, Object> sectionRow = new LinkedHashMap<>();
				List<Section> rowSections = sectionDAO.findAllByRetroIdAndRowId(retro.getId(), rowId);
				sectionRow.put("sectionRowId", "sectionRow" +rowId);
				sectionRow.put("sections", rowSections);
				sectionRows.add(sectionRow);
				sections.addAll(rowSections);
			}
		}
		
		model.addAttribute("retroId",retro.getUuid());
		model.addAttribute("retroName",retro.getName());
		model.addAttribute("sectionRows",sectionRows);
		model.addAttribute("sections",sections);
		
		return "retro-board";
	}

}
