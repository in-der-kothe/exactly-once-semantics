# A pretty naive payment service

## Prerequisites

* You need to have Java 21 installed. Consider using [SDKMan](https://sdkman.io/) to install it.
  * `sdk install java 21.0.7-tem` # to install a appropriate Java-Version 
* You should be able to access the internet to retrieve dependencies
* You should have [Python](https://www.python.org/) installed. Any version from the 3 series should be enough.
* You should have [PIP](https://pypi.org/project/pip/) installed. Check it with: `python3 -m pip`
* You should have docker and docker-compose installed
* No special IDE is needed, however [IntelliJ IDEA](https://www.jetbrains.com/idea/) / [VS Code](https://code.visualstudio.com/) will be easiest to follow the examples. In case you want to use VScode, consider to instal this extension: [Rest Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)

## Our example - A naive payment service

In this example we will work with an easy payment service. I will use an in memory database ([H2](https://www.h2database.com/html/main.html)) to store each processed transaction.

To keep the system simple, we will consider a transaction as processed when it is stored in the database.

We will show demonstrate how a naive payment service will behave in case of network issues.

## How to use this project

The System is composed out of two services.

![image](architecture.svg)

* The Payment service running on port 8080
* A Proxy-Server ([ToxiProxy](https://github.com/Shopify/toxiproxy)) so simulate, that could be used to simulate networking problems

The client can decide whether he wants to connect directly to the payment service (on port 8080) or via the Proxy (on port 8888).

The Proxy Server and the Payment Service, are used via a [REST-API](https://ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm)

We provided two file to make it easy for you to interact with both of them:

 * `payment.http` / [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) 
 * `toxi.http` / [toxi.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/toxi.http).

## Setup the system
in case you have docker:
```bash
./build-and-run-docker.sh
```
and when you have podman
```bash
./build-and-run-docker.sh
```

Now use the above-mentioned REST-Endpoints to interact with the system:

 - with `localhost:8888` the request will effectively go through toxy proxy to the payment service.
 - with `localhost:8080` the request go straight to the payment service.

Let's take a tour:

### Test the direct connection to the service
Use WITHOUT proxy - play a bit around
1. from `payment.http` -> use `STATS-Endpoint` to assure no money has been transferred
2. from `payment.http` -> use `DIRECT-Payments-Endpoint` ONCE  to transfer 1€.
3. from `payment.http` -> use `STATS-Endpoint` to assure the 1€ has been transferred
4. from `payment.http` -> use `Delete all transactions` to delete all the money :money_with_wings:

### Test the connection with issues
Use WITH proxy / configure proxy
1. from `payment.http` -> use `TOXIPROXY-Payments-Endpoint` to assure that it does NOT work - you need to have a configured toxi proxy
2. from `toxi.http` -> use `Configure Proxy` to configure the toxi proxy 
3. from `payment.http` -> use `TOXIPROXY-Payments-Endpoint` to transfer 1€
4. from `payment.http` -> use `STATS-Endpoint` to verify that the payment has been processed

You should also play around here, to gain some more money or delete all your savings. Just make sure, everything works fine.

## Some more load / the system under stress
Now we check if the service works for one thousand payments.

In [payments.csv](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payments.csv) you find a prepared list of 1000 transactions.\
There is also a prepared python script [mass_test.py](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/mass_test.py) that uses the list as input and issues a post call agains the payment endpoint.

### Setup the python script
```
python3 -m venv venv
source ./venv/bin/activate
pip install -r requirements.txt
```

### Run it with stable connection
* from `payment.http` -> use `Delete all transactions` to clear all transactions 
* run the python script -> `python3 mass_test.py`
* from `payment.http` -> use 'STATS-Endpoint' to assert the 1000€ has been transferred 
* The output of the script will tell you that it took 1000 attempts to transfer the money

But what will happen when the network connection is unstable?

### Run it with an unstable connection
Configure toxi proxy (`toxi.http` -> `Configure Proxy`) to gain a broken connection **before** the request reaches the payment services, with a likelihood of 30% and a broken connection **after** the request should return to the client, again with a likelihood of 30%.

* Use the following endpoints from `toxy.http` to setup the issues / problems in the connection:
  - `set upstream-reset-peer toxic`
  - `set downstream-reset-peer toxic`

* Delete all transactions via `payment.http` - `Delete all transactions`
* Run the python script again - `python3 mass_test.py`
  
Check now again with `payment.http` - `STATS-Endpoint`how much money was really transferred\
Instead of 1000€ you should see, that there was transferred much less than expected.
Still there where 1000 attempts to send the money.

So for 1000 attempts to send the message of paying, we succeeded in an average of less than once per attempt.

This is called at most guarantee. Consider reading [this from the Apache Kafka docu](https://docs.confluent.io/kafka/design/delivery-semantics.html).

How can we make sure that we exactly will transfer 1000€ (in 1000 Transactions)?

Ask yourself:

What could you infer in case you issue a Rest-Call in case of a broken connection:

* did the payment succeed?
* or did it fail?

try this out, with this setup.

# Next step - Never pay to little
Go to branch :computer: [Never pay too little](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/never-pay-too-little)

```bash
git checkout code/never-pay-too-little
```

[^1]: IntelliJ - this should work out-of-the-box\
VS Code - you need this plugin: [Humao Rest Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
