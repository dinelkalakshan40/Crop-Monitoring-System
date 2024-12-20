package lk.ijse.cropMonitoringSystem.service;

import lk.ijse.cropMonitoringSystem.DTO.MonitorDTO;
import lk.ijse.cropMonitoringSystem.entity.MonitorLogEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.repository.MonitoringRepo;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            entity.setStaff(null);
        }


        monitorLogRepository.save(entity);
    }

    public MonitorLogEntity getSelectedLog(String LogCode) {
        // Retrieve the entity from the database
        try {
            // Directly fetch the entity from the repository
            MonitorLogEntity logEntity = monitorLogRepository.findById(LogCode).orElse(null);

            // Return the entity if found or null if not found
            if (logEntity == null) {
                throw new RuntimeException("LogCode not found in the system.");
            }
            return logEntity;

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            throw new RuntimeException("Error while fetching the LogCode details.");
        }
    }
    public double calculateImageSizeInMB(String observedImage) {
        if (observedImage == null || !observedImage.contains(",")) {
            throw new IllegalArgumentException("Invalid observedImage data.");
        }

        // Extract the Base64 part of the image
        String base64Data = observedImage.split(",")[1]; // "data:image/png;base64,..."

        // Decode the Base64 string to get the size in bytes
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        double sizeInBytes = imageBytes.length;

        // Convert bytes to MB (1 MB = 1024 * 1024 bytes)
        return sizeInBytes / (1024 * 1024);
    }
    public void updateMonitorLog(MonitorDTO monitorDTO){
        if (monitorDTO.getLogCode() == null || monitorDTO.getLogCode().isEmpty()) {
            throw new IllegalArgumentException("LogCode must not be null or empty.");
        }

        // Retrieve the entity from the repository
        Optional<MonitorLogEntity> optionalLog = monitorLogRepository.findById(monitorDTO.getLogCode());
        if (optionalLog.isEmpty()) {
            throw new IllegalArgumentException("MonitorLog with given LogCode not found.");
        }

        // Update entity
        MonitorLogEntity logEntity = optionalLog.get();
        logEntity.setDate(monitorDTO.getDate());
        logEntity.setLogDetails(monitorDTO.getLogDetails());
        logEntity.setObservedImage(monitorDTO.getObservedImage());


        monitorLogRepository.save(logEntity);
    }
    public boolean deleteMonitoringLog(String logCode) {
        Optional<MonitorLogEntity> optionalLog = monitorLogRepository.findById(logCode);

        if (optionalLog.isPresent()) {
            monitorLogRepository.delete(optionalLog.get());
            return true;
        } else {
            return false; // LogCode not found
        }
    }
    public List<Map<String, Object>> getAllMonitorLogs() {
        List<MonitorLogEntity> monitorLogs = monitorLogRepository.findAll();

        List<Map<String, Object>> response = new ArrayList<>();
        for (MonitorLogEntity logEntity : monitorLogs) {
            Map<String, Object> logData = new HashMap<>();
            logData.put("LogCode", logEntity.getLogCode());
            logData.put("date", logEntity.getDate());  // Assuming date is already in String format
            logData.put("logDetails", logEntity.getLogDetails());

            // Extract the staffId from the related StaffEntity
            String staffId = logEntity.getStaff() != null ? logEntity.getStaff().getStaffId(): null;
            logData.put("staffId", staffId);

            // Calculate image size in MB
            double imageSizeMB = calculateImageSizeInMB(logEntity.getObservedImage());
            logData.put("observedImage", String.format("%.3fMB", imageSizeMB));  // Format to 3 decimal places

            response.add(logData);
        }

        return response;  // Return the formatted response list
    }
}
