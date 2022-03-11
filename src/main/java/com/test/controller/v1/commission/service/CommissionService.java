package com.test.controller.v1.commission.service;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Date;

import com.test.persistence.client.Client;
import org.springframework.stereotype.Service;

import com.test.controller.v1.commission.exception.CommissionException;
import com.test.controller.v1.commission.model.CommissionRequest;
import com.test.controller.v1.commission.model.CommissionResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CommissionService {
	private final ClientService clientService;

	public Mono<CommissionResponse> calculateCommission(final CommissionRequest request) {
		return Mono
				.justOrEmpty(request)
				.filter(this::isValidRequest)
				.flatMap(this::getClient)
				.switchIfEmpty(Mono.defer(() -> Mono.error(new CommissionException())));
	}

	private Boolean isValidRequest(final CommissionRequest request) {
		if (request.getClientId() == null)
			return false;

		if (isEmpty(request.getCurrency()))
			return false;

		if (isEmpty(request.getAmount()) || Double.parseDouble(request.getAmount()) <= 0)
			return false;

		return request.getDate() != null && !request.getDate().after(new Date());
	}

	private Mono<CommissionResponse> getClient(final CommissionRequest request) {
		return clientService.getClient(request.getClientId()).map(client -> buildResponse(client, request));
	}

	private CommissionResponse buildResponse(final Client client, final CommissionRequest request) {
		final String amount = client.getType().getRules().applyRules(client, request);

		return CommissionResponse.builder().amount(amount).build();
	}
}
