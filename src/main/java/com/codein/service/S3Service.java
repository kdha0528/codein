package com.codein.service;

import com.codein.error.exception.profileimage.ImageTooLargeException;
import com.codein.error.exception.profileimage.InvalidImageException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Autowired
    private S3Client s3Client;

    private final String bucketName = "codein-resource-bucket";
    private final String profilePath = "images/profile/";

    public String uploadProfileImage(MultipartFile multipartFile) throws IOException {

        // 이미지 변경 없을 시 return null
        if (multipartFile == null) return null;

        // 이미지파일 아닐 시 EXCEPTION
        if (!Objects.requireNonNull(multipartFile.getContentType()).startsWith("image")) {
            throw new InvalidImageException();
        }

        // 사진 크기가 1MB 넘어가면 Exception
        if (multipartFile.getSize() > 1000000) throw new ImageTooLargeException();
        
        // 파일 이름 중복 안되게 uuid로 처리
        String extension = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + extension;

        // S3를 통해 파일 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(profilePath+imageFileName)
                .acl(ObjectCannedACL.PRIVATE)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

        return imageFileName;
    }

    @Transactional
    public void removeProfileImage(String imgFileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(profilePath + imgFileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }


}
