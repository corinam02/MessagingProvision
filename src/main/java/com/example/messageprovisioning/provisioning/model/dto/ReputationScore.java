package com.example.messageprovisioning.provisioning.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReputationScore {

    private String  phoneNumber;
    private int     score;
    private String  level;
    private boolean safeToProvision;
}
