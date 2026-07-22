# Submission details for maximize-active-section-with-trade-ii

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 136 ms (Better than 40.00% of submissions)
- **Memory:** 274.5 MB (Better than 10.00% of submissions)

## Solution Code
```java
import java.util.regex.*;


class Solution {
    private int[] zs, ze, V;
    private int nblocks;
    private List<int[]> sparse;

    public List<Integer> maxActiveSectionsAfterTrade(String s, int[][] queries) {
        int ones = (int) s.chars().filter(c -> c == '1').count();

        // maximal zero-blocks (inclusive ends), split into starts / ends
        List<Integer> zsL = new ArrayList<>(), zeL = new ArrayList<>();
        Matcher mo = Pattern.compile("0+").matcher(s);
        while (mo.find()) { zsL.add(mo.start()); zeL.add(mo.end() - 1); }
        zs = zsL.stream().mapToInt(Integer::intValue).toArray();
        ze = zeL.stream().mapToInt(Integer::intValue).toArray();
        nblocks = zs.length;

        // valley j: full value = sum of the two adjacent block lengths
        V = IntStream.range(0, nblocks - 1)
                     .map(j -> (ze[j] - zs[j] + 1) + (ze[j + 1] - zs[j + 1] + 1))
                     .toArray();

        // sparse table for range-max over V
        int nv = V.length;
        sparse = new ArrayList<>();
        sparse.add(V);
        for (int half = 1; half * 2 <= nv; half *= 2) {
            int[] prev = sparse.get(sparse.size() - 1);
            int[] next = new int[prev.length - half];
            for (int i = 0; i < next.length; i++)
                next[i] = Math.max(prev[i], prev[i + half]);
            sparse.add(next);
        }

        List<Integer> ans = new ArrayList<>(queries.length);
        for (int[] q : queries) ans.add(ones + gain(q[0], q[1]));
        return ans;
    }

    private int rmq(int lo, int hi) {                 // inclusive max over V[lo..hi]
        int t = 31 - Integer.numberOfLeadingZeros(hi - lo + 1);
        return Math.max(sparse.get(t)[lo], sparse.get(t)[hi - (1 << t) + 1]);
    }

    private int clip(int j, int l, int r) {           // valley j's gain, clipped to [l, r]
        return V[j] - Math.max(0, l - zs[j]) - Math.max(0, ze[j + 1] - r);
    }

    private int gain(int l, int r) {
        if (nblocks < 2) return 0;
        int ja = lowerBound(ze, l);                   // first usable valley: left block ends >= l
        int jb = upperBound(zs, r) - 2;               // last  usable valley: right block starts <= r
        if (ja > jb) return 0;
        return Math.max(Math.max(clip(ja, l, r), clip(jb, l, r)),
                        jb - ja >= 2 ? rmq(ja + 1, jb - 1) : 0);
    }

    // bisect_left / bisect_right equivalents
    private static int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) { int mid = (lo + hi) >>> 1; if (a[mid] < x) lo = mid + 1; else hi = mid; }
        return lo;
    }
    private static int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) { int mid = (lo + hi) >>> 1; if (a[mid] <= x) lo = mid + 1; else hi = mid; }
        return lo;
    }
}
```

## Detailed Explanation
**1. Algorithm analysis – what the code is doing**

The problem asks for the maximum number of *active* (i.e. `1`) sections that can be obtained after at most one “trade” (a single contiguous interval `[l, r]` that can be flipped).  
If we denote by  

* `ones` – the total number of `1`s in the original string (the answer without any trade),  

* a *zero‑block* – a maximal contiguous run of `0`s, with start index `zs[j]` and end index `ze[j]` (both inclusive).  

Then a trade that covers a part of a zero‑block can turn some of those `0`s into `1`s, but it also may “steal” `1`s that lie outside the interval (because those `1`s become `0`s after the flip).  

The key observation used in the solution is that the *net gain* of a trade is completely determined by the **two** zero‑blocks that are intersected by the interval:

