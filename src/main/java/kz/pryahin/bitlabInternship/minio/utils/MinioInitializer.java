package kz.pryahin.bitlabInternship.minio.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioInitializer {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;


    @PostConstruct
    public void createBucketIfNotExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs
                    .builder()
                    .bucket(bucketName)
                    .build());

            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs
                        .builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket created: {}", bucketName);
            } else {
                log.info("Bucket already exists: {}", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating bucket", e);
        }
    }
}

