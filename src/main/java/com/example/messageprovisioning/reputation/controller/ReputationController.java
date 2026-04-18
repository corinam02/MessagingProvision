
package com.example.messageprovisioning.reputation.controller;


import com.example.messageprovisioning.reputation.dto.ReputationScore;
import com.example.messageprovisioning.reputation.dto.SpamReportRequest;
import com.example.messageprovisioning.reputation.service.ReputationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reputation")
public class ReputationController {

    private final ReputationService scoringService;

    public ReputationController(ReputationService scoringService) {
        this.scoringService = scoringService;
    }


    @GetMapping("/{phoneNumber}")
    public ResponseEntity<ReputationScore> getScore(
            @PathVariable String phoneNumber) {
        return ResponseEntity.ok(scoringService.calculateScore(phoneNumber));
    }


    @PostMapping("/report")
    public ResponseEntity<String> reportSpam(
            @Valid @RequestBody SpamReportRequest request) {
        scoringService.recordSpamReport(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registerd for: " + request.getPhoneNumber());
    }


    @PutMapping("/dispute/{reportId}")
    public ResponseEntity<String> dispute(@PathVariable String reportId) {
        scoringService.disputeReport(reportId);
        return ResponseEntity.ok("The report " + reportId + " was contested");
    }
}