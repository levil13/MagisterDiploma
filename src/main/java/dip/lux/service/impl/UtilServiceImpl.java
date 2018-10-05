package dip.lux.service.impl;

import dip.lux.service.UtilService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UtilServiceImpl implements UtilService {

    @Override
    public boolean createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        return dir.exists() || dir.mkdirs();
    }

    @Override
    public String getFileFormat(File doc) {
        String docName = doc.getName();
        String[] nameParts = docName.split("\\.");
        if(nameParts.length != 2){
            return "Wrong file format";
        }
        return nameParts[1];
    }

    @Override
    public String getNameWithoutFormat(File doc) {
        String docName = doc.getName();
        String[] nameParts = docName.split("\\.");
        if(nameParts.length != 2){
            return "Wrong file format";
        }
        return nameParts[0];
    }


}
