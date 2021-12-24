package com.aiamadeus.ems.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Document(collection = "feedback")
public class Feedback {
	@Id  @JsonSerialize
	private ObjectId fid;
	private String rid;
	private String message;
	private Employee from;
	private int rating;
	private Date completed_date;
	public String getFid() {
		return fid.toHexString();
	}
}
