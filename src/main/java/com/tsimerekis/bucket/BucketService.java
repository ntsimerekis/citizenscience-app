package com.tsimerekis.bucket;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.HttpMethod;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

@Service
public class BucketService {

    private final String projectId;

    private final String bucketName;

    private final Storage storage;

    private BucketService(@Value("${cloud.project-id}") String projectId, @Value("${cloud.bucket}") String bucketName, @Value("${cloud.secrets-path}") String secretsPath) {
        this.projectId = projectId;
        this.bucketName = bucketName;

        try {
            this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(secretsPath)))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public URL getSignedDownloadURL(String objectName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();
        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
    }

    public String uploadSubmissionPhoto(Long submissionId,
                                       InputStream data,
                                       String contentType) throws IOException {

        // You can adjust this path structure as you like
        String objectName = "submission/" + submissionId + "/photo";

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName))
                .setContentType(contentType)
                .build();

        // Upload the file bytes from the InputStream
        storage.createFrom(blobInfo, data);

        return objectName;
    }

}
