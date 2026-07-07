# Submission details for concatenate-non-zero-digits-and-multiply-by-sum-i

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 99.85% of submissions)
- **Memory:** 42.5 MB (Better than 83.11% of submissions)

## Solution Code
```java
class Solution {
    public long sumAndMultiply(int n) {
        long x = 0, s = 0;
        for (char c : String.valueOf(n).toCharArray())
            if (c != '0') {
                x = x * 10 + c - '0';
                s += c - '0';
            }
        return x * s;
    }
}
```

## Detailed Explanation

# LeetCode Solution Analysis: Concatenate Non-Zero Digits and Multiply by Sum-I

## 1. Algorithm Analysis

This solution implements a straightforward approach to solve the problem:

**Problem Understanding:**
- Given an integer `n`, we need to:
  1. Extract all non-zero digits from `n`
  2. Concatenate these non-zero digits to form a new number
  3. Calculate the sum of these non-zero digits
  4. Return the product of the concatenated number and the sum

**Strategy:**
1. Convert the integer to a string to process each digit individually
2. Iterate through each character/digit
3. For each non-zero digit:
   - Build the concatenated number using `x = x * 10 + digit_value`
   - Accumulate the sum of digits
4. Return the final product

## 2. Step-by-Step Walkthrough (DRY Run)

Let's trace through the sample test case: **n = 10203004**

| Iteration | Character | Is Non-Zero? | x Calculation | x New Value | s Calculation | s New Value |
|-----------|-----------|--------------|---------------|-------------|---------------|-------------|
| Initial   | -         | -            | -             | 0           | -             | 0           |
| 1         | '1'       | Yes          | 0×10 + 1      | 1           | 0 + 1         | 1           |
| 2         | '0'       | No           | No change     | 1           | No change     | 1           |
| 3         | '2'       | Yes          | 1×10 + 2      | 12          | 1 + 2         | 3           |
| 4         | '0'       | No           | No change     | 12          | No change     | 3           |
| 5         | '3'       | Yes          | 12×10 + 3     | 123         | 3 + 3         | 6           |
| 6         | '0'       | No           | No change     | 123         | No change     | 6           |
| 7         | '0'       | No           | No change     | 123         | No change     | 6           |
| 8         | '4'       | Yes          | 123×10 + 4    | 1234        | 6 + 4         | 10          |

**Final Result:** `x × s = 1234 × 10 = 12340`

**Key Observations:**
- Non-zero digits extracted: 1, 2, 3, 4
- Concatenated number (x): 1234
- Sum of digits (s): 10
- Final result: 12340

## 3. Complexity Analysis

### Time Complexity: **O(d)** where d = number of digits in n

**Justification:**
- Converting integer to string: O(d)
- Iterating through each character: O(d)
- Each iteration performs constant-time operations (character comparison, arithmetic)
- Since d = ⌊log₁₀(n)⌋ + 1, we can also express this as **O(log n)**

### Space Complexity: **O(d)** where d = number of digits in n

**Justification:**
- `String.valueOf(n)`: Creates a string of length d → O(d) space
- `.toCharArray()`: Creates a character array of length d → O(d) space
- Variables `x` and `s` use constant space
- Overall space is dominated by the string representation → **O(log n)**

## Additional Notes

**Why `long` instead of `int`?**
The concatenated number could be very large (up to 9 digits if all non-zero), exceeding `Integer.MAX_VALUE`. Using `long` prevents overflow.

**Alternative Approaches:**
- Mathematical approach using modulo operations (more complex but avoids string conversion)
- Using `StringBuilder` for concatenation (slightly different performance characteristics)

The current solution prioritizes readability and simplicity while maintaining optimal time complexity.

