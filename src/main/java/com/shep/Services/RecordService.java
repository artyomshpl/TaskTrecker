package com.shep.Services;

import com.shep.Repositories.RecordRepository;
import com.shep.Entities.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    public Record getRecord(Long id) {
        return recordRepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public List<Record> getRecords() {
        return recordRepository.findAll();
    }

    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}
