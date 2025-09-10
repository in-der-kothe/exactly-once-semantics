# A pretty naive payment service

## Prerequisites

* You need to have Java 21 installed. Consider using https://sdkman.io/ to install it.
  * `sdk install java 21.0.7-tem` # as an example
* You should be able to access the internet to retrieve dependencies
* You should have https://www.python.org/ installed. Any version from the 3 series should be enough.
* You should have https://pypi.org/project/pip/ installed. Check it with: `python3 -m pip`
* You should have docker and docker-compose installed

## Our example - A naive payment service

In this example we will see an easy payment service. I will use an in memory database to store each processed transaction.\
To keep the system not to complicated, we will consider a transaction to be processed, when it is stored in the database.\
Each entry in the database will be one transaction.\
We will show how the payment service will behave in case of network issues.

We provided a file with the configured rest endpoints[^1]: [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http)

To mock network issues we will put a proxy server between the Rest-Client and the Payment Service.\
For that we use [ToxyProxy](https://github.com/Shopify/toxiproxy).

Both services start via `docker-compose.yml` / [docker-compose.yml](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/docker-compose.yml).\
The system landscape will look like this:\
![image](architecture.svg)

So in each case the client invokes our Payment Service:
 - with `localhost:8888` the request will effecticly go through toxy proxy to the payment service.
 - with `localhost:8080` the request go straight to the payment service.

## Setup the system
Choose the appropriate command for you
```bash
./build-and-run-podman.sh
./build-and-run-docker-desktop.sh
```

Now you should be able to use the configured rest endpoints from `payment.http` / [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payment.http) and `toxy.http` / [toxy.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/toxy.http).

Let's take this tour (please have the picture above in mind):\

### Test the connection to the service
Use WITHOUT proxy - play a bit around
1. from `payment.http` -> use `STATS-Endpoint` to assure no money has been transferred
2. from `payment.http` -> use `DIRECT-Payments-Endpoint` ONE time to transfer 1€.
3. from `payment.http` -> use `STATS-Endpoint` again to assure the 1€ has been transfered
4. from `payment.http` -> use `Delete all transactions` to delete all the money :money_with_wings:

### Test the connection with issues
Use WITH proxy / configure proxy
1. from `payment.http` -> use `TOXYPROXY-Payments-Endpoint` to assure that it does NOT work - you need to have a configured toxy proxy
2. from `toxy.http` -> use `Configure Proxy` to configure the toxy proxy 
3. from `payment.http` -> use `TOXYPROXY-Payments-Endpoint` to transfer 1€
4. from `payment.http` -> use `STATS-Endpoint` to verify that the payment has been processed

You can also here play around, to gain some more money or delete all your savings. Just make sure, everthing works fine.

## A distributed system under stress
Now we check if the service works for one thousand payments.

In `payments.csv` / [payments.csv](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/payments.csv) you find a prepared list of 1000 transactions.\
There is also a prepared python script `mass_test.py` / [mass_test.py](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/naive-payment-system/mass_test.py) that uses the list as input and issues a post call agains the payment endpoint.

### Setup for the python script
```
python3 -m venv venv
source ./venv/bin/activate
pip install -r requirements.txt
```

### perform thousand transactions
1. from `payment.http` -> use `Delete all transactions` to clear all transactions
2. run the python script -> `python3 mass_test.py`
3. from `payment.http` -> use 'STATS-Endpoint' to assert the 1000€ has been transferred

But what will happen when the network connection is unstable?

### perform thousand transactions with a connection under stress
Configure toxi proxy to gain:
  - a broken connection **before** the request reaches the payment services, with a likelyhood of 30%
  - a broken connection **after** the request should return to client, again with a likelyhood of 30%

Use the following endpoints from `toxy.http` to reach the issues / problems in the conections:
  - `set upstream-reset-peer toxic`
  - `set downstream-reset-peer toxic`

Delete all transactions via `payment.http` - `Delete all transactions`

Run the python script again - `python3 mass_test.py`
  
Check now again with `payment.http` - `STATS-Endpoint`how much money was really transferred\
Instead of 1000€ you should see, that there was transferred much less then expected\

How can we make sure, that we exactly will transfer 1000€ (in 1000 Transactions)?

# Next step - Never pay to little
```bash
git checkout code/never-pay-too-little
```

[^1]: IntelliJ - this should work out-of-the-box\
VS Code - you need this plugin: [Humao Rest Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
