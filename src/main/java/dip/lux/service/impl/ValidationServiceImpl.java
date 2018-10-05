package dip.lux.service.impl;

import dip.lux.service.ValidationService;
import dip.lux.service.model.DocType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {

    private List<String> validTypes = new ArrayList<>();

    @PostConstruct
    private void init(){
        for(DocType type: DocType.values()){
            validTypes.add(type.getType());
        }
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

    @Override
    public boolean isDOCX(String format){
        return DocType.DOCX.equals(format);

    }

    @Override
    public boolean isDOC(String format){
        return DocType.DOC.equals(format);
    }

    @Override
    public boolean isODT(String format){
        return DocType.ODT.equals(format);
    }
}
