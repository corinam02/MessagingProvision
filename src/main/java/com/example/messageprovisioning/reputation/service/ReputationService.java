package com.example.messageprovisioning.reputation.service;

import com.example.messageprovisioning.reputation.dto.ReputationScore;
import com.example.messageprovisioning.reputation.dto.ScoreLevel;
import com.example.messageprovisioning.reputation.dto.SpamReportRequest;
import com.example.messageprovisioning.reputation.model.ReputationRecord;
import com.example.messageprovisioning.reputation.repository.ReputationRecordRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.slf4j.Logger;

@Service
@AllArgsConstructor

public class ReputationService {

    private static final Logger log = LoggerFactory.getLogger(ReputationService.class);
    private static final int POINTS_PER_REPORT = 15;
    private static final int DAYS_WINDOW = 30;
    private final ReputationRecordRepository reputationRecordRepository;

    public ReputationScore calculateScore(String phoneNumber) {
        log.info("Calculating score for: {}", phoneNumber);

        LocalDateTime since = LocalDateTime.now().minusDays(DAYS_WINDOW);

        long totalReports = reputationRecordRepository.countByPhoneNumber(phoneNumber);
        long recentReports = reputationRecordRepository.countRecentUndisputedReports(phoneNumber, since);

        int score = (int) Math.min(100, recentReports * POINTS_PER_REPORT);

        ScoreLevel level = score <= 30 ? ScoreLevel.CLEAN
                : score <= 70 ? ScoreLevel.MEDIUM_RISK
                : ScoreLevel.HIGH_RISK;

        boolean safeToProvision = (score <= 30);

        log.info("Result for {}: Score={}, Level={}", phoneNumber, score, level);

        return new ReputationScore(phoneNumber, score, level, safeToProvision);
    }

    @Transactional
    public void recordSpamReport(SpamReportRequest request) {
        ReputationRecord record = new ReputationRecord(
                request.getPhoneNumber(),
                request.getReporterId(),
                request.getReportType()
        );
        reputationRecordRepository.save(record);


        log.info("Spam report saved: phone={}, type={}", request.getPhoneNumber(), request.getReportType());
    }

    @Transactional
    public void disputeReport(String reportId) {
        ReputationRecord record = reputationRecordRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));
        record.setDisputed(true);
        reputationRecordRepository.save(record);
        log.info("Report disputed: {}", reportId);
    }
}