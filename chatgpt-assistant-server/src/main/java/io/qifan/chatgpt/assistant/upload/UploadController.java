package io.qifan.chatgpt.assistant.upload;


import io.qifan.infrastructure.common.constants.ResultCode;
import io.qifan.infrastructure.common.exception.BusinessException;
import io.qifan.infrastructure.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Autowired
    OSSUploadServiceImpl uploadService;

    /**
     * 上传图片到阿里云oss得到url返回给前端
     */
    @PostMapping("/upload")
    public R<Map<String, String>> uploadImg(@RequestParam Map<String, MultipartFile> files, String path) {
        List<String> arrayList = new ArrayList<>();
        files.forEach((String key, MultipartFile file) -> {
            try {
                log.info(key);
                String url = uploadService.upload(file);
                arrayList.add(url);
            } catch (Exception e) {
                throw new BusinessException(ResultCode.TransferStatusError, "上传失败");
            }
        });
        String join = String.join(";", arrayList);
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", join);
        return R.ok(urlMap);
    }


}
