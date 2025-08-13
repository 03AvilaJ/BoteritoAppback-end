package com.uptc.edu.boterito.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.stereotype.Repository;

import com.uptc.edu.boterito.model.User;

@Repository
public class UserRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    // Constructor injection (mejor que @Autowired en campos)
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> findAllUsersWithRoles() {
        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("roles_id")
                .foreignField("_id")
                .as("rol");
        UnwindOperation unwindRole = Aggregation.unwind("rol", true);

        Aggregation aggregation = Aggregation.newAggregation(
                lookupRole,
                unwindRole
        );

        AggregationResults<User> results =
                mongoTemplate.aggregate(aggregation, "usuarios", User.class);

        return results.getMappedResults();
    }
}
