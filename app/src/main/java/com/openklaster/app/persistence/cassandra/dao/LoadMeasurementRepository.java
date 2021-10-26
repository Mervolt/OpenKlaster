package com.openklaster.app.persistence.cassandra.dao;

import com.openklaster.app.model.entities.measurement.LoadMeasurementEntity;
import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoadMeasurementRepository extends CassandraRepository<LoadMeasurementEntity, String> {

    @Query(allowFiltering = true)
    List<LoadMeasurementEntity> findByTimestampBetweenAndUnitAndInstallationId(Date startDate, Date endDate, MeasurementUnit unit, String installationId);

    @Query(allowFiltering = true)
    List<LoadMeasurementEntity> findByTimestampBeforeAndUnitAndInstallationId(Date endDate, MeasurementUnit unit, String installationId);

    @Query(allowFiltering = true)
    List<LoadMeasurementEntity> findByTimestampAfterAndUnitAndInstallationId(Date startDate, MeasurementUnit unit, String installationId);

    @Query(allowFiltering = true)
    List<LoadMeasurementEntity> findByInstallationIdAndUnit(String installationId, MeasurementUnit unit);
}
