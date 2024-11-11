package lk.ijse.cropMonitoringSystem.api;

import lk.ijse.cropMonitoringSystem.DTO.FieldDTO;
import lk.ijse.cropMonitoringSystem.DTO.StaffDTO;
import lk.ijse.cropMonitoringSystem.exception.DataPersistException;
import lk.ijse.cropMonitoringSystem.service.FieldService;
import lk.ijse.cropMonitoringSystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fields")
public class FieldController {
    @Autowired
    private FieldService fieldService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveField(@RequestBody FieldDTO fieldDTO) {
        try {
            fieldService.saveField(fieldDTO);
            return new ResponseEntity<>("Field and staff data saved successfully",HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Bad request: Invalid data",HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{fieldCode}")
    public ResponseEntity<FieldDTO> getField(@PathVariable String fieldCode) {
        FieldDTO fieldDTO = fieldService.getSelectedStaffAndField(fieldCode);
        return new ResponseEntity<>(fieldDTO, HttpStatus.OK);
    }
    @GetMapping("/details/{fieldCode}")
    public ResponseEntity<FieldDTO> getFieldDetails(@PathVariable String fieldCode) {
        FieldDTO fieldDetails = fieldService.getOnlySelectedField(fieldCode);
        return new ResponseEntity<>(fieldDetails, HttpStatus.OK);
    }
    @PutMapping("/{fieldCode}")
    public ResponseEntity<FieldDTO> updateFieldAndStaff(@PathVariable String fieldCode, @RequestBody FieldDTO fieldDTO) {
        try {
            FieldDTO updatedField = fieldService.updateFieldAndStaff(fieldCode, fieldDTO);
            return new ResponseEntity<>(updatedField, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{fieldCode}")
    public ResponseEntity<Void> deleteFieldAndStaff(@PathVariable String fieldCode) {
        try {
            fieldService.deleteFieldAndStaff(fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>((HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
