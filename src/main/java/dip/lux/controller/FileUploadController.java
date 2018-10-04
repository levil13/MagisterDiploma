package dip.lux.controller;

import dip.lux.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Controller
public class FileUploadController {
    @Autowired
    UploadService uploadService;

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        if (file.isEmpty()) {
            redirectAttributes.addAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        if (uploadService.upload(file)) {
            redirectAttributes.addAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } else {
            redirectAttributes.addAttribute("message",
                    "Error in file upload '" + file.getOriginalFilename() + "'");
        }
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
