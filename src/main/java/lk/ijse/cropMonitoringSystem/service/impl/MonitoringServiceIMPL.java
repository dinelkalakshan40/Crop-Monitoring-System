package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.MonitorDTO;
import lk.ijse.cropMonitoringSystem.entity.MonitorLogEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.repository.MonitoringRepo;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MonitoringServiceIMPL {
    @Autowired
    private MonitoringRepo monitorLogRepository;
    @Autowired
    private StaffRepo staffRepo;

    public void saveMonitorLog(MonitorDTO monitorDTO) {
        MonitorLogEntity entity = new MonitorLogEntity();
        entity.setLogCode(monitorDTO.getLogCode());
        entity.setDate(monitorDTO.getDate());
        entity.setLogDetails(monitorDTO.getLogDetails());
        entity.setObservedImage(monitorDTO.getObservedImage());
        // Handle optional staffId
        if (monitorDTO.getStaffId() != null && !monitorDTO.getStaffId().isEmpty()) {
            StaffEntity staff = staffRepo.findById(monitorDTO.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + monitorDTO.getStaffId()));
            entity.setStaff(staff);
        } else {
            entity.setStaff(null); // Explicitly set staff to null
        }
        // Map cropDTOS if necessary

        monitorLogRepository.save(entity);
    }
}
