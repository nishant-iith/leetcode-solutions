# Submission details for number-of-zigzag-arrays-i

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 196 ms (Better than 97.50% of submissions)
- **Memory:** 43.8 MB (Better than 97.50% of submissions)

## Solution Code
```java
class Solution {
    private static final int MOD = 1000000007;

    public int zigZagArrays(int n, int l, int r) {
        int m = r - l + 1;
        int[] dp = new int[m];
        Arrays.fill(dp, 1);

        for (int i = 2; i <= n; i++) {
            int sum = 0;
            if ((i & 1) == 0)
                for (int j = 0; j < m; j++) {
                    int t = dp[j];
                    dp[j] = sum;
                    sum = (sum + t) % MOD;
                }
            else
                for (int j = m - 1; j >= 0; j--) {
                    int t = dp[j];
                    dp[j] = sum;
                    sum = (sum + t) % MOD;
                }
        }

        int res = 0;
        for (int j = 0; j < m; j++)
            res = (res + dp[j]) % MOD;

        return (res << 1) % MOD;
    }
}
```

## Detailed Explanation
Based on the analysis of the provided Java code, I will infer the problem context and provide a comprehensive breakdown.

### 1. Algorithm Analysis

**Problem Context (Inferred):**
The code appears to solve a dynamic programming problem: **"Number of Zigzag Arrays"**.
The problem asks to count the number of valid arrays of length `n`, where the elements are distinct integers within a range `[l, r]`, and the elements must form a strict zigzag pattern.
*   A zigzag pattern alternates between increasing and decreasing: `a1 < a2 > a3 < a4 > ...` (Valley type) or `a1 > a2 < a3 > a4 < ...` (Peak type).
*   Since the solution runs in `O(n * m)` time where `m = r - l + 1`, the problem involves counting permutations/subsequences of size `n` from the set of values `{l, l+1, ..., r}`.

**State Definition:**
*   `dp[j]`: Represents the number of valid zigzag arrays of the current length being processed, which end with the value corresponding to index `j` (where index `0` represents value `l`, index `1` represents `l+1`, ..., index `m-1` represents `r`).

**Strategy & Logic:**
The algorithm uses a single 1D array `dp` of size `m` and updates it iteratively for each array length from 1 to `n`.

1.  **Initialization:** We start with arrays of length 1. Any single value is a valid array. Thus, `dp[j] = 1` for all `j` in range `[0, m-1]`.
2.  **Transitions:**
    *   When extending an array from length `i` to `i+1`, the relationship between the last element of the previous array (`prev_end`) and the new element (`curr`) depends on the parity of the next length (`i+1`).
    *   **Even `i+1` (e.g., 2, 4, 6...):** This implies the trend is `<` then `>`.
        *   At step `i` (currently built), we are preparing for `i+1`.
        *   If we are building a sequence `... < prev_end > curr`, we need `prev_end < curr` for the even step (since the previous step was odd/down).
        *   However, looking at the code, it handles the transition efficiently.
        *   For even steps, the code iterates forward. `dp[j]` (new) depends on the `sum` (accumulator).
        *   `dp[j] = sum`. Here `sum` holds the cumulative total of previously processed states.
        *   `sum += old_dp[j]`.
        *   This means `new_dp[j]` becomes the sum of `new_dp[0]` to `new_dp[j-1]` (since `sum` carries the running total).
        *   This is exactly what we need for a **strictly increasing** constraint: `curr > prev`. To end at `j`, the previous ending value `k` must be `< j`. So we sum `dp[0]` to `dp[j-1]`.
    *   **Odd `i+1` (e.g., 3, 5, 7...):** This implies the trend is `>` then `<`.
        *   When extending, we need `prev_end > curr`.
        *   The code iterates backward.
        *   `dp[j] = sum`. The `sum` accumulates values processed from the right.
        *   This means `new_dp[j]` becomes the sum of `new_dp[j+1]` to `new_dp[m-1]`.
        *   This is exactly what we need for a **strictly decreasing** constraint: `curr < prev`. To end at `j`, the previous ending value `k` must be `> j`. So we sum `dp[j+1]` to `dp[m-1]`.

