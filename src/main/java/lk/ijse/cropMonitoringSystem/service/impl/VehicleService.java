package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.VehicleDTO;
import lk.ijse.cropMonitoringSystem.entity.VehicleEntity;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import lk.ijse.cropMonitoringSystem.repository.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<VehicleDTO> getAllVehicles() {

        List<VehicleEntity> entities = vehicleRepo.findAll();
        return entities.stream().map(entity -> {
            VehicleDTO dto = new VehicleDTO();
            dto.setVehicleCode(entity.getVehicleCode());
            dto.setPlateNumber(entity.getPlateNumber());
            dto.setCategory(entity.getCategory());
            dto.setFuelType(entity.getFuelType());
            dto.setStatus(entity.getStatus());
            dto.setRemarks(entity.getRemarks());
            dto.setStaffId(entity.getStaff() != null ? entity.getStaff().getStaffId(): "N/A");
            return dto;
        }).collect(Collectors.toList());
    }
    public VehicleDTO  getSelectedVehicle(String vehicleCode) {
        VehicleEntity entity = vehicleRepo.findById(vehicleCode)
                .orElseThrow(() -> new RuntimeException("Vehicle with code " + vehicleCode + " not found"));
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleCode(entity.getVehicleCode());
        dto.setPlateNumber(entity.getPlateNumber());
        dto.setCategory(entity.getCategory());
        dto.setFuelType(entity.getFuelType());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setStaffId(entity.getStaff() != null ? entity.getStaff().getStaffId() : "Staff ID is Null");
        return dto;
    }
    public void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO) {
        VehicleEntity entity = vehicleRepo.findById(vehicleCode)
                .orElseThrow(() -> new RuntimeException("Vehicle with code " + vehicleCode + " not found"));

        entity.setPlateNumber(vehicleDTO.getPlateNumber());
        entity.setCategory(vehicleDTO.getCategory());
        entity.setFuelType(vehicleDTO.getFuelType());
        entity.setStatus(vehicleDTO.getStatus());
        entity.setRemarks(vehicleDTO.getRemarks());
        if (vehicleDTO.getStaffId() != null) {
            entity.setStaff(staffRepo.findById(vehicleDTO.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff with ID " + vehicleDTO.getStaffId() + " not found")));
        }

        vehicleRepo.save(entity);
    }
}
