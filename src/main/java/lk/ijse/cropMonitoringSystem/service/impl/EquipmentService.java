package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.EquipmentDTO;
import lk.ijse.cropMonitoringSystem.entity.EquipmentEntity;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.repository.EquipmentRepo;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EquipmentService {
    @Autowired
    private EquipmentRepo equipmentRepo;

    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private FieldRepository fieldRepo;

    @Transactional
    public void saveEquipment(EquipmentDTO equipmentDTO) {

        EquipmentEntity equipmentEntity = new EquipmentEntity();
        equipmentEntity.setEquipmentId(equipmentDTO.getEquipmentId());
        equipmentEntity.setName(equipmentDTO.getName());
        equipmentEntity.setType(equipmentDTO.getType());
        equipmentEntity.setStatus(equipmentDTO.getStatus());

        /*equipmentEntity.setStaff(staffRepo.findById(equipmentDTO.getStaffId()).orElseThrow(
                () -> new RuntimeException("Staff not found")
        ));
        equipmentEntity.setField(fieldRepo.findById(equipmentDTO.getFieldCode()).orElseThrow(
                () -> new RuntimeException("Field not found")
        ));*/

        // StaffEntity
        StaffEntity staff = staffRepo.findById(equipmentDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + equipmentDTO.getStaffId()));
        equipmentEntity.setStaffEquipment(staff);
        //FieldEntity

        FieldEntity field = fieldRepo.findById(equipmentDTO.getFieldCode())
                .orElseThrow(() -> new RuntimeException("Field not found with code: " + equipmentDTO.getFieldCode()));
        equipmentEntity.setFieldEquipment(field);
        equipmentRepo.save(equipmentEntity);
    }
}
