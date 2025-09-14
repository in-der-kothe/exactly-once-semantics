# Paying exactly once

## Our example - A payment service - Solution from previous chapter

In case you did not figure out how to cope with multiple messages for one the payment transaction in the previous chapter :computer: - [Avoid paying twice](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/avoid-paying-twice), you will see here **one** solution.

The process of identifying duplicated messages/rest-calls is being called **deduplication**.

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

## Let's try it

* start the system
* configure it as you know it (with the broken connection)
* run the python script

After the python script finished
* assure that the script used much more than 1000 attemps.
* assure that exactly 1000€ has been transferred.

This is called **exactly once semantic**: even if the message has been transferred more than once, that side effect (of paying) has been excecuted exactly once.

> [!NOTE]
> How does it work? - Inspect the implementation!

### The controller

It will just forward the idempotence key to the service that processes the payment.

### The service

The service will try to save the payment with the idempotence key. In this case this yields a certain exception - it will throw a duplicate exception. 

The duplicate exception let the controller to send a **already reported http status code - 208**[^1]. This will be treated by our http client (the python script as a success case).

In which case will the attempt to save led to that exception:

### The entity

We configured the **idempotence-key field in the database as unique**. So the database will complain when we attemd to save the very same idempotence key.

## The learnings

Cooperative: client and service/server
- You have to make sure to **retry always with the very same idempotence key**
  - You should consider this when your automativ maybe three attemps fails consecutivly
- Tools like Kafka and other make most times something like this under the hood
- **Exactly once delivery is impossible** but **exactly once semantic is achievable**
- Do **never put effort in sending exactly once**. It is - mathematically proven - impossible
  - You have to cope with multiple messages.

# Summary / Conclusions - The Fallacies
Go to branch :book: [The eight fallacies](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/fallacies)

```bash
git checkout theory/fallacies
```

[^1]: https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
