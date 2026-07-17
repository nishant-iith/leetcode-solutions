# Submission details for sorted-gcd-pair-queries

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 26 ms (Better than 96.88% of submissions)
- **Memory:** 104.9 MB (Better than 9.38% of submissions)

## Solution Code
```java
class Solution {
    public int[] gcdValues(int[] A, long[] queries) {
        int max = Arrays.stream(A).max().getAsInt();

        int[] freq = new int[max + 1];
        long[] GCD = new long[max + 1];

        for (int a : A) freq[a]++;

        for (int i = max; i > 0; i--) {
            long sum = 0, extra = 0;
            for (int j = i; j <= max; j += i) {
                sum += freq[j];
                extra += GCD[j];
            }
            GCD[i] = sum * (sum - 1) / 2 - extra;
        }

        Arrays.parallelPrefix(GCD, Long::sum);
        int n = queries.length;

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            long q = queries[i];
            int l = 0, h = max + 1;
            while (l < h) {
                int m = (l + h) >>> 1;
                if (GCD[m] <= q) l = m + 1;
                else h = m;
            }
            res[i] = l;
        }

        return res;
    }
}
```

## Detailed Explanation
Below is a detailed, educator‑style analysis of the provided accepted Java solution for the LeetCode problem **“sorted‑gcd‑pair‑queries”** (often framed as: given an array `A`, count pairs `(i, j)` with `i < j` such that `gcd(A[i], A[j]) = g` for each `g`, then answer queries that ask: *“what is the largest `g` such that the number of pairs with gcd ≤ g is > q?”* or equivalently *“return the smallest g with prefix count > q”*).

---

## 1. Algorithm Analysis

### Problem Restatement (Implicit)
Given:
- Array `A` of positive integers
- Array `queries` of long values

Define:
- For each integer `g ≥ 1`, let `C(g)` = number of unordered pairs `(i, j)`, `i < j`, such that `gcd(A[i], A[j]) = g`
- Let `P(g) = C(1) + C(2) + … + C(g)` (prefix sum of pair counts)

For each query `q`, return the **smallest** `g` such that `P(g) > q` (this matches the binary search logic in the code).

---

### High‑Level Strategy

The solution uses **frequency counting + inclusion–exclusion over multiples + prefix sums + binary search**.

#### Step 1: Frequency Array
- `freq[x]` = how many times `x` appears in `A`
- Only values up to `max(A)` matter

#### Step 2: Count Pairs with GCD *Exactly* `i`
We compute `GCD[i]` = number of pairs whose gcd is **exactly** `i`.

Key observation:
- All numbers that are multiples of `i` can form pairs whose gcd is a multiple of `i`
- Let `sum = total numbers in A divisible by i`
- Number of pairs among them = `sum * (sum - 1) / 2`
- These pairs have gcd = `i, 2i, 3i, …`
- So:
```
GCD[i] = (pairs with gcd divisible by i) − (pairs with gcd strictly > i but divisible by i)
       = sum*(sum-1)/2 − Σ GCD[k*i]
```
We compute this **backwards** from `max` down to `1`.

#### Step 3: Prefix Sum
- `Arrays.parallelPrefix(GCD, Long::sum)` converts exact counts into cumulative counts `P(g)`

#### Step 4: Answer Queries
- Binary search for smallest `m` where `GCD[m] > q`
- This is a standard lower‑bound search

---

## 2. Step‑by‑Step Walkthrough (Dry Run)

### Sample Input
```
A = [2, 3, 4]
queries = [0, 2, 2]
```

---

### Step 1: Initialization
```
max = 4
freq = [0, 0, 1, 1, 1]   // indices 0..4
GCD  = [0, 0, 0, 0, 0]
```

Explanation:
- `freq[2]=1`, `freq[3]=1`, `freq[4]=1`

---

### Step 2: Compute Exact GCD Pair Counts

Loop `i` from `4` down to `1`

#### i = 4
Multiples: `4`
```
sum = freq[4] = 1
extra = GCD[4] = 0
GCD[4] = 1*0/2 - 0 = 0
```
No pair possible

#### i = 3
Multiples: `3`
```
sum = 1
GCD[3] = 0
```

#### i = 2
Multiples: `2, 4`
```
sum = freq[2] + freq[4] = 2
extra = GCD[2] + GCD[4] = 0
GCD[2] = 2*1/2 - 0 = 1
```
One pair: (2,4)

#### i = 1
Multiples: `1,2,3,4`
```
sum = 1+1+1 = 3
extra = GCD[1]+GCD[2]+GCD[3]+GCD[4] = 0+1+0+0 = 1
GCD[1] = 3*2/2 - 1 = 2
```
Pairs: (2,3), (3,4)

State now:
```
GCD = [0, 2, 1, 0, 0]
```

---

### Step 3: Prefix Sum
```
GCD = [0, 2, 3, 3, 3]
```
Meaning:
- ≤1 → 2 pairs
- ≤2 → 3 pairs
- ≤3 → 3 pairs
- ≤4 → 3 pairs

---

### Step 4: Answer Queries

Binary search logic:
- Find smallest `l` where `GCD[l] > q`

#### Query q = 0
- `GCD[1]=2 > 0` → answer = `1`

#### Query q = 2
- `GCD[2]=3 > 2` → answer = `2`

#### Query q = 2 (again)
- Same → `2`

Result:
```
res = [1, 2, 2]
```

---

## 3. Complexity Analysis

### Time Complexity

1. **Frequency array**
   - `O(n)` where `n = |A|`

2. **GCD exact counting**
   - For each `i`, iterate multiples → harmonic sum
   - Total: `O(max log max)`

3. **Prefix sum**
   - `O(max)`

4. **Queries**
   - Each binary search: `O(log max)`
   - Total: `O(q log max)`

✅ **Overall Time Complexity**:
```
O(n + max log max + q log max)
```

---

### Space Complexity

- `freq`, `GCD`: size `max + 1`
- `res`: size `q`

✅ **Overall Space Complexity**:
```
O(max + q)
```

---

### Final Remarks
This solution is elegant because it avoids explicitly generating pairs (which would be `O(n²)`) and instead leverages number theory (multiples & gcd structure) and prefix sums to answer queries efficiently.

If you’d like, I can also:
- Convert this to a visual diagram
- Compare it with a brute‑force version
- Explain why binary search bounds are correct
