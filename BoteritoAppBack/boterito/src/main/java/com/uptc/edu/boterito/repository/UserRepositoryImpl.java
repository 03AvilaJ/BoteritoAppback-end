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
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    // Constructor injection (mejor que @Autowired en campos)
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    
    @Override
    public List<User> findAllUsersWithRoles() {
        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("roles_id")
                .foreignField("_id")
                .as("role");
        UnwindOperation unwindRole = Aggregation.unwind("role", true);

        Aggregation aggregation = Aggregation.newAggregation(
                lookupRole,
                unwindRole
        );

        AggregationResults<User> results =
                mongoTemplate.aggregate(aggregation, "usuarios", User.class);
        System.out.println(results.getMappedResults());
        return results.getMappedResults();
    }
}
