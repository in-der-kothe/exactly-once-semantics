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

Now it is up to you, change the code, that you will execute a payment only once for the same idempotence key.

ask seniorita biance or seniore marce in case of questions

```bash
git stash
git switch code/paying-exactly-once
```


