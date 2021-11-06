package com.openklaster.app.services;

import com.openklaster.app.model.entities.measurement.LoadMeasurementEntity;
import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.entities.measurement.SourceMeasurementEntity;
import com.openklaster.app.model.requests.MeasurementRequest;
import com.openklaster.app.model.responses.MeasurementResponse;
import com.openklaster.app.persistence.cassandra.dao.LoadMeasurementRepository;
import com.openklaster.app.persistence.cassandra.dao.SourceMeasurementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeasurementsService {
    LoadMeasurementRepository loadMeasurementRepository;
    SourceMeasurementRepository sourceMeasurementRepository;

    private static final Logger logger = LoggerFactory.getLogger(MeasurementsService.class);

    public SourceMeasurementEntity addSourceMeasurementEntity(double value, String installationId, Date timestamp, MeasurementUnit unit) {
        SourceMeasurementEntity loadMeasurementEntity = SourceMeasurementEntity.builder()
                .installationId(installationId)
                .unit(unit)
                .value(value)
                .timestamp(timestamp)
                .build();
        logger.debug(("Added source measurement" + loadMeasurementEntity.toString()));
        return sourceMeasurementRepository.save(loadMeasurementEntity);
    }

    public MeasurementResponse addLoadMeasurement(MeasurementRequest request, MeasurementUnit unit) {
        Date date = Optional.ofNullable(request.getTimestamp()).orElse(new Date());
        LoadMeasurementEntity newMeasurement = LoadMeasurementEntity.builder()
                .installationId(request.getInstallationId())
                .unit(unit)
                .value(request.getValue())
                .timestamp(date)
                .build();
        loadMeasurementRepository.save(newMeasurement);
        logger.debug(("Added source measurement" + newMeasurement.toString()));
        return createLoadMeasurementResponse(request, unit, date);
    }

    public MeasurementResponse addSourceMeasurement(MeasurementRequest request, MeasurementUnit unit) {
        Date date = Optional.ofNullable(request.getTimestamp()).orElse(new Date());
        SourceMeasurementEntity newMeasurement = SourceMeasurementEntity.builder()
                .installationId(request.getInstallationId())
                .unit(unit)
                .value(request.getValue())
                .timestamp(date)
                .build();
        sourceMeasurementRepository.save(newMeasurement);
        logger.debug(("Added source measurement" + newMeasurement.toString()));
        return createLoadMeasurementResponse(request, unit, date);
    }

    public List<MeasurementResponse> getLoadMeasurements(String installationId, Date startDate, Date endDate, MeasurementUnit unit) {
        Optional<Date> startDateOpt = Optional.ofNullable(startDate);
        Optional<Date> endDateOpt = Optional.ofNullable(endDate);
        return getLoadEntities(installationId, startDateOpt, endDateOpt, unit)
                .stream()
                .map(this::loadMeasurementEntityToResponse)
                .limit(1000)
                .collect(Collectors.toList());

    }

    private List<LoadMeasurementEntity> getLoadEntities(String installationId, Optional<Date> startDateOpt,
                                                        Optional<Date> endDateOpt, MeasurementUnit unit) {
        if (startDateOpt.isPresent() && endDateOpt.isPresent()) {
            return loadMeasurementRepository.findByTimestampBetweenAndUnitAndInstallationId(startDateOpt.get(),
                    endDateOpt.get(), unit, installationId);
        } else if (startDateOpt.isPresent()) {
            return loadMeasurementRepository.findByTimestampAfterAndUnitAndInstallationId(startDateOpt.get(),
                    unit, installationId);
        } else if (endDateOpt.isPresent()) {
            return loadMeasurementRepository.findByTimestampBeforeAndUnitAndInstallationId(endDateOpt.get(),
                    unit, installationId);
        } else {
            return loadMeasurementRepository.findByInstallationIdAndUnit(installationId, unit);
        }
    }

    public List<MeasurementResponse> getSourceMeasurements(String installationId, Date startDate, Date endDate, MeasurementUnit unit) {
        Optional<Date> startDateOpt = Optional.ofNullable(startDate);
        Optional<Date> endDateOpt = Optional.ofNullable(endDate);
        return getSourceEntities(installationId, startDateOpt, endDateOpt, unit)
                .stream()
                .map(this::sourceMeasurementEntityToResponse)
                .limit(1000)
                .collect(Collectors.toList());

    }

    private List<SourceMeasurementEntity> getSourceEntities(String installationId, Optional<Date> startDateOpt,
                                                            Optional<Date> endDateOpt, MeasurementUnit unit) {
        if (startDateOpt.isPresent() && endDateOpt.isPresent()) {
            return sourceMeasurementRepository.findByTimestampBetweenAndUnitAndInstallationId(startDateOpt.get(),
                    endDateOpt.get(), unit, installationId);
        } else if (startDateOpt.isPresent()) {
            return sourceMeasurementRepository.findByTimestampAfterAndUnitAndInstallationId(startDateOpt.get(),
                    unit, installationId);
        } else if (endDateOpt.isPresent()) {
            return sourceMeasurementRepository.findByTimestampBeforeAndUnitAndInstallationId(endDateOpt.get(),
                    unit, installationId);
        } else {
            return sourceMeasurementRepository.findByInstallationIdAndUnit(installationId, unit);
        }
    }

    private MeasurementResponse createLoadMeasurementResponse(MeasurementRequest request, MeasurementUnit unit, Date date) {
        return MeasurementResponse.builder()
                .installationId(request.getInstallationId())
                .timestamp(date)
                .unit(unit.name())
                .value(request.getValue())
                .build();
    }

    private MeasurementResponse loadMeasurementEntityToResponse(LoadMeasurementEntity entity) {
        return MeasurementResponse.builder()
                .installationId(entity.getInstallationId())
                .unit(entity.getUnit().name())
                .timestamp(entity.getTimestamp())
                .value(entity.getValue())
                .build();
    }

    private MeasurementResponse sourceMeasurementEntityToResponse(SourceMeasurementEntity entity) {
        return MeasurementResponse.builder()
                .installationId(entity.getInstallationId())
                .unit(entity.getUnit().name())
                .timestamp(entity.getTimestamp())
                .value(entity.getValue())
                .build();
    }
}
