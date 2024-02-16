package com.example.news_feed.common.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.news_feed.common.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 유저 프로필 이미지 업로드
    public String uploadProfile(MultipartFile file) {
        try {
            String fileName=file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,"profile/"+ uuid + "_"+fileName,file.getInputStream(),metadata);
            return amazonS3Client.getUrl(bucket, "profile/"+ uuid + "_"+fileName).toString();

        } catch (IOException e) {
            throw new HttpException(false, "프로필 이미지 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST);        }
    }


    // 게시글 멀티미디어 저장
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> uploadedFileUrls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String fileUrl= "https://" + bucket  + ".s3.ap-northeast-2.amazonaws.com/post/" + uuid + "_" + fileName;
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket, "post/"+ uuid + "_" + fileName, file.getInputStream(), metadata);
                uploadedFileUrls.add(fileUrl);
            }
            return uploadedFileUrls;
        } catch (IOException e) {
            throw new HttpException(false, "이미지 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }

    }
}