package lk.ijse.cropMonitoringSystem.service;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.MonitorLogEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.MonitoringRepo;
import lk.ijse.cropMonitoringSystem.repository.StaffRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FieldServiceIMPL  {
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private StaffRepo staffRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MonitoringRepo monitoringRepo;

    @Transactional
    public void saveField(FieldDTO fieldDTO) {

        // Convert FieldDTO to FieldEntity
        FieldEntity fieldEntity = modelMapper.map(fieldDTO, FieldEntity.class);

        // Map and associate StaffEntity objects
        List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                .map(staffDTO -> {

                    // Convert StaffDTO to StaffEntity
                    StaffEntity staffEntity = modelMapper.map(staffDTO, StaffEntity.class);

                    // Check if the staff already exists in the database
                    return staffRepository.findById(staffEntity.getStaffId())
                            .orElse(staffEntity); // Use the existing entity if found, or the new one
                })
                .collect(Collectors.toList());

        // Set staff list in the field entity
        fieldEntity.setStaff(staffEntities);

        if (fieldDTO.getLogCode() != null) {
            MonitorLogEntity logEntity = monitoringRepo.findById(fieldDTO.getLogCode())
                    .orElseThrow(() -> new RuntimeException("Log not found for LogCode: " + fieldDTO.getLogCode()));
            fieldEntity.setMonitor_log(logEntity); // Assuming a relation exists in FieldEntity
        }

        // Save the field entity along with its associated staff
        fieldRepository.save(fieldEntity);
    }

    public List<Map<String, Object>> getAllFields() {
        List<FieldEntity> fieldEntities = fieldRepository.findAll();

        return fieldEntities.stream()
                .map(field -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("fieldCode", field.getFieldCode());
                    response.put("fieldName", field.getFieldName());
                    response.put("fieldLocation", field.getFieldLocation());
                    response.put("fieldSize", field.getFieldSize());
                    response.put("fieldImage1", field.getFieldImage1());
                    response.put("fieldImage2", field.getFieldImage2());
                    if (field.getMonitor_log() != null) {
                        response.put("logCode", field.getMonitor_log().getLogCode());
                    } else {
                        response.put("logCode", null); // Handle cases where no log is associated
                    }
                    response.put("staffIds", field.getStaff().stream()
                            .map(StaffEntity::getStaffId)
                            .collect(Collectors.toList()));
                    return response;
                })
                .collect(Collectors.toList());
    }
    public String getLastFieldCode() {
        // Query database for the last field code
        return fieldRepository.findLastFieldCode();
    }
    public String generateNextFieldCode() {

        String lastFieldCode = getLastFieldCode();


        if (lastFieldCode != null && lastFieldCode.startsWith("FLD-")) {

            String numericPart = lastFieldCode.substring(4);


            int lastIdNumber = Integer.parseInt(numericPart);

            // Increment the number
            lastIdNumber++;

            return String.format("FLD-%04d", lastIdNumber);
        } else {

            return "FLD-0001";
        }
    }

    public FieldDTO getSelectedStaffAndField(String fieldCode) {
        if (fieldRepository.existsById(fieldCode)) {

            FieldEntity fieldEntity = fieldRepository.getReferenceById(fieldCode);
            // Convert FieldEntity to FieldDTO
            return modelMapper.map(fieldEntity, FieldDTO.class);
        } else {
            throw new RuntimeException("Field with code " + fieldCode + " not found");
        }
    }

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

    public FieldDTO updateFieldAndStaff(String fieldCode, FieldDTO fieldDTO) {
        // Fetch the FieldEntity by fieldCode
        Optional<FieldEntity> existingFieldOptional = fieldRepository.findById(fieldCode);

        if (!existingFieldOptional.isPresent()) {
            throw new RuntimeException("Field with code " + fieldCode + " not found");
        }

        FieldEntity existingField = existingFieldOptional.get();

        // Update the FieldEntity
        existingField.setFieldName(fieldDTO.getFieldName());
        existingField.setFieldLocation(fieldDTO.getFieldLocation());
        existingField.setFieldSize(fieldDTO.getFieldSize());
        existingField.setFieldImage1(fieldDTO.getFieldImage1());
        existingField.setFieldImage2(fieldDTO.getFieldImage2());

        // Update staff details (if provided in the DTO)
       /* List<StaffDTO> staffDTOs = fieldDTO.getStaff();
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
        }*/
        // Save the updated FieldEntity
        fieldRepository.save(existingField);

        // Return the updated FieldDTO
        return modelMapper.map(existingField, FieldDTO.class);
    }

    public void deleteFieldAndStaff(String fieldCode) {
        Optional<FieldEntity> field = fieldRepository.findById(fieldCode);
        if (field.isPresent()) {
            fieldRepository.deleteById(fieldCode);
        } else {
            throw new RuntimeException("Field not found with code: " + fieldCode);
        }
    }

    public List<StaffDTO> getOnlySelectedFiled(String fieldCode) {
        FieldEntity fieldEntity = fieldRepository.findById(fieldCode)
                .orElseThrow(() -> new EntityNotFoundException("Field not found with code: " + fieldCode));

        // Map StaffEntity to StaffDTO
        return fieldEntity.getStaff().stream()
                .map(staff -> modelMapper.map(staff, StaffDTO.class))
                .collect(Collectors.toList());
    }
}
