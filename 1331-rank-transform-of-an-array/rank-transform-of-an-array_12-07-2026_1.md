# Submission details for rank-transform-of-an-array

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 30 ms (Better than 71.06% of submissions)
- **Memory:** 77.3 MB (Better than 15.51% of submissions)

## Solution Code
```java
class Solution {
    public int[] arrayRankTransform(int[] arr) {
        int[] temp = Arrays.copyOf(arr, arr.length);

        Arrays.sort(temp);

        int rank = 1;
        HashMap<Integer, Integer> hm = new HashMap<>();

        for (int ele : temp) {
            if (!hm.containsKey(ele)) {
                hm.put(ele, rank++);
            }
        }

        for (int i = 0; i < arr.length; i++) {
            arr[i] = hm.get(arr[i]);
        }

        return arr;
    }
}
```

## Detailed Explanation
**Solution Overview**

The problem *Rank Transform of an Array* asks us to replace every element of the input array `arr` by its “rank”.  
The rank of a value is defined as  

* the smallest positive integer `1,2,3,…` that is larger than the rank of any smaller distinct value,  
* and equal values receive the same rank.

In other words we need a **stable, order‑preserving compression** of the distinct values that appear in `arr`.

The posted Java solution does exactly that in three phases:

1. **Copy & sort** a duplicate of the original array – this gives the values in non‑decreasing order.  
2. **Assign ranks** while scanning the sorted array: the first unseen value gets rank 1, the next new value gets rank 2, etc. The mapping `value → rank` is stored in a `HashMap`.  
3. **Rewrite** the original array by looking up each original element in the map.

Below we dissect each phase, dry‑run the algorithm on the sample input `[40,10,20,30]`, and analyse the time and space costs.



---

## 1. Algorithm Analysis (Strategy)

| Phase | Goal | Implementation | Why it works |
|-------|------|----------------|--------------|
| **A – Copy & sort** | Obtain all values in sorted order so that distinct values appear consecutively. | `int[] temp = Arrays.copyOf(arr, arr.length);` → `Arrays.sort(temp);` | Sorting guarantees that when we scan left‑to‑right, every new value is *strictly larger* than any value we have already processed. |
| **B – Build rank map** | Create a dictionary `value → rank` where each distinct value gets a unique increasing rank. | `int rank = 1; HashMap<Integer,Integer> hm = new HashMap<>();` <br> `for (int ele : temp) { if (!hm.containsKey(ele)) hm.put(ele, rank++); }` | Because `temp` is sorted, the first time we meet a value we know it is the smallest remaining distinct value; we assign the current `rank` and then increment it for the next distinct value. |
| **C – Transform original array** | Replace each original entry by its rank using the map built in B. | `for (int i = 0; i < arr.length; i++) { arr[i] = hm.get(arr[i]); }` | Lookup in a hash map is O(1) average, so each element can be replaced in constant time. The array is transformed **in‑place**, fulfilling the contract of returning the same array reference. |

The algorithm is essentially **coordinate compression** – a classic technique used in many competitive‑programming problems.



---

## 2. Step‑by‑Step Walkthrough (Dry Run)

We will follow the three phases using the sample input.

```
Input arr = [40, 10, 20, 30]
```

### Phase A – Copy & sort

| Variable | Action | Result |
|----------|--------|--------|
| `temp`   | `Arrays.copyOf(arr, arr.length)` | `[40, 10, 20, 30]` (a separate array) |
| `temp`   | `Arrays.sort(temp)` | `[10, 20, 30, 40]` (sorted) |

*Note*: `arr` is **unchanged** at this point.

### Phase B – Build rank map

We maintain:

* `rank` – next rank to assign (starts at **1**)
* `hm` – empty map `value → rank`

Iterate over `temp`:

| Loop iteration | `ele` (current value) | `hm.containsKey(ele)`? | Action | `hm` after action | `rank` after action |
|----------------|----------------------|------------------------|--------|-------------------|---------------------|
| 1 | 10 | `false` | `hm.put(10,1)`; then `rank++` | `{10→1}` | 2 |
| 2 | 20 | `false` | `hm.put(20,2)`; then `rank++` | `{10→1, 20→2}` | 3 |
| 3 | 30 | `false` | `hm.put(30,3)`; then `rank++` | `{10→1, 20→2, 30→3}` | 4 |
| 4 | 40 | `false` | `hm.put(40,4)`; then `rank++` | `{10→1, 20→2, 30→3, 40→4}` | 5 |

After the loop the map contains the exact rank for each distinct number.

### Phase C – Transform original array

Now we replace each entry of `arr` by the rank from `hm`.

| Index `i` | `arr[i]` before | `hm.get(arr[i])` | `arr[i]` after |
|-----------|-----------------|------------------|----------------|
| 0 | 40 | 4 | 4 |
| 1 | 10 | 1 | 1 |
| 2 | 20 | 2 | 2 |
| 3 | 30 | 3 | 3 |

Final `arr` is returned:

```
[4, 1, 2, 3]
```

(Indeed, the smallest value 10 gets rank 1, the next distinct value 20 gets rank 2, …)

The dry run also shows that duplicate values would be handled automatically because the map already contains an entry the second time they appear – the `if (!hm.containsKey(ele))` guard prevents assigning a new rank.



---

## 3. Complexity Analysis

Let **n** = `arr.length`.

| Phase | Operations Count | Time Complexity | Reason |
|-------|-------------------|-----------------|--------|
| **A – copy** | `n` element copies | **O(n)** | Simple linear scan by `Arrays.copyOf`. |
| **A – sort** | Comparison‑based sort on `temp` | **O(n log n)** | Java `Arrays.sort` uses Dual‑Pivot Quicksort (average `O(n log n)`). |
| **B – build map** | One pass over sorted array (n iterations). Each `containsKey` / `put` is *amortised* O(1). | **O(n)** | HashMap operations are constant‑time on average. |
| **C – rewrite** | One pass over original array (n look‑ups). | **O(n)** | Same hash‑map lookup cost. |

**Overall Time Complexity**:  

\[
O(n) + O(n \log n) + O(n) + O(n) = O(n \log n)
\]

The dominant term is the sorting step.

---

### Space Complexity

| Allocation | Size | Explanation |
|------------|------|-------------|
| `temp` (copy of array) | `n` integers | Needed for sorting without destroying the original order. |
| `HashMap<Integer,Integer>` | up to `m` entries, where `m` = number of *distinct* values ≤ `n` | Stores rank for each unique element. |
| Misc variables (`rank`, loop indices, etc.) | O(1) | Constant. |

Thus the extra auxiliary space is **O(n)** in the worst case (when all elements are distinct). The algorithm transforms the input *in‑place* (the returned reference is the original `arr`), but it still needs the auxiliary `temp` and hash map.

**Overall Space Complexity**: **O(n)**.



---

## 4. Summary

* The solution performs coordinate compression by sorting a copy of the array, assigning increasing ranks to distinct values, and finally looking up those ranks to replace the original elements.  
* On the sample `[40,10,20,30]` the algorithm builds the map `{10→1,20→2,30→3,40→4}` and rewrites the array to `[4,1,2,3]`.  
* **Time** `O(n log n)` – dominated by sorting.  
* **Space** `O(n)` – due to the sorted copy and hash map.

The approach is optimal for the general case because any algorithm that must order the distinct values (to know which one is “smaller”) must inspect them at least `Ω(n log n)` time in the comparison model, and the extra linear space is unavoidable for the ranking map.
