# TT_API_COMMISSION

This is my project related to the Api Commission test, I decided to opt for a reactive and functional approach 
(with the pros and cons that comes with it).

## How to Start

Run the application and make a post at this endpoint: http://127.0.0.1:8080/api/v1/commission-calculation  

The **required body** is

```
{
    "client_id": integer,
    "amount": string,
    "currency": string,
    "date": "yyyy-MM-dd"
}
```

The **expected response** is

```
{
    "amount": string,
    "currency": string
}
```

## Rules

#### 1) By default the price for every transaction is 0.5% but not less than 0.05€.

#### 2) Transaction price for the client with ID of 42 is 0.05€ (unless other rules set lower commission).

#### 3) Client after reaching transaction turnover of 1000.00€ (per month) gets a discount and transaction commission is 0.03€ for the following transactions.


## Addon
In order to apply rule #3 I decided to mock a Client collection (mongoDB for example) and made a mock repository to get different type of Client.

The client has the following structure:

```
{
    clientId: 123,
    transactions: [],
    type: DEFAULT
}
```

I also decided to modify rule #2 (it's not the client_id that specifies if a client is special or not), each client has type that define how the client is treated for the calculation of the fee.

There are current 3 types (but could be many more!) that show how the application can be personalized for each type of client:

* DEFAULT: Client with no specific benefit (Rule 1 and 3 applied)
* SPECIAL: Client with fixed fee at max of 0.05€ (Rule 2 and Rule 3 applied)
* TEST_TYPE: Client with a special discount after 10 transaction, pays only 0.01€ for the next one (Rule 1 and Rule 3 applied)

## Implementation
The application start with the call of the controller which redirect to the service the request in order to be checked and get the response. 

The service after checking the request, call the ClientService in order to get the client from the **client_id**, from the client it's calculated the fee for the current transaction.

If the client is of type **DEFAULT** there are 3 possible ways which the fee is calculated:

1) The sum of previous transaction of current month is >= 1000€, the fee is: "0.03"
2) The currency is not on Euros, so it needs to be converted calling ApiExchangeService and then the fee is calculated normally
3) The currency is on Euros, so it doesn't need to be converted and the fee is calculated normally.

If the client is not **DEFAULT** there are 4 possible ways:
1) The sum of previous transaction of current month is >= 1000€ the fee is: "0.03"
2) The client try to apply his "benefit" to get special fee price
3) The currency is not on Euros, so it needs to be converted calling ApiExchangeService and then the fee is calculated normally
4) The currency is on Euros, so it doesn't need to be converted and the fee is calculated normally.

If the fee calculated is lower than 0.05€ as per Rule #1 then the fee with be 0.05€

### Exceptions

* CommissionException: Any parameter on the request is invalid
* ApiExchangeException: The call to the API to get exchange failed for any reason
* ExchangeCurrencyException: The currency on the request is invalid
* ClientNotFoundException: The client_id on the request is not found
    
### Test

I made two different classes for test:

* CommissionServiceTest: unit test of the service
* CommissionControllerTest: integration test of the controller


### Duration: 5 hours.


## License

Copyright 2022 Giampaolo Conca

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
