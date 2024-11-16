package lk.ijse.cropMonitoringSystem.repository;

import lk.ijse.cropMonitoringSystem.entity.CropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropRepo extends JpaRepository<CropEntity,String> {
}
