package io.qifan.chatgpt.assistant.infrastructure.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.StorageClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class OSSUtils {
    @Autowired
    OSSInfo ossInfo;
    @Autowired
    OSS oss;

    public String basicUpload(String objectName, InputStream inputStream) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossInfo.getBucketName(), objectName, inputStream);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setObjectAcl(CannedAccessControlList.PublicRead);
        putObjectRequest.setMetadata(metadata);
        oss.putObject(putObjectRequest);

        String url = "https://" + ossInfo.getBucketName() + "." +
                ossInfo.getEndpoint().replace("https://", "") +
                "/" + objectName;
        return url;
    }
}
