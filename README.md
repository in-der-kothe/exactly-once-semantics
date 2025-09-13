# Avoid paying twice

In the previous chapters, we saw that our system never behaved as we wanted it:
* once we transferred to less money
* the other time to much

We already knew that we must send a message more than once (The two generals)

So the problem is how to handle the case when we receive a message more than once.

This branch will show you **one way** how you could avoid handling a message more than once: idempotence keys...

## Let's start

* start the system as you should be already familar with and configure the broken connection ;-)
* run the provided python script
* you should see in the docker output that the controller now gets an idempotence key.

Check the controller implementation. You will see that it just logs a received idempotence key from the request. The python script will send distinct idempotence keys for distinct transactions. But in case of a retry, it will send the same idempotence key again.

Now it is up to you, change the code, that you will execute a payment only once for the same idempotence key.

# Next step — paying exactly once
Go to branch :computer: [Paying exactly once](https://github.com/in-der-kothe/exactly-once-semantics/tree/code/paying-exactly-once)

```bash
git checkout code/paying-exactly-once
```
