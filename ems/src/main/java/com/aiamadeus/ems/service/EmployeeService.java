package com.aiamadeus.ems.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.aiamadeus.ems.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService {

	@Autowired
	MongoTemplate mongoTemplate;

	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	public Employee createEmployee(Employee e) throws Exception {
		try {
			return mongoTemplate.save(e);
		} 
		catch (Exception ex) {
			throw new Exception();
		}
	}

	public List<Employee> getAllEmployees() throws Exception {
		try {
			return mongoTemplate.findAll(Employee.class);
		} 
		catch (Exception e) {
			throw new Exception(e);
		}
	}

	public boolean deleteEmployee(List<String> ids) throws Exception {
		try {
			List<ObjectId> oid = ids.stream().map(x -> new ObjectId(x)).collect(Collectors.toList());
			Query q = new Query();
			q.addCriteria(Criteria.where("id").in(oid));
			List<Employee> deleted = mongoTemplate.findAllAndRemove(q, Employee.class);
			if (deleted.size() > 0)
				return true;
			else return false;
		} 
		catch (Exception e) {
			throw new Exception(e);
		}

	}

	public Boolean updateEmployee(String old, Employee e) throws Exception {
		try {
			ObjectId oid = new ObjectId(old);
			Query q = new Query();
			q.addCriteria(Criteria.where("id").is(oid));
			Update update = new Update();
			ObjectMapper oMapper = new ObjectMapper();
			Map<String, Object> map = oMapper.convertValue(e, Map.class);
			map.entrySet().stream().forEach(s -> {
				update.set(s.getKey(), s.getValue());
			});
			Employee result =  mongoTemplate.findAndModify(q, update, Employee.class);
			if (result != null) {
				return true;
			}
			else return false;
		} 
		catch (Exception ex) {
			throw new Exception(ex);
		}
	}
}
