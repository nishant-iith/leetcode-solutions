# Submission details for concatenate-non-zero-digits-and-multiply-by-sum-ii

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 36 ms (Better than 23.33% of submissions)
- **Memory:** 122.4 MB (Better than 98.33% of submissions)

## Solution Code
```java
class Solution {
    private static final int MOD = 1000000007;
    private static final int MAX = 100001;
    private static final int[] pow = new int[MAX];

    static {
        pow[0] = 1;
        for (int i = 1; i < MAX; i++)
            pow[i] = (int) ((pow[i - 1] * 10L) % MOD);
    }

    public int[] sumAndMultiply(String s, int[][] queries) {
        int n = s.length();
        int[] A = new int[n + 1];
        int[] B = new int[n + 1];
        int[] len = new int[n + 1];

        for (int i = 0; i < n; i++) {
            int d = s.charAt(i) - '0';            
            A[i + 1] = A[i] + d;
            
            if (d > 0) {
                B[i + 1] = (int) ((B[i] * 10L + d) % MOD);
                len[i + 1] = len[i] + 1;
            } else {
                B[i + 1] = B[i];
                len[i + 1] = len[i];
            }
        }

        int[] res = new int[queries.length];
        int i = 0;

        for (int[] q : queries) {
            int l = q[0], r = q[1] + 1;
            
            long sub = ((long) B[l] * pow[len[r] - len[l]]) % MOD;
            long x = (B[r] - sub + MOD) % MOD;
            
            res[i++] = (int) ((x * (A[r] - A[l])) % MOD);
        }

        return res;
    }
}
```

## Detailed Explanation
### 1. Algorithm Analysis

The problem asks us to process multiple queries on a string of digits `s`. For each query `[l, r]` (inclusive), we must:
1.  Extract all **non-zero** digits from the substring `s[l..r]` and concatenate them to form an integer `X`. (If there are no non-zero digits, `X = 0`).
2.  Compute the **sum** of all digits (including zeros) in `s[l..r]`, denoted as `S`.
3.  Return `(X * S) % MOD`, where `MOD = 1,000,000,007`.

**Core Challenge:** Answering each query in `O(1)` time after preprocessing, because the string length and number of queries can be up to `10^5`.

**Strategy: Prefix Arrays + Modular Arithmetic**

The solution uses three prefix arrays of size `n+1` (where `n = s.length()`), where index `i` corresponds to the prefix `s[0..i-1]`:

1.  **`A[i]` (Digit Sum Prefix):** `A[i] = sum of digits in s[0..i-1]`.
    *   Range sum `S = A[r+1] - A[l]`.
2.  **`B[i]` (Concatenated Non-Zero Value Prefix):** `B[i]` = the integer formed by concatenating non-zero digits of `s[0..i-1]`, taken modulo `MOD`.
    *   Transition: If `s[i-1] == '0'`, `B[i] = B[i-1]`. Else `B[i] = (B[i-1] * 10 + digit) % MOD`.
3.  **`len[i]` (Non-Zero Count Prefix):** `len[i]` = count of non-zero digits in `s[0..i-1]`.
    *   This tracks the "length" of the number stored in `B[i]` (in digits).

**Answering a Query `[l, r]`:**
We need the concatenated non-zero value for the range `s[l..r]`. Let `L = l`, `R = r+1` (using prefix indices).
*   `B[R]` contains non-zero digits from `s[0..r]`.
*   `B[L]` contains non-zero digits from `s[0..l-1]`.
*   The non-zero digits in the range correspond to the suffix of `B[R]` after removing the prefix `B[L]`.
*   Since `B[L]` has `len[L]` digits, and `B[R]` has `len[R]` digits, the range has `k = len[R] - len[L]` non-zero digits.
*   To isolate the range value `X`: `X = (B[R] - B[L] * 10^k) % MOD`.
*   We precompute `pow[i] = 10^i % MOD` for `i` up to `100,000` (max possible non-zero digits) to get `10^k` in `O(1)`.
*   Sum `S = A[R] - A[L]`.
*   Result = `(X * S) % MOD`.

**Correctness of `X` formula:**
`B[R]` represents the concatenation: `Prefix || Range_NonZero`.
Numerically: `B[R] = (Prefix * 10^k + Range_NonZero) % MOD`.
Rearranging: `Range_NonZero = (B[R] - Prefix * 10^k) % MOD`.
Here `Prefix = B[L]` and `k = len[R] - len[L]`.

---

### 2. Step-by-Step Walkthrough (Dry Run)

**Input:**
*   `s = "10203004"` (length `n = 8`)
*   `queries = [[0,7], [1,3], [4,6]]`

**Precomputation: `pow` array**
`pow[0]=1, pow[1]=10, pow[2]=100, pow[3]=1000, pow[4]=10000, ...`

**Building Prefix Arrays (Indices 0 to 8):**

