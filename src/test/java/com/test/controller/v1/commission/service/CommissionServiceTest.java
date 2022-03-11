package com.test.controller.v1.commission.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.test.controller.v1.commission.exception.ExchangeCurrencyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.test.controller.v1.commission.exception.CommissionException;
import com.test.controller.v1.commission.model.CommissionRequest;
import com.test.controller.v1.commission.model.CommissionResponse;
import com.test.controller.v1.commission.service.CommissionService;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableAutoConfiguration
class CommissionServiceTest {
	@Autowired
	private CommissionService service;

	@Test
	@DisplayName("Wrong commission request")
	void getCommissionFailed() {
		final Instant testDate = LocalDate.of(2123, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		final List<CommissionRequest> listRequest = List
				.of( //
						CommissionRequest.builder().build(), //
						CommissionRequest.builder().amount("100").clientId(BigInteger.valueOf(1)).build(), //
						CommissionRequest.builder().currency("EUR").clientId(BigInteger.valueOf(1)).build(), //
						CommissionRequest
								.builder()
								.date(Date.from(testDate))
								.clientId(BigInteger.valueOf(0))
								.currency("EUR")
								.amount("100")
								.build() //
				);

		listRequest.forEach(c -> {
			final Mono<CommissionResponse> response = service.calculateCommission(c);
			assertThrows(CommissionException.class, response::block);
		});
	}

	@Test
	@DisplayName("Get commission for not existing currency")
	void getCommissionKoCurrencyNotFound() {
		final CommissionRequest request = CommissionRequest
				.builder()
				.clientId(BigInteger.valueOf(1))
				.amount("100")
				.currency("EWWWDADW")
				.date(new Date())
				.build();

		final Mono<CommissionResponse> response = service.calculateCommission(request);
		assertThrows(ExchangeCurrencyException.class, response::block);
	}

	@Test
	@DisplayName("Get commission for low amount")
	void getCommissionOkLowAmount() {
		final Instant testDate = LocalDate.of(2021, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		final CommissionRequest request = CommissionRequest
				.builder()
				.clientId(BigInteger.valueOf(43))
				.amount("1.00")
				.currency("USD")
				.date(Date.from(testDate))
				.build();

		final CommissionResponse response = service.calculateCommission(request).block();
		assertNotNull(response);
		assertEquals("0.05", response.getAmount());
		assertEquals("EUR", response.getCurrency());
	}

	@Test
	@DisplayName("Get commission for Rule #1")
	void getCommissionOkRule1() {
		final Instant testDate = LocalDate.of(2021, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		final CommissionRequest request = CommissionRequest
				.builder()
				.clientId(BigInteger.valueOf(43))
				.amount("500.00")
				.currency("EUR")
				.date(Date.from(testDate))
				.build();

		final CommissionResponse response = service.calculateCommission(request).block();
		assertNotNull(response);
		assertEquals("2.50", response.getAmount());
		assertEquals("EUR", response.getCurrency());
	}

	@Test
	@DisplayName("Get commission for Rule #2")
	void getCommissionOkRule2() {
		final CommissionRequest request = CommissionRequest
				.builder()
				.clientId(BigInteger.valueOf(42))
				.amount("10000")
				.currency("EUR")
				.date(new Date())
				.build();

		final CommissionResponse response = service.calculateCommission(request).block();
		assertNotNull(response);
		assertEquals("0.05", response.getAmount());
		assertEquals("EUR", response.getCurrency());
	}

	@Test
	@DisplayName("Get commission for Rule #3")
	void getCommissionOkRule3() {
		final Instant testDate = LocalDate.of(2021, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		final CommissionRequest request = CommissionRequest
				.builder()
				.clientId(BigInteger.valueOf(1))
				.amount("1")
				.currency("EUR")
				.date(Date.from(testDate))
				.build();

		final CommissionResponse response = service.calculateCommission(request).block();
		assertNotNull(response);
		assertEquals("0.03", response.getAmount());
		assertEquals("EUR", response.getCurrency());
	}

	@Test
	@DisplayName("Get commission for Test Rule")
	void getCommissionOkTestRule() {
		final Instant testDate = LocalDate.of(2021, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		final CommissionRequest request = CommissionRequest
				.builder()
				.clientId(BigInteger.valueOf(999))
				.amount("1")
				.currency("USD")
				.date(Date.from(testDate))
				.build();

		final CommissionResponse response = service.calculateCommission(request).block();
		assertNotNull(response);
		assertEquals("0.01", response.getAmount());
		assertEquals("EUR", response.getCurrency());
	}
}
