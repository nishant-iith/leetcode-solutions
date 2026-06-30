# Submission details for number-of-substrings-containing-all-three-characters

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 11 ms (Better than 91.60% of submissions)
- **Memory:** 46.2 MB (Better than 62.27% of submissions)

## Solution Code
```java
class Solution {
    public int numberOfSubstrings(String s) {
        int res = 0;
        int[] p = {-1, -1, -1};

        for (int i = 0; i < s.length(); i++) {
            p[(s.charAt(i) & 31) - 1] = i;
            res += Math.min(p[0], Math.min(p[1], p[2])) + 1;
        }

        return res;
    }
}
```

## Detailed Explanation

# Analysis of LeetCode Solution: Number of Substrings Containing All Three Characters

## 1. Algorithm Analysis

This solution employs a clever **greedy tracking approach** that efficiently counts valid substrings by leveraging the positions of the most recent occurrences of each character.

### Core Strategy:
- Track the **last seen positions** of 'a', 'b', and 'c' using an array `p`
- For each position `i`, calculate how many new valid substrings **end at position `i`**
- A substring ending at `i` is valid if it contains all three characters
- The key insight: if we know the last positions of all three characters, any substring starting from index 0 to `min(p[0], p[1], p[2])` will be valid

### Mathematical Foundation:
When processing character at index `i`, after updating its position:
- `min(p[0], p[1], p[2])` gives the earliest position among the last occurrences
- Any substring starting at index `0` to `min_pos` and ending at `i` is guaranteed to contain all three characters
- This contributes `(min_pos + 1)` new valid substrings (indices 0 through min_pos, inclusive)

### Character Mapping:
The expression `(s.charAt(i) & 31) - 1` efficiently maps characters to indices:
- 'a' = 97, 'a' & 31 = 1, index = 0
- 'b' = 98, 'b' & 31 = 2, index = 1  
- 'c' = 99, 'c' & 31 = 3, index = 2

## 2. Step-by-Step Walkthrough (DRY Run)

**Sample Input:** `"abcabc"` (length = 6)

| Iteration | Char | Index | Action | p Array State | Min Position | New Substrings Added | Result |
|-----------|------|-------|--------|---------------|--------------|---------------------|---------|
| Initial   | -    | -     | Setup  | [-1, -1, -1]  | -            | 0                   | 0       |
| 0         | 'a'  | 0     | p[0]=0 | [0, -1, -1]   | -1           | max(0, -1+1)=0      | 0       |
| 1         | 'b'  | 1     | p[1]=1 | [0, 1, -1]    | -1           | max(0, -1+1)=0      | 0       |
| 2         | 'c'  | 2     | p[2]=2 | [0, 1, 2]     | 0            | 0+1=1               | 1       |
| 3         | 'a'  | 3     | p[0]=3 | [3, 1, 2]     | 1            | 1+1=2               | 3       |
| 4         | 'b'  | 4     | p[1]=4 | [3, 4, 2]     | 2            | 2+1=3               | 6       |
| 5         | 'c'  | 5     | p[2]=5 | [3, 4, 5]     | 3            | 3+1=4               | 10      |

### Detailed Explanation of Key Steps:

**Iteration 2 (char='c'):**
- First time all three characters seen
- `p = [0, 1, 2]`, `min(p) = 0`
- Substrings ending at index 2 that are valid: `"abc"` (starts at 0)
- Count: `0 + 1 = 1`

**Iteration 3 (char='a'):**
- Updated `p[0] = 3`
- `p = [3, 1, 2]`, `min(p) = 1`
- Valid substrings ending at index 3: `"bca"` (start=1), `"abc"` (start=0)
- Count: `1 + 2 = 3` (cumulative)

**Iteration 5 (final):**
- All positions updated: `p = [3, 4, 5]`
- `min(p) = 3`
- Valid substrings ending at index 5: starting positions 0,1,2,3
- Count: `3 + 4 = 10` (cumulative)

## 3. Complexity Analysis

### Time Complexity: **O(n)**
- Single loop iterating through the string once
- Each iteration performs constant-time operations:
  - Character access: O(1)
  - Array index calculation: O(1)
  - Array update: O(1)
  - Min calculation among 3 elements: O(1)
  - Arithmetic operations: O(1)

### Space Complexity: **O(1)**
- Fixed-size array `p` of length 3: O(1)
- Integer variables `res` and loop counter: O(1)
- No additional data structures that grow with input size
- Total auxiliary space is constant regardless of input string length

### Justification:
The algorithm makes exactly one pass through the string, performing only constant-time work per character. The space usage doesn't depend on the input size, making both time and space complexities optimal for this problem.

