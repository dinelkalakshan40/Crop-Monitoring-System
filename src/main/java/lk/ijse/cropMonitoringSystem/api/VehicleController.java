package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.VehicleDTO;
import lk.ijse.cropMonitoringSystem.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("api/v1/vehicle")
public class VehicleController {
    private static Logger logger= LoggerFactory.getLogger(VehicleController.class);
    @Autowired
    private VehicleService vehicleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVehicle(@RequestBody VehicleDTO vehicleDTO){
        logger.info("saveVehicle method called");
        if (!vehicleDTO.getVehicleCode().matches("^VEH-\\d{3}$")) {
            logger.info("saved vehicle validation successful");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid vehicle code format. Expected format: VEH-00");
        }
        try {
            vehicleService.savedVehicle(vehicleDTO);
            logger.info("Vehicle saved successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Vehicle saved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save vehicle.");
        }
        
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        logger.info("getAllVehicles method called");
        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
    @GetMapping(value = "/{vehicleCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVehicle(@PathVariable String vehicleCode) {
        logger.info("getVehicle method called");
        if (!vehicleCode.matches("^VEH-\\d{3}$")) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid vehicle code format. Expected format: VEH-00");
        }
        try {
            VehicleDTO vehicle = vehicleService.getSelectedVehicle(vehicleCode);
            return ResponseEntity.ok(vehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while retrieving the vehicle."));
        }
    }
    @PutMapping(value = "/{vehicleCode}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVehicle(
            @PathVariable String vehicleCode,
            @RequestBody VehicleDTO vehicleDTO) {
        logger.info("updateVehicle method called");

        if (!vehicleDTO.getVehicleCode().matches("^VEH-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid vehicle code format"));
        }

        try {
            vehicleService.updateVehicle(vehicleCode, vehicleDTO);
            logger.info("Vehicle updated successfully");
            return ResponseEntity.ok(Map.of("message", "Vehicle updated successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while updating the vehicle."));
        }
    }
    @DeleteMapping(value = "/{vehicleCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteVehicle(@PathVariable String vehicleCode) {
        logger.info("deleteVehicle method called");
        if (!vehicleCode.matches("^VEH-\\d{3}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid vehicle code format"));
        }
        try {
            vehicleService.deleteVehicle(vehicleCode);
            logger.info("Vehicle deleted successfully");
            return ResponseEntity.ok(Map.of("message", "Vehicle deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while deleting the vehicle."));
        }
    }


}
