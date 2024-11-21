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


}
