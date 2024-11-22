package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.service.impl.StaffServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/staff")
public class StaffController {
    @Autowired
    private StaffServiceIMPL staffService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@RequestBody StaffDTO staffDTO){
        if (!staffDTO.getStaffId().matches("^STF-\\d{3}$")) {
            return (ResponseEntity<Void>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        try {
            staffService.saveStaff(staffDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        try {
            List<StaffDTO> staffDTOList = staffService.getAllStaff(); // Call service method to fetch all staff
            return ResponseEntity.ok(staffDTOList); // Return the list of staff DTOs
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>()); // Return empty list in case of failure
        }
    }
    @GetMapping(value = "/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffDTO> getSelectedStaff(@PathVariable String staffId) {
        if (!staffId.matches("^STF-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            StaffDTO staffDTO = staffService.getSelectedStaff(staffId); // Fetch staff by ID
            if (staffDTO != null) {
                return ResponseEntity.ok(staffDTO); // Return the staff details
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Return 404 if staff not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 in case of failure
        }
    }
    @PutMapping(value = "/{staffId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateStaff(@PathVariable String staffId, @RequestBody StaffDTO staffDTO) {
        if (!staffId.matches("^STF-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            boolean isUpdated = staffService.updateStaff(staffId, staffDTO);
            if (isUpdated) {
                return ResponseEntity.ok("Staff updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Staff with ID " + staffId + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update staff.");
        }
    }
    @DeleteMapping("/{staffId}")
    public ResponseEntity<String> deleteStaff(@PathVariable String staffId) {
        if (!staffId.matches("^STF-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            staffService.deleteStaffByStaffId(staffId);
            return ResponseEntity.ok("Staff deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Staff member not found with ID: " + staffId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete staff.");
        }
    }
}
