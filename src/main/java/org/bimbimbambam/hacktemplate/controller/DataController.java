package org.bimbimbambam.hacktemplate.controller;

import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.bimbimbambam.hacktemplate.service.impl.DataImportService;
import org.bimbimbambam.hacktemplate.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {

    private final DataImportService dataImportService;

    @PostMapping("/import-data")
    public ResponseEntity<String> importData() throws IOException {
        dataImportService.importData();
        return ResponseEntity.ok("Data imported successfully");
    }

}