| i | Char `s[i-1]` | Digit `d` | `A[i]` (Sum) | `d > 0`? | `B[i]` (Concat Non-Zero) | `len[i]` (Count) |
|:-:|:-------------:|:---------:|:------------:|:--------:|:------------------------:|:----------------:|
| 0 | -             | -         | **0**        | -        | **0**                    | **0**            |
| 1 | '1'           | 1         | 0+1=**1**    | Yes      | (0*10+1)=**1**           | 0+1=**1**        |
| 2 | '0'           | 0         | 1+0=**1**    | No       | **1** (copy)             | **1** (copy)     |
| 3 | '2'           | 2         | 1+2=**3**    | Yes      | (1*10+2)=**12**          | 1+1=**2**        |
| 4 | '0'           | 0         | 3+0=**3**    | No       | **12**                   | **2**            |
| 5 | '3'           | 3         | 3+3=**6**    | Yes      | (12*10+3)=**123**        | 2+1=**3**        |
| 6 | '0'           | 0         | 6+0=**6**    | No       | **123**                  | **3**            |
| 7 | '0'           | 0         | 6+0=**6**    | No       | **123**                  | **3**            |
| 8 | '4'           | 4         | 6+4=**10**   | Yes      | (123*10+4)=**1234**      | 3+1=**4**        |

**Final Prefix State:**
*   `A  = [0, 1, 1, 3, 3, 6, 6, 6, 10]`
*   `B  = [0, 1, 1, 12, 12, 123, 123, 123, 1234]`
*   `len= [0, 1, 1, 2, 2, 3, 3, 3, 4]`

---

**Processing Queries:**

#### Query 0: `[0, 7]`
*   `l = 0`, `r = 7` $\rightarrow$ `L = 0`, `R = 8`
*   `k = len[8] - len[0] = 4 - 0 = 4`
*   `pow_k = pow[4] = 10000`
*   `sub = (B[0] * pow_k) % MOD = (0 * 10000) = 0`
*   `X = (B[8] - sub + MOD) % MOD = (1234 - 0) = 1234`
*   `S = A[8] - A[0] = 10 - 0 = 10`
*   `Result = (1234 * 10) % MOD = 12340`
*   *Manual Check:* Substring `"10203004"`. Non-zero: `1,2,3,4` $\rightarrow$ `1234`. Sum: `1+0+2+0+3+0+0+4=10`. Product: `12340`. **Match.**

#### Query 1: `[1, 3]`
*   `l = 1`, `r = 3` $\rightarrow$ `L = 1`, `R = 4`
*   `k = len[4] - len[1] = 2 - 1 = 1`
*   `pow_k = pow[1] = 10`
*   `sub = (B[1] * 10) % MOD = (1 * 10) = 10`
*   `X = (B[4] - 10 + MOD) % MOD = (12 - 10) = 2`
*   `S = A[4] - A[1] = 3 - 1 = 2`
*   `Result = (2 * 2) % MOD = 4`
*   *Manual Check:* Substring `"020"`. Non-zero: `2` $\rightarrow$ `2`. Sum: `0+2+0=2`. Product: `4`. **Match.**

#### Query 2: `[4, 6]`
*   `l = 4`, `r = 6` $\rightarrow$ `L = 4`, `R = 7`
*   `k = len[7] - len[4] = 3 - 2 = 1`
*   `pow_k = pow[1] = 10`
*   `sub = (B[4] * 10) % MOD = (12 * 10) = 120`
*   `X = (B[7] - 120 + MOD) % MOD = (123 - 120) = 3`
*   `S = A[7] - A[4] = 6 - 3 = 3`
*   `Result = (3 * 3) % MOD = 9`
*   *Manual Check:* Substring `"300"`. Non-zero: `3` $\rightarrow$ `3`. Sum: `3+0+0=3`. Product: `9`. **Match.**

**Final Output:** `[12340, 4, 9]`

---

### 3. Complexity Analysis

Let `n = s.length()` and `q = queries.length`. Let `MAX = 100,001` (constant upper bound for string length).

**Time Complexity:**
1.  **`pow` Precomputation:** `O(MAX)` $\approx$ `O(10^5)`. This is a one-time static initialization.
2.  **Prefix Arrays Construction:** Single pass over string `s` $\rightarrow$ `O(n)`.
3.  **Query Processing:** Each query does `O(1)` arithmetic operations $\rightarrow$ `O(q)`.
4.  **Total:** `O(MAX + n + q)`. Since `MAX` is a constant constraint (`10^5`), this simplifies to **`O(n + q)`**.

**Space Complexity:**
1.  `pow` array: `O(MAX)` $\approx$ `O(10^5)` integers.
2.  Prefix arrays `A`, `B`, `len`: `O(n)` each $\rightarrow$ `O(n)` total.
3.  Output array `res`: `O(q)`.
4.  **Total:** **`O(n + q + MAX)`** $\rightarrow$ **`O(n + q)`** (since `MAX` is constant relative to problem constraints).
