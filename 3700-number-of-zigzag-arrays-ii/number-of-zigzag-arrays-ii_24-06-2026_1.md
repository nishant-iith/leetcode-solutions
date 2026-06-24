# Submission details for number-of-zigzag-arrays-ii

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 310 ms (Better than 58.06% of submissions)
- **Memory:** 47.3 MB (Better than 9.68% of submissions)

## Solution Code
```java
class Solution {
    private static final long MOD = 1_000_000_007L;

    private long[][] multiply(long[][] A, long[][] B) {
        int sz = A.length;

        long[][] C = new long[sz][sz];

        for (int i = 0; i < sz; i++) {
            for (int k = 0; k < sz; k++) {
                if (A[i][k] == 0) continue;

                long cur = A[i][k];

                for (int j = 0; j < sz; j++) {
                    if (B[k][j] == 0) continue;

                    C[i][j] = (C[i][j] + cur * B[k][j]) % MOD;
                }
            }
        }

        return C;
    }

    public int zigZagArrays(int n, int l, int r) {
        int m = r - l + 1;
        int sz = 2 * m;

        long[][] T = new long[sz][sz];

        for (int x = 0; x < m; x++) {

            for (int y = x + 1; y < m; y++) {
                T[x][m + y] = 1;
            }

            for (int y = 0; y < x; y++) {
                T[m + x][y] = 1;
            }
        }

        long[][] result = new long[sz][sz];
        for (int i = 0; i < sz; i++) {
            result[i][i] = 1;
        }

        long power = n - 1;

        while (power > 0) {
            if ((power & 1) == 1) {
                result = multiply(result, T);
            }

            T = multiply(T, T);
            power >>= 1;
        }

        long answer = 0;

        for (int i = 0; i < sz; i++) {
            long rowSum = 0;

            for (int j = 0; j < sz; j++) {
                rowSum = (rowSum + result[i][j]) % MOD;
            }

            answer = (answer + rowSum) % MOD;
        }

        return (int) answer;
    }
}
```

## Detailed Explanation
## 1.  Algorithm Overview  

The problem asks for the number of **zig‑zag arrays** of length `n` whose elements are taken from the integer interval  

```
[l , r]   (inclusive)
```

A *zig‑zag* array is required to **alternate** between “increase” and “decrease”.  
If we write the array as  

```
a1 , a2 , a3 , … , an          (1‑based indexing)
```

the signs of the differences must be

```
+ – + – …   or   – + – + …
```

The accepted solution exploits this alternation with **matrix exponentiation**:

| Step | What is done | Why it works |
|------|--------------|--------------|
| 1️⃣   | Let `m = r‑l+1`.  Create a **state space of size `2·m`**.  The first `m` indices (`0 … m‑1`) represent the *lower* half of the allowed numbers (`l … l+m‑1`). The second `m` indices (`m … 2·m‑1`) represent the *upper* half (`l+m … r`). | Because a zig‑zag step always flips the direction, after an “up” step we must go *down* and vice‑versa.  By separating the two directions into two halves we can encode the direction in the index set. |
| 2️⃣   | Build a **transition matrix `T` (size `2m × 2m`)**.  For every pair `(x,y)` with `0 ≤ x < m` and `y > x` we set `T[x][m+y] = 1`.  For every pair `(x,y)` with `0 ≤ y < x` we set `T[m+x][y] = 1`. | `T[i][j] = 1` means “it is legal to move from state `i` to state `j` in one step”.  The rule `y > x` guarantees the next value is larger (an *up* step) when we come from the lower half, and `y < x` guarantees the next value is smaller (a *down* step) when we come from the upper half. |
| 3️⃣   | Raise `T` to the power `n‑1` with binary exponentiation.  While doing that we keep an accumulating `result` matrix that starts as the identity.  If the current binary digit of `power = n‑1` is `1` we multiply `result ← result·T`.  In any case we square `T ← T·T` and shift the power right. | A matrix power counts **paths of a given length** in a directed graph.  `T^k[i][j]` tells us how many ways we can start in state `i` and reach state `j` after exactly `k` transitions.  `n‑1` steps correspond to building an array of length `n` (the first element is fixed by the start state). |
| 4️⃣   | After exponentiation, each row of `result` contains the number of ways to start from a particular state and finish anywhere after `n‑1` moves.  Sum **all entries** of `result` to obtain the total number of valid zig‑zag arrays. | The start state can be any of the `2·m` possible “directions + value” positions, therefore the answer is the sum of **all** reachable states after `n‑1` steps. |
| 5️⃣   | Return the sum modulo `10^9+7`. | The statement of the original LeetCode problem requires the answer modulo `10^9+7`. |

