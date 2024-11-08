package lk.ijse.cropMonitoringSystem.service.impl;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.service.FieldService;
import lk.ijse.cropMonitoringSystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FieldServiceIMPL implements FieldService {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveField(FieldDTO fieldDTO){
        FieldEntity saved=fieldRepository.save(mapping.toFieldEntity(fieldDTO));
        if (saved==null){
            throw new DataPersistException("Field Not Saved");
        }
    }
}
