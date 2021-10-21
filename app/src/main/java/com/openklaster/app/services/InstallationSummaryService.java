package com.openklaster.app.services;

import com.openklaster.app.model.responses.InstallationSummaryResponse;
import com.openklaster.app.persistence.cassandra.dao.LoadMeasurementRepository;
import com.openklaster.app.persistence.cassandra.dao.SourceMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstallationSummaryService {

    @Autowired
    LoadMeasurementRepository loadMeasurementRepository;
    @Autowired
    SourceMeasurementRepository sourceMeasurementRepository;

    public InstallationSummaryResponse getSummary(String installationId) {
        throw new UnsupportedOperationException();
        //TODO oj poprzeciągać tu liczenie
    }
}
