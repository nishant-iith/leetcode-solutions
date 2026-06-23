# Submission details for find-median-from-data-stream

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 134 ms (Better than 71.07% of submissions)
- **Memory:** 111.1 MB (Better than 94.84% of submissions)

## Solution Code
```java
class MedianFinder {
    private PriorityQueue<Integer> lowerHalf; // max-heap
    private PriorityQueue<Integer> upperHalf; // min-heap

    public MedianFinder() {
        lowerHalf = new PriorityQueue<>(Collections.reverseOrder());
        upperHalf = new PriorityQueue<>();
    }

    public void addNum(int num) {
        if (lowerHalf.isEmpty() || num <= lowerHalf.peek()) {
            lowerHalf.offer(num);
        } else {
            upperHalf.offer(num);
        }

        // Balance the heaps
        if (lowerHalf.size() > upperHalf.size() + 1) {
            upperHalf.offer(lowerHalf.poll());
        } else if (upperHalf.size() > lowerHalf.size()) {
            lowerHalf.offer(upperHalf.poll());
        }
    }

    public double findMedian() {
        if (lowerHalf.size() > upperHalf.size()) {
            return lowerHalf.peek();
        }
        return (lowerHalf.peek() + upperHalf.peek()) / 2.0;
    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */
```

## Detailed Explanation
## 1️⃣  Algorithm Analysis – What the solution is doing  

| Goal | How the code solves it |
|------|------------------------|
| **Maintain a dynamic collection of numbers** (numbers can arrive one‑by‑one) | Two priority‑queues (binary heaps):  <br>• **`lowerHalf`** – a **max‑heap** (Java’s `PriorityQueue` with `reverseOrder()`) that holds the *smaller* half of the numbers.  <br>• **`upperHalf`** – a **min‑heap** (default `PriorityQueue`) that holds the *larger* half of the numbers. |
| **Be able to return the median** at any moment, even after an arbitrary number of inserts | The *median* of a sorted list of `n` elements is:  <br>• If `n` is odd → the element at position `⌈n/2⌉` (the greatest element of the *left* half).  <br>• If `n` is even → the average of the two middle elements.  <br>Because `lowerHalf` always stores the **≤**median side and is allowed to be **at most one element larger** than `upperHalf`, the top of `lowerHalf` (`lowerHalf.peek()`) is precisely the middle element(s). |
| **Insert a new value in *logarithmic* time** | `addNum(int num)` does the following in **O(log n)**: <br>1. **Placement decision** – Compare `num` with the current maximum of the lower half (`lowerHalf.peek()`). If `lowerHalf` is empty or `num` ≤ that max, we put the number in `lowerHalf`; otherwise it goes to `upperHalf`. This step is O(1) plus the heap insertion cost. <br>2. **Re‑balancing** – After insertion the size invariant must hold: `size(lowerHalf)` is either equal to `size(upperHalf)` or exactly one larger. If it becomes larger by 2, we move the top element of `lowerHalf` to `upperHalf`. If `upperHalf` becomes larger than `lowerHalf`, we move its top element back to `lowerHalf`. Each movement is a `poll` + `offer`, i.e. two heap operations, each O(log n). |
| **Return the median in O(1)** | `findMedian()` just looks at the tops of the two heaps (which are O(1) accesses) and, depending on the size relationship, either returns `lowerHalf.peek()` (odd count) or the arithmetic mean of the two tops (even count). |

### Why this is correct  

1. **Invariant after every `addNum`**  

   After the optional re‑balancing step we guarantee:  

   ```
   size(lowerHalf) == size(upperHalf)      // even total elements
   or
   size(lowerHalf) == size(upperHalf) + 1  // odd total elements
   ```

   *Proof sketch*:  
   - Initially both queues are empty → invariant holds trivially.  
   - Insertion puts the element into one of the queues → the size difference can become `+2`, `0`, or `-1`.  
   - The two `if` statements correct any violation:  
     *If `lowerHalf` is two larger → move its max to `upperHalf`* → brings sizes back to equality or “lower larger by 1”.  
     *If `upperHalf` is larger → move its min to `lowerHalf`* → restores the “lower can be at most one larger” rule.  

   Hence the invariant is preserved forever.

2. **Finding the median**  

   *Case 1 – odd total count* (`size(lowerHalf) == size(upperHalf)+1`)  
   The extra element must reside in `lowerHalf` (by invariant). Therefore its maximum (`lowerHalf.peek()`) is exactly the `(n+1)/2`‑th smallest element – i.e. the median.

   *Case 2 – even total count* (`size(lowerHalf) == size(upperHalf)`)  
   The two middle elements are the greatest element of the lower half and the smallest element of the upper half, i.e. `lowerHalf.peek()` and `upperHalf.peek()`. Their average is the median.

   Thus the return value matches the mathematically defined median of the multiset built so far.

