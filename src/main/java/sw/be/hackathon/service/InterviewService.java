package sw.be.hackathon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.be.hackathon.repository.InterviewRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewService {
    private final InterviewRepository interviewRepository;
}
