# Submission details for roman-to-integer

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 2 ms (Better than 100.00% of submissions)
- **Memory:** 46.2 MB (Better than 85.71% of submissions)

## Solution Code
```java
class Solution {
    public int romanToInt(String s) {
        int total = 0;
        int prev = 0;

        // Iterate backwards through the string
        for (int i = s.length() - 1; i >= 0; i--) {
            int curr = getValue(s.charAt(i));

            // If current is smaller than the value to its right, subtract it
            if (curr < prev) {
                total -= curr;
            } else {
                total += curr;
            }
            prev = curr;
        }
        return total;
    }

    private int getValue(char c) {
        switch (c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }
}

```

## Detailed Explanation
###1. Algorithm Analysis

The solution employs a **single-pass, right-to-left (reverse) iteration** strategy. This approach elegantly handles the subtractive notation rule of Roman numerals (e.g., IV = 4, IX = 9) without requiring look-ahead or complex conditional branching for specific pairs.

**Core Logic:**
*   Roman numerals are generally written from largest to smallest (left to right).
*   **Subtractive Rule:** If a smaller value appears *before* a larger value (e.g., `I` before `V`), the smaller value is subtracted.
*   By iterating **backwards** (right to left), the algorithm processes the "larger" value *before* the "smaller" value that might precede it in the original string.
*   We maintain a `prev` variable storing the value of the character processed in the previous iteration (which corresponds to the character immediately to the **right** in the original string).
*   **Decision Rule:**
    *   If `curr < prev`: The current character is a "subtractive" prefix (e.g., `I` in `IV`). We **subtract** `curr` from the total.
    *   Else (`curr >= prev`): The current character is additive or part of a standard sequence (e.g., `V` in `IV`, or `I` in `III`). We **add** `curr` to the total.

**Why this works:**
Consider `IV` (4). Original: `I` (1), `V` (5).
*   Iteration 1 (Rightmost `V`): `curr=5`, `prev=0`. `5 >= 0` → `total += 5` (total=5). `prev=5`.
*   Iteration 2 (Leftmost `I`): `curr=1`, `prev=5`. `1 < 5` → `total -= 1` (total=4). Correct.

Consider `VI` (6). Original: `V` (5), `I` (1).
*   Iteration 1 (`I`): `curr=1`, `prev=0` → `total=1`, `prev=1`.
*   Iteration 2 (`V`): `curr=5`, `prev=1`. `5 >= 1` → `total += 5` (total=6). Correct.

The helper method `getValue` provides O(1) mapping from Roman character to integer value using a `switch` statement.

---

### 2. Step-by-Step Walkthrough (Dry Run)

**Sample Test Case:** `s = "III"` (Expected Output: 3)

**Initial State:**
*   `total = 0`
*   `prev = 0`
*   String Length = 3. Indices: 0:'I', 1:'I', 2:'I'
*   Loop: `i` from 2 down to 0.

| Iteration | `i` | `s.charAt(i)` | `curr = getValue(...)` | Condition: `curr < prev` | Action | `total` (after) | `prev` (after) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Start** | - | - | - | - | - | **0** | **0** |
| 1 | 2 | `'I'` | 1 | `1 < 0` → **False** | `total += 1` | **1** | **1** |
| 2 | 1 | `'I'` | 1 | `1 < 1` → **False** | `total += 1` | **2** | **1** |
| 3 | 0 | `'I'` | 1 | `1 < 1` → **False** | `total += 1` | **3** | **1** |

**Final Return:** `total` = **3**.

**Trace Summary:** Since all characters are `'I'` (value 1), `curr` is never less than `prev` (which becomes 1 after the first step). The algorithm simply accumulates the sum: 1 + 1 + 1 = 3.

---

### 3. Complexity Analysis

#### Time Complexity: **O(n)**
*   **Justification:** The algorithm performs a single loop over the input string `s` of length `n`. Inside the loop, all operations (`charAt`, `getValue` switch, comparison, arithmetic, assignment) are **O(1)** constant time. Therefore, total time scales linearly with the input size: `T(n) = n * O(1) = O(n)`.

#### Space Complexity: **O(1)** (Constant Auxiliary Space)
*   **Justification:** The algorithm uses a fixed number of primitive variables (`total`, `prev`, `i`, `curr`) regardless of the input size. The `getValue` method uses only the call stack (O(1) depth) and no auxiliary data structures proportional to `n`. The input string `s` is not copied. Thus, extra space is constant.

---

**Key Takeaway:** This reverse-iteration technique is a canonical pattern for parsing Roman Numerals (and similar "look-behind" problems) because it transforms a "look-ahead" requirement (checking the *next* character) into a simple state variable (`prev`) check, achieving optimal O(n) time and O(1) space.
