package com.shep.controllers;

import com.shep.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.shep.entities.Record;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Record> createRecord(@RequestBody Record record) {
        return ResponseEntity.ok(recordService.saveRecord(record));
    }

    @GetMapping("/getRecords")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Record>> getRecords() {
        return ResponseEntity.ok(recordService.getRecords());
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Record> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getRecord(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Record> updateRecord(@PathVariable Long id, @RequestBody Record record) {
        Record existingRecord = recordService.getRecord(id);
        existingRecord.setUser(record.getUser());
        existingRecord.setProject(record.getProject());
        existingRecord.setStartTime(record.getStartTime());
        existingRecord.setEndTime(record.getEndTime());
        return ResponseEntity.ok(recordService.saveRecord(existingRecord));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
