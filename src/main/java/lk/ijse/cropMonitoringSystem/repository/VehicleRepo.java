package lk.ijse.cropMonitoringSystem.repository;

import lk.ijse.cropMonitoringSystem.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<VehicleEntity,String> {
}
