package lk.ijse.cropMonitoringSystem.repository;

import lk.ijse.cropMonitoringSystem.entity.MonitorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringRepo extends JpaRepository<MonitorLogEntity,String> {

}
