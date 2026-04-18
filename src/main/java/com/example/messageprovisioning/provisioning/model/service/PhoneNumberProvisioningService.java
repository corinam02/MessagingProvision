package com.example.messageprovisioning.provisioning.model.service;

import com.example.messageprovisioning.billing.dto.BillingNotificationRequest;
import com.example.messageprovisioning.provisioning.model.client.BillingClient;
import com.example.messageprovisioning.provisioning.model.client.ReputationClient;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningRequest;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningResponse;
import com.example.messageprovisioning.provisioning.model.exception.NoNumberFoundException;
import com.example.messageprovisioning.provisioning.model.exception.NumberNotFoundException;
import com.example.messageprovisioning.provisioning.model.model.NumberStatus;
import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import com.example.messageprovisioning.provisioning.model.repository.PhoneNumberRepository;
import com.example.messageprovisioning.provisioning.model.dto.ReputationScore;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhoneNumberProvisioningService {
    private static final Logger log = (Logger) LoggerFactory.getLogger(PhoneNumberProvisioningService.class);

    private final PhoneNumberRepository phoneNumberRepository;
    private final ReputationClient reputationClient;
    private final BillingClient billingClient;

    public PhoneNumberProvisioningService(PhoneNumberRepository phoneNumberRepository, ReputationClient reputationClient, BillingClient billingClient) {
        this.phoneNumberRepository = phoneNumberRepository;
        this.reputationClient = reputationClient;
        this.billingClient = billingClient;
    }

    @Transactional
    public ProvisioningResponse provisionNumber(ProvisioningRequest request) {
        log.info("The request for provisioning: areaCode={}, client={}" + request.getAreaCode() + request.getClientId());


        PhoneNumber number = (PhoneNumber) phoneNumberRepository
                .findFirstByStatusAndAreaCode(
                        NumberStatus.AVAILABLE, request.getAreaCode());

        ReputationScore score = reputationClient.getScore(number.getNumber());
        if (!score.isSafeToProvision()) {
            log.warn("The number" + number.getNumber() + " has the score of spam. Find another number" +
                    score.getScore());
            number = findCleanNumber(request.getAreaCode());;
        }
        number.setStatus(NumberStatus.PROVIDED);
        number.setClientId(request.getClientId());
        number.setProvisionedAt(LocalDateTime.now());
        phoneNumberRepository.save(number);

        log.info("Number provisioned: {} " + number.getNumber() + "-> client {}" +
                request.getClientId());

        notifyBilling("NUMBER_PROVISIONED",
                request.getClientId(), number.getNumber());
        return new ProvisioningResponse(number);
    }

        @Transactional
        public void releaseNumber(String numberId,String  clientId){
            log.info("The request for liberation of the number" + numberId + clientId);

            PhoneNumber phoneNumber = phoneNumberRepository.findById(numberId)
                    .orElseThrow(() -> new NumberNotFoundException(numberId));
            if(!phoneNumber.getClientId().equals(clientId)){
                throw new RuntimeException( "The number" + numberId + "don't belong to" + clientId);
            }

            phoneNumber.setStatus(NumberStatus.AVAILABLE);
            phoneNumber.setClientId(null);
            phoneNumberRepository.save(phoneNumber);

            log.info("The Free number" + phoneNumber.getNumber());

            notifyBilling("NUMBER_RELEASED", clientId, phoneNumber.getNumber());
        }

        public List<String> findAvailable(String areaCode){
        return phoneNumberRepository
                .findFirstByStatusAndAreaCode(NumberStatus.AVAILABLE, areaCode)
                .stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.toList());
        }

        public List<ProvisioningRequest> getNumbersByClient(String clientId){
        return phoneNumberRepository
                .findByClientId(clientId)
                .stream()
                .map(ProvisioningRequest::new)
                .collect(Collectors.toList()).reversed();
        }
        private PhoneNumber findCleanNumber(String areaCode){
        List<PhoneNumber> candidates = phoneNumberRepository
                .findFirstByStatusAndAreaCode(NumberStatus.AVAILABLE,areaCode);
        for(PhoneNumber candidate: candidates){
            ReputationScore score = reputationClient.getScore(candidate.getNumber());
            if(score.isSafeToProvision()){
                return candidate;
            }
        }
        throw new NoNumberFoundException(areaCode);
        }
        private void notifyBilling(String eventType, String clientId,String phone){

        try{
            billingClient.notifyProvisioning(
                    new BillingNotificationRequest (eventType, clientId, phone, MDC.get("correlationId"))
            );
        } catch (Exception e){
            log.error("The billing notification failed for the number" + phone + " client" + clientId, e);

            }
        }
    }