3.  **Final Answer Calculation:**
    *   The loop computes the `dp` array for length `n` based on the pattern starting with a "Valley" (1 < 2 > 3).
    *   This covers all valid arrays starting with an increase (`<`).
    *   However, valid zigzag arrays can also start with a decrease (`>`).
    *   The result `res` sums all the counts in `dp` for length `n` (one direction).
    *   The final line `(res << 1) % MOD` (which is `res * 2 % MOD`) accounts for the symmetric case (arrays starting with a decrease).
    *   It is possible to swap `l` and `r` to invert the sequence, effectively doubling the count, hence the shift.

**Complexity Justification:**
*   **Time Complexity:** The outer loop runs `n` times (length of array). The inner loops process `m` elements (the range of values). Thus, for each length, we process all possible values. **Complexity: O(n * m)**.
*   **Space Complexity:** We only use one auxiliary array `dp` of size `m`. The iterative update reuses the same array. **Complexity: O(m)**.

---

### 2. Step-by-Step Walkthrough (Dry Run)

We will trace the execution for `n = 3` and a specific range `[l, r]` to make the logic concrete. Since the sample test cases provide `n` values (3, 4, 5) but do not specify `l` and `r`, we will use `l=1, r=3` (so `m=3`, values: 1, 2, 3). This range is small enough to verify manually.

**Environment:**
*   `n = 3`, `l = 1`, `r = 3`
*   `m = r - l + 1 = 3`
*   `dp = [1, 1, 1]` (Initially, arrays of length 1: `[1], [2], [3]`)
*   `MOD` is implicit for logic checks.

**Step 1: Initializing `n = 1`**
Arrays: `[1]`, `[2]`, `[3]`
`dp`: `[1, 1, 1]`
(Loop starts at `i = 2`).

**Step 2: Extending to length 2 (`i = 2`)**
Since `i` (2) is **even**, we use the "Increasing" logic (`prev < curr`).
We need to count arrays of length 2 ending in `j` where `val_j > previous_val`.
Previous states (length 1): 1, 2, 3.
Transition for j=0 (value 1): No previous value < 1. Count = 0.
Transition for j=1 (value 2): Previous values are {1}. Count = 1.
Transition for j=2 (value 3): Previous values are {1, 2}. Count = 2.

*Trace using code logic:*
```text
Initial:  dp = [1, 1, 1]
          sum = 0

i=2 (Even):
  j=0:
    t = dp[0] = 1
    dp[0] = sum = 0
    sum = (sum + t) % MOD = 1
    State: dp = [0, 1, 1], sum = 1

  j=1:
    t = dp[1] = 1
    dp[1] = sum = 1
    sum = (sum + t) % MOD = 2
    State: dp = [0, 1, 1], sum = 2

  j=2:
    t = dp[2] = 1
    dp[2] = sum = 2
    sum = (sum + t) % MOD = 3
    State: dp = [0, 1, 2], sum = 3
```
**Result after `i=2` (Length 2):**
Valid arrays (starting with `<`): `[1, 2]`, `[1, 3]`, `[2, 3]`.
`dp = [0, 1, 2]` corresponds to ending in 1 (0 ways), 2 (1 way), 3 (2 ways). **Total = 3**.

**Step 3: Extending to length 3 (`i = 3`)**
Since `i` (3) is **odd**, we use the "Decreasing" logic (`prev > curr`).
We need to count arrays of length 3 ending in `j` where `val_j < previous_val`.
Previous states (length 2): `[1, 2]`, `[1, 3]`, `[2, 3]` (endings: 2, 3, 3).
Transition for j=2 (value 3): No previous value > 3. Count = 0.
Transition for j=1 (value 2): Previous values are {3, 3}. Count = 2.
Transition for j=0 (value 1): Previous values are {2, 3, 3}. Count = 3.

