package lk.ijse.cropMonitoringSystem.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.cropMonitoringSystem.DTO.CropDTO;
import lk.ijse.cropMonitoringSystem.DTO.MonitorDTO;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.service.impl.MonitoringServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

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
            @RequestPart(value = "staffId", required = false) String staffId ) {
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
            return new ResponseEntity<>("Bad request: Invalid data",HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
