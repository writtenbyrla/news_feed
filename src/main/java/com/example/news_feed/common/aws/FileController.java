package com.example.news_feed.common.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.news_feed.common.exception.HttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/upload")
    public List<String> uploadFiles(@RequestPart("files") List<MultipartFile> files) {
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
            throw new HttpException("이미지 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }

    }
}
