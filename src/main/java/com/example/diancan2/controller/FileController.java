package com.example.diancan2.controller;

import com.example.diancan2.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${app.upload-dir:${user.home}/diancan/uploads/images}")
    private String uploadDir;

    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/webp"
    ));

    private static final Map<String, String> CONTENT_TYPE_EXT = new HashMap<String, String>() {{
        put(MediaType.IMAGE_JPEG_VALUE, ".jpg");
        put(MediaType.IMAGE_PNG_VALUE, ".png");
        put(MediaType.IMAGE_GIF_VALUE, ".gif");
        put("image/webp", ".webp");
    }};

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiResponse.error("文件为空");
        }
        try {
            String contentType = Optional.ofNullable(file.getContentType()).orElse("");
            if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
                return ApiResponse.error("不支持的图片类型");
            }

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // 生成安全文件名
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), ""));
            // 防御目录穿越
            if (originalFilename.contains("..")) {
                return ApiResponse.error("非法文件名");
            }

            // 决定后缀：优先取原始文件名中的合法后缀，否则按 content-type 映射
            String ext = extractExt(originalFilename);
            if (ext.isEmpty()) {
                ext = CONTENT_TYPE_EXT.getOrDefault(contentType, "");
            }
            if (ext.isEmpty()) {
                return ApiResponse.error("无法识别的文件后缀");
            }

            String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;
            Path target = uploadPath.resolve(newFileName).normalize();

            // 再次防御：确保目标路径仍在上传目录内
            if (!target.startsWith(uploadPath)) {
                return ApiResponse.error("非法保存路径");
            }

            // 保存文件
            try {
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                return ApiResponse.error("保存文件失败");
            }

            // 生成可访问 URL（相对路径）
            String fileUrl = "/images/" + newFileName;

            return ApiResponse.success("上传成功", fileUrl);
        } catch (Exception e) {
            return ApiResponse.error("服务器内部错误");
        }
    }

    private static String extractExt(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) return "";
        String ext = filename.substring(idx).toLowerCase(Locale.ROOT);
        // 简单白名单校验
        switch (ext) {
            case ".jpg":
            case ".jpeg":
                return ".jpg"; // 统一用 .jpg
            case ".png":
            case ".gif":
            case ".webp":
                return ext;
            default:
                return "";
        }
    }
}
