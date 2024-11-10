package lk.ijse.cropMonitoringSystem.util;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.entity.FieldEntity;
import lk.ijse.cropMonitoringSystem.entity.StaffEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Mapping {
    @Autowired
    public ModelMapper modelMapper;




    public StaffEntity toStaffEntity(StaffDTO staffDTO){
        return modelMapper.map(staffDTO,StaffEntity.class);
    }

}
