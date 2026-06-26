# Submission details for count-subarrays-with-majority-element-ii

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 5 ms (Better than 80.33% of submissions)
- **Memory:** 89.8 MB (Better than 55.74% of submissions)

## Solution Code
```java
class Solution {
    public long countMajoritySubarrays(int[] nums, int target) {
        int n = nums.length;
        long cnt = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] == target) nums[i] = 1;
            else nums[i] = -1;
        }

        int[] pref = new int[n];
        pref[0] = nums[0];

        for (int i = 1; i < n; i++) {
            pref[i] = pref[i - 1] + nums[i];
        }

        int shift = n;
        int[] freq = new int[2 * n + 1];

        freq[shift] = 1;

        long valid = 0;
        int lastSum = 0;

        for (int i = 0; i < n; i++) {
            if (pref[i] > lastSum) {
                valid += freq[lastSum + shift];
            } else {
                valid -= freq[pref[i] + shift];
            }

            cnt += valid;
            freq[pref[i] + shift]++;
            lastSum = pref[i];
        }

        return cnt;
    }
}
```

## Detailed Explanation
### **Algorithm Analysis**

The problem requires counting the number of subarrays where an element occurs **more than half the subarray length**. The solution employs a **prefix sum transformation** and a **frequency array** to efficiently determine valid subarrays using a **sliding window** approach.

#### **Key Steps**
1. **Transform the Array**: Convert the array such that elements equal to `target` are replaced with `1`, and others with `-1`. This allows the problem to be rephrased: count subarrays where the sum is **positive** (indicating the target's count exceeds half the subarray length).
2. **Prefix Sum Calculation**: Compute prefix sums to leverage range sum properties. Each prefix sum represents the cumulative effect of the array up to that index.
3. **Frequent Indices Window Management**: Use a sliding window over prefix indices to track valid subarrays. For each index `j`, the valid subarrays ending at `j` are those starting at indices `i` where the prefix sum at `i` (<= `j`) is **greater** than the prefix sum at `j` (+1). The frequency array tracks valid `i` indices dynamically as the window slides.
4. **Pointer and Frequency Tracking**: A pointer `lastSum` maintains the highest valid prefix sum encountered so far. Frequencies of prefix sums within the current valid window are stored in `freq` to compute the count of valid subarrays efficiently.

---

### **Step-by-Step Walkthrough**

**Sample Test Case**: `nums = [1,2,2,3]`, `target = 2`

#### **Step 1: Transform Array**
Original: `[1,2,2,3]`  
Transformed: `[0, 1, 1, -1]` (1 for `target=2`, -1 otherwise).

#### **Step 2: Compute Prefix Sums**
- `pref[0] = 0`  
- `pref[1] = 0 + 1 = 1`  
- `pref[2] = 1 + 1 = 2`  
- `pref[3] = 2 + (-1) = 1`

#### **Step 3: Initialize Frequency Array**
- `shift = n = 4` (to handle negative prefix sums).
- Initial `freq[shift] = freq[4] = 1` (prefix sum 0 is present before processing).

#### **Step 4: Process Each Index**
**Index 0 (`pref[0] = 0`)**:
- `lastSum = -1` (initially).
- No valid `i` (empty window). `valid = 0`.
- `freq[0 + 4] = 1` (now tracks prefix 0).  
**Variables**: `cnt = 0`, `lastSum = 0`, `freq[4] = 1`.

**Index 1 (`pref[1] = 1`)**:
- `lastSum = 0` (<=1).
  - All `i` in `[-1,0]` (indices processed so far) have `pref[i] < pref[1]`.
  - `valid += 2` (sum for prefix 0 and -1).  
- `cnt += 2` (total `cnt = 2`).  
- `freq[1 + 4] = 1` (now tracks prefix 1).  
**Variables**: `cnt = 2`, `lastSum = 1`.

**Index 2 (`pref[2] = 2`)**:
- `lastSum = 1` (<=2).
  - All `i` in `[-1,0,1]` have `pref[i] < pref[2]`.
  - `valid += 3` (sum for prefixes -1, 0, 1).  
- `cnt += 3` (total `cnt = 5`).  
- `freq[2 + 4] = 1` (now tracks prefix 2).  
**Variables**: `cnt = 5`, `lastSum = 2`.

**Index 3 (`pref[3] = 1`)**:
- `lastSum = 2` (>1), `valid -= 1` (only `i=2` has `pref[i] > pref[3]`).
- `cnt += 0` (net `valid = 0`).  
- `freq[1 + 4] = 2` (now tracks prefix 1 twice).  
**Variables**: `cnt = 5`, `lastSum = 1`.

#### **Final Result**: `cnt = 5` (matches sample output).

---

### **Complexity Analysis**

**Time Complexity**: **O(n)**  
- Transforming the array: **O(n)**.
- Computing prefix sums: **O(n)**.
- Iterating with the sliding window and frequency updates: **O(n)** with constant-time operations per iteration.  
**Total**: Linear time.

**Space Complexity**: **O(n)**  
- Prefix sum array: **O(n)**.
- Frequency array: **O(2n + 1) ≈ O(n)** (shifted by `n` to handle negative sums).

---

### **Key Insights**
- **Sum > 0**: A subarray `(i, j)` has a positive sum, ensuring `2 * count(target) > j - i + 1` (target count > half length).
- **Sliding Window**: The window `[-1, k-1]` expands/expands to track valid previous indices `i`, with `freq` dynamically updated to count valid subarrays ending at each `j`.

This approach efficiently narrows down valid subarrays using prefix sums and frequency tracking, achieving optimal linear time complexity.

