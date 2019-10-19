package com.quickthinker.agile.retro.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Point {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "POINT_SEQ")
	@SequenceGenerator(sequenceName = "point_seq", allocationSize=1, name= "POINT_SEQ")
	private Long id;
	private Long section_id;
	private String message;
	private Long votes_count;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSection_id() {
		return section_id;
	}
	public void setSection_id(Long section_id) {
		this.section_id = section_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getVotes_count() {
		return votes_count;
	}
	public void setVotes_count(Long votes_count) {
		this.votes_count = votes_count;
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
