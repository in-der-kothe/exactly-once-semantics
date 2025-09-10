# Never pay to little

## Prerequisites

See :computer: [A Naive payment system](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/naive-payment-system)

## Our example - A payment service - Recap

In the previous chapter you saw a pretty easy payment service with its different transactions. You as well played around with these connections and caused minor troubles and investigated the outcomes.

### System landscape
![image](architecture.svg)

### REST-Services and known commands / REST-calls
- `payment.http` / [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/payment.http)
  - use `STATS-Endpoint` to assure no money has been transferred
  - use `DIRECT-Payments-Endpoint` ONE time to transfer ONE €.
  - use `Delete all transactions` to delete all the money 💸
- `toxy.http` / [toxy.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/toxy.http)
  - use 'Configure Proxy' to configure the toxy proxy
  - `set upstream-reset-peer toxic` - a broken connection before the request reaches the payment services, with a likelyhood of 30%
  - `set downstream-reset-peer toxic` - a broken connection after the request should return to client, again with a likelyhood of 30%

## System setup
Make sure, all services are shutdown and the system is 'clear' to start again with a slightly different behaviour.\

Choose the appropriate command for you:
```bash
./build-and-run-podman.sh
./build-and-run-docker-desktop.sh
```

# Does retrying solve the problem?
The previous chapter showed, that with a broken connection

 - before the request reaches the payment service
 - after the request returns to the client

there is no chance to gain 1000€ within 1000 transactions

## Just do it again, and again, and again, ...
The script `mass_test.py` / [mass_test.py](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/mass_test.py) has now been changed to **retry until it get a proper http status**.

### Try again
Run the script with `python3 mass_test.py` and check with `STATS-Endpoint` how much money you earned.

You should see that now, there has been transferred too much money.

### Investigate by yourself
- Why does this happen?
- What could this has to do with the methaphor of :book: [The two generals](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/two-generals)?
- How can you make sure that exactly 1000 transactions will be executed?
