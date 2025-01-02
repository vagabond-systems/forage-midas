package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
}