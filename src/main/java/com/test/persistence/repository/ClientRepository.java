package com.test.persistence.repository;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.test.persistence.client.Client;
import com.test.persistence.client.ClientType;
import com.test.persistence.client.Transaction;

import reactor.core.publisher.Mono;

@Component
public class ClientRepository {

	//Mock find
	public Mono<Client> findClient(final BigInteger clientId) {
		if (clientId.intValue() == 1) {
			final Instant testDate = LocalDate.of(2021, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

			final List<Transaction> transactions = List
					.of( //
							Transaction.builder().amount("500.00").currency("EUR").date(Date.from(testDate)).build(), //
							Transaction.builder().amount("499.00").currency("EUR").date(Date.from(testDate)).build(), //
							Transaction.builder().amount("100.00").currency("EUR").date(Date.from(testDate)).build() //
					);

			return Mono.just(Client.builder().clientId(clientId).transactions(transactions).build());
		}

		if (clientId.intValue() == 999) {
			final List<Transaction> transactions = IntStream
					.range(0, 10)
					.mapToObj(i -> Transaction.builder().amount("10.00").currency("EUR").date(new Date()).build())
					.collect(Collectors.toList());

			return Mono
					.just(Client
							.builder()
							.clientId(clientId)
							.type(ClientType.TEST_TYPE)
							.transactions(transactions)
							.build());
		}

		if (clientId.intValue() == 42) {
			return Mono
					.just(Client.builder().clientId(clientId).type(ClientType.SPECIAL).transactions(List.of()).build());
		}

		return Mono.just(Client.builder().clientId(clientId).transactions(List.of()).build());
	}
}
