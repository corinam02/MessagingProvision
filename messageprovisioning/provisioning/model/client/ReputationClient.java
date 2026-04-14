package com.example.messageprovisioning.provisioning.model.client;


import com.teleprov.provisioning.dto.ReputationScore;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "Reputation-service",
        url = "${services.reputation.url}"
)

public interface ReputationClient {

        @GetMapping("/api/v1/reputation/{phoneNumber}")
        ReputationScore getScore(@PathVariable("phoneNumber") String phoneNumber);
}
