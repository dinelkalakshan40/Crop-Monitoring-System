package lk.ijse.cropMonitoringSystem.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.cropMonitoringSystem.DTO.CropDTO;
import lk.ijse.cropMonitoringSystem.DTO.MonitorDTO;
import lk.ijse.cropMonitoringSystem.entity.MonitorLogEntity;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.service.impl.MonitoringServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/monitors")
public class MonitorController {
    @Autowired
    private MonitoringServiceIMPL monitoringServiceIMPL;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveMonitor(
            @RequestPart("LogCode") String LogCode,
            @RequestPart("date") String date,
            @RequestPart("logDetails") String logDetails,
            @RequestPart("observedImage") MultipartFile observedImage,
            @RequestPart(value = "staffId", required = false) String staffId) {
        if (!LogCode.matches("^LogCode-\\d{3}$")) {
            return ResponseEntity.badRequest().body("Invalid LogCode");
        }
        try {
            // Extract file type (e.g., image/png or image/jpeg)
            String contentType = observedImage.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File is not a valid image.");
            }
            long maxFileSize = 5 * 1024 * 1024; // 5 MB
            if (observedImage.getSize() > maxFileSize) {
                return ResponseEntity.badRequest().body("Image size exceeds 5 MB.");
            }

            // Convert MultipartFile to Base64
            String base64Image = "data:" + observedImage.getContentType() + ";base64," +
                    Base64.getEncoder().encodeToString(observedImage.getBytes());


            // Create DTO and populate fields
            MonitorDTO monitorLogDTO = new MonitorDTO();
            monitorLogDTO.setLogCode(LogCode);
            monitorLogDTO.setDate(date);
            monitorLogDTO.setLogDetails(logDetails);
            monitorLogDTO.setObservedImage(base64Image);
            monitorLogDTO.setStaffId(staffId);

            // Save using a service
            monitoringServiceIMPL.saveMonitorLog(monitorLogDTO);

            return ResponseEntity.ok("Monitor log saved successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the request.");
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Bad request: Invalid data", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{LogCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelectedLog(@PathVariable("LogCode") String LogCode) {
        if (!LogCode.matches("^LogCode-\\d{3}$")) {
            return ResponseEntity.badRequest().body("Invalid LogCode");
        }
        try {
            // Fetch the log using the service
            MonitorLogEntity logEntity = monitoringServiceIMPL.getSelectedLog(LogCode);

            // Check if logEntity is null
            if (logEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("LogCode not found.");
            }
            double imageSizeMB = monitoringServiceIMPL.calculateImageSizeInMB(logEntity.getObservedImage());
            String staffId = (logEntity.getStaff() != null) ? logEntity.getStaff().getStaffId() : null;

            // Format the response
            Map<String, Object> response = new HashMap<>();
            response.put("LogCode", logEntity.getLogCode());
            response.put("date", logEntity.getDate());
            response.put("logDetails", logEntity.getLogDetails());
            response.put("observedImage", String.format("%.3fMB", imageSizeMB));
            response.put("staffId", staffId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllMonitorLogs() {
        try {
            // Fetch all monitor logs from the service
            List<Map<String, Object>> monitorLogs = monitoringServiceIMPL.getAllMonitorLogs();

            if (monitorLogs.isEmpty()) {
                return ResponseEntity.noContent().build();  // Return 204 No Content if no logs are found
            }

            return ResponseEntity.ok(monitorLogs);  // Return 200 OK with the list of formatted logs
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((List<Map<String, Object>>) Collections.singletonMap("error", "An error occurred while fetching monitor logs."));
        }
    }

    @PutMapping(value = "/{LogCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMonitorLog(
            @PathVariable("LogCode") String LogCode,
            @RequestPart("date") String date,
            @RequestPart("logDetails") String logDetails,
            @RequestPart("observedImage") MultipartFile observedImage) {
        try {
            String contentType = observedImage.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File is not a valid image.");
            }
            String base64Image = "data:" + observedImage.getContentType() + ";base64," +
                    Base64.getEncoder().encodeToString(observedImage.getBytes());

            MonitorDTO monitorDTO = new MonitorDTO();
            monitorDTO.setLogCode(LogCode);
            monitorDTO.setDate(date);
            monitorDTO.setLogDetails(logDetails);
            monitorDTO.setObservedImage(base64Image);
            monitoringServiceIMPL.updateMonitorLog(monitorDTO);
            return new ResponseEntity<>("MoniotrLog Updated", HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Not Updated", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{LogCode}")
    public ResponseEntity<String> deleteMonitoringLog(@PathVariable("LogCode") String LogCode) {
        if (!LogCode.matches("^LogCode-\\d{3}$")) {
            return ResponseEntity.badRequest().body("Invalid LogCode");
        }
        try {
            // Call the service method to delete the log
            boolean isDeleted = monitoringServiceIMPL.deleteMonitoringLog(LogCode);

            if (isDeleted) {
                return ResponseEntity.ok("MonitorLog deleted successfully. StaffId relationships remain intact.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("LogCode not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the MonitorLog.");
        }
    }
}
