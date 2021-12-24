package com.aiamadeus.ems.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aiamadeus.ems.model.Employee;
import com.aiamadeus.ems.model.Response;
import com.aiamadeus.ems.model.Review;
import com.aiamadeus.ems.service.ReviewService;

@RestController
public class ReviewController {
	@Autowired
	private ReviewService service;
	
	@PutMapping("/review")
	public ResponseEntity<Response> addReview(@RequestBody Review r) {
		try{
			r.setRid(ObjectId.get());
			Review result = service.addReview(r);
			Response response = new Response(0, result.getRid() + " is added");
			return ResponseEntity.ok(response);
		}
		catch(Exception ex) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}
	@GetMapping("/reviews/{eid}")
	public ResponseEntity<Response> getReviews(@PathVariable String eid) {
		try {
			List<Review> list = service.getReviews(eid);
			return ResponseEntity.ok(new Response(0, list));
		}
		catch(Exception e) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}
	@PostMapping("/review")
	public ResponseEntity<Response> updateReview(@RequestBody Review r) {
		try {
			boolean result = service.updateReview(r);
			if (result)
				return ResponseEntity.ok(new Response(0, result));
			else return ResponseEntity.ok(new Response(1, "Update review failed"));
		}
		catch(Exception ex) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
		
	}
	@PostMapping("/closereview/{rid}")
	public ResponseEntity<Response> closeReview(@PathVariable String rid) {
		try {
			boolean result = service.closeReview(rid);
			if (result)
				return ResponseEntity.ok(new Response(0, result));
			else return ResponseEntity.ok(new Response(1, "Close review failed")); 
		}
		catch(Exception ex) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}
	@PostMapping("/updateparticipants/{rid}")
	public ResponseEntity<Response> updateParticipants(@PathVariable String rid, @RequestBody List<Employee> list){
		try {
			boolean result = service.updateParticipants(rid, list);
			if (result)
				return ResponseEntity.ok(new Response(0, result));
			else return ResponseEntity.ok(new Response(1, "Update review participants failed")); 
		}
		catch(Exception ex) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}
	@GetMapping("/pendingreviews/{eid}")
	public ResponseEntity<Response> getPendingReviews(@PathVariable String eid) {
		try {
			List<Review> list = service.getPendingReviews(eid);
			return ResponseEntity.ok(new Response(0, list));
		}
		catch(Exception e) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}
}
