package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.VehicleDTO;
import lk.ijse.cropMonitoringSystem.service.impl.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVehicle(@RequestBody VehicleDTO vehicleDTO){
        if (!vehicleDTO.getVehicleCode().matches("^VEH-00\\\\d*$")) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid vehicle code format. Expected format: VEH-00");
        }
        try {
            vehicleService.savedVehicle(vehicleDTO);
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
        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}
