package lk.ijse.cropMonitoringSystem.repository;

import lk.ijse.cropMonitoringSystem.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepo extends JpaRepository<EquipmentEntity,String> {
}
