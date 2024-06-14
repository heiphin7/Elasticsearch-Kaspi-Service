package com.parser.parser.controller;

import com.parser.parser.repository.SearchRepository;
import com.parser.parser.service.DataMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchRepository searchRepository; // elasticsearchrepo

    @Autowired
    private DataMigrationService dataMigrationService;

    @PostMapping("/api/v1/find/{title}")
    public ResponseEntity<?> findByTitle(@PathVariable String title) {
        return ResponseEntity.ok(searchRepository.findByTitle(title));
    }

    @GetMapping("/api/v1/migrate/data")
    public ResponseEntity<?> migrateData() {
        dataMigrationService.migrateData();
        return ResponseEntity.ok("Данные мигрированы!");
    }
}
