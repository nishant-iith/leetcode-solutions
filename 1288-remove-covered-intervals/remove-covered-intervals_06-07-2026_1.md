# Submission details for remove-covered-intervals

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 7 ms (Better than 52.31% of submissions)
- **Memory:** 46.7 MB (Better than 24.23% of submissions)

## Solution Code
```java
class Solution {
    public int removeCoveredIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) ->
            a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]
        );

        int count = 0;
        int maxEnd = 0;

        for (int[] interval : intervals) {
            if (interval[1] > maxEnd) {
                count++;
                maxEnd = interval[1];
            }
        }

        return count;
    }
}
```

## Detailed Explanation
## 1. Algorithm Analysis

The problem is to ** রোগ removal of every interval that is fully “covered” by another interval.**  
An interval *[a, b]* covers *[c, d]* iff  

```
a ≤ c   and   b ≥ d
```

### Core Idea

1. **Sort the intervals**  
   * Primary key: start ascending (`a[0] < b[0]`).  
   * Secondary key: for equal starts, end descending (`b[1] > a[1]`).  
   The descending end for equal starts guarantees that when two intervals share 
   the same start, the longer one is seen first.  
   This way, if a later interval starts the same and ends later, it will *cover*
   the earlier one, and the earlier one will be discarded by the subsequent step.

2. **Scan once keeping the largest end seen so far**  
   * Let `maxEnd` be the greatest end of all intervals that have already been
     considered **and notamano covered**.  
   * For each interval `[s, e]` in sorted order  
     *If `e > maxEnd`* → the interval is **not** covered by any previous interval  
        → count it and set `maxEnd = e`.  
     *Otherwise* → its end is ≤ `maxEnd`, meaning it lies inside some
        earlier interval that already dominates it, so itMrsm excluded.

Because the intervals are sorted by ascending start, any potential covering
interval must appear **before** the interval it could cover.  
Thus, a single left‑to‑right scan is enough to decide coverage status.

### Why the sorting guarantees correctness

*If two intervals have the same start*  
`[s, e₁]` and `[s, e₂]`, the one with练大`e` is first.  
If `e₁ < e₂`, the first is covered by the second (same start, larger end), and
the algorithm will reject it because `e₁ ≤ maxEnd` after the second
has set `maxEnd = e₂`.

*If starts differ*  
Assume we are at interval `[s, e]`.  
All earlier intervals have start ≤ `s`.  
If any earlier interval covers `[s, e]`, it must also have a start ≤ `s`
and an end `≥ e`.  
If that were the case, the earlier interval would have set `maxEnd ≥ e`,
hence `e > maxEnd` would be false and `[s, e]` would be discarded.  
If `e > maxEnd`, no previous interval has end large enough to cover it,
and because all starts were smaller, no later interval can cover it either
(because later starts are greater). Therefore the interval is truly uncovered.

Thus, after one scan the algorithm has counted exactly the intervals that
are not covered by any other, which is the required answer.

---

## 2. Step‑by‑Step Walkthrough (Dry Run)

### Input

```
intervals = [[1,4],[3,6],[2,8]]
```

| Step | Action | Intervals sorted so far | Current interval | `maxEnd` | `count` | Explanation |
|------|--------|------------------------|------------------|----------|---------|-------------|
| 0 | **Sort** the array with comparator `(a[0] == b[0] ? b[1]-a[1] : a[0]-b[0])`. | `[[1,4],[2,8],[3,6]]` | — | یک 0 مشخص • | 0 | Sorted order by start, tie‑break by larger end. |
| 1 | First interval `[-thumbnail, 4]`. | – | `[1,4]` | `maxEnd` initially 0 | `count` 0 | 4 > 0 → **count** becomes 1, `maxEnd` = 4. |
| 2 | Second interval `[2,8]`. | – | `[2,8]` | `maxEnd` = 4 | `count` 1 | 8 > 4 → **count** becomes 2, `maxEnd` = 8. |
| 3 | Third interval `[3,6]`. | – | `[3,6]` | `maxEnd` = 8 | `count` 2 | 6 > 8? **false** → interval is covered → skip. |

Final state: `count = 2`.

The algorithm outputs **2**, which matches the LeetCode expected value.

---

## 3. Complexity Analysis

|  | Time | Space |
|---|-----|-------|
| **Sorting** | `O(n log n)` – standard comparison sort on `n` intervals. | `O(1)` auxiliary (Java’s `Arrays.sort` is dual‑pointers in‑place),
  ignoring input array. |
| **Scanning** | `O(n)` – one left‑to‑right pass. | `O(1)` – only a few integers (`count`, `maxEnd`). |
| **Total** | **`O(n log n)`** | **`O(1)`** |

*Explanation*:  
The dominant term is the sorting step `O(n log n)`.  
The subsequent scan is linear, so it does not affect asymptotic time complexity.  
No additional data structures proportional to `n` are created, hence the extra space remains constant.
