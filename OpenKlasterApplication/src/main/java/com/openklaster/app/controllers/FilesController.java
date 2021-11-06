package com.openklaster.app.controllers;

import com.openklaster.app.services.FileRepositoryService;
import com.openklaster.app.validation.installation.SafeInstallation;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "charts", description = "Charts and their dates info")
@Validated
public class FilesController {

    @Autowired
    FileRepositoryService fileRepositoryService;


    @GetMapping("selectableDates")
    public List<String> getDates(@RequestParam String installationId){
        return fileRepositoryService.getSelectableDates(installationId);
    }

    @GetMapping("chart")
    public Map<String, String> getCharts(@RequestParam String installationId, String date){
        return fileRepositoryService.getChartBase64Encoded(installationId, date);
    }


}
