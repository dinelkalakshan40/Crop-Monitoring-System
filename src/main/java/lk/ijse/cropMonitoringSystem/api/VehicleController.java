package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.VehicleDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

    @PostMapping
    public void saveVehicle(VehicleDTO vehicleDTO){
        
    }
}