### Summary of the strategy  

- **Two heaps** give us O(1) access to the “border” elements needed for median.  
- **Max‑heap** for the left side, **min‑heap** for the right side → natural split point.  
- **Balancing** enforces a strict size relation (low side may have at most one extra element).  
- **Insertion** is `O(log n)` (heap insertion + at most two heap removals/additions).  
- **Median query** is `O(1)` – just peek at the tops and possibly compute an average.

---

## 2️⃣  Dry‑Run / Step‑by‑Step Trace  

The sample test case from LeetCode:

```
["MedianFinder","addNum","addNum","findMedian","addNum","findMedian"]
[[],[1],[2],[],[3],[]]
```

Interpretation (line‑by‑line):

1. **`new MedianFinder()`** – instantiate an empty object.  
2. **`addNum(1)`** – insert 1.  
3. **`addNum(2)`** – insert 2.  
4. **`findMedian()`** – query median after the two inserts.  
5. **`addNum(3)`** – insert 3.  
6. **`findMedian()`** – query median again.

We will keep three pieces of state after each operation:

- `lowerHalf` (max‑heap) – stores the “left” numbers.  
- `upperHalf` (min‑heap) – stores the “right” numbers.  
- `totalSize = lowerHalf.size() + upperHalf.size()`.

We also note the **peek** values (the top of each heap) because they are what `findMedian` uses.

---

### Step 0 – Construction  

```
lowerHalf = []          // empty max‑heap
upperHalf = []          // empty min‑heap
```

Both heaps are empty; totalSize = 0.

---

### Step 1 – `addNum(1)`  

| Action | Reasoning |
|--------|-----------|
| `lowerHalf.isEmpty()` is **true** → place `1` in `lowerHalf`. | Condition `num <= lowerHalf.peek()` is never evaluated because the heap is empty, but the rule “if empty or ≤ peek” is satisfied. |
| After insertion: `lowerHalf = [1]` (as a max‑heap, top = 1) <br> `upperHalf = []` | Insertion into a heap is O(log n) = O(log 1) = O(1). |
| **Re‑balancing check** | `lowerHalf.size() = 1`, `upperHalf.size() = 0`. <br> `1 > 0 + 1`? → **false** (they differ by exactly 1, not 2). <br> `upperHalf.size() > lowerHalf.size()`? → **false**. <br> → No moves. |
| **State after step 1** | `lowerHalf = [1]` (size = 1) <br> `upperHalf = []` (size = 0) <br> `totalSize = 1` |

Visualization (max‑heap order shown):

```
lowerHalf (max‑heap) -> 1   (top = 1)
upperHalf (min‑heap) ->   (empty)
```

---

### Step 2 – `addNum(2)`  

| Action | Reasoning |
|--------|-----------|
| `lowerHalf.peek() = 1`. Since `2 > 1`, the condition `num <= lowerHalf.peek()` fails → we **offer 2 to `upperHalf`**. |
| After insertion: `lowerHalf = [1]` (unchanged) <br> `upperHalf = [2]` (min‑heap, top = 2) |
| **Re‑balancing check** | `lowerHalf.size() = 1`, `upperHalf.size() = 1`. <br> 1 > 1 + 1? → **false**. <br> `upperHalf.size() > lowerHalf.size()`? → **false** (they are equal). <br> → No moves. |
| **State after step 2** | `lowerHalf = [1]` (size = 1) <br> `upperHalf = [2]` (size = 1) <br> `totalSize = 2` |

Median of `{1,2}` will later be `(1 + 2)/2 = 1.5`.

---

### Step 3 – `findMedian()` (first query)  

| Condition | Evaluation |
|-----------|------------|
| `lowerHalf.size() > upperHalf.size()`? → `1 > 1`? → **false**. |
| Hence we go to the *even* branch: ` (lowerHalf.peek() + upperHalf.peek()) / 2.0 ` |
| `lowerHalf.peek() = 1` <br> `upperHalf.peek() = 2` |
| Result = `(1 + 2) / 2.0 = 3 / 2.0 = 1.5` |

**Output** → `1.5` (matches the expected median of `{1,2}`).

---

### Step 4 – `addNum(3)`  

