package com.example.messageprovisioning.provisioning.model.controller;

import com.example.messageprovisioning.provisioning.model.dto.ProvisioningRequest;
import com.example.messageprovisioning.provisioning.model.dto.ProvisioningResponse;
import com.example.messageprovisioning.provisioning.model.service.PhoneNumberProvisioningService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/numbers")

public class PhoneNumberController {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberController.class);
    private final PhoneNumberProvisioningService phoneNumberProvisioningService;

    public PhoneNumberController(PhoneNumberProvisioningService phoneNumberProvisioningService) {
        this.phoneNumberProvisioningService = phoneNumberProvisioningService;
    }
    @PostMapping("/provision")
    public ResponseEntity<ProvisioningResponse> provision(@Valid @RequestBody ProvisioningRequest request){
        log.info("The HTTP request for provisioning" + request.getClientId() + request.getAreaCode());
        ProvisioningResponse response = phoneNumberProvisioningService.provisionNumber(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{numberId}/release")
    public ResponseEntity<String> release(@PathVariable String numberId, @RequestParam String clientId){
        log.info("The HTTP request liberated" + numberId+clientId);
        phoneNumberProvisioningService.releaseNumber(numberId,clientId);
        return ResponseEntity.ok("The number was succesfully liberated");
    }

    @GetMapping("/api/v1/numbers/available?areaCode=40")
    public ResponseEntity<List<String>> getAvailable(@RequestParam String areaCode){
        List<String> numbers = phoneNumberProvisioningService.findAvailable(areaCode);
        return ResponseEntity.ok(numbers);
    }
    @GetMapping("/client/{clientId}")
    public List<ProvisioningResponse> getNumbersByClient(
            @PathVariable String clientId) {

        List<ProvisioningRequest> numbers =
                phoneNumberProvisioningService.getNumbersByClient(clientId);
        return (List<ProvisioningResponse>) ResponseEntity.ok(numbers);
    }
}
