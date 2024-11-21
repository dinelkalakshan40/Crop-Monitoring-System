package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceIMPL {
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    public void saveStaff(StaffDTO staffDTO) {
        // Convert StaffDTO to StaffEntity
        StaffEntity staffEntity = modelMapper.map(staffDTO, StaffEntity.class);

        // Initialize the list to hold all field associations
        List<FieldEntity> fieldEntities = staffDTO.getFields().stream()
                .map(fieldDTO -> {
                    // Convert FieldDTO to FieldEntity
                    FieldEntity fieldEntity = modelMapper.map(fieldDTO, FieldEntity.class);

                    // Check if the field already exists in the database
                    return fieldRepository.findById(fieldEntity.getFieldCode())
                            .orElse(fieldEntity); // Use the existing entity if found, otherwise create a new one
                })
                .collect(Collectors.toList());

        // Associate the staff with the fields
        staffEntity.setFields(fieldEntities);

        // Update each field entity to include the current staff in its staff list
        fieldEntities.forEach(field -> {
            // Initialize staff list if null
            if (field.getStaff() == null) {
                field.setStaff(new ArrayList<>());
            }
            // Avoid duplicate association
            if (!field.getStaff().contains(staffEntity)) {
                field.getStaff().add(staffEntity);
            }
        });

        // Save the staff entity, which will save the association with fields
        staffRepo.save(staffEntity);
    }
    public List<StaffDTO> getAllStaff() {
        // Fetch all staff entities
        List<StaffEntity> staffEntities = staffRepo.findAll();

        // Map to StaffDTO with only the required fields
        return staffEntities.stream()
                .map(staff -> {
                    StaffDTO dto = new StaffDTO();
                    dto.setStaffId(staff.getStaffId());
                    dto.setFirstName(staff.getFirstName());
                    dto.setLastName(staff.getLastName());
                    dto.setDesignation(staff.getDesignation());
                    dto.setGender(String.valueOf(staff.getGender()));
                    dto.setDob(staff.getDob());
                    dto.setJoinedDate(staff.getJoinedDate());
                    dto.setAddress(staff.getAddress());
                    dto.setContact(staff.getContact());
                    dto.setRole(String.valueOf(staff.getRole()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
