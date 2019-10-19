package com.quickthinker.agile.retro.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Section {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "SECTION_SEQ")
	@SequenceGenerator(sequenceName = "section_seq", allocationSize = 1, name= "SECTION_SEQ")
	private Long id;
	private Long retroId;
	private Long rowId;
	private String style;
	private String name;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRetroId() {
		return retroId;
	}
	public void setRetroId(Long retroId) {
		this.retroId = retroId;
	}
	public Long getRowId() {
		return rowId;
	}
	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	
	
}
