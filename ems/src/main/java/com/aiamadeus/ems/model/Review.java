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
@Document(collection = "review")
public class Review {
	@Id  @JsonSerialize
	private ObjectId rid;
	private Date assignedDate;
	private boolean completed;
	private String targetId;
	private List<Employee> participants;
	public String getRid() {
		return rid.toHexString();
	}
}
