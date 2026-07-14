# Submission details for sequential-digits

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 0 ms (Better than 100.00% of submissions)
- **Memory:** 42.4 MB (Better than 56.22% of submissions)

## Solution Code
```java
class Solution {
    public List<Integer> sequentialDigits(int low, int high) {
        List<Integer> ans = new ArrayList<>();

        String s = "123456789";
        String l = String.valueOf(low);
        String h = String.valueOf(high);

        for (int len = l.length(); len <= h.length(); len++) {
            for (int start = 0; start <= 9 - len; start++) {
                int num = Integer.parseInt(s.substring(start, start + len));
                if (num >= low && num <= high) {
                    ans.add(num);
                }
            }
        }

        return ans;
    }
}
```

## Detailed Explanation
**1. Algorithm Analysis**

The problem asks for all *sequential‑digit* integers that lie in the inclusive interval **[low, high]**.  
A sequential‑digit integer is a number whose decimal digits increase by 1 when read from left to right, e.g. `123`, `2345`, `6789`.  

The key observations that make the solution trivial:

| Observation | Reason |
|-------------|--------|
| The only possible digits are `1 … 9` (0 can never be followed by a larger digit). | The definition requires each digit to be exactly one larger than the previous one, so a `0` could never appear (except as the first digit, which would break the “increase” rule). |
| The longest possible sequential number has 9 digits (`123456789`). | After `9` there is no larger digit to continue the sequence. |
| For a fixed length `len` there are exactly `9‑len+1` different sequential numbers (they start at `1`, `2`, …, `9‑len+1`). | Example: length 3 → `123`, `234`, …, `789` → 7 numbers = 9‑3+1. |
| Consequently the total number of sequential numbers that can ever be generated is  

\[
\sum_{len=1}^{9} (9-len+1)=1+2+…+9 = 45 .
\]

Thus the algorithm can simply **enumerate** all 45 candidates, test whether each lies in `[low,high]`, and collect the valid ones. No recursion, no back‑tracking, no extra data structures beyond the result list.

The provided Java code follows exactly this plan:

1. Convert `low` and `high` to strings (`l` and `h`) to obtain their digit lengths.
2. Loop over every possible length `len` from `l.length()` up to `h.length()` (inclusive).  
   – This guarantees we only consider numbers whose digit‑count can possibly fall inside the interval.
3. For each `len`, loop over every valid start position `start` (`0 … 9‑len`).  
   – `start` is the index of the first digit in the base string `"123456789"`.  
   – The substring `s.substring(start, start+len)` yields the exact sequential number of that length.
4. Parse the substring to an `int`, check the range condition, and add to the answer list if it satisfies `low ≤ num ≤ high`.

Because the enumeration is exhaustive and the number of candidates is constant (45), the algorithm is **O(1)** in both time and auxiliary space (the output list itself is O(k) where *k* ≤ 45).

---

**2. Step‑by‑Step Walkthrough (DRY‑Run) – Sample Input `low = 100`, `high = 300`**

| Step | Variable(s) | Meaning / Value | Action |
|------|-------------|-----------------|--------|
| 0 | `low = 100`  <br> `high = 300` | Input bounds | – |
| 1 | `s = "123456789"` | Fixed source string for all sequential numbers | – |
| 2 | `l = "100"` → `l.length() = 3` | Number of digits of the lower bound | – |
| 3 | `h = "300"` → `h.length() = 3` | Number of digits of the upper bound | – |
| 4 | `len` loop: `len = 3` (since 3 ≤ len ≤ 3) | Only length‑3 numbers can possibly be in the interval | – |
| 5 | `start` loop: `start` runs from `0` to `9‑3 = 6` | Enumerates every possible start position for a 3‑digit sequential number | – |
| 6 | **Iteration 6.1** (`start = 0`) | `s.substring(0,3) = "123"` → `num = 123` | `123 ≥ 100` **and** `123 ≤ 300` → **add** `123` to `ans`. |
| 7 | **Iteration 6.2** (`start = 1`) | `s.substring(1,4) = "234"` → `num = 234` | `234` is in range → **add** `234`. |
| 8 | **Iteration 6.3** (`start = 2`) | `s.substring(2,5) = "345"` → `num = 345` | `345 > 300` → **skip**. |
| 9 | **Iteration 6.4** (`start = 3`) | `"456"` → `456 > 300` → **skip**. |
|10 | **Iteration 6.5** (`start = 4`) | `"567"` → `567 > 300` → **skip**. |
|11 | **Iteration 6.6** (`start = 5`) | `"678"` → `678 > 300` → **skip**. |
|12 | **Iteration 6.7** (`start = 6`) | `"789"` → `789 > 300` → **skip**. |
|13 | End of `len` loop (no further lengths) | – | – |
|14 | Return `ans = [123, 234]` | The list now contains exactly the sequential numbers that satisfy the bounds. | – |

**Result:** For the sample test case the method returns `[123, 234]`, which matches the expected output.

---

**3. Complexity Analysis**

*Let* **N** be the number of sequential‑digit numbers that can possibly be generated. As shown above, **N = 45** (a constant, independent of the input values).

| Metric | Derivation | Complexity |
|--------|------------|------------|
| **Time** | The outer loop runs for each length `len` (at most 9). The inner loop runs at most `9‑len+1` times. The total number of iterations is exactly `N = 45`. Inside each iteration we perform: <br> • `substring` (O(len)) <br> • `Integer.parseInt` (O(len)) <br> • a constant‑time range check. Since `len ≤ 9`, each iteration is O(1). | **O(N) = O(1)** (constant time) |
| **Auxiliary Space** (excluding the output list) | Only a few primitive variables (`len`, `start`, the string `s`, the two bound strings) are used. | **O(1)** |
| **Output Space** | The list `ans` stores at most one entry per valid sequential number, i.e., ≤ 45 integers. | **O(k)** where *k* ≤ 45 → **O(N)** in the worst case (still constant). |

Thus the algorithm is optimal for this problem: it enumerates a fixed, tiny set of candidates and therefore runs in constant time with negligible extra memory.
