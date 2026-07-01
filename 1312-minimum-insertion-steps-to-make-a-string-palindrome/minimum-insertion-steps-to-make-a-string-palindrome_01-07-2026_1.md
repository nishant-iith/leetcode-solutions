# Submission details for minimum-insertion-steps-to-make-a-string-palindrome

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 27 ms (Better than 46.71% of submissions)
- **Memory:** 47.4 MB (Better than 48.41% of submissions)

## Solution Code
```java
class Solution {
    public int minInsertions(String s) {
        String rev = new StringBuilder(s).reverse().toString();
        int n = s.length();
        int dp[][] = new int[n + 1][n + 1];
        for(int i = 0; i <= n; i++) dp[i][0] = 0;
        for(int j = 0; j <= n; j++) dp[0][j] = 0;

        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                if(s.charAt(i - 1) == rev.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1] + 1;
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return n - dp[n][n];
    }
}
```

## Detailed Explanation
### **1. Algorithm Analysis**

The problem asks for the minimum number of insertions required to turn a given string into a palindrome.  
A key insight is that the minimum insertions needed equals the total length of the string minus the length of its **Longest Palindromic Subsequence (LPS)**.  

Why?  
- The LPS represents the longest set of characters that are already in a palindromic order (they can stay unchanged).  
- Any character not part of this subsequence must be paired with a matching character inserted elsewhere to achieve a full palindrome.  
- Thus, we need to add one character for each character outside the LPS: `minInsertions = n - LPS_length`.

To compute the LPS length efficiently, we use a classic dynamic programming approach:  
The LPS of a string `s` is exactly the **Longest Common Subsequence (LCS)** of `s` and its reverse `rev`.  

**Algorithm Steps**:  
1. Compute the reversed string `rev = new StringBuilder(s).reverse().toString()`.  
2. Let `n = s.length()`. Create a DP table `dp[n+1][n+1]`, where `dp[i][j]` stores the LCS length between the prefixes `s[0..i-1]` and `rev[0..j-1]`.  
3. **Base Cases**: Set all `dp[i][0] = 0` and `dp[0][j] = 0` for `0 ≤ i, j ≤ n`.  
4. **Transition**: For `i` from `1` to `n` and `j` from `1` to `n`:  
   - If `s.charAt(i-1) == rev.charAt(j-1)` then `dp[i][j] = dp[i-1][j-1] + 1`.  
   - Else `dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1])`.  
5. The LPS length is `dp[n][n]`. The answer is `n - dp[n][n]`.

The DP is filled in a bottom‑up manner, ensuring that each value depends only on previously computed states.

---

### **2. Step‑by‑Step Walkthrough (DRY Run) for Sample Test Case `"zzazz"`**

String `s = "zzazz"`, `n = 5`, `rev = "zzazz"` (palindrome).  
We will trace the DP table `dp[0..5][0..5]`. The initial base row/column are all 0.

#### **Iteration i = 1** (character `s[0] = 'z'`)
| j | rev[j-1] | Condition | dp[1][j] | Explanation |
|---|----------|-----------|----------|-------------|
| 1 | 'z' | match | `dp[1][1] = dp[0][0] + 1 = 0+1 = 1` | |
| 2 | 'z' | match | `dp[1][2] = dp[0][1] + 1 = 0+1 = 1` | |
| 3 | 'a' | mismatch | `dp[1][3] = max(dp[0][3], dp[1][2]) = max(0,1) = 1` | |
| 4 | 'z' | match | `dp[1][4] = dp[0][3] + 1 = 0+1 = 1` | |
| 5 | 'z' | match | `dp[1][5] = dp[0][4] + 1 = 0+1 = 1` | |

After i=1, the first row (i=1) is filled.

#### **Iteration i = 2** (character `s[1] = 'z'`)
| j | rev[j-1] | Condition | dp[2][j] | Explanation |
|---|----------|-----------|----------|-------------|
| 1 | 'z' | match | `dp[2][1] = dp[1][0] + 1 = 0+1 = 1` | |
| 2 | 'z' | match | `dp[2][2] = dp[1][1] + 1 = 1+1 = 2` | |
| 3 | 'a' | mismatch | `dp[2][3] = max(dp[1][3], dp[2][2]) = max(1,2) = 2` | |
| 4 | 'z' | match | `dp[2][4] = dp[1][3] + 1 = 1+1 = 2` | |
| 5 | 'z' | match | `dp[2][5] = dp[1][4] + 1 = 1+1 = 2` | |

