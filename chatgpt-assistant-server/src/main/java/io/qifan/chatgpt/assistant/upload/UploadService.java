package io.qifan.chatgpt.assistant.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String upload(MultipartFile multipartFile) throws IOException;

    String uploadByPath(MultipartFile multipartFile, String path) throws IOException;

    boolean delete(String url);
}