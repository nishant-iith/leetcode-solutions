# Submission details for find-the-index-of-the-first-occurrence-in-a-string

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 2 ms (Better than 14.53% of submissions)
- **Memory:** 43.2 MB (Better than 33.82% of submissions)

## Solution Code
```java

class Solution {
    public int strStr(String haystack, String needle) {
        if (
            haystack == null || needle == null || 
            needle.length() > haystack.length()
        ) return -1;

        // Step 1: build lps[] array for needle
        int[] lps = buildLPSArray(needle);

        // Step 2: KMP Sliding Window
        int hIndex = 0;
        int nIndex = 0;

        while (hIndex < haystack.length()) {
            // case 1: haystack[hIndex] == needle[nIndex]
            if (haystack.charAt(hIndex) == needle.charAt(nIndex)) {
                hIndex++;
                nIndex++;
            } else { // haystack[hIndex] != needle[nIndex]
                if (nIndex > 0) nIndex = lps[nIndex]; // case 2.1
                else hIndex++;                        // case 2.2
            }

            // check if window is valid
            if (nIndex == needle.length()) return hIndex - needle.length();
        }

        return -1;
    }
    private int[] buildLPSArray(String s) {
        int[] lps = new int[s.length()];
        lps[0] = -1;
        if (1 < s.length()) lps[1] = 0;
        if (s.length() <= 2) return lps;
        int prefixLen = 0;
        int i = 2;
        while (i < lps.length) {
            if (s.charAt(i - 1) == s.charAt(prefixLen)) lps[i++] = ++prefixLen;
            else if (prefixLen > 0) prefixLen = lps[prefixLen];
            else lps[i++] = 0;
        }

        return lps;
    }
}
```

## Detailed Explanation
### 1. **Algorithm Analysis**

The provided solution implements the **Knuth-Morris-Pratt (KMP)** algorithm to solve the string matching problem efficiently. The KMP algorithm improves upon the naive approach by avoiding redundant comparisons using a **Longest Prefix Suffix (LPS)** array (also known as the failure function). This array helps skip characters in the haystack that are guaranteed to match, ensuring linear time complexity.

#### Key Steps:
1. **Edge Case Handling**: Check if either string is `null` or if the needle is longer than the haystack.
2. **LPS Array Construction**: Preprocess the needle to build the LPS array, which stores the length of the longest proper prefix that is also a suffix for every prefix of the needle.
3. **KMP Search**: Use the LPS array to traverse the haystack and needle efficiently, adjusting the current position in the needle based on character mismatches.

### 2. **Step-by-Step Walkthrough (Dry Run)**

**Sample Test Case**:  
- **Haystack**: `"sadbutsad"`  
- **Needle**: `"sad"`  

**LPS Array Construction** for `"sad"`:
1. `lps[0] = -1` (base case for `i=0`)
2. `lps[1] = 0` (since `s[0] != s[1]`)
3. For `i=2`:
   - `s[0] == s[1]`? `"s" != "a"` → `lps[2] = 0`.

**KMP Search Execution**:
1. **Initialize pointers**:
   - `hIndex = 0` (haystack index)
   - `nIndex = 0` (needle index)
   - `lps = [-1, 0, 0]`.

2. **Iterate through haystack**:
   - **Step 1**: `haystack[0] == needle[0]` → `hIndex=1`, `nIndex=1`.
   - **Step 2**: `haystack[1] == needle[1]` → `hIndex=2`, `nIndex=2`.
   - **Step 3**: `haystack[2] == needle[2]` → `hIndex=3`, `nIndex=3`.
   - **Check**: `nIndex == needle.length()` → Return `hIndex - needle.length() = 3 - 3 = 0`.

**Final Result**: `0` (first occurrence at index 0).

### 3. **Complexity Analysis**

#### **Time Complexity**:
- **LPS Construction**:  
  Processes each character of the needle once. Time complexity: `O(m)`, where `m` is the length of the needle. Each character is compared at most twice (due to the `prefixLen` backtracking logic).
  
- **KMP Search**:  
  Traverses the haystack once (`O(n)`, where `n` is the haystack length). Each mismatch causes `nIndex` to adjust backward using the LPS array, but each character is examined at most twice. Total time: `O(n)`.

**Total Time Complexity**: `O(n + m)`. For cases where `m < n` (common in practice), this reduces to `O(n)`.

#### **Space Complexity**:
- **LPS Array**: Uses `O(m)` space to store the LPS values.  
- **Other Variables**: Constant space (`hIndex`, `nIndex`, etc.).

**Total Space Complexity**: `O(m)`.

### Summary
- **Algorithm**: KMP with LPS preprocessing.
- **Time**: `O(n + m)` (efficient for large inputs).
- **Space**: `O(m)` (for LPS array).

This approach ensures optimal performance for string matching problems, avoiding the quadratic time complexity of the naive method.

