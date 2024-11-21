package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.VehicleDTO;
import lk.ijse.cropMonitoringSystem.entity.VehicleEntity;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import lk.ijse.cropMonitoringSystem.repository.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleService {
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private VehicleRepo vehicleRepo;

    public void savedVehicle(VehicleDTO vehicleDTO) {
        VehicleEntity entity =new VehicleEntity();
        entity.setVehicleCode(vehicleDTO.getVehicleCode());
        entity.setPlateNumber(vehicleDTO.getPlateNumber());
        entity.setCategory(vehicleDTO.getCategory());
        entity.setFuelType(vehicleDTO.getFuelType());
        entity.setStatus(vehicleDTO.getStatus());
        entity.setRemarks(vehicleDTO.getRemarks());
        entity.setStaff(staffRepo.findById(vehicleDTO.getStaffId()).orElseThrow(
                () -> new RuntimeException("Staff ID " + vehicleDTO.getStaffId() + " not found")
        ));
        vehicleRepo.save(entity);

    }
}
