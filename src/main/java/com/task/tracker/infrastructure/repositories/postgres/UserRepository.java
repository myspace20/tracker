package com.task.tracker.infrastructure.repositories.postgres;

import com.task.tracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Query(value = """
        SELECT d.name AS name, COUNT(t.id) AS taskCount
        FROM users d
        JOIN tasks t ON d.id = t.developer_id
        GROUP BY d.name
        ORDER BY taskCount DESC
        LIMIT 5
    """, nativeQuery = true)

    List<DeveloperTaskCount> findTop5DevelopersWithTaskCount();

    record DeveloperTaskCount(String name, Long taskCount) {}

    Optional<User> findByEmail(String email);


}



