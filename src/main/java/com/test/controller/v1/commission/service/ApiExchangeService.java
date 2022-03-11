package com.test.controller.v1.commission.service;

import static com.test.controller.v1.common.Constant.DATE_PATTERN;
import static com.test.controller.v1.common.Constant.EXCHANGE_ENDPOINT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.google.gson.Gson;
import com.test.controller.v1.commission.exception.ApiExchangeException;
import com.test.controller.v1.commission.exception.ExchangeCurrencyException;
import com.test.controller.v1.commission.model.ApiExchange;
import com.test.controller.v1.commission.model.CommissionRequest;

import io.vavr.control.Try;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiExchangeService {
	public double getExchange(final CommissionRequest commissionRequest) {
		final Request request = constructRequest(commissionRequest.getDate());
		final Call call = new OkHttpClient().newCall(request);

		final ApiExchange apiExchange = Try.of(() -> {
			final Response response = call.execute();

			final String json = response.body().string();
			return new Gson().fromJson(json, ApiExchange.class);
		}).filter(Objects::nonNull).getOrElseThrow(ApiExchangeException::new);

		return getCurrency(apiExchange, commissionRequest.getCurrency());
	}

	private double getCurrency(final ApiExchange exchange, final String currency) {
		return Try
				.of(() -> exchange.getRates().get(currency))
				.filter(Objects::nonNull)
				.getOrElseThrow(ExchangeCurrencyException::new);
	}

	private Request constructRequest(final Date date) {
		final DateFormat df = new SimpleDateFormat(DATE_PATTERN);
		final String formattedDate = df.format(date);

		return new Request.Builder().url(EXCHANGE_ENDPOINT + formattedDate).build();
	}
}
