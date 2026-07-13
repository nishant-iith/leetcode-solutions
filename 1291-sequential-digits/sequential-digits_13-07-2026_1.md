# Submission details for sequential-digits

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 0 ms (Better than 100.00% of submissions)
- **Memory:** 42.3 MB (Better than 56.92% of submissions)

## Solution Code
```java
class Solution {
    public List<Integer> sequentialDigits(int low, int high) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            int num = i;
            for (int j = i + 1; j <= 9; j++) {
                num = num * 10 + j;
                if (num >= low && num <= high)
                    res.add(num);
            }
        }
        Collections.sort(res);
        return res;
    }
}
```

## Detailed Explanation
**Solution Overview**

The method `sequentialDigits` builds every integer whose digits are consecutive increasing numbers (e.g. 12, 234, 3456 …) that lies in the interval `[low, high]`.  
Because the largest possible sequential‑digit number is `123456789` (9 digits), there are only a **fixed, tiny** amount of candidates – at most  

\[
\sum_{i=1}^{9} (9-i)=8+7+\dots+0 = 36
\]

numbers. The algorithm therefore:

1. **Generates** every candidate by fixing a starting digit `i` (1 … 9) and repeatedly appending the next digit `j = i+1 … 9`.
2. **Keeps** the candidate if it falls inside `[low, high]`.
3. **Sorts** the collected numbers (the generation order is not guaranteed to be sorted) and returns the list.

The nested loops are the core; the `Collections.sort` call is only a tiny post‑process.

---

## 1. Algorithm Analysis (step‑by‑step reasoning)

| Phase | What we do | Why it works |
|------|------------|--------------|
| **Outer loop** (`for i = 1 … 9`) | Choose the first digit of the sequential number. | Any sequential number must start with some digit `d` where `1 ≤ d ≤ 9`. |
| **Inner loop** (`for j = i+1 … 9`) | Starting from `num = i`, repeatedly do `num = num*10 + j`. After each update `num` holds the number formed by the digits `i, i+1, …, j`. | Appending the next higher digit creates exactly the next longer sequential number (e.g. from `12` → `123`). The inner loop stops when we run out of digits (`j > 9`). |
| **Range test** (`if (num >= low && num <= high)`) | Keep `num` only if it belongs to the required interval. | Guarantees the output respects the problem constraints. |
| **Collect** (`res.add(num)`) | Store every valid candidate. | After generation we have all qualifying numbers (maybe unsorted). |
| **Sort** (`Collections.sort(res)`) | Put the result in ascending order as required by LeetCode’s output format. | The generation order is lexicographic by start digit then length, which is not globally sorted (e.g. `234` appears before `1234`). Sorting fixes this. |
| **Return** | Return the list. | Finished. |

Because the total number of iterations is bounded by a constant (36), the algorithm runs in constant time for any input values.

---

## 2. Step‑by‑step Walk‑through (Dry‑run) – Sample `low = 100`, `high = 300`

We will trace the variables `i`, `j`, `num`, and the contents of `res` after each *addition* (i.e. when the `if` condition is true).  
For brevity, we skip iterations where `num` is out of range, but we still show the intermediate `num` values to illustrate how the number is built.

