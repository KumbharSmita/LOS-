package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="credit_scores")
public class CreditScores {
	
	@Id	
	private Integer id;
	private Integer application_id;
	private String bereau;
	private Integer score;
	private LocalDateTime fatched_at;
	public CreditScores() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CreditScores(Integer id, Integer application_id, String bereau, Integer score, LocalDateTime fatched_at) {
		super();
		this.id = id;
		this.application_id = application_id;
		this.bereau = bereau;
		this.score = score;
		this.fatched_at = fatched_at;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getApplication_id() {
		return application_id;
	}
	public void setApplication_id(Integer application_id) {
		this.application_id = application_id;
	}
	public String getBereau() {
		return bereau;
	}
	public void setBereau(String bereau) {
		this.bereau = bereau;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public LocalDateTime getFatched_at() {
		return fatched_at;
	}
	public void setFatched_at(LocalDateTime fatched_at) {
		this.fatched_at = fatched_at;
	}
	
	
}
