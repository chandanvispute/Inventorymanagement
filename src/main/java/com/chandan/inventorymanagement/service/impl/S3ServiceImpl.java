package com.chandan.inventorymanagement.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.chandan.inventorymanagement.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadImage(MultipartFile file) {
//        System.out.println("Entered 2");
        String key = "products/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        System.out.println("Entered3");
        System.out.println("Bucket: " + bucketName);
        System.out.println("Region: " + amazonS3.getRegionName());
        AWSSecurityTokenService sts =
                AWSSecurityTokenServiceClientBuilder.defaultClient();

        System.out.println(
                sts.getCallerIdentity(new GetCallerIdentityRequest()).getArn()
        );
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            System.out.println("asdasdasascqwqcqqcq");
            amazonS3.putObject(
                    bucketName,
                    key,
                    file.getInputStream(),
                    metadata
            );
            System.out.println("asdasdasascqwqcqqcq");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image to S3", e);
        }
        System.out.println("Exit");
        return amazonS3.getUrl(bucketName, key).toString();
    }
}