* the **leftmost** zero‑block that the interval touches – call it `jl`.  
  Only the part of this block that lies **inside** `[l, r]` can be turned into `1`s, i.e. at most `r - max(l, zs[jl]) + 1` new `1`s.  
  The part of the block that lies **outside** the interval (the prefix `l … zs[jl]-1`) would become `0`s, which reduces the gain.  
  The net contribution of this block is  

  ```
  gain_left(jl) = (size of the intersected part) - (size of the outside part)
                = (ze[jl] - max(l, zs[jl]) + 1) - (max(l, zs[jl]) - l)
                = V[jl] - max(0, l - zs[jl])
  ```

  where `V[j] = (ze[j] - zs[j] + 1) + (ze[j+1] - zs[j+1] + 1)` is the *total length* of the two adjacent zero‑blocks (the block on the left of `j` and the block on the right of `j`).  
  The term `- max(0, l - zs[j])` simply subtracts the portion of the left block that lies left of `l`.

* the **rightmost** zero‑block that the interval touches – call it `jr`.  
  Symmetrically its net contribution is  

  ```
  gain_right(jr) = V[jr] - max(0, ze[jr+1] - r)
  ```

* if the interval covers **both** zero‑blocks (`jl < jr`), then the part of the interval that lies **between** them (i.e. the whole segment from `ze[jl]+1` to `zs[jr]`) also becomes `1`s.  
  The total gain contributed by the *middle* part is exactly the sum of the *intermediate* `V` values, i.e. `V[jl+1] + … + V[jr-1]`.  
  Because we need the maximum possible sum over a contiguous range of `V`, a **range‑maximum query (RMQ)** data structure is used.  

Putting the three pieces together, the net gain for a concrete interval `[l, r]` is

```
gain(l, r) = max(  gain_left(ja) , gain_left(jb) ,  (jb-ja >= 2 ? rmq(ja+1, jb-1) : 0) )
```

where  

* `ja` = first zero‑block whose **right end** is ≥ `l` (`lowerBound(ze, l)`).  
  This is the leftmost block that can be intersected on the left side.  

* `jb` = last zero‑block whose **left end** is ≤ `r` (`upperBound(zs, r) - 2`).  
  This is the rightmost block that can be intersected on the right side.  

If `ja > jb` there is no block that can be touched, so the gain is `0`.

The algorithm therefore reduces the problem to:

1. **Pre‑processing**  
   * Count total `1`s (`ones`).  
   * Extract all zero‑blocks → arrays `zs[]` (starts) and `ze[]` (ends).  
   * Build array `V[]` where `V[j] = len(block_j) + len(block_{j+1})`.  
   * Build a **sparse table** for `O(1)` range‑maximum queries on `V`.  

2. **Answering a query `[l, r]`**  
   * Find `ja` and `jb` with binary searches (`lowerBound` / `upperBound`).  
   * If `ja > jb` → gain = `0`.  
   * Otherwise compute three candidates:  
        * `c1 = clip(ja, l, r)` – gain if we only touch the left block.  
        * `c2 = clip(jb, l, r)` – gain if we only touch the right block.  
        * `c3 = (jb-ja >= 2) ? rmq(ja+1, jb-1) : 0` – gain when we touch both blocks (the middle part).  
   * The answer for the query is `ones + max(c1, c2, c3)`.

The whole solution is therefore a **two‑phase** algorithm: heavy preprocessing (linear in the number of zero‑blocks, plus `O(n log n)` for the sparse table) and `O(log n)` per query (binary searches + O(1) RMQ).

---

**2. Step‑by‑step walkthrough (DRY run) – sample test case**

Input  

```
s = "01"
queries = [[0, 1]]
```

We will follow the code line‑by‑line, keeping a table of the most important variables after each logical block.

