package lk.ijse.cropMonitoringSystem.service;

import lk.ijse.cropMonitoringSystem.DTO.CropDTO;
import lk.ijse.cropMonitoringSystem.entity.CropEntity;
import lk.ijse.cropMonitoringSystem.repository.CropRepo;
import lk.ijse.cropMonitoringSystem.repository.FieldRepository;
import lk.ijse.cropMonitoringSystem.repository.MonitoringRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class CropService {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private MonitoringRepo monitoringRepo;
    @Autowired
    private CropRepo cropRepo;

    public void saveCrop(CropDTO cropDTO, MultipartFile cropImage) throws IOException {
        CropEntity cropEntity = new CropEntity();
        cropEntity.setCropCode(cropDTO.getCropCode());
        cropEntity.setCropName(cropDTO.getCropName());

        // Convert the MultipartFile (image) to Base64 and set it

        String base64Image = Base64.getEncoder().encodeToString(cropImage.getBytes());
        cropEntity.setCropImage(base64Image);  // Store as Base64 string
        cropEntity.setCategory(cropDTO.getCategory());
        cropEntity.setCropSeason(cropDTO.getCropSeason());
        cropEntity.setFieldCrops(fieldRepository.findById(cropDTO.getFieldCode()).orElseThrow(
                () -> new RuntimeException("Field not found")
        ));
        if (cropDTO.getLogCode() != null) {
            cropEntity.setMonitorCrop(monitoringRepo.findById(cropDTO.getLogCode()).orElse(null));
        }

        // Save the entity
        cropRepo.save(cropEntity);
    }
    public CropEntity getSelectedCrop(String cropCode) {

        return cropRepo.findById(cropCode).orElse(null);
    }
    public List<CropEntity> getAllCrops() {
        return cropRepo.findAll(); // Retrieves all crop entities
    }
    //update crop
    public void updateCrop(CropDTO cropDTO, MultipartFile cropImage, double imageSizeMB) {
        CropEntity cropEntity = cropRepo.findById(cropDTO.getCropCode())
                .orElseThrow(() -> new RuntimeException("Crop not found with code: " + cropDTO.getCropCode()));

        cropEntity.setCropName(cropDTO.getCropName());
        cropEntity.setCategory(cropDTO.getCategory());
        cropEntity.setCropSeason(cropDTO.getCropSeason());

        // Update field reference
        cropEntity.setFieldCrops(fieldRepository.findById(cropDTO.getFieldCode()).orElseThrow(
                () -> new RuntimeException("Field not found with code: " + cropDTO.getFieldCode())
        ));

        // Update monitor reference if provided
        if (cropDTO.getLogCode() != null) {
            cropEntity.setMonitorCrop(monitoringRepo.findById(cropDTO.getLogCode()).orElse(null));
        }

        // Update crop image
        if (cropImage != null && !cropImage.isEmpty()) {
            cropEntity.setCropImage(String.format("%.2f MB", imageSizeMB));
        }

        cropRepo.save(cropEntity);
    }
    public void deleteCrop(String cropCode) {
        CropEntity cropEntity = cropRepo.findById(cropCode)
                .orElseThrow(() -> new RuntimeException("Crop not found with code: " + cropCode));
        cropRepo.delete(cropEntity);
    }

}
