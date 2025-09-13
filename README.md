# Never pay to little

## Prerequisites

See :computer: [A Naive payment system](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/naive-payment-system)

## Our example - A payment service - Recap

In the previous chapter, you saw a pretty easy payment service. You played around with these connections and caused minor troubles and investigated the outcomes. You saw, that you will lose messages, in case of network problems.

Can we just retry to send the message until it succeeds?

Let's check.

In this branch the `mass_test.py` has been changed to retry until it got a proper HTTP-Response.

Let's run it...
### System landscape
![image](architecture.svg)



* start the system with the proper script (build-and-run...)
* from [toxi.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/toxi.http) use the _configure_, the _set upstream-reset-peer toxic_ and the _set downstream-reset-peer toxic_ endpoints.
* from [payment.http](https://github.com/in-der-kothe/exactly-once-semantics/blob/code/never-pay-too-little/payment.http) delete transactions and check the stats endpoint

* run the Python script ```python3 ./mass_test.py```

You will now see that the script used much more attempts to send the money.

Also check how much money has been transferred.

We have more money transferred that we intentionally wanted. This kind of guarantee is being called [at least once (Kafka docu))]([https://docs.confluent.io/kafka/design/delivery-semantics.html](https://docs.confluent.io/kafka/design/delivery-semantics.html#semantic-guarantees)). That means we transferr each message at least once, but maybe even two or more times.


### Ask yourself
- Why does this happen?
- What could this have to do with the metaphor of :book: [The two generals](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/two-generals)?
- How can you make sure that exactly 1000 transactions will be executed?

# Next step - Avoid paying twice
Go to branch :computer: [Avoid paying twice](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/avoid-paying-twice)

```bash
git checkout code/avoid-paying-twice
```
