# Submission details for online-stock-span

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 32 ms (Better than 46.19% of submissions)
- **Memory:** 54.4 MB (Better than 58.22% of submissions)

## Solution Code
```java
class StockSpanner {
    private Stack<int[]> stk;

    public StockSpanner() {
        stk = new Stack<>();
    }

    public int next(int price) {
        int span = 1;

        while (!stk.isEmpty() && price >= stk.peek()[0]) {
            span += stk.peek()[1];
            stk.pop();
        }

        stk.push(new int[] { price, span });

        return span;
    }
}

/**
 * Your StockSpanner object will be instantiated and called as such:
 * StockSpanner obj = new StockSpanner();
 * int param_1 = obj.next(price);
 */
```

## Detailed Explanation
## 1. Algorithm Analysis

The classic **stock‑span** problem asks, for each day `i`, how many consecutive days *before* (and including) `i` had a closing price `≤` the price on day `i`.  
A naïve solution would scan backwards for each query, yielding O(n²).  
The accepted solution uses a **monotonic stack** to achieve *amortised* O(1) per call.

### Data structure

```
Stack<int[]> stk   // each element is {price, span}
```

* `price` – the price on a day
* `span`  – the pre‑computed span of that day

The stack is kept **strictly decreasing in price** (top has the smallest price among the elements in the stack).  
When a new price `p` arrives:

1. Start with `span = 1` (the current day itself).
2. While the stack top’s price is ≤ `p`:
   * The top day and all its *consecutive* days that were already collapsed are part of the span.
   * Add that `top.span` to the current `span`.
   * Pop the top because it can never contribute to spans of later days.
3. Push a new element `{p, span}` on the stack.
4. Return `span`.

Because each day’s entry is **pushed once** and **popped at most once**, the total number of stack operations over *n* calls is *O(n)*, giving an amortised O(1) per query.

--------------------------------------------------------------------

## 2. Step‑by‑Step Dry Run (Sample Test Case)

The sequence of operations from the test case is:

```
["StockSpanner","next","next","next","next","next","next","next"]
[[],[100],[80],[60],[70],[60],[75],[85]]
```

| Call | Price `p` | Initial `span` | Stack before `while` | `while` loop trace | After `while` | Stack after `push` | Returned `span` |
|------|-----------|-----------------|-----------------------|---------------------|----------------|---------------------|-----------------|
| 1    | 100       | 1               | empty                 | – (stack empty)     | 1              | `{100,1}`           | 1               |
| 2    | 80        | 1               | `{100,1}`              | – (top.price 100 > 80) | 1              | `{100,1}`, `{80,1}` | 1               |
| 3    | 60        | 1               | `{100,1}` `{80,1}`     | – (top.price 80 > 60) | 1              | `{100,1}` `{80,1}` `{60,1}` | 1               |
| 4    | 70        | 1               | `{100,1}` `{80,1}` `{60,1}` | <br>pop `{60,1}` → `span=2`<br>stop (top.price 80 > 70) | 2 | `{100,1}` `{80,1}` `{70,2}` | 2 |
| 5    | 60        | 1               | `{100,1}` `{80,1}` `{70,2}` | – (top.price 70 > 60) | 1 | `{100,1}` `{80,1}` `{70,2}` `{60,1}` | 1 |
| 6    | 75        | 1               | `{100,1}` `{80,1}` `{70,2}` `{60,1}` | <br>pop `{60,1}` → `span=2`<br>pop `{70,2}` → `span=4`<br>stop (top.price 80 > 75) | 4 | `{100,1}` `{80,1}` `{75,4}` | 4 |
| 7    | 85        | 1               | `{100,1}` `{80,1}` `{75,4}` | <br>pop `{75,4}` → `span=5`<br>pop `{80,1}` → `span=6`<br>stop (top.price 100 > 85) | 6 | `{100,1}` `{85,6}` | 6 |

**Resulting spans**: `[1, 1, 1, 2, 1, 4, 6]`.  
These are exactly the expected outputs for the stock‑span problem.

--------------------------------------------------------------------

## 3. Complexity Analysis

| Operation | Average | Worst‑case |
|-----------|---------|------------|
| `next(int price)` | **O(1)** amortised | **O(n)** (in the pathological case of a strictly decreasing sequence, every call pops nothing; in strictly increasing sequence, each element is popped once, still amortised O(1)) |
| Total for `m` calls | **O(m)** | **O(m)** |
| Auxiliary space | **O(n)** (stack holds at most one entry per day) | **O(n)** |

*Justification*  
- **Time**: Each array entry `{price, span}` is pushed once and popped at most once. The `while` loop thus runs a total of `O(m)` times over all calls, giving an amortised O(1) per `next`.
- **Space**: The stack can contain at most `m` entries, one per price queried. No additional data structures grow with input size.

--------------------------------------------------------------------

### Takeaway

The solution cleverly turns a seemingly cumulative problem into a *monotonic stack* problem, providing an elegant and efficient O(1) amortised algorithm for each day’s stock span. The dry run above demonstrates explicitly how spans are built and how older entries are collapsed, which is the key intuition behind the stack approach.
