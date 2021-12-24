package com.aiamadeus.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.aiamadeus.ems.model.Feedback;
import com.aiamadeus.ems.model.Review;

@Service
public class FeedbackService {
	@Autowired
	MongoTemplate mongoTemplate;
	public Feedback saveFeedback(Feedback f) throws Exception {
		try {
			return mongoTemplate.save(f);
		}
		catch(Exception e) {
			throw new Exception(e);
		}
	}

	public List<Feedback> getFeedbacks(String rid) throws Exception{
		try {
			Query q = new Query();
			q.addCriteria(Criteria.where("rid").is(rid));
			return mongoTemplate.find(q, Feedback.class);
		}
		catch(Exception e) {
			throw new Exception(e);
		}
	}
}
