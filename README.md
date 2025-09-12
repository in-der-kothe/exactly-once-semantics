# Avoid paying twice

So we realy want to achive pay to less, but also not to much. So let's see how we can achive this.

This branch will show you **one way**  how you could achive this: idempotence keys...

Choose the appropriate command for you:
```bash
./build-and-run-podman.sh
./build-and-run-docker.sh
```

Now you should be able to use the configured rest endpoints form `payment.http` and `toxy.http`.

Setup the already familiar broken connection ;-)

run the provided python script

you should see in the docker output, that the controller now get's an idempotence key.

check the controller implementation

we prepared that python script, that it will send unique idempotence keys for distince transactions. but for a retry for the same transaction it will send the very same idemtpotence key...

The implementation of the payment service will only log out the idemtpotence key


Let's take this tour (please have the picture above in mind): \
Use WITHOUT proxy
1. from payment.http -> use the stats endpoint to assert no money has been transferred
2. from payment.http -> use the _direct_ connected POST payments endpoint to transfered one €.
3. from payment.http -> use the stats endpoint to assert the one € has been transfered

Use WITH proxy / configure proxy
1. from payment.http -> use the toxy proxy version of the POST payments endpoint to assure that it does NOT work. you need to have a configured toxy proxy
2. from toxy.http -> use configure proxy to configure a toxy proxy 
3. from payment.http -> use the toxy proxy version of the POST endpoint to transfer one €
4. from payment.http -> use the stats endpoint to verify that the payment has been processed

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

1. from payment.http -> use the delete endpoint to clear all transactions
2. run the python script: `python3 mass_test.py`
3. from payment.http -> use status endpoint to assert 1000 € has been transferred.

Looks good.

But what will happen when the network connection is unstable?

Now, we configure toxi proxy to have:

* a broken connection before the request reaches the payment services, with a likelyhood of 30%
* a broken connection after the request should return to client, again with a likelyhood of 30%

To do so, use the following endpoints from `toxy.http`

* set upstream-reset-peer toxic
* set downstream-reset-peer toxic



* delete all transactions with the delete Endpoint in `payment.http`.
* Run the script again: `python3 mass_test.py`
* check again with the stats endpoint from `payment.http` how much money was transferred

Instead of 1000€ we should see, that we transferred much less.

How can we make sure, the we exactly will transfer 1000€ (in 1000 Transactions)?

# Does retrying solve the problem?

the script `mass_test.py` has now been changed to retry until it get an proper http status.

Run it again and check with the stats endpoint from `payment.http`.

You will see that, now we have transferred to much money.

Why does this happen?

What has this to do with the two general problem?

How can you make sure, that exactly 1000 transactions will be executed?

```bash
git stash
git switch code/paying-exactly-once
```