| Step | Code fragment (concept) | Variable values (after execution) | Comments |
|------|------------------------|-----------------------------------|----------|
| 0 | `int ones = (int) s.chars().filter(c -> c == '1').count();` | `ones = 1` (the character at index 1 is `'1'`) | Total number of `1`s in the original string. |
| 1 | Build zero‑blocks with regex `0+`.<br>`Matcher mo = Pattern.compile("0+").matcher(s);` | `mo` finds the single run of zeros: start = 0, end = 1 (because `end‑1` is used). | There is exactly **one** zero‑block. |
| 2 | `while (mo.find()) { zsL.add(mo.start()); zeL.add(mo.end() - 1); }` | `zsL = [0]`<br>`zeL = [0]` (because `1‑1 = 0`) | `zs = [0]`, `ze = [0]`. |
| 3 | `nblocks = zs.length;` | `nblocks = 1` | Number of zero‑blocks. |
| 4 | Compute `V` (valley values).<br>`V = IntStream.range(0, nblocks-1) …` | Since `nblocks‑1 = 0`, the range is empty → `V = []` (length 0). | `V` would hold `len(block_j) + len(block_{j+1})`. With only one block there is no *pair* of adjacent blocks, so `V` is empty. |
| 5 | Build sparse table for `V`.<br>`int nv = V.length;` → `nv = 0` | `sparse = new ArrayList<>();` (empty) | No entries because `nv = 0`. The loop `for (int half = 1; half * 2 <= nv; half *= 2)` never executes. |
| 6 | Start answering queries.<br>`for (int[] q : queries) ans.add(ones + gain(q[0], q[1]));` | `q = [0, 1]` | We need `gain(0, 1)`. |
| 7 | `if (nblocks < 2) return 0;` inside `gain` | `nblocks = 1` → condition true → `gain` returns `0`. | Because we need **at least two** zero‑blocks to obtain a non‑zero gain (the interval must touch two different blocks to have a “middle” part). With a single block the best we can do is turn the whole block into `1`s, but that would also turn the existing `1` (at index 1) into `0`, netting no gain. |
| 8 | `ans.add(1 + 0) = 1` | Final answer list = `[1]`. | The original string already contains one `1`. No trade can increase that number. |

**Summary of the dry‑run**

* `ones = 1`  
* One zero‑block `[0,0]` (length 1).  
* `V` is empty → no range‑max possible.  
* `gain(0,1) = 0` (the `if (nblocks < 2)` guard).  
* Final answer = `ones + 0 = 1`.

Thus the algorithm correctly returns `1` for the sample.

---

**3. Complexity analysis**

*Let* `B = nblocks = number of zero‑blocks`.  
`B ≤ n` where `n = s.length()`.

| Phase | Work | Reason |
|-------|------|--------|
| **Counting ones** | `O(n)` (character filter) | One linear scan of the string. |
| **Extracting zero‑blocks** | `O(n)` (regex matcher) | Each character is examined once by the matcher. |
| **Building `V`** | `O(B)` | One addition per adjacent pair of blocks (at most `B‑1` operations). |
| **Sparse table construction** | `O(B log B)` | For each power‑of‑two `half = 2^k` we process `B‑half` entries; the total is `B + B/2 + B/4 + … = O(B log B)`. |
| **Answering a query** | `O(log B)` | Two binary searches (`lowerBound` / `upperBound`) each `O(log B)`, plus O(1) RMQ via the sparse table. |
| **Total for Q queries** | `O(Q log B)` | Summed over all queries. |

**Overall time complexity**

*Pre‑processing*: `O(n + B log B)` → `O(n log n)` in the worst case (when every character is a zero, so `B = n`).  
*Query processing*: `O(Q log n)`.

Hence **total time** = `O(n log n + Q log n)`.

**Space complexity**

* `zs`, `ze`, `V` – each `O(B)` ≤ `O(n)`.  
* Sparse table: stores `⌈log₂ B⌉` layers, each up to `B` integers → `O(B log B)` ≤ `O(n log n)`.  
* The list `sparse` itself holds that many integers.  

All other structures are `O(1)` per query.  

Therefore **space complexity** = `O(n log n)` (dominated by the sparse table).  

If the input size is modest (as in typical LeetCode constraints, `n ≤ 10^5`), this fits comfortably within the usual memory limits.

---

### TL;DR

* The algorithm turns the “maximise active sections after one trade” problem into a **range‑maximum query** on a derived array `V` that represents the combined length of two adjacent zero‑blocks.  
* Pre‑processing extracts zero‑blocks, builds `V`, and builds a **sparse table** for O(1) RMQ.  
* For each query we locate the leftmost and rightmost blocks that the interval can touch (binary search), compute three candidate gains (left‑only, right‑only, both), take the maximum, and add it to the original number of `1`s.  
* With `B` zero‑blocks, preprocessing costs `O(n log n)` time and `O(n log n)` space; each query costs `O(log n)` time, giving an overall `O(n log n + Q log n)` time and `O(n log n)` space.  

The provided sample `"01"` with query `[0,1]` demonstrates the whole pipeline and yields the correct answer `1`.
