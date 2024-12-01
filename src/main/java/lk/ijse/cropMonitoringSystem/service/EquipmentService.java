package lk.ijse.cropMonitoringSystem.service;

import lk.ijse.cropMonitoringSystem.DTO.EquipmentDTO;
import lk.ijse.cropMonitoringSystem.entity.EquipmentEntity;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.repository.EquipmentRepo;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

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
    //getAll
    public List<EquipmentDTO> getAllEquipment() {
        List<EquipmentEntity> equipmentEntities = equipmentRepo.findAll();
        return equipmentEntities.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private EquipmentDTO convertToDTO(EquipmentEntity entity) {
        return new EquipmentDTO(
                entity.getEquipmentId(),
                entity.getName(),
                entity.getType(),
                entity.getStatus(),
                entity.getStaffEquipment() != null ? entity.getStaffEquipment().getStaffId(): null,
                entity.getFieldEquipment() != null ? entity.getFieldEquipment().getFieldCode() : null
        );
    }
    public EquipmentDTO getEquipmentById(String equipmentId) {
        return equipmentRepo.findById(equipmentId)
                .map(this::convertToDTO)
                .orElse(null);
    }
    //update
    public EquipmentDTO updateEquipment(String equipmentId, EquipmentDTO equipmentDTO) {
        // Find the EquipmentEntity
        Optional<EquipmentEntity> optionalEntity = equipmentRepo.findById(equipmentId);

        if (!optionalEntity.isPresent()) {
            throw new RuntimeException("Equipment not found with ID: " + equipmentId);
        }

        EquipmentEntity existingEntity = optionalEntity.get();

        // Update the DTO
        existingEntity.setName(equipmentDTO.getName());
        existingEntity.setType(equipmentDTO.getType());
        existingEntity.setStatus(equipmentDTO.getStatus());

        //Staff ID  update
        if (equipmentDTO.getStaffId() != null) {
            StaffEntity staff = staffRepo.findById(equipmentDTO.getStaffId()).orElseThrow(
                    () -> new RuntimeException("Staff not found with ID: " + equipmentDTO.getStaffId()));
            existingEntity.setStaffEquipment(staff);
        }

        // Field Code  update
        if (equipmentDTO.getFieldCode() != null) {
            FieldEntity field = fieldRepo.findById(equipmentDTO.getFieldCode()).orElseThrow(
                    () -> new RuntimeException("Field not found with code: " + equipmentDTO.getFieldCode()));
            existingEntity.setFieldEquipment(field);
        }

        // Save the updated entity return updated DTO
        EquipmentEntity updatedEntity = equipmentRepo.save(existingEntity);
        return convertToDTO(updatedEntity);
    }
    public void deleteEquipment(String equipmentId) {
        Optional<EquipmentEntity> optionalEntity = equipmentRepo.findById(equipmentId);
        if (!optionalEntity.isPresent()) {
            throw new RuntimeException("Equipment not found with ID: " + equipmentId);
        }
        equipmentRepo.delete(optionalEntity.get());
    }
}
