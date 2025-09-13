# Paying exactly once

In case you did not figure out how to cope with multiple messages for the the payment, you will see here one solution.

The process of identifying duplicated messages/rest-calls is being called deduplication.

## First try

* start the system
* configure it as you know it (with the broken connection)
* run the python script

assert that the script used much more than 1000 attemps.
assert that exactly 1000€ has been transferred.

this is called exactly once semantic: even if the message has been transferred more than once, that side effect (of paying) has been excecuted exactly once.

## How does it work

Inspect the implemetation.

### the controller

wil just forword the idempotence key to the service that processes the pyment

### the service

the service will try to save the payment with the idempotence key. in case this yields  an certain exception it will throw an duplicate exception.

the duplicate exception let the controller send an http already reported http status code. this will be treated by our http client (the python script as a success case)

In wiche case will the attempt to save led to that exception:

### the entity

we configured the idempotence-key field in the database as unique. so the database will complain when we attemd to save the very same idempotence key



### the learings

cooperative: client and service/server
you have to make sure to retry always with the very same idempotence key

you should consider this when your automativ maybe three attemps fails consecutivly

tools like kafka and other make most times somethign like this under the hood.

exactly once delivery is impossible but exactly once semantic is achivable

do not put effort in sending exactyl once. it it impossible

you have to cope with multiple messages.








