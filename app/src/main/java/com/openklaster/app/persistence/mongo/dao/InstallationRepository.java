package com.openklaster.app.persistence.mongo.dao;

import com.openklaster.app.model.entities.installation.InstallationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallationRepository extends MongoRepository<InstallationEntity, String> {

    List<InstallationEntity> findAllByUsername(String username);

}
