package lk.ijse.cropMonitoringSystem.repository;

import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity,String> {
    @Query(value = "SELECT field_code FROM field ORDER BY CAST(SUBSTRING(field_code, 5) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findLastFieldCode();
}