| i (start digit) | j (next digit) | num after update | In range? ([100,300]) | Action | res after this step |
|-----------------|----------------|------------------|-----------------------|--------|----------------------|
| **1** | – | `num = 1` (initial) | – | – | `[]` |
|  | 2 | `num = 1*10 + 2 = 12` | 12 < 100 → ignore | – | `[]` |
|  | 3 | `num = 12*10 + 3 = 123` | 100 ≤ 123 ≤ 300 → **add** | `res.add(123)` | `[123]` |
|  | 4 | `num = 123*10 + 4 = 1234` | 1234 > 300 → ignore | – | `[123]` |
|  | 5 | `num = 1234*10 + 5 = 12345` | >300 → ignore | – | `[123]` |
|  | 6 | `num = 12345*10 + 6 = 123456` | >300 → ignore | – | `[123]` |
|  | 7 | `num = … +7 = 1234567` | >300 → ignore | – | `[123]` |
|  | 8 | `num = … +8 = 12345678` | >300 → ignore | – | `[123]` |
|  | 9 | `num = … +9 = 123456789` | >300 → ignore | – | `[123]` |
| **2** | – | `num = 2` | – | – | `[123]` |
|  | 3 | `num = 2*10 + 3 = 23` | 23 < 100 → ignore | – | `[123]` |
|  | 4 | `num = 23*10 + 4 = 234` | 100 ≤ 234 ≤ 300 → **add** | `res.add(234)` | `[123, 234]` |
|  | 5 | `num = 234*10 + 5 = 2345` | >300 → ignore | – | `[123,234]` |
|  | 6 | `num = … +6 = 23456` | >300 → ignore | – | `[123,234]` |
|  | 7 | `num = … +7 = 234567` | >300 → ignore | – | `[123,234]` |
|  | 8 | `num = … +8 = 2345678` | >300 → ignore | – | `[123,234]` |
|  | 9 | `num = … +9 = 23456789` | >300 → ignore | – | `[123,234]` |
| **3** | – | `num = 3` | – | – | `[123,234]` |
|  | 4 | `num = 3*10 + 4 = 34` | <100 → ignore | – | `[123,234]` |
|  | 5 | `num = 34*10 + 5 = 345` | 345 > 300 → ignore | – | `[123,234]` |
|  | 6 | `num = … +6 = 3456` | >300 → ignore | – | `[123,234]` |
|  | … (continue to 9) – all >300 | – | – | – | `[123,234]` |
| **4** | – | `num = 4` | – | – | `[123,234]` |
|  | 5 | `num = 4*10 + 5 = 45` | <100 → ignore | – | `[123,234]` |
|  | 6 | `num = 45*10 + 6 = 456` | >300 → ignore | – | `[123,234]` |
|  | … (rest >300) | – | – | – | `[123,234]` |
| **5** | – | `num = 5` | – | – | `[123,234]` |
|  | 6 | `num = 5*10 + 6 = 56` | <100 → ignore | – | `[123,234]` |
|  | 7 | `num = 56*10 + 7 = 567` | >300 → ignore | – | `[123,234]` |
|  | … | – | – | – | `[123,234]` |
| **6** | – | `num = 6` | – | – | `[123,234]` |
|  | 7 | `num = 6*10 + 7 = 67` | <100 → ignore | – | `[123,234]` |
|  | 8 | `num = 67*10 + 8 = 678` | >300 → ignore | – | `[123,234]` |
|  | … | – | – | – | `[123,234]` |
| **7** | – | `num = 7` | – | – | `[123,234]` |
|  | 8 | `num = 7*10 + 8 = 78` | <100 → ignore | – | `[123,234]` |
|  | 9 | `num = 78*10 + 9 = 789` | >300 → ignore | – | `[123,234]` |
| **8** | – | `num = 8` | – | – | `[123,234]` |
|  | 9 | `num = 8*10 + 9 = 89` | <100 → ignore | – | `[123,234]` |
| **9** | (inner loop empty) | – | – | – | `[123,234]` |

After the loops finish, `res = [123, 234]`.  
The final `Collections.sort(res)` does not change the order because the list is already sorted, but we keep the call for correctness.

**Result returned:** `[123, 234]`.

---

## 3. Complexity Analysis

### Time Complexity

*Outer loop*: runs for `i = 1 … 9` → **9 iterations**.  
*Inner loop*: for a given `i`, runs `j = i+1 … 9` → at most `9‑i` iterations.

Total number of times the statement `num = num*10 + j;` is executed:

\[
\sum_{i=1}^{9} (9-i) = 8+7+6+5+4+3+2+1+0 = 36.
\]

All other operations inside the loops (`if`, `add`) are O(1).  
After generation we sort at most 36 numbers:

\[
O(k \log k) \quad\text{with } k \le 36 \;\Rightarrow\; O(36 \log 36) = O(1).
\]

Hence the overall time complexity is **O(1)** with respect to the numeric inputs (`low`, `high`).  
If we express it in terms of the maximum possible digit length `d = 9`, the complexity is **O(d²)** (since the double loop generates O(d²) candidates).

### Space Complexity

*Result list*: stores at most all valid sequential numbers. In the worst case (when `[low, high]` covers the whole range) this is 36 integers → **O(36) = O(1)** auxiliary space.  
*Variables*: a few integers (`i`, `j`, `num`) → O(1).  

Thus the algorithm uses **O(1)** extra space (ignoring the output list itself, which is required by the problem).

---

### Summary

* The algorithm enumerates every possible sequential‑digit number by fixing a start digit and repeatedly appending the next higher digit.
* With at most 36 candidates, the work is constant‑time; sorting the tiny result list keeps the overall complexity O(1).
* The dry‑run for `low = 100`, `high = 300` shows how the numbers `123` and `234` are generated, collected, and finally returned in sorted order.
