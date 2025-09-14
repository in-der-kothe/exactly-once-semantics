# Avoid paying twice

## Our example - A payment service - Recap

In the previous chapters, we saw that our system never behaved as expected:

* once we transferred to less money
* the other time too much

We already knew that we must send a message more than once ( :book: [The two generals](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/two-generals))

So the problem is how to handle the case when we receive a message more than once.

This branch will show you **one way** how you could avoid handling a message more than once: idempotence keys...
<details>
  <summary>Prerequisites, Recap - System landscape, setup, commands</summary>

## Prerequisites

See :computer: [A Naive payment system](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/naive-payment-system)
  
### System landscape
![image](architecture.svg)

### REST-Services and known commands / REST-calls
- `payment.http` / [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/payment.http)
  - use `STATS-Endpoint` to assure no money has been transferred
  - use `DIRECT-Payments-Endpoint` ONE time to transfer ONE €.
  - use `Delete all transactions` to delete all the money 💸
- `toxi.http` / [toxi.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/toxi.http)
  - use 'Configure Proxy' to configure the toxi proxy
  - `set upstream-reset-peer toxic` - a broken connection before the request reaches the payment services, with a likelyhood of 30%
  - `set downstream-reset-peer toxic` - a broken connection after the request should return to client, again with a likelyhood of 30%

### System setup -  not essential but maybe helpful
Make sure, all services are shutdown and the system is 'clear' to start again with a slightly different behaviour.

Setup your system as before:
```bash
./build-and-run-docker.sh
# or
./build-and-run-podman.sh

# you can skip this, when you still have the venv directory from the previous chapter and have activated that environment
python3 -m venv venv
source ./venv/bin/activate
pip install -r requirements.txt
```
</details>


## Let's start

* start the system as you should be already familar with and configure the broken connection ;-)
* run the provided Python script
* you should see in the docker output that the Controller gets an idempotence key.

Check the controller implementation. 
You will see that it just logs a received idempotence key from the request. 
The python script will send distinct idempotence keys for distinct transactions. But in case of a retry, it will send the same idempotence key again.

Now it is up to you, change the code, that you will execute a payment only once for the same idempotence key.

# Next step — paying exactly once
Go to branch :computer: [Paying exactly once](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/paying-exactly-once)

```bash
git checkout code/paying-exactly-once
```
