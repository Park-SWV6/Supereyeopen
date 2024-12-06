package com.example.demo.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class GcsService {

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${GOOGLE_APPLICATION_CREDENTIALS_JSON}")
    private String googleCredentialsJson;

    private Storage storage;

    @PostConstruct
    public void init() throws IOException {
        // 서비스 계정 키 파일 생성
        String tempFilePath = "/tmp/google-credentials.json";
        try (FileOutputStream fos = new FileOutputStream(tempFilePath)) {
            fos.write(googleCredentialsJson.getBytes());
        }

        // GCS 클라이언트 초기화
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(tempFilePath));
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public String uploadFile(String folder, String fileName, byte[] content) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folder + "/" + fileName).build();
        storage.create(blobInfo, content);
        return String.format("https://storage.googleapis.com/%s/%s/%s", bucketName, folder, fileName);
    }

    // 파일 삭제 메소드 추가
    public void deleteFile(String folder, String fileName) {
        BlobId blobId = BlobId.of(bucketName, folder + "/" + fileName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new IllegalArgumentException("Failed to delete the object: " + fileName);
        }
    }
}
