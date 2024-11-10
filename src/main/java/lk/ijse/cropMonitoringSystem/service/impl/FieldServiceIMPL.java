package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import lk.ijse.cropMonitoringSystem.service.FieldService;
import lk.ijse.cropMonitoringSystem.util.AppUtil;
import lk.ijse.cropMonitoringSystem.util.Mapping;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FieldServiceIMPL implements FieldService {
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private StaffRepo staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void saveField(FieldDTO fieldDTO) {
        fieldDTO.setFieldCode(AppUtil.generateFieldId());
        // Convert FieldDTO to FieldEntity
        FieldEntity fieldEntity = modelMapper.map(fieldDTO, FieldEntity.class);

        // Map and associate StaffEntity objects
        List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                .map(staffDTO -> {
                    staffDTO.setStaffId(AppUtil.generateStaffId());
                    // Convert StaffDTO to StaffEntity
                    StaffEntity staffEntity = modelMapper.map(staffDTO, StaffEntity.class);

                    // Check if the staff already exists in the database
                    return staffRepository.findById(staffEntity.getStaffId())
                            .orElse(staffEntity); // Use the existing entity if found, or the new one
                })
                .collect(Collectors.toList());

        // Set staff list in the field entity
        fieldEntity.setStaff(staffEntities);

        // Save the field entity along with its associated staff
        fieldRepository.save(fieldEntity);
    }

    @Override
    public FieldDTO getSelectedStaffAndField(String fieldCode) {
        if (fieldRepository.existsById(fieldCode)) {

            FieldEntity fieldEntity = fieldRepository.getReferenceById(fieldCode);
            // Convert FieldEntity to FieldDTO
            return modelMapper.map(fieldEntity, FieldDTO.class);
        } else {
            throw new RuntimeException("Field with code " + fieldCode + " not found");
        }
    }
    @Override
    public FieldDTO getOnlySelectedField(String fieldCode){
        if (fieldRepository.existsById(fieldCode)) {
            FieldEntity fieldEntity = fieldRepository.getReferenceById(fieldCode);

            // Custom mapping where we map the FieldEntity to FieldDTO excluding staff
            FieldDTO fieldDTO = modelMapper.map(fieldEntity, FieldDTO.class);

            // Instead of setting staff to null, we skip the staff field entirely
            fieldDTO.setStaff(null); // If needed

            return fieldDTO;
        } else {
            throw new RuntimeException("Field with code " + fieldCode + " not found");
        }
    }
    @Override
    public FieldDTO updateFieldAndStaff(String fieldCode, FieldDTO fieldDTO) {
        // Fetch the FieldEntity by fieldCode
        Optional<FieldEntity> existingFieldOptional = fieldRepository.findById(fieldCode);

        if (!existingFieldOptional.isPresent()) {
            throw new RuntimeException("Field with code " + fieldCode + " not found");
        }

        FieldEntity existingField = existingFieldOptional.get();

        // Update the FieldEntity attributes
        existingField.setFieldName(fieldDTO.getFieldName());
        existingField.setFieldLocation(fieldDTO.getFieldLocation());
        existingField.setFieldSize(fieldDTO.getFieldSize());
        existingField.setFieldImage1(fieldDTO.getFieldImage1());
        existingField.setFieldImage2(fieldDTO.getFieldImage2());

        // Update staff details (if provided in the DTO)
        List<StaffDTO> staffDTOs = fieldDTO.getStaff();
        if (staffDTOs != null) {
            List<StaffEntity> updatedStaffList = new ArrayList<>();

            for (StaffDTO staffDTO : staffDTOs) {
                // Fetch the StaffEntity by staffId
                Optional<StaffEntity> existingStaffOptional = staffRepository.findById(staffDTO.getStaffId());
                if (existingStaffOptional.isPresent()) {
                    StaffEntity existingStaff = existingStaffOptional.get();

                    // Update StaffEntity details (you can add more fields as needed)
                    existingStaff.setFirstName(staffDTO.getFirstName());
                    existingStaff.setLastName(staffDTO.getLastName());
                    existingStaff.setDesignation(staffDTO.getDesignation());

                    existingStaff.setDob(staffDTO.getDob());
                    existingStaff.setJoinedDate(staffDTO.getJoinedDate());
                    existingStaff.setAddress(staffDTO.getAddress());
                    existingStaff.setContact(staffDTO.getContact());
                //    existingStaff.setRole(staffDTO.getRole());

                    // Save the updated StaffEntity
                    staffRepository.save(existingStaff);
                    updatedStaffList.add(existingStaff); // Add to the updated list
                } else {
                    throw new RuntimeException("Staff with ID " + staffDTO.getStaffId() + " not found");
                }
            }
            // Set the updated staff list to the FieldEntity
            existingField.setStaff(updatedStaffList);
        }
        // Save the updated FieldEntity
        fieldRepository.save(existingField);

        // Return the updated FieldDTO
        return modelMapper.map(existingField, FieldDTO.class);
    }
}
