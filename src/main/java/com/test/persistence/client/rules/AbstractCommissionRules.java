package com.test.persistence.client.rules;

import static com.test.controller.v1.common.Constant.DEFAULT_EURO_FEE;
import static com.test.controller.v1.common.Constant.RULE_3_FEE;
import static io.vavr.API.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.test.controller.v1.commission.model.CommissionRequest;
import com.test.controller.v1.commission.service.ApiExchangeService;
import com.test.persistence.client.Client;
import com.test.persistence.client.Transaction;

public abstract class AbstractCommissionRules {
	private final ApiExchangeService apiExchangeService = new ApiExchangeService();

	protected abstract String specificRules(final Client client, final CommissionRequest request);

	public String applyRules(final Client client, final CommissionRequest request) {
		final double sum = calculateSumTransaction(client, request.getDate());
		final String specificFee = specificRules(client, request);

		return Match(true)
				.of( //
						Case($(sum >= 1000), b -> RULE_3_FEE), //
						Case($(!specificFee.isEmpty()), b -> specificFee), //
						Case($(!"EUR".equalsIgnoreCase(request.getCurrency())), b -> {
							final double conversion = apiExchangeService.getExchange(request);
							return calculateFees(conversion, request);
						}), //
						Case($(), b -> calculateFees(1, request)) //
				);
	}

	private double calculateSumTransaction(final Client c, final Date date) {
		return c
				.getTransactions()
				.stream()
				.filter(t -> t.getDate().getMonth() == date.getMonth())
				.map(Transaction::getAmount)
				.map(Double::parseDouble)
				.mapToDouble(Double::doubleValue)
				.sum();
	}

	private String calculateFees(final double conversion, final CommissionRequest request) {
		final double sumConversion = conversion * Double.parseDouble(request.getAmount());

		BigDecimal bd = BigDecimal.valueOf((sumConversion * 0.5) / 100);

		if (bd.doubleValue() >= DEFAULT_EURO_FEE) {
			bd = bd.setScale(2, RoundingMode.UNNECESSARY);
			return bd.toString();
		}

		return BigDecimal.valueOf(DEFAULT_EURO_FEE).toString();
	}
}
