package com.task.tracker.infrastructure.repositories.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import com.task.tracker.models.Developer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long>{
    @Query(value = """
        SELECT d.name AS name, COUNT(t.id) AS taskCount
        FROM developers d
        JOIN tasks t ON d.id = t.developer_id
        GROUP BY d.name
        ORDER BY taskCount DESC
        LIMIT 5
    """, nativeQuery = true)
    List<DeveloperTaskCount> findTop5DevelopersWithTaskCount();

    record DeveloperTaskCount(String name, Long taskCount) {}

}



