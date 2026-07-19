# Submission details for smallest-subsequence-of-distinct-characters

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 2 ms (Better than 98.45% of submissions)
- **Memory:** 42.6 MB (Better than 92.89% of submissions)

## Solution Code
```java
class Solution {
    public String smallestSubsequence(String s) {
        int[] last = new int[26];

        for (int i = 0; i < s.length(); i++)
            last[s.charAt(i) - 'a'] = i;

        boolean[] inStack = new boolean[26];
        StringBuilder st = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (inStack[ch - 'a'])
                continue;

            while (st.length() > 0 && st.charAt(st.length() - 1) > ch && last[st.charAt(st.length() - 1) - 'a'] > i) {
                inStack[st.charAt(st.length() - 1) - 'a'] = false;
                st.deleteCharAt(st.length() - 1);
            }

            st.append(ch);
            inStack[ch - 'a'] = true;
        }

        return st.toString();
    }
}
```

## Detailed Explanation
## 1. Algorithm Analysis  

### Problem Restatement  
Given a string `s` consisting only of lowercase letters, return the *smallest* (lexicographically) subsequence that contains **each distinct character of `s` exactly once**.aggregating? Actually the classic LeetCode “Smallest Subsequence of Distinct Characters”ELL.

### High‑level Idea  
We want a string that

1. contains every distinct character from `s` exactly once, and  
2. is lexicographically minimal.

A classic greedy + stack trick solves this:

* **Last occurrence array** – For every character record the index of its last appearance in `s`.  
  If we ever see a character `c` at position `i`, we know that `c` will **not** appear later after `i` if `i` equals that last index – we *must* keep it.  

* **Stack (or StringBuilder)** – We build the answer from left to right.  
  While iterating `s`:
  * If the current character is already on the stack, skip it (`continue`).  
  * Otherwise, try to pop characters from the stack that are **greater than the current character** *and* will appear again later (`last[stackTop] > i`).  
    *Popping* is safe because we can put the larger character back later, and removing it allows the current smaller character to appear earlier, giving a lexicographically smaller result.  
  * Push the current character onto the stack.

The process is a typical “monotonic stack” solution erfolgt. Its correctness can be sketched:

* Every character is considered only once, so it appears at most once in the final stack (due to the `inStack` check).  
* When a character `c` is popped, at least one later occurrence exists (`last[stackTop] > i`), so the string can still contain that character.  
* The popping condition guarantees that at any point the characters in the stack form the smallest possible prefix under the constraint that each remaining character still has a future occurrence.

Thus, at the end the stack contains all distinct characters in the *lexicographically smallest* order.

### Detailed Steps  

1. **Build `last[26]`** – Scan `s` once, store the last index of each letter.  
2. **Initialize tracking structures**  
   * `inStack[26]`: Boolean array to remember whether a char is already in the stack.  
   * `StringBuilder st`: acts as a mutable stack.
3. **Iterate over `s`** (index `i` from `0` to `n‑1`)  
   * `ch = s.charAt(i)`
   * If `inStack[ch]` is `true`, continue (the character is already chosen).  
   * While the stack is non‑empty *and* the top character is lexicographically larger than `ch` **and** the top character will appear again later (`last[top] > i`):
     * Pop the top character (`st.deleteCharAt`)  
     * Set `inStack[top] = false`
   * Push `ch` onto the stack: `st.append(ch)` and mark it in `inStack`.
4. After the loop, convert `st` to string (`st.toString()`) and return it.

The algorithm uses a classic “greedy removal” strategy and runs in linear time.

---

## 2. Step‑by‑Step Dry Run  
Let's run the algorithm on the sample test case:

```
s = "bcabc"
```

| i | s[i] | last[26]   | inStack[26] | Stack (st) | Operation | Comment |
|---|-------|------------|-------------|------------|-----------|---------|
| 0 | 'b'  | `{'b':3,'c':4,'a':2}` (see below) | all false | `"" prison` | push 'b' | first 'b', stack:iyac 'b' |
| 1 | ' indoor '`c' | last[ ] doesn't change | 'b' is on stack |           | push 'c' | stack: "bc" |
| 2 | 'a' | last remains | 'b','c' present | stack: "bc" | While pop? `ुले` check: top='c' > 'a'? yes; last['c']=4 > 2? yes -> pop 'c'. | pop 'c', stack: "b". Now top='b'>'a'? yes; last['b']=3acar? > 2? yes -> pop 'b'. | after pops, stack empty |
|   |     |            |             |            | push 'a' | stack: "a" |
| 3 | 'b' |            | 'a' already in stack? no | stack: "a|`c? 'b'?  Actually we popped earlier, so stack: "a". | not inStack, while loop: top='a'> 'b'? No → stop; push 'b'. | stack: "ab" |
| 4 | 'c' |            | 'a','b' present | stack: "ab" | top='b'>'c'? No → push 'c'. | stack: "abc" |

Final stack: "abc", which is the expected answer.

### Build `last[]` table for this example

We scan once:

| i | char | last[char] |
|---|------|------------|
| 0 | 'b'  | last['b'] = 0 |
| 1 | 'c'  | last['c'] = 1 |
| 2 | 'a'  | last['a'] = 2 |
| 3 | 'b'  | last['b'] = 3 |
| 4 | 'c'  | last['c'] = 4 |

Thus final `last` after traversal:  
- `last['a'] = 2`
- `last['b'] = 3`
- `last['c'] = 4`

All other letters remain `0` (unused).

During the iteration, we always refer to this table to decide whether we can safely pop the top character.

---

## 3. Complexity Analysis  

Let `n = s.length()`.

| Aspect | Explanation | Big‑O |
|--------|-------------|-------|
| **Time** | Two linear passes:  
  1. First pass to fill `last` (`O(n)`).  
  2. Second pass iterating over the string, pushing each character once and popping at most once per character (`O(n)` total). | **O(n)** |
| **Space** |  
  * `last[26]` – fixed size constant array.  
  * `inStack[26]` – fixed size constant array.  
  * The `StringBuilder` holds at most 26 characters (distinct lowercase letters). | **O(1)** (constant additional space) |

The algorithm uses only a few arrays of fixed length and a small auxiliary builder; memory usage does not grow with the input size.

---

### Recap

* **Strategy**: Greedy stack → maintain the smallest lexicographical order while ensuring all distinct letters are present.  
* **Key insight indip**: A character can be removed from the current answer only if it will appear again *later*; otherwise, it cannot be omitted.  
* **Dry‑run**: On `"bcabc"` we obtain `"abc"`.  
* **Complexities**: Linear time and constant auxiliary space.  

This is a canonical solution for the “smallest subsequence of distinct characters” problem.
