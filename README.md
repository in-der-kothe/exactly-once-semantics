# The eight fallacies of distributed computing[^1]

Originated at sun microsystems - L. Peter Deutsch, one of the original sun 'fellows', first created a list of seven fallacies in 1994 around 1997

James Gosling, another sun fellow and the inventor of Java, added the eigth fallacy

1. The network is reliable
2. Latency is zero
3. Bandwidth is infinite
4. The network is secure
5. Topology doesn't change
6. There is one administrator
7. Transport cost is zero
8. The network is homogeneous

## Resulting from this is - among others - the exactly-once delivery guarantee fallacy

Exactly-once delivery guarantee is the guarantee that a message can be delivered to a recipient once, and only once. While having a message be delivered only once by a recipient, is the norm, it is impossible to guarantee it.[^2]

[^1]: https://en.wikipedia.org/wiki/Fallacies_of_distributed_computing
[^2]: https://blog.bulloak.io/post/20200917-the-impossibility-of-exactly-once/
