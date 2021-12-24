package com.aiamadeus.ems.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aiamadeus.ems.model.Employee;
import com.aiamadeus.ems.model.Response;
import com.aiamadeus.ems.service.EmployeeService;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService service;
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@PutMapping("/employee")
	public ResponseEntity<Response> createEmployee(@RequestBody Employee e) {
		try {
			e.setId(ObjectId.get());
			Employee result = service.createEmployee(e);
			Response response = new Response(0, result.getId() + " is added");
			return ResponseEntity.ok(response);
		} 
		catch (Exception ex) {
			Response response = new Response(1, "System Error Occurred: " + ex);
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping("/employee/{id}")
	public ResponseEntity<Response> updateEmployee(@PathVariable String id, @RequestBody Employee e) {
		try {
			boolean result = service.updateEmployee(id, e);
			if (result) {
				Response response = new Response(0, id + " is updated");
				return ResponseEntity.ok(response);
			} 
			else return ResponseEntity.ok(new Response(1, "Update failed."));
		} 
		catch (Exception ex) {
			Response response = new Response(1, "System Error Occurred: " + ex);
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<Response> deleteEmployee(@RequestBody List<String> ids) {
		try {
			boolean result = service.deleteEmployee(ids);
			if(result) {
				Response response = new Response(0, "Employee " + ids + " is deleted");
				return ResponseEntity.ok(response);
			}
			else return ResponseEntity.ok(new Response(1, "Delete failed.")); 
		}
		catch(Exception e) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}

	@GetMapping("/employees")
	public ResponseEntity<Response> getAllEmployees() {
		try {
			List<Employee> list = service.getAllEmployees();
			Response response = new Response(0, list);
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}

}
