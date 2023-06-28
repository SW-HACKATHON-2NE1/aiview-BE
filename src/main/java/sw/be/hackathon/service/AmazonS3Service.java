package sw.be.hackathon.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.domain.Member;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Service {

    @Value("${cloud.aws.S3.bucket}")
    private String S3_BUCKET;

    private final AmazonS3Client amazonS3Client;

    private Date getExpiration() {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 30); // 30분

        return expiration;
    }

    public String getPresignedUrlForUpload(Member member, Long questionId) {
        String fileName = member.getUuid() + "/" + questionId;
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, fileName)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(getExpiration());

            return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String getPresignedUrl(Member member, Long questionId) {
        String fileName = member.getUuid() + "/" + questionId;
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, fileName)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(getExpiration());

            return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}


