package sw.be.hackathon.service;

import org.opencv.video.Video;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaceRekognitionService {

	private final VideoFrameExtractor videoFrameExtractor;
}