*Trace using code logic:*
```text
Initial (i=2 end): dp = [0, 1, 2]
                   sum = 0

i=3 (Odd):
  j=2: (Iterating m-1 to 0)
    t = dp[2] = 2
    dp[2] = sum = 0
    sum = (sum + t) % MOD = 2
    State: dp = [0, 1, 0], sum = 2

  j=1:
    t = dp[1] = 1
    dp[1] = sum = 2
    sum = (sum + t) % MOD = 3
    State: dp = [0, 2, 0], sum = 3

  j=0:
    t = dp[0] = 0
    dp[0] = sum = 3
    sum = (sum + t) % MOD = 3
    State: dp = [3, 2, 0], sum = 3
```
**Result after `i=3` (Length 3):**
Valid arrays (Valley type: 1 < 2 > 3...): `[1, 3, 2]`, `[1, 2, 1]`, `[2, 3, 1]`, `[2, 3, 2]`.
(Wait, my manual list was small. Let's re-verify `m=3` (1,2,3).
Valid sequences of 3 distinct elements?
Pattern: `< >`.
Sequences: `1 < x > y`.
`x` can be 2 or 3.
If `x=2`: `y < 2`, valid `y` is 1. `1 < 2 > 1`.
If `x=3`: `y < 3`, valid `y` is 1 or 2. `1 < 3 > 2`, `2 < 3 > 1`.
Total = 3. Where did my code trace `[3, 2, 0]` (Total 5) come from?
Ah, the previous logic `"Valid arrays (length 2) ending in 3: 2"` counts `[1, 3]` and `[2, 3]`.
For `i=3`:
Ending at 2 (value 2): previous ends must be > 2. Only value 3 is valid. Previous endings at value 3 are `[1, 3]` and `[2, 3]`. Total 2. Correct.
Ending at 1 (value 1): previous ends must be > 1. Values 2 and 3 are valid.
Previous endings at value 2: `[1, 2]` (1 way). Previous endings at value 3: `[1, 3]`, `[2, 3]` (2 ways).
Total 1 + 2 + 2 = 5?
Wait, `dp` after `i=2` is `[0, 1, 2]`.
Values corresponding to indices:
Index 0 (val 1): 0 ways.
Index 1 (val 2): 1 way (`[1, 2]`).
Index 2 (val 3): 2 ways (`[1, 3]`, `[2, 3]`).
For `i=3`, ending in `val 1` (idx 0):
We need previous value > 1.
So previous values can be 2 (idx 1) or 3 (idx 2).
Sum of `dp[1]` and `dp[2]` from previous step = `1 + 2 = 3`. Correct. Arrays:
`2, 3` -> `3, 1` (3 > 1). (From `[1, 2, 1]`?)
Wait, `[1, 2, 1]` ends in 1. Previous end was 2.
`3, 1`: `3 > 1`. Previous end was 3. `1, 3` -> `3, 1`. Yes.
So `[1, 2]` -> `[2, 1]` (1 < 2 > 1).
`[1, 3]` -> `[3, 1]`.
`[2, 3]` -> `[3, 1]`.
So 3 arrays ending in 1. Matches `dp[0] = 3`.
Ending in `val 2` (idx 1):
Previous value > 2. Only 3 (idx 2).
Sum of `dp[2]` from previous step = 2. Arrays:
`1, 3` -> `3, 2`.
`2, 3` -> `3, 2`.
So 2 arrays ending in 2. Matches `dp[1] = 2`.
Ending in `val 3` (idx 2):
Previous value > 3. None.
`dp[2] = 0`. Matches.
Total arrays = 3 + 2 = 5.
My manual count missed duplicates? `[1, 3, 2]` and `[2, 3, 2]`.
`[1, 3, 2]` (1<3>2).
`[2, 3, 2]` (2<3>2).
`[1, 2, 1]` (1<2>1).
`[1, 3, 1]` (1<3>1).
`[2, 3, 1]` (2<3>1).
Total 5. Correct.

The code logic is robust.

**Final Calculation:**
Sum of `dp` array at `n = 3`: `3 + 2 + 0 = 5`.
Result `= (5 << 1) % MOD = 10`.
This represents `5` valley-type arrays + `5` peak-type arrays = `10` total zigzag arrays.

---

### 3. Complexity Analysis

**Time Complexity:**
*   We iterate through the length of the array `n` exactly once. The outer loop is `for (int i = 2; i <= n; i++)`.
*   Inside the loop, we iterate through the `m` distinct values (`j`).
*   Inside the inner loop, operations are O(1) (addition, assignment, modulo).
*   Therefore, the total time complexity is **O(n * m)**.

**Space Complexity:**
*   We use a single 1D array `dp` to store the dynamic programming states.
*   The size of this array is determined by `m`, which is `r - l + 1`.
*   We only use one such array; we do not need a full `(n x m)` table because we only ever need the previous state to compute the current state (though here, the update is in-place using the running sum, effectively optimizing space).
*   Therefore, the auxiliary space complexity is **O(m)**.
