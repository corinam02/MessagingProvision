package com.example.messageprovisioning.MessagingUnitaryTest;

import com.example.messageprovisioning.reputation.dto.ReputationScore;
import com.example.messageprovisioning.reputation.dto.ScoreLevel;
import com.example.messageprovisioning.reputation.dto.SpamReportRequest;
import com.example.messageprovisioning.reputation.model.ReputationRecord;
import com.example.messageprovisioning.reputation.repository.ReputationRecordRepository;
import com.example.messageprovisioning.reputation.service.ReputationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReputationScoringServiceTest {

    @Mock
    private ReputationRecordRepository reputationRepo;

    @InjectMocks
    private ReputationService service;

    private final String PHONE = "+19195550100";

    @Test
    @DisplayName("0 reports, CLEAN, safeToProvision=true")
    void score_noReports_isClean() {
        when(reputationRepo.countByPhoneNumber(PHONE)).thenReturn(0L);
        when(reputationRepo.countRecentUndisputedReports(eq(PHONE), any()))
                .thenReturn(0L);

        ReputationScore score = service.calculateScore(PHONE);

        assertThat(score.getScore()).isEqualTo(0);
        assertThat(score.getLevel()).isEqualTo(ScoreLevel.CLEAN);
        assertThat(score.isSafeToProvision()).isTrue();
    }

    @Test
    @DisplayName("2 recent reports, score 30, CLEAN")
    void score_twoRecentReports_stillClean() {
        when(reputationRepo.countByPhoneNumber(PHONE)).thenReturn(2L);
        when(reputationRepo.countRecentUndisputedReports(eq(PHONE), any()))
                .thenReturn(2L);

        ReputationScore score = service.calculateScore(PHONE);

        assertThat(score.getScore()).isEqualTo(30);
        assertThat(score.getLevel()).isEqualTo(ScoreLevel.CLEAN);
    }

    @Test
    @DisplayName("5 raports, score 75, HIGH_RISK, safeToProvision=false")
    void score_fiveRecentReports_highRisk() {
        when(reputationRepo.countRecentUndisputedReports(eq(PHONE), any()))
                .thenReturn(5L);

        ReputationScore score = service.calculateScore(PHONE);

        assertThat(score.getScore()).isEqualTo(75);
        assertThat(score.getLevel()).isEqualTo(ScoreLevel.HIGH_RISK);
        assertThat(score.isSafeToProvision()).isFalse();
    }

    @Test
    @DisplayName("100 reports, maxim 100")
    void score_manyReports_cappedAt100() {
        when(reputationRepo.countRecentUndisputedReports(eq(PHONE), any()))
                .thenReturn(50L);

        ReputationScore score = service.calculateScore(PHONE);

        assertThat(score.getScore()).isEqualTo(100);
        assertThat(score.getLevel()).isEqualTo(ScoreLevel.HIGH_RISK);
    }

    @Test
    @DisplayName("Report spam, save ReputationRecord in DB")
    void recordSpam_savesRecord() {
        SpamReportRequest request = new SpamReportRequest();
        request.setPhoneNumber("+19195550100");
        request.setReporterId("user-456");
        request.setReportType("SPAM");

        service.recordSpamReport(request);

        ArgumentCaptor<ReputationRecord> recordCaptor = ArgumentCaptor.forClass(ReputationRecord.class);
        verify(reputationRepo).save(recordCaptor.capture());

        ReputationRecord savedRecord = recordCaptor.getValue();

        verify(reputationRepo).save(argThat(record ->
                PHONE.equals(record.getPhoneNumber()) &&
                        "user-456".equals(record.getReporterId()) &&
                        "SPAM".equals(record.getReportType())
        ));
    }


    @Test
    @DisplayName("contest the existing report")
    void disputeReport_notFound_throwsException() {
        when(reputationRepo.findById("inexistent"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.disputeReport("inexistent"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("inexistent");
    }
}