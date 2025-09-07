# A pretty naive Payment Service

## Prerequisites

* You need to have Java 21 installed. Consider using https://sdkman.io/ to install it.
  * `sdk install java 21.0.7-tem` # as an example
* You should be able to access the internet to retrieve dependencies
* You should have https://www.python.org/ installed. Any version from the 3 series should be enough.
* You should have https://pypi.org/project/pip/ installed. Check it with: `python3 -m pip`
* You should have docker and docker-compose installed

## Our example - A payment service

In this example we will see an pretty easy payment service. I will use an in memory database to store each processed transaction.
To keep the system not to complicated, we will consider a transaction to be processed, when it is stored in the database.
Each entry in the database will be one transaction.

The API is pretty simple.

I provided a file with the configured rest endpoints: [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http)

You can execute those requests directly by clicking into the file.[^1]

As we will show how the payment service will behave in case of network issues. We will put a proxy server between the Rest-Client and the Payment Service. We will use https://github.com/Shopify/toxiproxy for this purpose.

We will start both service, via a `docker-compose.yml`: 

![image](architecture.svg)

So in each case the client invokes our Payment Service via localhost:8888, the request will effecticly go through toxy proxy. Requests via localhost:8080 go straight to the payment service.

```bash
cd payment
./mvnw clean install
cd ..
docker compose build
docker compose up
```

Now you should be able to use the configured rest endpoints form `payment.http` and `toxy.http`.

Let's take this tour (please have the picture above in mind): \
Use WITHOUT proxy
1. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the stats endpoint to assert no money has been transferred
2. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the _direct_ connected POST payments endpoint to transfered one €.
3. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the stats endpoint to assert the one € has been transfered

Use WITH proxy / configure proxy
1. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the toxy proxy version of the POST payments endpoint to assure that it does NOT work. you need to have a configured toxy proxy
2. from [toxy.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/toxy.http) -> use configure proxy to configure a toxy proxy 
3. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the toxy proxy version of the POST endpoint to transfer one €
4. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the stats endpoint to verify that the payment has been processed

Now we check that it works for one thousand payments.

I prepared a list of to be processed transactions in `payments.csv`.
I prepared a python script that reads this list and issues a post call agains the payment endpoint.

_setup the python script_
```
python3 -m venv venv
source ./venv/bin/activate
pip install -r requirements.tx
```

Now check this thousand transactions

1. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use the delete endpoint to clear all transactions
2. run the python script: `python3 mass_test.py`
3. from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) -> use status endpoint to assert 1000 € has been transferred.

Looks good.

But what will happen when the network connection is unstable?

Now, we configure toxi proxy to have:

* a broken connection **before** the request reaches the payment services, with a likelyhood of 30%
* a broken connection **after** the request should return to client, again with a likelyhood of 30%

To do so, use the following endpoints from [toxy.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/toxy.http)

* set upstream-reset-peer toxic
* set downstream-reset-peer toxic



* delete all transactions with the delete Endpoint in `payment.http`.
* Run the script again: `python3 mass_test.py`
* check again with the stats endpoint from `payment.http` how much money was transferred

Instead of 1000€ we should see, that we transferred much less.

How can we make sure, that we exactly will transfer 1000€ (in 1000 Transactions)?

[^1]: IntelliJ - this should work out-of-the-box\
VS Code - you need this plugin: [Humao Rest Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)