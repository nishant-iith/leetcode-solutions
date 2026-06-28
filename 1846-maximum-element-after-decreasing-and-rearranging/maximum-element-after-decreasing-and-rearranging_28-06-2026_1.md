# Submission details for maximum-element-after-decreasing-and-rearranging

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 10 ms (Better than 29.58% of submissions)
- **Memory:** 77.3 MB (Better than 78.75% of submissions)

## Solution Code
```java
class Solution {
    public int maximumElementAfterDecrementingAndRearranging(int[] A) {
        Arrays.sort(A);
        int n = A.length;

        A[0] = 1;
        for (int i = 1; i < n; i++)
            A[i] = Math.min(A[i], A[i - 1] + 1);
        
        return A[n - 1];
    }
}
```

## Detailed Explanation
### 1. **Algorithm Analysis**

The algorithm aims to maximize the largest element in an array after performing two operations: **decreasing elements** and **rearranging them**. The key insight is that the optimal arrangement is a non-decreasing sequence where each element is at most one greater than the previous element. This ensures the sequence grows as quickly as possible given the original values.

#### **Steps of the Algorithm:**
1. **Sort the Array**: Sorting helps in systematically processing elements from smallest to largest, allowing us to build the sequence incrementally.
2. **Initialize the First Element**: Set the first element to 1. This is the minimal starting point to allow subsequent elements to grow.
3. **Greedy Adjustment**: For each subsequent element, set it to the minimum of its original value and the previous element + 1. This ensures:
   - The sequence is non-decreasing.
   - Each element is as large as possible under the constraint of being at most one more than the previous.
4. **Return the Last Element**: The last element of the adjusted array is the maximum possible value after the operations.

#### **Why This Works:**
- Sorting ensures we process smaller elements first, which are more restrictive in terms of growth.
- By setting each element to the minimum of its original value and the previous + 1, we maximize the final element without violating the constraints.

---

### 2. **Step-by-Step Walkthrough (DRY Run)**

**Sample Input:** `[2, 2, 1, 2, 1]`

**Step 1: Sort the Array**
- Sorted array: `[1, 1, 2, 2, 2]`
- `n = 5`

**Step 2: Initialize First Element**
- `A[0] = 1` → Array becomes `[1, 1, 2, 2, 2]`

**Step 3: Iterate and Adjust Elements**
- **i = 1**:
  - `A[1] = min(1, 1 + 1) = min(1, 2) = 1`
  - Array: `[1, 1, 2, 2, 2]`
- **i = 2**:
  - `A[2] = min(2, 1 + 1) = min(2, 2) = 2`
  - Array: `[1, 1, 2, 2, 2]`
- **i = 3**:
  - `A[3] = min(2, 2 + 1) = min(2, 3) = 2`
  - Array: `[1, 1, 2, 2, 2]`
- **i = 4**:
  - `A[4] = min(2, 2 + 1) = min(2, 3) = 2`
  - Array: `[1, 1, 2, 2, 2]`

**Final Array:** `[1, 1, 2, 2, 2]`  
**Maximum Element:** `2`

---

### 3. **Complexity Analysis**

#### **Time Complexity: O(n log n)**
- **Sorting**: The `Arrays.sort()` operation dominates the time complexity, which is O(n log n) for Java's built-in sort.
- **Loop**: The subsequent loop runs in O(n) time.
- **Total**: O(n log n) + O(n) = **O(n log n)**.

#### **Space Complexity: O(1)**
- The algorithm modifies the input array in-place during sorting and adjustments.
- Only a constant amount of extra space is used for variables like `n` and loop indices.
- **Total**: **O(1)** auxiliary space.

---

### **Key Takeaways**
- The algorithm leverages sorting and greedy adjustments to construct the optimal sequence.
- The constraints ensure the sequence grows as quickly as possible, maximizing the final element.
- Sorting is critical for systematically processing elements and applying the greedy strategy.

