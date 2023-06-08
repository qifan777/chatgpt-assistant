package io.qifan.chatgpt.assistant.upload;

import io.qifan.chatgpt.assistant.infrastructure.oss.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OSSUploadServiceImpl implements UploadService {
    @Autowired
    OSSUtils ossUtils;

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String objectName = sdf.format(new Date()) + filename;

        return ossUtils.basicUpload("plan-manager/" + objectName, multipartFile.getInputStream());
    }

    @Override
    public String uploadByPath(MultipartFile multipartFile, String path) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String objectName = path + "/" + sdf.format(new Date()) + filename;
        return ossUtils.basicUpload(objectName, multipartFile.getInputStream());
    }

    @Override
    public boolean delete(String url) {
        return true;
    }

//    @Override
//    public int delete(String url) {
//        url = url.replace("/resource/", "");
//        String[] splits = url.split("/");
//        StringBuilder filename1 = new StringBuilder();
//        for (int i = 0; i <= splits.length - 1; i++) {
//            filename1.append("/").append(splits[i]);
//        }
//        String filename2 = filename1.substring(1, filename1.length());
//        oss.deleteObject(ossInfo.getBucketName(), filename2);
//        return 1;
//    }


}
