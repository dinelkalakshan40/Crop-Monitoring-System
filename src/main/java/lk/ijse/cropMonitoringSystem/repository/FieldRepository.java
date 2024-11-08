package lk.ijse.cropMonitoringSystem.repository;

import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity,String> {
}
