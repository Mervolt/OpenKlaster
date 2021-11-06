package com.openklaster.app.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileRepositoryService {

    @Autowired
    InstallationService usersService;

    @Value("${openklaster.files.base-path:file-repository/data}")
    private String rootFolder;

    private static final String installationReposRegex = "base/username/id";
    private static final String installationReposDateRegex = "base/username/id/date/charts";

    public List<String> getSelectableDates(String installationId) {
        String username = usersService.getInstallation(installationId).getUsername();
        String directoryPath = getRootPath(installationId, username);
        try (Stream<Path> stream = Files.list(Paths.get(directoryPath))) {
            return stream
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public Map<String, String> getChartBase64Encoded(String installationId, String date) {
        String username = usersService.getInstallation(installationId).getUsername();
        String chartsPath = getDatePath(installationId, username, date);
        try (Stream<Path> stream = Files.list(Paths.get(chartsPath))) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith("png"))
                    .flatMap(file -> readImageBase64(file).map(Stream::of).orElseGet(Stream::empty))
                    .collect(Collectors.toMap(ChartEntry::getFilename, ChartEntry::getData));
        } catch (IOException ex) {
            return new HashMap<>();
        }
    }

    private Optional<ChartEntry> readImageBase64(Path file) {
        try {
            byte[] content = Files.readAllBytes(file);
            String encoded = Base64.getEncoder().encodeToString(content);
            return Optional.of(new ChartEntry("data:image/png;base64, " + encoded, file.getFileName().toString()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private String getRootPath(String installationId, String username) {
        String id = installationId.split(":")[1];
        return installationReposRegex
                .replace("base", rootFolder)
                .replace("username", username)
                .replace("id", id);
    }

    private String getDatePath(String installationId, String username, String date) {
        String id = installationId.split(":")[1];
        return installationReposDateRegex
                .replace("base", rootFolder)
                .replace("username", username)
                .replace("id", id)
                .replace("date", date);
    }

    @Data
    @AllArgsConstructor
    private static class ChartEntry {
        private String data;
        private String filename;

        public String getFilename() {
            return FilenameUtils.getBaseName(filename);
        }
    }
}
