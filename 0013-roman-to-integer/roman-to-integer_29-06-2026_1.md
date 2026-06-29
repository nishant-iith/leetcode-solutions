# Submission details for roman-to-integer

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 2 ms (Better than 100.00% of submissions)
- **Memory:** 46.4 MB (Better than 62.59% of submissions)

## Solution Code
```java
class Solution {
    public int romanToInt(String s) {
        int ans = 0, num = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            switch (s.charAt(i)) {
                case 'I':
                    num = 1;
                    break;
                case 'V':
                    num = 5;
                    break;
                case 'X':
                    num = 10;
                    break;
                case 'L':
                    num = 50;
                    break;
                case 'C':
                    num = 100;
                    break;
                case 'D':
                    num = 500;
                    break;
                case 'M':
                    num = 1000;
                    break;
            }
            if (4 * num < ans)
                ans -= num;
            else
                ans += num;
        }
        return ans;

    }
}
```

## Detailed Explanation
### 1. Algorithm Analysis

The core challenge of converting Roman Numerals to Integers lies in the **subtractive property**. While most Roman numerals are additive (e.g., `VI = 5 + 1`), certain combinations signify subtraction (e.g., `IV = 5 - 1`).

#### The Strategy: Right-to-Left Processing
Most standard solutions process the string from left to right, requiring a "look-ahead" to see if the current character is smaller than the next. This specific solution employs a more elegant approach: **it processes the string from right to left (backward).**

**The Logic:**
1. **Reverse Traversal:** By starting from the end of the string, we always know the value of the "previous" (to the right) numeral.
2. **The Greedy Addition/Subtraction Rule:** 
   - If we encounter a number that is significantly smaller than the current total accumulated (`ans`), it implies a subtraction case (like `I` in `IV`).
   - **The "Magic" Constant `4 * num < ans`:** This is a clever heuristic used instead of a standard `if (num < prev_num)`. 
     - In Roman numerals, subtraction only occurs in specific patterns: `IV (1, 5)`, `IX (1, 10)`, `XL (10, 50)`, `XC (10, 100)`, `CD (100, 500)`, and `CM (100, 1000)`.
     - In all these cases, the value being subtracted is much smaller than the value already accumulated. 
     - If `num` is 1 and `ans` is 5 (from `V`), `4 * 1 < 5` is true, so we subtract 1.
     - If `num` is 10 and `ans` is 1000, `4 * 10 < 1000` is true, so we subtract 10.
     - This constant `4` acts as a threshold to distinguish between additive cases and subtractive cases without needing to track the `prev_num` explicitly.

---

2. **Step-by-Step Walkthrough (Dry Run)**

**Input:** `s = "III"`

| Iteration | Index `i` | `s.charAt(i)` | `num` (assigned) | Condition: `4 * num < ans` | Action | `ans` (Updated) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Initial** | - | - | 0 | - | - | 0 |
| **1** | 2 | `'I'` | 1 | `4 * 1 < 0` $\rightarrow$ **False** | `ans += 1` | 1 |
| **2** | 1 | `'I'` | 1 | `4 * 1 < 1` $\rightarrow$ **False** | `ans += 1` | 2 |
| **3** | 0 | `'I'` | 1 | `4 * 1 < 2` $\rightarrow$ **False** | `ans += 1` | 3 |

**Final Result:** `3`

---

**Complexity Test Case (Subtractive):** `s = "IV"`

| Iteration | Index `i` | `s.charAt(i)` | `num` | Condition: `4 * num < ans` | Action | `ans` |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Initial** | - | - | 0 | - | - | 0 |
| **1** | 1 | `'V'` | 5 | `4 * 5 < 0` $\rightarrow$ **False** | `ans += 5` | 5 |
| **2** | 0 | `'I'` | 1 | `4 * 1 < 5` $\rightarrow$ **True** | `ans -= 1` | 4 |

**Final Result:** `4`

---

### 3. Complexity Analysis

#### **Time Complexity: $O(n)$**
- **Reasoning:** The algorithm uses a single `for` loop that iterates through the string exactly once. 
- Let $n$ be the length of the input string $s$. 
- Inside the loop, the `switch` statement performs a constant time $O(1)$ lookup, and the arithmetic comparisons/operations are also $O(1)$.
- Therefore, the total time is proportional to the length of the string: $T(n) = n \times O(1) = O(n)$.

#### **Space Complexity: $O(1)$**
- **Reasoning:** The algorithm only uses a fixed number of integer variables (`ans`, `num`, `i`). 
- The space used does not grow with the size of the input string. 
- No additional data structures (like HashMaps or Arrays) are allocated that scale with $n$. Thus, the space complexity is constant.
