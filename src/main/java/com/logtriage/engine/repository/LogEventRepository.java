package com.logtriage.engine.repository;

import com.logtriage.engine.entity.LogEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, String> {
}
