package com.test.controller.v1.commission.service;

import java.math.BigInteger;

import com.test.persistence.client.Client;
import com.test.persistence.exception.ClientNotFoundException;
import com.test.persistence.repository.ClientRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ClientService {
	private final ClientRepository repository;

	public Mono<Client> getClient(final BigInteger clientId) {
		return repository
				.findClient(clientId)
				.switchIfEmpty(Mono.defer(() -> Mono.error(ClientNotFoundException::new)));
	}
}
