package com.openklaster.app.persistence.mongo.installation;

import com.openklaster.app.model.entities.installation.InstallationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallationRepository extends MongoRepository<InstallationEntity, String> {

    List<InstallationEntity> findAllByUsername(String username);

    Optional<InstallationEntity> findByIdAndUsername(String id, String username);
}
