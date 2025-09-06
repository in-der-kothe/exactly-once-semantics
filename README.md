# A pretty naive Payment Service

## Prerequisites

* You need to have Java 21 installed. Consider using https://sdkman.io/ to install it.
* You should be able to access the internet to retrieve dependencies
* You should have https://www.python.org/ installed. Any version from the 3 series should be enough.
* You should have https://pypi.org/project/pip/ installed. Check it with: `python3 -m pip`
* You should have docker and docker-compose installed

## Our example - A payment service

In this example we will see an pretty easy payment service. I will use an in memory database to store each processed transaction.
To keep the system not to complicated, we will consider a transaction to be processed, when it is stored in the database.
Each entry in the database will be one transaction.

The API is pretty simple.

I provided a file with the configured rest endpoints: `payment.http`

You can execute those requests directly by clicking into the file. In Intellij this should work without any special plugin and in case of VS Code you need this plugin

* VS Code: Rest Client from humao (humao.rest-client)

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

Let's take this tour:

1. from payment.http -> use the stats endpoint to assert no money has been transferred
2. from payment.http -> use the _direct_ connected POST payments endpoint to transfered one €.
3. from payment.http -> use teh stats endpoint to assert the one € has been transfered
4. from payment.http -> use the toxy proxy version of the POST payments endpoint to assert that it does not work. you need to have a configured toxy proxy
5. from toxy.http -> use configure proxy to configure a toxy proxy 
6. from payment.http -> use the toxy proxy version of the POST endpoint to transfer one €
7. from payment.http -> use the stats endpoint to verify

Now, you see that it works.

Now we check that it works for one thousand payments.

I prepared a list of to be processed transactions in `payments.csv`.
I prepared a python script that reads this list and issues a post call agains the payment endpoint.

_setup the python script_
```
python3 -m venv venv
source ./venv/bin/activate
```

Now check this thousand transactions

1. from payment.http -> use the reset endpoint to clear all transactions
2. run the python script: `python3 mass_test.py`
3. from payment.http -> use status endpoint to assert 1000 € has been transferred.

Looks good.

But what will happen when the network connection is unstable.



