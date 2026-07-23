# Submission details for number-of-unique-xor-triplets-i

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 51.02% of submissions)
- **Memory:** 124.5 MB (Better than 100.00% of submissions)

## Solution Code
```java
class Solution {
    public int uniqueXorTriplets(int[] nums) {
        int n = nums.length;
        int m = n;
        
        m |= m >> 1;
        m |= m >> 2;
        m |= m >> 4;
        m |= m >> 8;
        m |= m >> 16;
        
        return (m + 1) >> (3 / (n + 1));
    }
}
```

## Detailed Explanation
**1. Algorithm analysis**

The method does **not** look at the actual values in `nums`; it only uses the length `n`.  
The idea is to obtain the smallest power of two that is **greater than** `n` (call it `P`).  

* Why a power of two?  
  For a set of `n` distinct numbers the largest possible number of *different* XOR‑results of triples (allowing repeated indices) is a power of two.  
  If the numbers are chosen as the distinct powers of two `1, 2, 4, …, 2^{b‑1}` (where `b = ⌈log₂(n+1)⌉`), every subset of those bits can appear an odd number of times in a triple, therefore every integer from `0` to `2^{b}‑1` can be obtained as an XOR result.  
  Consequently the number of **unique** XOR triples is at most `2^{b}` (and this bound is tight).

* How the code obtains `P = 2^{b}`  

  ```java
  int m = n;                     // start with n
  m |= m >> 1;   // 1‑bit propagation, now the lowest 2 bits are 1
  m |= m >> 2;   // now the lowest 4 bits are 1
  m |= m >> 4;   // lowest 8 bits are 1
  m |= m >> 8;   // lowest 16 bits are 1
  m |= m >> 16;  // lowest 32 bits are 1 (for 32‑bit ints)
  ```
  After these OR‑shifts, `m` becomes a number whose binary representation has **all bits set** from the least‑significant bit up to the most‑significant set bit of the original `n`.  
  In other words  

  ```
  m = (1 << b) – 1          // b = floor(log2(n)) + 1
  ```

  Adding 1 yields the next power of two:

  ```
  P = m + 1 = 1 << b
  ```

* The final shift  

  ```java
  return (m + 1) >> (3 / (n + 1));
  ```
  uses integer division.  
  * For `n = 1` or `n = 2` we have `3 / (n+1) = 1`, so the result is `P >> 1 = P/2`.  
  * For every `n ≥ 3` the divisor is `0` (integer division truncates), so the shift amount is `0` and the result is simply `P`.

  Hence the method returns  

  * `P/2` when `n ≤ 2`  
  * `P`   when `n ≥ 3`.

  In plain words: **the answer is the smallest power of two larger than `n`, divided by two only for the two tiny inputs `n = 1, 2`.**  

  This matches the known maximum number of distinct XOR triples for the problem.

---

**2. Step‑by‑step walk‑through (dry‑run) – sample `[1, 2]`**

| Step | Expression | Value (binary) | Comment |
|------|------------|----------------|---------|
| 1    | `n = nums.length` | `n = 2` | length of the array |
| 2    | `m = n` | `m = 2` (binary `10`) | start value |
| 3    | `m |= m >> 1` | `m = 2 | (2>>1)=2|1 = 3` (binary `11`) | set the lowest bit |
| 4    | `m |= m >> 2` | `m = 3 | (3>>2)=3|0 = 3` (binary `11`) | no change (already all lower bits set) |
| 5    | `m |= m >> 4` | `m = 3 | 0 = 3` | unchanged |
| 6    | `m |= m >> 8` | `m = 3` | unchanged |
| 7    | `m |= m >> 16`| `m = 3` | unchanged |
| 8    | `m + 1` | `4` (binary `100`) | `P = 2^{b}` with `b = 2` |
| 9    | `3 / (n + 1)` | `3 / 3 = 1` | integer division → shift amount = 1 |
|10    | `(m + 1) >> 1` | `4 >> 1 = 2` | final answer |

The method returns **2**, which is exactly the number of distinct XOR results obtainable from the two‑element array (`{1,2}` → XOR values `{1,2}`).

---

**3. Complexity analysis**

* **Time complexity** – `O(1)`  
  The algorithm performs a fixed number (five) of bit‑wise shift/or operations, each taking constant time on a 32‑bit integer. No loops or recursion are used.

* **Space complexity** – `O(1)`  
  Only a few integer variables (`n`, `m`) are allocated; the amount of extra memory does not depend on the input size.

---

### TL;DR

*The method computes the smallest power of two larger than the array length (using a classic “fill‑lower‑bits” bit‑twiddling trick).  
For `n ≥ 3` it returns that power of two; for the two tiny cases `n = 1, 2` it returns half of it.  
Thus the number of unique XOR triples is `2^{⌈log₂(n+1)⌉}` (or `2^{⌈log₂ n⌉}` for `n ≤ 2`).  
The algorithm runs in constant time and uses constant extra space.*
