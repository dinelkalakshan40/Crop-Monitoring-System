package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.EquipmentDTO;
import lk.ijse.cropMonitoringSystem.service.impl.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("api/v1/equipments")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        //check validation
        if (!isValidEquipmentId(equipmentDTO.getEquipmentId())) {
            return ResponseEntity.badRequest().body("Invalid EquipmentId format. It should be 'EqID-00'.");
        }
        try {
            equipmentService.saveEquipment(equipmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Equipment saved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save equipment.");
        }
    }
    private boolean isValidEquipmentId(String equipmentId) {
        return equipmentId != null && equipmentId.matches("EqID-\\d+");
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllEquipment() {
        try {
            List<EquipmentDTO> equipmentList = equipmentService.getAllEquipment();
            return ResponseEntity.ok(equipmentList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch equipment data."));
        }
    }
    @GetMapping(value = "/{equipmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelectedEquipment(@PathVariable String equipmentId) {
        try {
            // Validate equipmentId
            if (!equipmentId.matches("^EqID-\\d{3}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid equipment ID format'"));
            }

            // Fetch equipment by ID
            EquipmentDTO equipmentDTO = equipmentService.getEquipmentById(equipmentId);
            if (equipmentDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Equipment not found with ID: " + equipmentId));
            }

            // Return the equipment data
            return ResponseEntity.ok(equipmentDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch equipment data."));
        }

    }
    @PutMapping(value = "/{equipmentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEquipment(@PathVariable("equipmentId") String equipmentId, @RequestBody EquipmentDTO equipmentDTO) {

        try {
            // Validate equipmentId
            if (!equipmentId.matches("^EqID-\\d{3}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(String.valueOf(Map.of("error", "Invalid equipment ID format'")));
            }
            // Call service to update the equipment based on the provided ID
            equipmentService.updateEquipment(equipmentId, equipmentDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Equipment updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update equipment.");
        }
    }
    @DeleteMapping(value = "/{equipmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEquipment(@PathVariable("equipmentId") String equipmentId) {
        try {
            if (!equipmentId.matches("^EqID-\\d{3}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(String.valueOf(Map.of("error", "Invalid equipment ID format'")));
            }
            // Call service to delete the equipment by its ID
            equipmentService.deleteEquipment(equipmentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Equipment deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete equipment.");
        }
    }
}
