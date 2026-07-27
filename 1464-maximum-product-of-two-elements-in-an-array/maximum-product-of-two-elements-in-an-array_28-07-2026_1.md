# Submission details for maximum-product-of-two-elements-in-an-array

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 0 ms (Better than 100.00% of submissions)
- **Memory:** 44.6 MB (Better than 60.68% of submissions)

## Solution Code
```java
class Solution {
    public int maxProduct(int[] nums) {
        int max1 = 0;
        int max2 = 0;
        for (int num : nums) {
            if (num > max1) {
                max2 = max1;
                max1 = num;
            }
            else if (num > max2)
                max2 = num;
        }
        return (max1 - 1) * (max2 - 1);
    }
}
```

## Detailed Explanation
# Analysis of Maximum Product of Two Elements in an Array

---

## 1. Algorithm Analysis

### Problem Recap
Given an array `nums`, pick two distinct indices `i` and `j` and maximize the value of `(nums[i] - 1) * (nums[j] - 1)`.

### Core Strategy: **Single-Pass Two-Max Tracking**

Instead of sorting (O(n log n)) or checking all pairs (O(n²)), this solution recognizes:

> The maximum product `(a-1)*(b-1)` is achieved when `a` and `b` are the **two largest** elements in the array.

Since the transformation `(x → x-1)` is monotonically increasing for positive integers, maximizing `a` and `b` individually maximizes the product.

### Algorithm Steps

| Step | Description |
|------|-------------|
| 1 | Initialize `max1 = 0` and `max2 = 0` (sentinels for the top two largest values). |
| 2 | Iterate through each element `num` in `nums`. |
| 3 | If `num > max1`: cascade the old `max1` down to `max2`, then assign `num` to `max1`. |
| 4 | Else if `num > max2`: update `max2` only. |
| 5 | After the loop, return `(max1 - 1) * (max2 - 1)`. |

### Why This Works
- **`max1` always holds the global maximum** seen so far.
- **`max2` always holds the second-largest maximum** seen so far (it is the old `max1` whenever a new global max is found, or it gets updated from below).
- These two variables maintain a **sorted invariant**: `max1 ≥ max2` at all times.
- At the end, they contain the top-2 elements of the entire array.

---

## 2. Step-by-Step Dry Run

### Input: `nums = [3, 4, 5, 2]`

**Initial State:**
```
max1 = 0, max2 = 0
```

### Iteration 1: `num = 3`
```
Condition: 3 > max1(0)?  →  YES
  max2 = max1 = 0
  max1 = 3
```
| Variable | Value |
|----------|-------|
| max1     | **3** |
| max2     | **0** |

### Iteration 2: `num = 4`
```
Condition: 4 > max1(3)?  →  YES
  max2 = max1 = 3
  max1 = 4
```
| Variable | Value |
|----------|-------|
| max1     | **4** |
| max2     | **3** |

### Iteration 3: `num = 5`
```
Condition: 5 > max1(4)?  →  YES
  max2 = max1 = 4
  max1 = 5
```
| Variable | Value |
|----------|-------|
| max1     | **5** |
| max2     | **4** |

### Iteration 4: `num = 2`
```
Condition: 2 > max1(5)?  →  NO
Condition: 2 > max2(4)?  →  NO
  → No update
```
| Variable | Value |
|----------|-------|
| max1     | 5 (unchanged) |
| max2     | 4 (unchanged) |

### Final Computation
```
return (max1 - 1) * (max2 - 1)
     = (5 - 1) * (4 - 1)
     = 4 * 3
     = 12
```

### Trace Summary Table

| Iteration | `num` | Comparison              | Action            | `max1` | `max2` |
|:---------:|:-----:|:-----------------------:|:-----------------:|:------:|:------:|
| 1         | 3     | 3 > 0 ✔                | Cascade + assign  | 3      | 0      |
| 2         | 4     | 4 > 3 ✔                | Cascade + assign  | 4      | 3      |
| 3         | 5     | 5 > 4 ✔                | Cascade + assign  | 5      | 4      |
| 4         | 2     | 2 > 5 ✘, 2 > 4 ✘     | None              | 5      | 4      |
| **Result**| —     | —                      | `(5-1)*(4-1)`    | **12** |        |

---

## 3. Complexity Analysis

### Time Complexity: **O(n)**

| Operation | Cost |
|-----------|------|
| Single `for-each` loop over `nums` of length `n` | O(n) |
| Inside the loop: constant-time comparisons and assignments | O(1) per iteration |
| **Total** | **O(n)** |

> *Contrast with brute-force O(n²) pairwise check or sorting-based O(n log n).*

### Space Complexity: **O(1)**

| Variable | Space |
|----------|-------|
| `max1` (int) | O(1) |
| `max2` (int) | O(1) |
| Loop variable `num` | O(1) |
| **Total auxiliary space** | **O(1)** |

> *No extra data structures are allocated. Only two integer variables are used regardless of input size.*

### Correctness Guarantee
The invariant `max1 ≥ max2` is maintained at every iteration. After processing all elements, `{max1, max2}` is exactly the set of the two largest values in the array. Since `(x-1)` is strictly increasing for `x ≥ 1` (all constraints guarantee `nums[i] ≥ 2`), maximizing both factors independently yields the globally optimal product.
