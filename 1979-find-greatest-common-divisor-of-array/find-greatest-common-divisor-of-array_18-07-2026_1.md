# Submission details for find-greatest-common-divisor-of-array

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 0 ms (Better than 100.00% of submissions)
- **Memory:** 44.9 MB (Better than 73.69% of submissions)

## Solution Code
```java
class Solution {
    public int findGCD(int[] nums) {
        int mn = 1001, mx = 0;

        for (int num : nums) {
            if (num < mn)
                mn = num;
            if (num > mx)
                mx = num;
        }

        return gcd(mn, mx);
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int rem = a % b;
            a = b;
            b = rem;
        }
        return a;
    }
}
```

## Detailed Explanation
### 1. Algorithm Analysis

**Problem Restatement**:  
Given an integer array `nums`, find the greatest common divisor (GCD) of the smallest and largest elements in the array. The GCD of two numbers is the largest positive integer that divides both without a remainder.

**High‑Level Strategy**:  
1. **Find the minimum and maximum values** in the array by a single linear scan.  
2. **Compute the GCD** of these two numbers using the Euclidean algorithm, which repeatedly replaces the larger number by its remainder when divided by the smaller number until the remainder becomes zero; the last non‑zero remainder is the GCD.

**Why This Works**:  
- The problem asks explicitly for the GCD of the smallest and largest numbers, not of all array elements. Thus, locating the extremes is sufficient.
- The Euclidean algorithm is a well‑known, efficient method to compute the GCD of two integers. Its termination is guaranteed because the remainder strictly decreases in each step.

**Key Details in the Implementation**:
- The initial values for `mn` (minimum) and `mx` (maximum) are set to `1001` and `0` respectively, leveraging the problem’s constraints (`1 ≤ nums[i] ≤ 1000`). This ensures that any element will update both `mn` and `mx` correctly.
- The GCD subroutine is iterative to avoid recursion stack overhead and to keep constant extra space.

---

### 2. Step‑by‑Step Walkthrough (Dry‑Run)

**Input**: `nums = [2, 5, 6, 9, 10]`

We trace the variable states during the scan and then the Euclidean steps.

#### 2.1 Scan for Minimum and Maximum

| Step | Index | `num` | `mn` (current min) | `mx` (current max) |
|------|-------|-------|--------------------|--------------------|
| 0    | 0     | 2     | 2                  | 2                  |
| 1    | 1     | 5     | 2                  | 5                  |
| 2    | 2     | 6     | 2                  | 6                  |
| 3    | 3     | 9     | 2                  | 9                  |
| 4    | 4     | 10    | 2                  | 10                 |

After the loop: `mn = 2`, `mx = 10`.

#### 2.2 Euclidean GCD Computation (`gcd(2, 10)`)

We use variables `a = mn = 2`, `b = mx = 10`.

| Iteration | `a` | `b` | `b != 0?` | `rem = a % b` | Update `a = b`, `b = rem` |
|-----------|-----|-----|-----------|---------------|---------------------------|
| 1         | 2   | 10  | true      | `2 % 10 = 2` | `a = 10`, `b = 2`         |
| 2         | 10  | 2   | true      | `10 % 2 = 0` | `a = 2`, `b = 0`          |
| 3         | 2   | 0   | false     | –             | loop ends, result = `a = 2` |

**Final Result**: `2`

The function returns `2`, which is indeed the GCD of the smallest (2) and largest (10) numbers in the array.

---

### 3. Complexity Analysis

#### 3.1 Time Complexity
- **Finding min and max**: One pass over the array of length `n` → **O(n)**.
- **Euclidean GCD**: The iterative Euclidean algorithm runs in **O(log(min(a,b)))** time, as each step reduces the larger number by at least half (the remainder is `< b`). With `a = mn` and `b = mx`, the bound is `O(log(min(mn,mx)))`.
- **Overall**:  
  \[
  T(n) = O(n) + O(\log(\min(\text{mn},\text{mx}))) = O(n + \log M)
  \]
  where `M` is the maximum possible value (≤ 1000). In practice, `log M` is a tiny constant (~10), so the algorithm is effectively **linear, O(n)**.

#### 3.2 Space Complexity
- Only a few integer variables (`mn`, `mx`, `rem`, `a`, `b`) are used, regardless of input size.
- **Space**: **O(1)** (constant auxiliary space).

---

### Summary
The solution efficiently computes the required GCD by first locating the extremes of the array and then applying the Euclidean algorithm. It operates in linear time with constant extra space, making it optimal for the problem constraints. The dry‑run confirms correctness on the provided sample, yielding the expected result of `2`.
