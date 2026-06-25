# Submission details for count-subarrays-with-majority-element-i

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 36 ms (Better than 58.15% of submissions)
- **Memory:** 46.9 MB (Better than 80.92% of submissions)

## Solution Code
```java
class Solution {
    public int countMajoritySubarrays(int[] nums, int target) {
        int n = nums.length;
        long ans = 0;

        for (int i = 0; i < n; i++) {
            int cnt = 0;

            for (int j = i; j < n; j++) {
                if (nums[j] == target) cnt++;

                int len = j - i + 1;

                if (2 * cnt > len)
                    ans++;
            }
        }

        return (int)ans;
    }
}
```

## Detailed Explanation
**1. Algorithm Analysis**

The problem asks for the number of contiguous sub‑arrays in which a given value `target` appears **strictly more than half** of the elements (i.e. it is the *majority* element of that sub‑array).

The supplied solution follows a direct *enumerate‑all‑sub‑arrays* strategy:

| Step | What we do |
|------|------------|
| Outer loop (`i`) | Fixes the left‑hand endpoint of a sub‑array. |
| Inner loop (`j`) | Extends the right‑hand endpoint from `i` to the end of the array, thereby generating every sub‑array that starts at `i`. |
| Variable `cnt` | Keeps the number of occurrences of `target` seen so far in the current window `[i … j]`. It is updated in **O(1)** when `j` moves one step to the right (`if (nums[j] == target) cnt++`). |
| Length `len` | `j‑i+1` – the size of the current window. |
| Majority test | `2 * cnt > len`  ⇔  `cnt > len/2`. If true, the current window is a valid sub‑array and we increment the answer. |
| Accumulator `ans` | Stores the total count; it is a `long` because the number of sub‑arrays can be up to `n·(n+1)/2` (≈ 5·10⁹ for `n = 10⁵`), which does not fit in a 32‑bit `int`. |

Because every pair `(i, j)` with `0 ≤ i ≤ j < n` is visited exactly once, the algorithm examines **all** possible sub‑arrays and counts those that satisfy the majority condition.

*Why it works*:  
For a fixed start `i`, as we increase `j` we are exactly scanning the sub‑array `[i … j]`. The variable `cnt` is precisely the frequency of `target` inside that window, so the test `2*cnt > len` is mathematically equivalent to "`target` occurs more than half the time". Therefore each valid sub‑array contributes exactly one to `ans`, and no invalid sub‑array is counted.

---

**2. Step‑by‑Step Walkthrough (Dry Run)**  

We trace the algorithm on the sample input  

```
nums = [1, 2, 2, 3]
target = 2
n = 4
```

We maintain the following variables:

| Variable | Meaning |
|----------|---------|
| `i`      | left index of the current sub‑array |
| `j`      | right index (inclusive) of the current sub‑array |
| `cnt`    | number of `target` (=2) seen in `[i … j]` |
| `len`    | length of `[i … j]` = `j‑i+1` |
| `ans`    | accumulated count of valid sub‑arrays |

We will show the state **after** each inner‑loop iteration (i.e., after processing `j`).  
`ans` starts at `0`.

---

### Outer loop `i = 0`

| j | nums[j] | cnt (after update) | len = j‑i+1 | 2*cnt > len? | ans (after possible increment) |
|---|---------|--------------------|------------|--------------|--------------------------------|
| 0 | 1       | 0                  | 1          | 0 > 1? **F** | 0 |
| 1 | 2       | 1                  | 2          | 2 > 2? **F** | 0 |
| 2 | 2       | 2                  | 3          | 4 > 3? **T** | 1 |
| 3 | 3       | 2                  | 4          | 4 > 4? **F** | 1 |

*Result after `i=0`*: `ans = 1` (sub‑array `[0..2] = [1,2,2]`).

---

### Outer loop `i = 1`

| j | nums[j] | cnt (after update) | len = j‑i+1 | 2*cnt > len? | ans |
|---|---------|--------------------|------------|--------------|-----|
| 1 | 2       | 1                  | 1          | 2 > 1? **T** | 2 |
| 2 | 2       | 2                  | 2          | 4 > 2? **T** | 3 |
| 3 | 3       | 2                  | 3          | 4 > 3? **T** | 4 |

*Result after `i=1`*: `ans = 4` (added `[1]`, `[1,2]`, `[1,2,3]`).

---

### Outer loop `i = 2`

| j | nums[j] | cnt (after update) | len = j‑i+1 | 2*cnt > len? | ans |
|---|---------|--------------------|------------|--------------|-----|
| 2 | 2       | 1                  | 1          | 2 > 1? **T** | 5 |
| 3 | 3       | 1                  | 2          | 2 > 2? **F** | 5 |

*Result after `i=2`*: `ans = 5` (added `[2]`).

---

### Outer loop `i = 3`

| j | nums[j] | cnt (after update) | len = j‑i+1 | 2*cnt > len? | ans |
|---|---------|--------------------|------------|--------------|-----|
| 3 | 3       | 0                  | 1          | 0 > 1? **F** | 5 |

*Result after `i=3`*: `ans = 5` (no new sub‑array).

---

**Final answer**: `ans = 5`.

The five qualifying sub‑arrays are:

| Sub‑array (indices) | Elements | #target | Length | Majority? |
|---------------------|----------|---------|--------|-----------|
| `[0,2]`             | 1,2,2    | 2       | 3      | Yes |
| `[1,1]`             | 2        | 1       | 1      | Yes |
| `[1,2]`             | 2,2      | 2       | 2      | Yes |
| `[1,3]`             | 2,2,3    | 2       | 3      | Yes |
| `[2,2]`             | 2        | 1       | 1      | Yes |

All other sub‑arrays fail the `2*cnt > len` test.

---

**3. Complexity Analysis**

*Time Complexity*  

The outer loop runs `n` times. For each `i`, the inner loop runs `n‑i` times.  
Total number of iterations:

\[
\sum_{i=0}^{n-1} (n-i) = n + (n-1) + \dots + 1 = \frac{n(n+1)}{2} = O(n^{2}).
\]

Inside the inner loop we perform only O(1) work (a comparison, an optional increment, a subtraction, a multiplication and a comparison).  
Hence **overall time = O(n²)**.

*Space Complexity*  

The algorithm uses a constant amount of extra variables: `n`, `ans`, `i`, `j`, `cnt`, `len`. No auxiliary data structures that grow with `n` are allocated.  
Therefore **space = O(1)** (ignoring the input array itself).

---

### Summary

- **Approach**: Brute‑force enumeration of all sub‑arrays, maintaining a running count of the target value to test the majority condition in O(1) per extension.
- **Correctness**: Every sub‑array `[i … j]` is examined exactly once; the condition `2*cnt > len` is equivalent to "`target` appears more than half the time", so each valid sub‑array contributes exactly one to the answer.
- **Complexity**:  
  - **Time**: `O(n²)`  
  - **Space**: `O(1)`  

This solution is simple and works well for modest input sizes (e.g., `n ≤ 2000`). For larger constraints a more sophisticated technique (prefix‑sum + hashmap or divide‑and‑conquer) would be required to achieve `O(n log n)` or `O(n)` time.
