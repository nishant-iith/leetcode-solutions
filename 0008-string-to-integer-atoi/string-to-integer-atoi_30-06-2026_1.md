# Submission details for string-to-integer-atoi

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 100.00% of submissions)
- **Memory:** 43.6 MB (Better than 75.55% of submissions)

## Solution Code
```java
class Solution {
    public int myAtoi(String s) {
        s = s.trim(); 
        int sign = 1, i = 0;
        long res = 0; 

        if (s.length() == 0) return 0;

        if (s.charAt(0) == '-') { sign = -1; i++; }
        else if (s.charAt(0) == '+') i++; 

        while (i < s.length()) {
            char ch = s.charAt(i);
            if (ch < '0' || ch > '9') break; 

            res = res * 10 + (ch - '0'); 
            if (sign * res > Integer.MAX_VALUE) return Integer.MAX_VALUE; 
            if (sign * res < Integer.MIN_VALUE) return Integer.MIN_VALUE;

            i++;
        }
        return (int) (sign * res);
    }
}
```

## Detailed Explanation
**1. Algorithm Analysis**

The problem “String to Integer (atoi)” asks us to convert a string that may contain optional leading whitespace, an optional sign (`+` or `-`), and then a sequence of decimal digits into a 32‑bit signed integer.  
If the conversion is out of the range `[-2³¹, 2³¹‑1]` we must clamp the result to `INT_MAX` (`2³¹‑1`) or `INT_MIN` (`-2³¹`).  
If the string does not start with a valid numeric part (after trimming) we return `0`.

The presented solution follows a **straight‑forward linear scan** with the following logical steps:

| Step | What it does | Why it is needed |
|------|--------------|------------------|
| **Trim** (`s = s.trim()`) | Removes leading and trailing whitespace. | The specification says ignore any whitespace before the numeric part. |
| **Initialize** (`sign = 1, i = 0, res = 0`) | `sign` holds the final sign (`+1` or `-1`). `i` is the current index in the string. `res` accumulates the numeric value as a **long** (64‑bit) to make overflow detection easy. | We need a sign, a pointer to the current character, and a variable that can hold values larger than `int` while we are checking for overflow. |
| **Empty‑string guard** (`if (s.length()==0) return 0;`) | Handles the degenerate case where the string becomes empty after trimming. | No characters → no number → return 0. |
| **Sign detection** (`if (s.charAt(0)=='-') … else if (s.charAt(0)=='+')`) | Checks the first non‑whitespace character for a sign, updates `sign` and moves `i` past it. | The sign may appear only once and only before the digits. |
| **Main loop** (`while (i < s.length())`) | Repeatedly reads characters as long as they are digits (`'0'`‑`'9'`). For each digit: <br> `res = res * 10 + (ch - '0')` <br> then checks overflow: <br> `if (sign * res > INT_MAX) return INT_MAX;` <br> `if (sign * res < INT_MIN) return INT_MIN;` | The loop stops at the first non‑digit (the spec says “stop reading further”). The multiplication‑by‑10 step builds the number digit by digit. The overflow checks prevent the `long` accumulator from exceeding the 32‑bit range; if it does, we clamp immediately. |
| **Return** (`return (int) (sign * res);`) | Casts the final (clamped) value back to `int`. | The problem expects a 32‑bit signed integer. |

**Key design choices**

* **Use `long` for the accumulator** – Java’s `int` overflows silently; a `long` (64‑bit) can safely hold values up to `2³²` (≈ 4 billion) which is larger than the required `±2³¹`. This makes overflow detection trivial: we just compare `sign * res` with `INT_MAX` / `INT_MIN`.
* **Early clamping** – As soon as the intermediate value exceeds the 32‑bit limits we return the appropriate bound. This avoids doing the final cast on an out‑of‑range value (which would wrap around in Java).
* **Single pass** – The algorithm scans the string once, so it is optimal in time.

---

**2. Step‑by‑Step Walkthrough (DRY Run) – Sample Input `"42"`**

We will track the variables after each statement/iteration.

| Step | Action | `s` | `sign` | `i` | `res` (long) | Comments |
|------|--------|-----|--------|-----|--------------|----------|
| 0 | `s = s.trim()` | `"42"` (no spaces) | 1 | 0 | 0 | Trimming does nothing. |
| 1 | `if (s.length()==0)` → false | – | – | – | – | String not empty. |
| 2 | `if (s.charAt(0)=='-')` → false | – | 1 | 0 | 0 | No leading `-`. |
| 3 | `else if (s.charAt(0)=='+')` → false | – | 1 | 0 | 0 | No leading `+`. |
| 4 | **Enter loop** (`i=0 < 2`) | – | – | – | – | – |
| 4a | `ch = s.charAt(0) = '4'`<br>`if (ch < '0' || ch > '9')` → false | – | – | – | – | `'4'` is a digit, continue. |
| 4b | `res = res * 10 + (ch - '0')` → `0*10 + (57-48) = 4` | – | – | – | 4 | First digit processed. |
| 4c | Overflow check:<br>`sign * res = 1 * 4 = 4` → `4 > INT_MAX?` No.<br>`4 < INT_MIN?` No. | – | – | – | 4 | Still safe. |
| 4d | `i++` → `i = 1` | – | – | – | – | Move to next character. |
| 5 | **Loop again** (`i=1 < 2`) | – | – | – | – | – |
| 5a | `ch = s.charAt(1) = '2'`<br>`if (ch < '0' || ch > '9')` → false | – | – | – | – | `'2'` is a digit. |
| 5b | `res = res * 10 + (ch - '0')` → `4*10 + (50-48) = 42` | – | – | – | 42 | Second digit processed. |
| 5c | Overflow check:<br>`sign * res = 1 * 42 = 42` → still ≤ `INT_MAX` (2 147 483 647) and ≥ `INT_MIN`. | – | – | – | 42 | No overflow. |
| 5d | `i++` → `i = 2` | – | – | – | – | End of string reached. |
| 6 | Loop condition fails (`i=2` not `< s.length()`). | – | – | – | – | Exit loop. |
| 7 | `return (int) (sign * res)` → `(int) (1 * 42) = 42` | – | – | – | – | Final answer. |

**Result:** The method returns `42`, which matches the expected output.

---

**3. Complexity Analysis**

* **Time Complexity** – **O(n)**, where *n* is the length of the input string after trimming.  
  *Reason*: The algorithm makes a single pass over the characters, performing O(1) work (character check, arithmetic, overflow test) for each character. No nested loops or recursive calls are used.

* **Space Complexity** – **O(1)** (constant auxiliary space).  
  *Reason*: Only a few primitive variables (`sign`, `i`, `res`, `ch`) are kept, regardless of the input size. The trimmed string itself is a view of the original (or a new string created by `trim`, which is also O(1) extra in Java because it shares the underlying char array). No additional data structures proportional to *n* are allocated.

---

### Summary

* The algorithm trims whitespace, extracts an optional sign, then scans the remaining characters digit‑by‑digit, building the numeric value in a `long` accumulator.
* Overflow is detected early by comparing `sign * res` with `INT_MAX` / `INT_MIN`; if it exceeds the 32‑bit range we clamp to the appropriate bound.
* The sample `"42"` is processed step‑by‑step, ending with the correct result `42`.
* The solution runs in linear time and constant extra space, satisfying the problem constraints.
