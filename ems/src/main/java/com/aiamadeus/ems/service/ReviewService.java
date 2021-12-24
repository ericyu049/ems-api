package com.aiamadeus.ems.service;

import java.util.ArrayList;
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
import com.aiamadeus.ems.model.Feedback;
import com.aiamadeus.ems.model.Review;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReviewService {
	@Autowired
	MongoTemplate mongoTemplate;
	Logger logger = LoggerFactory.getLogger(ReviewService.class);
	public Review addReview(Review r) throws Exception {
		try {
			return mongoTemplate.save(r);
		}
		catch(Exception e) {
			throw new Exception(e);
		}
	}

	public List<Review> getReviews(String eid) throws Exception {
		try {
			Query q = new Query();
			q.addCriteria(Criteria.where("targetId").is(eid));
			return mongoTemplate.find(q, Review.class);
		}
		catch(Exception e) {
			throw new Exception(e);
		}
	}

	public boolean updateReview(Review r) throws Exception {
		try {
			ObjectId oid = new ObjectId(r.getRid());
			Query q = new Query();
			q.addCriteria(Criteria.where("id").is(oid));
			Update update = new Update();
			ObjectMapper oMapper = new ObjectMapper();
			Map<String, Object> map = oMapper.convertValue(r, Map.class);
			map.entrySet().stream().forEach(s -> {
				update.set(s.getKey(), s.getValue());
			});
			Review result = mongoTemplate.findAndModify(q, update, Review.class);
			if (result != null)
				return true;
			else return false;
		} 
		catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public boolean closeReview(String rid) throws Exception {
		try {
			ObjectId oid = new ObjectId(rid);
			Query q = new Query();
			q.addCriteria(Criteria.where("rid").is(oid));
			Update update = new Update();
			update.set("completed", true);
			Review result = mongoTemplate.findAndModify(q, update, Review.class);
			if (result != null) 
				return true;
			else return false;
		} 
		catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public boolean updateParticipants(String rid, List<Employee> list) throws Exception {
		try {
			ObjectId oid = new ObjectId(rid);
			Query q = new Query();
			q.addCriteria(Criteria.where("rid").is(oid));
			Update update = new Update();
			update.set("participants", list);
			Review result = mongoTemplate.findAndModify(q, update, Review.class);
			if (result != null) 
				return true;
			else return false;
		} 
		catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public List<Review> getPendingReviews(String eid) throws Exception {
		try {
			Query q = new Query();
			ObjectId oid = new ObjectId(eid);
			q.addCriteria(Criteria.where("participants")
					.elemMatch(Criteria.where("id").is(oid))
					.andOperator(Criteria.where("completed").is(false))
			);
			List<Review> allReviews = mongoTemplate.find(q, Review.class);
			List<Review> results = new ArrayList<Review>();
			allReviews.stream().forEach(r -> {
				Query fq = new Query();
				fq.addCriteria(Criteria.where("rid").is(r.getRid()));
				List<Feedback> feedbacks = mongoTemplate.find(fq, Feedback.class);
				int count = 0;
				for (int i=0; i<feedbacks.size(); i++) {
					if(feedbacks.get(i).getFrom().getId().equals(eid))
						count++;
				}
				if (count == 0) 
					results.add(r);
			});
			return results;
		}
		catch(Exception e) {
			throw new Exception(e);
		}
	}
}
