package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.VehicleDTO;
import lk.ijse.cropMonitoringSystem.service.impl.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVehicle(@RequestBody VehicleDTO vehicleDTO){
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
}
