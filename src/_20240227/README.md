# 2024-02-27

Solutions to HackerRank problems.

## Number Line Jumps (Kangaroo)

- [HackerRank problem](https://www.hackerrank.com/challenges/kangaroo/problem)
- [HackerRank solution](https://www.hackerrank.com/challenges/kangaroo/submissions/code/376366739)

### Solution overview

The position of a kangaroo at any point in time can be calculated by $nv + x$, where $n$ is the number of steps, $v$ is its jump size and $x$ is its initial position on the number line.

We can therefore say that the two 'roos will be at the same place on the number line after $n$ steps when

```math
n v_1 + x_1 = n v_2 + x_2
```

Rearranging gives us

```math
n = \frac{x_1 - x_2}{v_2 - v_1}
```

If $n$ turns out to be a positive integer, we know that they will be (stationary) at the same spot on the number line at some point and we can answer "YES".

There are probably some other edge cases which would necessarily preclude the kangaroos from crossing and can allow us to return "NO" earlier…

Assuming left-to-right (positive) travel, once we know which kangaroo is to the left, we should compare their jump sizes - if the left kangaroo has a smaller jump then it can never catch up.

## The Grid Search

- [HackerRank problem](https://www.hackerrank.com/challenges/the-grid-search/problem)
