package com.crepestrips.userservice.repository;

import com.crepestrips.userservice.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {}