The whole algorithm is a classic **“count walks of a given length with matrix exponentiation”** technique, often used when the transition graph is small (here it is `2·m` vertices) but the number of steps `n` can be as large as `10^9` or more.

---

## 2.  Detailed Dry‑Run (Step‑by‑Step Trace)  

We will trace the *sample* that the problem statement supplies:

```
Input:
3          // n
4          // l
5          // r
```

The function called is `zigZagArrays(3,4,5)`.

### 2.1  Build the auxiliary data  

| Quantity | Computation | Value |
|----------|-------------|-------|
| `m`      | `r‑l+1` = `5‑4+1` | `2` |
| `sz = 2·m` | `2·2` | `4` |
| **State indices** | `0,1` → lower half (values `4,5`? actually `0 → 4`, `1 → 5`) <br> `2,3` → upper half (values still `4,5` but interpreted as “we are on the *up* side”). | — |
| **Transition matrix `T`** (initially all zeros) |  |  |

- **First loop** (`x` from `0` to `m‑1 = 1`):  

  - `x = 0` → `y` runs from `x+1 = 1` to `m‑1 = 1` → only `y = 1`.  
    `T[x][m+y] = T[0][2+1] = T[0][3] = 1`.  

  - `x = 1` → no `y > x` (range empty) → nothing added.

- **Second loop** (`y` from `0` to `x‑1`):  

  - `x = 0` → no `y < 0` → nothing.  
  - `x = 1` → `y` runs `0 … 0` → only `y = 0`.  
    `T[m+x][y] = T[2+1][0] = T[3][0] = 1`.

All other cells stay `0`. Therefore

```
T =                               // rows ← columns
[ 0  0  0  1 ]   // row 0
[ 0  0  0  0 ]   // row 1
[ 0  0  0  0 ]   // row 2
[ 1  0  0  0 ]   // row 3
```

Interpretation:

* From **lower** index `0` we can go to **upper** index `3` (i.e., “up‑step from the smallest allowed value to a larger value”).  
* From **upper** index `3` we can go back to **lower** index `0` (i.e., “down‑step to a smaller value”).  

Indices `1` and `2` have no outgoing edges – they correspond to “values that cannot start a valid zig‑zag step”, which is exactly what we need for the range `{4,5}`.

### 2.2  Matrix exponentiation for `power = n‑1 = 2`

We start with  

```
result = I₄   (4×4 identity)
T      =  (the matrix above)
power  = 2
```

The loop runs while `power > 0`.

| Iteration | Binary digit (`power & 1`) | Action on `result` | `T` after squaring | `power` after shift |
|-----------|----------------------------|--------------------|--------------------|----------------------|
| **Start** | – | `result = I` | — | — |
| 1️⃣ | `2 & 1 = 0` (even) | **no multiplication** (`result` stays `I`) | `T ← T·T` (compute `T²`) | `power = 2 >> 1 = 1` |
| 2️⃣ | `1 & 1 = 1` (odd)  | **multiply**: `result ← result·T` → `result = T²` | `T ← T·T` (square again) | `power = 1 >> 1 = 0` (exit) |

#### Computing `T²`

We multiply the original `T` by itself, using the `multiply` routine that skips zero entries:

```
   T =
[0 0 0 1]
[0 0 0 0]
[0 0 0 0]
[1 0 0 0]

T·T :

- Row 0 has a single non‑zero at column 3 with value 1.
  → look at column 3 of the right factor: it has a 1 at row 3.
  → C[0][0] += 1*1 = 1  → C[0][0] = 1

- Row 3 has a single non‑zero at column 0 with value 1.
  → look at row 0 of the right factor: it has a 1 at column 3.
  → C[3][3] += 1*1 = 1  → C[3][3] = 1

All other rows are all‑zero → they contribute nothing.
```

