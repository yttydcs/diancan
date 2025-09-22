package com.example.diancan2.controller;

import com.example.diancan2.vo.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final String uploadDir = "src/main/resources/static/images/";

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error("文件为空");
        }
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            
            File dest = new File(dir.getAbsolutePath() + File.separator + newFileName);
            file.transferTo(dest);
            
            String filePath = "/images/" + newFileName;
            return ApiResponse.success("上传成功", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error("上传失败");
        }
    }
}
