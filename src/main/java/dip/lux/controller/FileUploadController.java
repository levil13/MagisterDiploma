package dip.lux.controller;

import dip.lux.service.UploadService;
import dip.lux.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {
    @Autowired
    UploadService uploadService;

    @Autowired
    ValidationService validationService;

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        if(!validationService.isValidFileType(file.getOriginalFilename())){
            redirectAttributes.addAttribute("message", "Invalid file format");
            return "redirect:uploadStatus";
        }

        String uploadedFileName = uploadService.upload(file);
        if(uploadedFileName.equalsIgnoreCase("ERROR")){
            redirectAttributes.addAttribute("message", "Error in file upload: " + file.getOriginalFilename());
            return "redirect:uploadStatus";
        }
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
