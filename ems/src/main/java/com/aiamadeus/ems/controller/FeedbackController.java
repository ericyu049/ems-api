package com.aiamadeus.ems.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aiamadeus.ems.model.Feedback;
import com.aiamadeus.ems.model.Response;
import com.aiamadeus.ems.model.Review;
import com.aiamadeus.ems.service.FeedbackService;
import com.aiamadeus.ems.service.ReviewService;

@RestController
public class FeedbackController {
	@Autowired
	private FeedbackService service;
	
	@PutMapping("/feedback")
	public ResponseEntity<Response> saveFeedback(@RequestBody Feedback f) {
		try {
			f.setFid(ObjectId.get());
			Feedback result = service.saveFeedback(f);
			Response response = new Response(0, result.getFid() + " is added");
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
		
	}
	@GetMapping("/feedback/{rid}")
	public ResponseEntity<Response> getFeedbacks(@PathVariable String rid) {
		try {
			List<Feedback> list = service.getFeedbacks(rid);
			return ResponseEntity.ok(new Response(0, list));
		}
		catch(Exception e) {
			return ResponseEntity.ok(new Response(1, "System Error Occurred"));
		}
	}
}
