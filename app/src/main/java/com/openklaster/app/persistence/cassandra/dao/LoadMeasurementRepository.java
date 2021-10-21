package com.openklaster.app.persistence.cassandra.dao;

import com.openklaster.app.model.entities.measurement.LoadMeasurementEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoadMeasurementRepository extends CassandraRepository<LoadMeasurementEntity, String> {

    List<LoadMeasurementEntity> findByTimestampBetweenAndUnitAndInstallationId(Date startDate, Date endDate, String unit,
                                                                                 String installationId);
    List<LoadMeasurementEntity> findByTimestampBeforeAndUnitAndInstallationId(Date endDate, String unit,
                                                                                String installationId);
    List<LoadMeasurementEntity> findByTimestampAfterAndUnitAndInstallationId(Date startDate, String unit,
                                                                               String installationId);
    List<LoadMeasurementEntity> findByInstallationIdAndUnit(String installationId, String unit);
}
