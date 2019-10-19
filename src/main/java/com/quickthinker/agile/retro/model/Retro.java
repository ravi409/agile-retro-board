package com.quickthinker.agile.retro.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Retro {
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "RETRO_SEQ")
	@SequenceGenerator(sequenceName= "retro_seq", allocationSize=1, name = "RETRO_SEQ")
	private Long id;
	private String uuid;
	private String name;
	private String descr;
	private Long numOfSections;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private String createdBy;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Long getNumOfSections() {
		return numOfSections;
	}
	public void setNumOfSections(Long numOfSections) {
		this.numOfSections = numOfSections;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDate getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

}