| Action | Reasoning |
|--------|-----------|
| Current `lowerHalf.peek() = 1`. New number `3` compared with `1` → `3 > 1`, so it goes to `upperHalf`. |
| Before insertion: `lowerHalf = [1]` (size = 1) <br> `upperHalf = [2]` (size = 1). |
| After `offer(3)` to `upperHalf`: `upperHalf = [2,3]` (min‑heap ordering → top stays `2`). |
| **Re‑balancing check** |
| - `lowerHalf.size() = 1`, `upperHalf.size() = 2`. <br> `1 > 2 + 1`? → **false**. <br> `upperHalf.size() > lowerHalf.size()`? → **true** (`2 > 1`). <br> Therefore we move the **minimum of `upperHalf`** to `lowerHalf`: `lowerHalf.offer(upperHalf.poll())`. |
| `upperHalf.poll()` removes the top element (`2`). It is now inserted into `lowerHalf`. |
| After this transfer: <br> `lowerHalf = [2,1]` (max‑heap order → top becomes `2`) <br> `upperHalf = [3]` (size = 1). |
| **Final check after transfer** (optional but performed again because re‑balancing runs after each insertion) | - `lowerHalf.size() = 2`, `upperHalf.size() = 1`. <br> `2 > 1 + 1`? → **false** (equal to 2, not >2). <br> `upperHalf.size() > lowerHalf.size()`? → false. |
| **State after step 4** | `lowerHalf = [2,1]` (size = 2) <br> `upperHalf = [3]` (size = 1) <br> `totalSize = 3`. <br> `lowerHalf.peek() = 2` (largest of the left side). |

The invariant now is: `lowerHalf` holds **2** elements (the smaller half), `upperHalf` holds **1** element (the larger half). Because `lowerHalf` is exactly one larger, the median will be its top.

---

### Step 5 – `findMedian()` (second query)  

| Condition |
|-----------|
| `lowerHalf.size() > upperHalf.size()`? → `2 > 1` → **true**. |
| Hence we return `lowerHalf.peek()` directly. |
| `lowerHalf.peek() = 2`. |

**Output** → `2.0` (the median of `{1,2,3}`).

---

### Summary of Variable Evolution  

| Step | lowerHalf (max‑heap) | upperHalf (min‑heap) | Median result |
|------|----------------------|----------------------|---------------|
| 0 – ctor | [] | [] | – |
| 1 – add(1) | [1] | [] | – |
| 2 – add(2) | [1] | [2] | – |
| 3 – find() | [1] | [2] | **1.5** |
| 4 – add(3) | [2,1] | [3] | – |
| 5 – find() | [2,1] | [3] | **2.0** |

---

## 3️⃣  Complexity Analysis  

### Time Complexity  

| Operation | Number of heap operations | Cost per operation | Total cost per call |
|-----------|---------------------------|--------------------|----------------------|
| `addNum`  | • One `offer` (insertion)  <br>• Possibly up to **two** `poll` + `offer` for re‑balancing | Each heap operation is **O(log k)** where *k* = current size of the heap being operated on. Since the two heaps together store *n* elements, each operation is bounded by **O(log n)**. | **O(log n)** (worst‑case when we do the balancing moves). |
| `findMedian` | • Up to two `peek` calls and a constant‑time arithmetic operation | `peek` on a `PriorityQueue` is **O(1)**. | **O(1)** |

Therefore, **amortized** (and worst‑case) time per `addNum` is **Θ(log n)**, and per `findMedian` is **Θ(1)**.

### Space Complexity  

- Both heaps together store **all** numbers that have been added so far.  
- Hence the total auxiliary space is **Θ(n)** where *n* is the number of elements inserted.  
- The data structure does **not** allocate any extra structures beyond the two priority queues, so the space overhead is minimal apart from the stored elements.

---

## 4️⃣  Take‑aways & Why This Solution Is “Elegant”

1. **Balanced Split Guarantees O(1) Median Access** – By always keeping the lower half *slightly* larger (or equal), the median becomes the top of a single heap.  
2. **Heap Choice Mirrors the Mathematics** – A max‑heap gives us the *maximum* of the left side, a min‑heap gives us the *minimum* of the right side. This mirrors exactly the definition of median in a sorted array.  
3. **Only Constant‑Time Re‑balancing Needed** – After each insertion, at most **one element moves** from one heap to the other. This keeps the heaps sizes aligned with a simple rule, making the algorithm easy to reason about.  
4. **No Need for Complex Balanced BST or Order‑Statistic Tree** – Java’s priority queues are lightweight, widely available, and have a clear API (`offer`, `poll`, `peek`). Using them directly leads to concise, production‑ready code.

---

### TL;DR  

- **Strategy:** Keep two heaps (`max‑heap` for the smaller half, `min‑heap` for the larger half) balanced such that the smaller half may have at most one extra element. Insert numbers by placing them in the appropriate heap, then rebalance if needed. The median is either the top of the larger heap (odd count) or the average of the two tops (even count).  
- **Sample Run:** With inputs `1, 2, 3` the heaps evolve as: <br>`lower = [1] → [1]` → `[2,1]`, `upper = [] → [2] → [3]`. Medians become `1.5` then `2.0`.  
- **Complexities:** `addNum` – **O(log n)** time, `findMedian` – **O(1)** time, overall **O(n)** space.  

This solution is both **optimal in asymptotic terms** and **simple to implement** using Java’s built‑in priority queues.
