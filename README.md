# Exactly one delivery

> Exactly-once delivery guarantee is the guarantee that a message can be delivered to a recipient once, and only once. While having a message be delivered only once by a recipient, is the norm, it is impossible to guarantee it.[^1]

Even if the problem of the [two generals](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/two-generals)[^2] is impossible to solve there are algorithms & strategies to ensure [common knowledge](https://github.com/in-der-kothe/exactly-once-semantics/tree/theory/common-knowledge) of the imformation.

To test strategies and the problems behind them you find different approaches in this repository under `code`.

[^1]: https://blog.bulloak.io/post/20200917-the-impossibility-of-exactly-once/
[^2]: https://en.wikipedia.org/wiki/Two_Generals%27_Problem