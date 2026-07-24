# Submission details for number-of-unique-xor-triplets-ii

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 213 ms (Better than 89.09% of submissions)
- **Memory:** 46.6 MB (Better than 98.18% of submissions)

## Solution Code
```java
class Solution {
    public int uniqueXorTriplets(int[] nums) {
        int n = nums.length;

        int[] pairXor = new int[2048];
        int[] tripleXor = new int[2048];

        // Compute all possible pair XORs
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                pairXor[nums[i] ^ nums[j]] = 1;
            }
        }

        // Generate triplet XORs
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2048; j++) {
                if (pairXor[j] == 1) {
                    tripleXor[j ^ nums[i]] = 1;
                }
            }
        }

        int cnt = 0;
        for (int value : tripleXor) {
            if (value == 1) {
                cnt++;
            }
        }

        return cnt;
    }
}
```

## Detailed Explanation
### 1. Algorithm Analysis

This solution employs a **Dynamic Programming / Bitset** approach (simulated using integer arrays as boolean flags) to compute the set of all possible XOR values formed by triplets `(i, j, k)` where `i <= j <= k` (or simply any combination of 3 elements with replacement, since XOR is commutative and associative).

**Core Insight:**
The XOR of a triplet `nums[i] ^ nums[j] ^ nums[k]` can be viewed as the XOR of a **pair** `(nums[i] ^ nums[j])` and a **single element** `nums[k]`.
Since the maximum value in `nums` is constrained (implied by the fixed array size `2048`, suggesting `nums[i] < 2048` or `2^11`), the maximum possible XOR value is also bounded by `2047` (`2^11 - 1`). This allows us to use fixed-size arrays of length `2048` as efficient bitsets to track achievable values.

**Algorithm Phases:**

1.  **Compute Pair XORs (`pairXor` array):**
    Iterate over all pairs of indices `(i, j)` with `i <= j`. Compute `nums[i] ^ nums[j]` and mark that value as achievable (`1`) in the `pairXor` array.
    *   *Why `j = i`?* This allows pairs formed by the same element twice (e.g., `x ^ x = 0`), which is necessary for triplets like `(x, x, y)` -> `0 ^ y = y`.

2.  **Generate Triplet XORs (`tripleXor` array):**
    For every element `nums[i]` in the array, combine it with every achievable pair XOR value `p` (where `pairXor[p] == 1`). The result `p ^ nums[i]` is a valid triplet XOR. Mark it in `tripleXor`.
    *   This effectively computes the set `{ p ^ x | p ∈ PairXORs, x ∈ nums }`.

3.  **Count Unique Values:**
    Iterate through `tripleXor` and count the number of indices marked `1`.

---

### 2. Step-by-Step Dry Run (Sample: `nums = [1, 3]`)

**Initialization:**
*   `n = 2`
*   `pairXor` = `int[2048]` (all 0)
*   `tripleXor` = `int[2048]` (all 0)

#### Phase 1: Compute Pair XORs
*Loop: `i` from 0 to 1, `j` from `i` to 1*

| Iteration | `i` | `j` | `nums[i]` | `nums[j]` | XOR Value (`nums[i] ^ nums[j]`) | `pairXor` State (Indices with value 1) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 0 | 0 | 1 | 1 | **0** | `{0}` |
| 2 | 0 | 1 | 1 | 3 | **2** | `{0, 2}` |
| 3 | 1 | 1 | 3 | 3 | **0** | `{0, 2}` (0 already set) |

**Result after Phase 1:** `pairXor` has `1` at indices **0** and **2**.

#### Phase 2: Generate Triplet XORs
*Loop: `i` from 0 to 1 (elements), `j` from 0 to 2047 (pair values)*

**Outer Loop `i = 0` (`nums[0] = 1`):**
Inner loop scans `j`. Only `j=0` and `j=2` have `pairXor[j] == 1`.

| Inner `j` (Pair Val) | `pairXor[j]` | Calculation (`j ^ nums[i]`) | `tripleXor` Index Set | `tripleXor` State (Indices with 1) |
| :--- | :--- | :--- | :--- | :--- |
| 0 | 1 | `0 ^ 1 = 1` | **1** | `{1}` |
| 2 | 1 | `2 ^ 1 = 3` | **3** | `{1, 3}` |
| Others | 0 | Skip | - | - |

**Outer Loop `i = 1` (`nums[1] = 3`):**
Inner loop scans `j`. Only `j=0` and `j=2` have `pairXor[j] == 1`.

| Inner `j` (Pair Val) | `pairXor[j]` | Calculation (`j ^ nums[i]`) | `tripleXor` Index Set | `tripleXor` State (Indices with 1) |
| :--- | :--- | :--- | :--- | :--- |
| 0 | 1 | `0 ^ 3 = 3` | **3** | `{1, 3}` (3 already set) |
| 2 | 1 | `2 ^ 3 = 1` | **1** | `{1, 3}` (1 already set) |
| Others | 0 | Skip | - | - |

**Result after Phase 2:** `tripleXor` has `1` at indices **1** and **3**.

#### Phase 3: Count Unique Values
Iterate `tripleXor`:
*   Index 1: Value 1 -> `cnt = 1`
*   Index 3: Value 1 -> `cnt = 2`
*   All others: 0.

**Final Return Value: `2`**

**Manual Verification:**
All triplets (indices `i <= j <= k`):
1.  `(0,0,0)`: `1 ^ 1 ^ 1 = 1`
2.  `(0,0,1)`: `1 ^ 1 ^ 3 = 3`
3.  `(0,1,1)`: `1 ^ 3 ^ 3 = 1`
4.  `(1,1,1)`: `3 ^ 3 ^ 3 = 3`
Unique values: `{1, 3}`. Count = **2**. **Matches.**

---

### 3. Complexity Analysis

#### Time Complexity: **O(N² + N·M)** → **O(N²)** (given constraints)
*   **Phase 1 (Pair XORs):** Nested loops over `n` elements. The inner loop runs `n - i` times.
    $\sum_{i=0}^{N-1} (N - i) = \frac{N(N+1)}{2} \in O(N^2)$.
*   **Phase 2 (Triplet XORs):** Outer loop `N` times. Inner loop fixed at `M = 2048` iterations.
    $N \times 2048 \in O(N \cdot M)$. Since $M=2048$ is a constant, this is $O(N)$.
*   **Phase 3 (Counting):** Fixed loop of `M = 2048` iterations $\in O(1)$.
*   **Total:** $O(N^2 + N) = O(N^2)$.
    *Note: This solution is accepted because the problem constraints likely limit `N` (e.g., $N \le 1000$ or $2000$), making $N^2$ feasible. If $N$ were $10^5$, an $O(N^2)$ pair generation would TLE, and a frequency-count approach ($O(M^2)$) would be required.*

#### Space Complexity: **O(M)** → **O(1)** (Constant)
*   Two integer arrays of fixed size `2048` (`pairXor` and `tripleXor`).
*   No data structures scale with input size `N`.
*   $2 \times 2048 \times 4 \text{ bytes} \approx 16 \text{ KB}$.