#### **Iteration i = 3** (character `s[2] = 'a'`)
| j | rev[j-1] | Condition | dp[3][j] | Explanation |
|---|----------|-----------|----------|-------------|
| 1 | 'z' | mismatch | `dp[3][1] = max(dp[2][1], dp[3][0]) = max(1,0) = 1` | |
| 2 | 'z' | mismatch | `dp[3][2] = max(dp[2][2], dp[3][1]) = max(2,1) = 2` | |
| 3 | 'a' | match | `dp[3][3] = dp[2][2] + 1 = 2+1 = 3` | |
| 4 | 'z' | mismatch | `dp[3][4] = max(dp[2][4], dp[3][3]) = max(2,3) = 3` | |
| 5 | 'z' | mismatch | `dp[3][5] = max(dp[2][5], dp[3][4]) = max(2,3) = 3` | |

#### **Iteration i = 4** (character `s[3] = 'z'`)
| j | rev[j-1] | Condition | dp[4][j] | Explanation |
|---|----------|-----------|----------|-------------|
| 1 | 'z' | match | `dp[4][1] = dp[3][0] + 1 = 0+1 = 1` | |
| 2 | 'z' | match | `dp[4][2] = dp[3][1] + 1 = 1+1 = 2` | |
| 3 | 'a' | mismatch | `dp[4][3] = max(dp[3][3], dp[4][2]) = max(3,2) = 3` | |
| 4 | 'z' | match | `dp[4][4] = dp[3][3] + 1 = 3+1 = 4` | |
| 5 | 'z' | match | `dp[4][5] = dp[3][4] + 1 = 3+1 = 4` | |

#### **Iteration i = 5** (character `s[4] = 'z'`)
| j | rev[j-1] | Condition | dp[5][j] | Explanation |
|---|----------|-----------|----------|-------------|
| 1 | 'z' | match | `dp[5][1] = dp[4][0] + 1 = 0+1 = 1` | |
| 2 | 'z' | match | `dp[5][2] = dp[4][1] + 1 = 1+1 = 2` | |
| 3 | 'a' | mismatch | `dp[5][3] = max(dp[4][3], dp[5][2]) = max(3,2) = 3` | |
| 4 | 'z' | match | `dp[5][4] = dp[4][3] + 1 = 3+1 = 4` | |
| 5 | 'z' | match | `dp[5][5] = dp[4][4] + 1 = 4+1 = 5` | |

#### **Final DP Table**

```
        j: 0 1 2 3 4 5
i=0:    0  0 0 0 0 0
i=1:    0  1 1 1 1 1
i=2:    0  1 2 2 2 2
i=3:    0  1 2 3 3 3
i=4:    0  1 2 3 4 4
i=5:    0  1 2 3 4 5
```

The value `dp[5][5] = 5` is the length of the LPS.  
Therefore, `minInsertions = n - dp[n][n] = 5 - 5 = 0`.  
Indeed, the string `"zzazz"` is already a palindrome; no insertions are needed.

---

### **3. Complexity Analysis**

**Time Complexity:**  
- The algorithm uses two nested loops, each running from `1` to `n`. For each pair `(i, j)` we perform constant‑time work (character comparison and a few assignments/max operations).  
- Hence, the total time is `O(n * n) = O(n^2)`.

**Space Complexity:**  
- The DP table `dp` has dimensions `(n+1) × (n+1)`, requiring `O(n^2)` memory.  
- No additional significant space is used.  
- Therefore, the space complexity is `O(n^2)`.

Both complexities are optimal for this DP approach given the problem constraints (LeetCode constraints allow up to `n = 500` or `1000`, making `O(n^2)` acceptable). If needed, space can be reduced to `O(n)` by using two rows (since each row depends only on the previous one), but the provided solution uses the full table.
