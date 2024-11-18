package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.CropDTO;
import lk.ijse.cropMonitoringSystem.entity.CropEntity;
import lk.ijse.cropMonitoringSystem.service.impl.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/crops")
public class CropController {

    private static final String CROP_CODE_REGEX = "^Crop-Id-\\d{3}$";

    @Autowired
    private CropService cropService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveCrop(
            @RequestPart("cropCode") String cropCode,
            @RequestPart("cropName") String cropName,
            @RequestPart("cropImage") MultipartFile cropImage,
            @RequestPart("category") String category,
            @RequestPart("cropSeason") String cropSeason,
            @RequestPart("fieldCode") String fieldCode,
            @RequestPart(value = "logCode", required = false) String logCode) {
        try {
            if (!isValidCropCode(cropCode)) {
                return ResponseEntity.badRequest().body("Invalid CropCode format. It should be 'Crop-Id-00'");
            }
            // Validate  image
            String contentType = cropImage.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File is not a valid image.");
            }
            // Create a DTO to pass to  service
            CropDTO cropDTO = new CropDTO();
            cropDTO.setCropCode(cropCode);
            cropDTO.setCropName(cropName);
            cropDTO.setCategory(category);
            cropDTO.setCropSeason(cropSeason);
            cropDTO.setFieldCode(fieldCode);
            cropDTO.setLogCode(logCode);

            // Save the crop data
            cropService.saveCrop(cropDTO, cropImage);

            return ResponseEntity.status(HttpStatus.CREATED).body("Crop saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save crop.");
        }
    }

    // Method to validate CropCode using regex
    private boolean isValidCropCode(String cropCode) {
        Pattern pattern = Pattern.compile(CROP_CODE_REGEX);
        Matcher matcher = pattern.matcher(cropCode);
        return matcher.matches();
    }

    @GetMapping(value = "/{cropCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelectedCrop(@PathVariable("cropCode") String cropCode) {
        if (!isValidCropCode(cropCode)) {
            return ResponseEntity.badRequest().body("Invalid CropCode format. It should be 'Crop-Id-00'");
        }
        try {
            CropEntity cropEntity = cropService.getSelectedCrop(cropCode);
            if (cropEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop not found for the provided CropCode.");
            }

            // Calculate image size in MB
            double imageSizeMB = cropEntity.getCropImage() != null
                    ? cropEntity.getCropImage().length() / (1024.0 * 1024.0)
                    : 0;

            // Prepare the response
            Map<String, Object> response = new HashMap<>();
            response.put("cropCode", cropEntity.getCropCode());
            response.put("cropName", cropEntity.getCropName());
            response.put("imageSizeMB", String.format("%.3f MB", imageSizeMB));
            response.put("category", cropEntity.getCategory());
            response.put("cropSeason", cropEntity.getCropSeason());
            response.put("fieldCode", cropEntity.getFieldCrops() != null ? cropEntity.getFieldCrops().getFieldCode() : null);
            response.put("logCode", cropEntity.getMonitorCrop() != null ? cropEntity.getMonitorCrop().getLogCode() : null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the crop details.");
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CropDTO>> getAllCrops() {
        try {
            List<CropDTO> crops = cropService.getAllCrops().stream().map(cropEntity -> {
                CropDTO cropDTO = new CropDTO();
                cropDTO.setCropCode(cropEntity.getCropCode());
                cropDTO.setCropName(cropEntity.getCropName());
                cropDTO.setCategory(cropEntity.getCategory());
                cropDTO.setCropSeason(cropEntity.getCropSeason());
                cropDTO.setFieldCode(cropEntity.getFieldCrops().getFieldCode());
                if (cropEntity.getMonitorCrop() != null) {
                    cropDTO.setLogCode(cropEntity.getMonitorCrop().getLogCode());
                }
                if (cropEntity.getCropImage() != null) {
                    double sizeInBytes = cropEntity.getCropImage().length();
                    cropDTO.setCropImage(String.format("%.3f MB", sizeInBytes / (1024 * 1024)));
                }
                return cropDTO;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(crops);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping(value = "/{cropCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCrop(
            @PathVariable("cropCode") String cropCode,
            @RequestPart("cropName") String cropName,
            @RequestPart("category") String category,
            @RequestPart("cropSeason") String cropSeason,
            @RequestPart(value = "cropImage", required = false) MultipartFile cropImage,
            @RequestPart("fieldCode") String fieldCode,
            @RequestPart(value = "logCode", required = false) String logCode) {
        if (!isValidCropCode(cropCode)) {
            return ResponseEntity.badRequest().body("Invalid CropCode format. It should be 'Crop-Id-00'.");
        }
        try {
            // Validate image if present
            double imageSizeMB = 0.0;
            if (cropImage != null && !cropImage.isEmpty()) {
                String contentType = cropImage.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body("Invalid image file format.");
                }
                imageSizeMB = (double) cropImage.getSize() / (1024 * 1024);
            }

            // Create CropDTO
            CropDTO cropDTO = new CropDTO();
            cropDTO.setCropCode(cropCode);
            cropDTO.setCropName(cropName);
            cropDTO.setCategory(category);
            cropDTO.setCropSeason(cropSeason);
            cropDTO.setFieldCode(fieldCode);
            cropDTO.setLogCode(logCode);

            // Update the crop
            cropService.updateCrop(cropDTO, cropImage, imageSizeMB);
            return ResponseEntity.ok("Crop updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update crop.");
        }
    }
    @DeleteMapping(value = "/{cropCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCrop(@PathVariable("cropCode") String cropCode) {
        if (!isValidCropCode(cropCode)) {
            return ResponseEntity.badRequest().body("Invalid CropCode format. It should be 'Crop-Id-00'.");
        }
        try {
            cropService.deleteCrop(cropCode);
            return ResponseEntity.ok("Crop deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete crop.");
        }
    }



}
