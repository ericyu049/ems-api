package com.aiamadeus.ems.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Document(collection = "employee")
public class Employee {
	@Id  @JsonSerialize
	private ObjectId id;
	private String name;
	private String role;
	private int level;
	private String department;
	private String avatar;
	
	public String getId() {
		return id.toHexString();
	}
	
}