Thus  

```
T² =   [1 0 0 0]
       [0 0 0 0]
       [0 0 0 0]
       [0 0 0 1]
```

Now the **second iteration**:

* `result = I` multiplied by `T²` → `result` becomes exactly the matrix above.
* `T` gets squared again, but squaring a diagonal matrix with only the `(0,0)` and `(3,3)` entries equal to `1` leaves it unchanged.
* `power` becomes `0`; the while‑loop terminates.

**Final `result` matrix** (the matrix we will sum):

```
        col0 col1 col2 col3
row0    1      0      0    0
row1    0      0      0    0
row2    0      0      0    0
row3    0      0      0    1
```

### 2.3  Summing all entries  

```
row 0 sum = 1
row 1 sum = 0
row 2 sum = 0
row 3 sum = 1
---------------------------------
totalSum = 1 + 0 + 0 + 1 = 2
```

`answer = totalSum % MOD = 2`.  
The method returns `(int)answer`, i.e. **2**.

> **Interpretation:** With `n = 3` and the value set `{4,5}` we can build exactly two zig‑zag arrays of length 3:  
> `4,5,4` and `5,4,5`. The algorithm correctly counts them.

---

## 3.  Complexity Analysis  

### 3.1  Size of the graph  

```
m   = r - l + 1        // number of distinct allowed values
sz  = 2 * m            // total number of states (lower + upper half)
```

The transition matrix `T` is a `sz × sz` matrix.

### 3.2  Building `T`  

Two nested loops each run `O(m²)` in the worst case (the inner `y` loops are bounded by `m`).  
Hence  

```
Time to build T :  O(m²)
Space for T      :  O(sz²) = O(m²)
```

### 3.3  Matrix multiplication  

The helper `multiply` runs three nested loops (`i`, `k`, `j`).  
Because it *skips* entries that are zero, the actual work is proportional to the number of non‑zero entries multiplied by `sz`.  
In the worst case (when `T` becomes dense) the cost is  

```
O(sz³)  =  O((2m)³)  =  O(m³)
```

The exponentiation routine performs **at most `⌊log₂(n)⌋ + 1`** multiplications (one squaring per bit, plus an extra multiplication for each set bit).  
Therefore

```
Total time for exponentiation =  O(log n) × O(m³)
                              =  O(m³·log n)
```

If `m` is small (as is typical for the problem constraints: `m ≤ 30` or similar), the cubic factor is negligible; for larger `m` the algorithm still stays within the time limits because `log n` is at most ~60 for `n ≤ 10¹⁸`.

### 3.4  Summation of the final matrix  

The final summation iterates over all `sz²` entries:

```
Time = O(sz²) = O(m²)
```

### 3.5  Overall  

| Quantity | Complexity |
|----------|------------|
| **Time** | `O(m³·log n + m²)`  (dominant term `O(m³·log n)`) |
| **Space**| `O(m²)`  (two `sz×sz` matrices are kept simultaneously) |

If the problem guarantees `m ≤ 30` (which is the typical bound for the LeetCode version), the algorithm runs comfortably within the 1‑second limit even for the maximum `n` (`≈ 10⁹`).  

---

## 4.  Recap – What Makes This Solution “Elegant”  

1. **Compact representation** – By splitting the state space into “lower” and “upper” halves the direction of the next step becomes part of the state, so a single matrix can encode the whole zig‑zag rule.  
2. **Matrix exponentiation** – Converts the combinatorial counting problem into a fast *path‑count* problem; the exponent can be as large as `n‑1` but we only need `O(log n)` matrix multiplications.  
3. **Modulo arithmetic** – All intermediate products are performed with `long` and reduced modulo `10⁹+7` to avoid overflow and to meet the problem requirement.  
4. **Sparse‑aware multiplication** – The `multiply` routine skips zero entries, giving a practical speed‑up when the transition matrix is sparse (which is exactly the case for the zig‑zag constraints).  

The dry‑run above shows concretely how the algorithm proceeds from the tiny input `{4,5}` to the final count `2`, confirming both correctness and the inner mechanics of the code.
