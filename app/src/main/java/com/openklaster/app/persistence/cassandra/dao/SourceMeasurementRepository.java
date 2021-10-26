package com.openklaster.app.persistence.cassandra.dao;

import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.entities.measurement.SourceMeasurementEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SourceMeasurementRepository extends CassandraRepository<SourceMeasurementEntity, String> {

    @Query(allowFiltering = true)
    List<SourceMeasurementEntity> findByTimestampBetweenAndUnitAndInstallationId(Date startDate, Date endDate, MeasurementUnit unit, String installationId);

    @Query(allowFiltering = true)
    List<SourceMeasurementEntity> findByTimestampBeforeAndUnitAndInstallationId(Date endDate, MeasurementUnit unit, String installationId);

    @Query(allowFiltering = true)
    List<SourceMeasurementEntity> findByTimestampAfterAndUnitAndInstallationId(Date startDate, MeasurementUnit unit, String installationId);

    @Query(allowFiltering = true)
    List<SourceMeasurementEntity> findByInstallationIdAndUnit(String installationId, MeasurementUnit unit);
}
