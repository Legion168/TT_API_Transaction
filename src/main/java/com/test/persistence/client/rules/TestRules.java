package com.test.persistence.client.rules;

import com.test.controller.v1.commission.model.CommissionRequest;
import com.test.persistence.client.Client;

public class TestRules extends AbstractCommissionRules {
    @Override
    public String specificRules(final Client client, final CommissionRequest request) {
        return client.getTransactions().size() >= 10 ? "0.01" : "";
    }
}
