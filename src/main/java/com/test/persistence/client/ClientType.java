package com.test.persistence.client;

import com.test.persistence.client.rules.DefaultRules;
import com.test.persistence.client.rules.AbstractCommissionRules;
import com.test.persistence.client.rules.SpecialRules;
import com.test.persistence.client.rules.TestRules;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public enum ClientType {
    DEFAULT(new DefaultRules()),
    SPECIAL(new SpecialRules()),
    TEST_TYPE(new TestRules());

    private final AbstractCommissionRules rules;
}
