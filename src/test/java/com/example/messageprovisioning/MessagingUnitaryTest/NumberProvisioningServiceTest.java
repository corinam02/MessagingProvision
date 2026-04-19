package com.example.messageprovisioning.MessagingUnitaryTest;

import com.example.messageprovisioning.provisioning.model.dto.BillingNotificationRequest;
import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import com.example.messageprovisioning.provisioning.model.model.NumberStatus;
import com.example.messageprovisioning.provisioning.model.client.BillingClient;
import com.example.messageprovisioning.provisioning.model.client.ReputationClient;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningRequest;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningResponse;
import com.example.messageprovisioning.provisioning.model.dto.ReputationScore;
import com.example.messageprovisioning.provisioning.model.exception.NoNumberFoundException;
import com.example.messageprovisioning.provisioning.model.repository.PhoneNumberRepository;
import com.example.messageprovisioning.provisioning.model.service.PhoneNumberProvisioningService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NumberProvisioningServiceTest {

    @Mock
    private PhoneNumberRepository numberRepo;
    @Mock
    private ReputationClient reputationClient;
    @Mock
    private BillingClient billingClient;

    @InjectMocks
    private PhoneNumberProvisioningService service;

    private PhoneNumber buildNumber(String number, String areaCode, NumberStatus status) {
        PhoneNumber p = new PhoneNumber(number, areaCode, "US", status);
        p.setStatus(status);
        return p;
    }

    private ProvisioningRequest buildRequest(String tenantId, String areaCode) {
        return new ProvisioningRequest(tenantId,"admin@test.com", areaCode  );
    }

    private ReputationScore cleanScore(String number) {
        ReputationScore s = new ReputationScore();
        s.setPhoneNumber(number);
        s.setScore(5);
        s.setLevel("CLEAN");
        s.setSafeToProvision(true);
        return s;
    }

    @Test
    @DisplayName("Allocated number")
    void provision_cleanNumber_success() {
        PhoneNumber number = buildNumber("+19195550100", "919", NumberStatus.AVAILABLE);

        when(reputationClient.getScore("+19195550100"))
                .thenReturn(cleanScore("+19195550100"));
        when(numberRepo.findFirstByStatusAndAreaCode(NumberStatus.AVAILABLE, "919"))
                .thenReturn(List.of(number));
        ProvisioningResponse response = service.provisionNumber(buildRequest("uber", "919"));

        assertThat(response.getStatus()).isEqualTo("PROVISIONED");
        assertThat(response.getPhoneNumber()).isEqualTo("+19195550100");

        verify(numberRepo).save(argThat(n ->
                n.getStatus() == NumberStatus.PROVIDED &&
                        "uber".equals(n.getClientId()) &&
                        n.getProvisionedAt() != null
        ));

        verify(billingClient).notifyProvisioning(argThat(req ->
                "NUMBER_PROVISIONED".equals(req.getEventType()) &&
                        "uber".equals(req.getTenantId())
        ));
    }

    @Test
    @DisplayName("No number available")
    void provision_noNumbersAvailable_throwsException() {

        when(numberRepo.findFirstByStatusAndAreaCode(NumberStatus.AVAILABLE, "555"))
                .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> service.provisionNumber(buildRequest("uber", "555")))
                .isInstanceOf(NoNumberFoundException.class);

        verifyNoInteractions(billingClient);
    }

    @Test
    @DisplayName("Liberated number")
    void release_success() {
        PhoneNumber number = buildNumber("+19195550100", "919", NumberStatus.PROVIDED);
        number.setClientId("uber");
        when(numberRepo.findById("num-id-1")).thenReturn(Optional.of(number));

        service.releaseNumber("num-id-1", "uber");

        verify(numberRepo).save(argThat(n ->
                n.getStatus() == NumberStatus.AVAILABLE &&
                        n.getClientId() == null

        ));
        verify(billingClient).notifyProvisioning(any(com.example.messageprovisioning.billing.dto.BillingNotificationRequest.class));

    }
}