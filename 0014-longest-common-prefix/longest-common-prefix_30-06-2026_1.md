# Submission details for longest-common-prefix

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 62.66% of submissions)
- **Memory:** 43 MB (Better than 82.41% of submissions)

## Solution Code
```java
class Solution {
    public String longestCommonPrefix(String[] v) {
        StringBuilder ans = new StringBuilder();
        Arrays.sort(v);
        String first = v[0];
        String last = v[v.length-1];
        for (int i=0; i<Math.min(first.length(), last.length()); i++) {
            if (first.charAt(i) != last.charAt(i)) {
                return ans.toString();
            }
            ans.append(first.charAt(i));
        }
        return ans.toString();
    }
}
```

## Detailed Explanation
### 1. Algorithm Analysis

**Strategy: Sorting + First/Last Comparison**

This solution leverages the lexicographical ordering properties of strings. The core insight is: **In a sorted array of strings, the strings with the most differing prefixes will be pushed to the extremes (first and last positions).**

*   **Sorting Phase**: `Arrays.sort(v)` arranges the strings lexicographically (dictionary order). This groups strings with similar prefixes together. Crucially, the *Longest Common Prefix (LCP) of the entire array* must be a prefix of the *LCP of the first and last strings*. If the first and last strings share a prefix of length $k$, every string in between must also share that prefix (because they fall lexicographically between the first and last).
*   **Comparison Phase**: We only need to compare the **first** string (`v[0]`) and the **last** string (`v[v.length - 1]`) character by character.
*   **Termination**: We iterate up to the length of the shorter string. The moment a character mismatch is found, the prefix built so far is the answer. If the loop completes without mismatch, the entire shorter string is the common prefix.

**Why this works:**
Lexicographical sorting compares characters left-to-right.
*   Example: `["flight", "flow", "flower"]`
*   The first string is the "smallest" lexicographically, the last is the "largest".
*   Any character position where `first` and `last` differ implies a "branch" in the trie of all strings. No string in the middle can cross that branch without violating the sort order.

---

### 2. Step-by-Step Walkthrough (Dry Run)

**Input:** `v = ["flower", "flow", "flight"]`

#### Phase 1: Sorting
`Arrays.sort(v)` modifies the array in-place.
Lexicographical order: `"flight"` < `"flow"` < `"flower"`

**State after sort:**
`v = ["flight", "flow", "flower"]`
`v.length = 3`

#### Phase 2: Initialization
```java
StringBuilder ans = new StringBuilder(); // ans = ""
String first = v[0];                     // first = "flight"
String last = v[v.length - 1];           // last = "flower"
```
*   `first.length()` = 6
*   `last.length()` = 6
*   Loop limit: `Math.min(6, 6)` = 6. Indices `0` to `5`.

#### Phase 3: Character Comparison Loop

| Iteration `i` | `first.charAt(i)` | `last.charAt(i)` | Match? | Action | `ans` State |
| :--- | :---: | :---: | :---: | :--- | :--- |
| **0** | `'f'` | `'f'` | **Yes** | `ans.append('f')` | `"f"` |
| **1** | `'l'` | `'l'` | **Yes** | `ans.append('l')` | `"fl"` |
| **2** | `'i'` | `'o'` | **No** | `return ans.toString()` | `"fl"` |

**Trace Details:**
1.  **i=0**: Compare `first[0]` ('f') vs `last[0]` ('f'). Equal. Append 'f'. `ans` = "f".
2.  **i=1**: Compare `first[1]` ('l') vs `last[1]` ('l'). Equal. Append 'l'. `ans` = "fl".
3.  **i=2**: Compare `first[2]` ('i') vs `last[2]` ('o'). **Not Equal**.
    *   Execute `return ans.toString()`.
    *   Method returns `"fl"`.

**Final Output:** `"fl"`

---

### 3. Complexity Analysis

#### Time Complexity: $O(N \cdot M \log N)$
*   **$N$**: Number of strings in the input array (`v.length`).
*   **$M$**: Maximum length of a string in the array.

**Breakdown:**
1.  **Sorting (`Arrays.sort`)**: Java uses **TimSort** (hybrid Merge/Insertion sort) for Object arrays.
    *   Comparisons: $O(N \log N)$.
    *   Cost per comparison: In the worst case (e.g., all strings are identical or share a long prefix), comparing two strings takes $O(M)$ time.
    *   Total Sort Cost: $O(M \cdot N \log N)$.
2.  **Comparison Loop**: Compares `first` and `last` up to length $M$.
    *   Cost: $O(M)$.
3.  **Total**: $O(M \cdot N \log N) + O(M) \rightarrow \mathbf{O(M \cdot N \log N)}$.

> **Note**: The Vertical Scanning approach (comparing column by column without sorting) achieves $O(N \cdot M)$ worst-case time, which is asymptotically faster. However, the Sorting approach is often faster in practice for small $N$ or highly dissimilar strings due to low overhead and cache efficiency, and it is very concise to write.

#### Space Complexity: $O(\log N)$ (or $O(N)$ depending on JVM implementation details)
*   **`Arrays.sort` (TimSort)**: Requires $O(N)$ auxiliary space for object references in the worst case for standard Merge Sort, but TimSort optimizes this to **$O(N)$ worst-case / $O(\log N)$ best-case** for the stack/merge buffers. For Object arrays (`String[]`), Java's `Arrays.sort` typically allocates a temporary array of size $N/2$ (or similar), leading to **$O(N)$ auxiliary space** in standard OpenJDK implementations.
*   **`StringBuilder ans`**: Stores the result, max length $M$. This is **Output Space**, usually excluded from Auxiliary Space analysis. If counted: $O(M)$.
*   **Variables (`first`, `last`, `i`)**: $O(1)$.

**Standard Auxiliary Space Complexity:** **$O(N)$** (due to the sorting algorithm's temporary array allocation for reference types).
**If using Primitive Sort (not applicable here)**: $O(\log N)$ stack space.
**In-Place Assumption (LeetCode Standard)**: Often cited as $O(1)$ or $O(\log N)$ ignoring the sort implementation details, but strictly **$O(N)$** for `String[]` in Java.
