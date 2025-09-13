# Never pay to little

## Prerequisites

See :computer: [A Naive payment system](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/naive-payment-system)

## Our example - A payment service - Recap

In the previous chapter, you saw a pretty easy payment service. You played around with these connections and caused minor troubles and investigated the outcomes. You saw, that you will lose messages, in case of network problems.

Can we just retry to send the message until it succeeds?

Let's check.

In this branch the `mass_test.py` / [mass_test.py](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/mass_test.py) has been changed to retry until it got a proper HTTP-Response.

Let's run it...

<details>
  <summary>Recap - System landscape, setup, commands</summary>
  
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
./build-and-run-podman.sh

python3 -m venv venv
source ./venv/bin/activate
pip install -r requirements.txt
```
</details>

## Retry the transactions
Configure your system as before:
* `toxi.http` -> `set upstream-reset-peer toxic` & `set downstream-reset-peer toxic`
* `payment.http` -> `Delete all transactions` & `STATS-Endpoint`
* run the Python script `python3 ./mass_test.py`

You will now see that the script used much more attempts to send the money.

Also check how much money has been transferred.

We have more money transferred that we intentionally wanted. This kind of guarantee is being called [at least once (Kafka doku)](https://docs.confluent.io/kafka/design/delivery-semantics.html]https://docs.confluent.io/kafka/design/delivery-semantics.html#semantic-guarantees). That means we transferr each message at least once, but maybe even two or more times.

### Ask yourself
- Why does this happen?
- What could this have to do with the metaphor of :book: [The two generals](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/two-generals)?
- How can you make sure that exactly 1000 transactions will be executed?

# Next step - Avoid paying twice
Go to branch :computer: [Avoid paying twice](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/avoid-paying-twice)

```bash
git checkout code/avoid-paying-twice
```
