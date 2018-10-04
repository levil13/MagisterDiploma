package dip.lux.service.impl;

import dip.lux.service.ValidationService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {

    private List<String> validTypes = new ArrayList<>();

    @PostConstruct
    private void init(){
        validTypes.add("DOCX");
        validTypes.add("DOC");
        validTypes.add("ODT");
    }

    @Override
    public boolean isValidFileType(String fileName) {
        String[] fileParts = fileName.split("\\.");
        if(fileParts.length != 2){
            return false;
        }
        for(String validType: validTypes){
            if(fileParts[1].toUpperCase().equals(validType)){
                return true;
            }
        }
        return false;
    }
}
