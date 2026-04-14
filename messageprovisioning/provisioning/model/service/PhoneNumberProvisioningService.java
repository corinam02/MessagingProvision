package com.example.messageprovisioning.provisioning.model.service;

import com.example.messageprovisioning.provisioning.model.client.BillingClient;
import com.example.messageprovisioning.provisioning.model.client.ReputationClient;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningRequest;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningResponse;
import com.example.messageprovisioning.provisioning.model.exception.NoNumberFoundException;
import com.example.messageprovisioning.provisioning.model.model.NumberStatus;
import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import com.example.messageprovisioning.provisioning.model.repository.PhoneNumberRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class PhoneNumberProvisioningService {
    private static final Logger log = LoggerFactory.getLogger(PhoneNumberProvisioningService.class);

    private final PhoneNumberRepository phoneNumberRepository;
    private final ReputationClient reputationClient;
    private final BillingClient billingClient;

    public PhoneNumberProvisioningService(PhoneNumberRepository phoneNumberRepository, ReputationClient reputationClient, BillingClient billingClient) {
        this.phoneNumberRepository = phoneNumberRepository;
        this.reputationClient = reputationClient;
        this.billingClient = billingClient;
    }

    @Transactional
    public ProvisioningResponse provisionNumber(ProvisioningRequest request){
        log.info("The request for provisioning: areaCode={}, client={}", request.getAreaCode(), request.getClientId());

        PhoneNumber number = phoneNumberRepository
                .findFirstByStatusAndAreaCode(
                        NumberStatus.AVAILABLE, request.getAreaCode())
                .orElseThrow(() ->
                        new NoNumberFoundException(request.getAreaCode()));

        ReputationScore score = reputationClient.getScore(number.getNumber());

        if(!score.isSafeToProvision()){
            log.warning("The number" +number.getNumber() +" has the score of spam. Find another number"+
                     score.getScore());
            number = findCleanNumber(request.getAreaCode());
        }
        number.setStatus(NumberStatus.PROVIDED);
        number.setClientId(request.getClientId());
        number.setProvisionedAt(LocalDateTime.now());
        phoneNumberRepository.save(number);

        log.info("Numar provizonat: {} " + number.getNumber() + "-> client {}"+
                 request.getClientId());

        notifyBilling("NUMBER_PROVISIONED",
                request.getClientId(),number.getNumber());
        return new ProvisioningResponse(number);


        @Transactional
                public void releaseNumber
    }
}
