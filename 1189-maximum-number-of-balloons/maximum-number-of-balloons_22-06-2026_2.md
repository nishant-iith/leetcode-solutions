# Submission details for maximum-number-of-balloons

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 48 ms (Better than 6.87% of submissions)
- **Memory:** 43.2 MB (Better than 87.41% of submissions)

## Solution Code
```java
class Solution {
    public int maxNumberOfBalloons(String text) {
        char[] arr = text.toCharArray();
        int ans = 0;

        while (true) {
            String word = "balloon";

            for (char c : word.toCharArray()) {
                boolean found = false;

                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] == c) {
                        arr[i] = '#';
                        found = true;
                        break;
                    }
                }

                if (!found) return ans;
            }

            ans++;
        }
    }
}
```

## Detailed Explanation
## 1. Algorithm Analysis  

| Aspect | Description |
|--------|-------------|
| **Goal** | Count how many disjoint copies of the word **“balloon”** can be built from the characters of `text`. |
| **Core Idea** | Repeatedly try to **consume** the whole word “balloon” from the mutable character array `arr`. Each successful consumption increments the answer `ans`. When any required character cannot be found the loop stops and the current count is returned. |
| **Why it works** | *“balloon”* contains the letters `b,a,l,l,o,o,n` (7 characters). The algorithm always removes **one** occurrence of each of those letters in that exact order. If it can remove all seven in a single pass, one complete balloon has been assembled. The process can be repeated until a needed letter disappears. This is exactly the definition of “maximum number of balloons”. |
| **Main Steps** |
| 1 | Convert `text` to a mutable `char[] arr`. |
| 2 | `while (true)` – keep trying to build a new balloon. |
| 3 | `String word = "balloon"`; iterate over its characters. |
| 4 | For each character `c` scan `arr` linearly until `c` is found. |
| 5 | When found, replace it with `'#'` (a sentinel marking “used”) and break the inner scan. |
| 6 | If a character is **not** found, exit the outer loop and return the current `ans`. |
| 7 | Otherwise, after the whole word has been found, `ans++` and the outer loop repeats. |

The algorithm is essentially a **greedy removal** strategy: each iteration consumes the *first* possible occurrence of each needed letter. Because the removal is permanent, later iterations can only use the remaining letters, guaranteeing that the count cannot overshoot the true maximum.

---

## 2. Step‑by‑Step Walkthrough (Dry‑Run)  

We trace the sample input  

```
text = "nlaebolko"
```

### Initialization  

| Variable | Value |
|----------|-------|
| `arr`    | `['n','l','a','e','b','o','l','k','o']` |
| `ans`    | `0`   |

---

### **First outer iteration** (`ans` still `0`)

| Step | Action | Effect on `arr` | `found` | Comment |
|------|--------|-----------------|----------|---------|
| 1    | `word = "balloon"` | – | – | The target word we try to consume. |
| 2    | Scan for `'b'` | `i=0→'n'`, `i=1→'l'`, `i=2→'a'`, `i=3→'e'`, **`i=4→'b'`** → match | `true` | Replace `arr[4]='#'`. |
| 3    | Scan for `'a'` | Remaining array now `['n','l','a','e','#','o','l','k','o']` | `'a'` found at index **2** → replace with `'#'` | `arr[2]='#'`. |
| 4    | Scan for first `'l'` | Scan finds index **1** (`'l'`) → replace with `'#'` | `arr[1]='#'`. |
| 5    | Scan for second `'l'` | Continue scan from start, next `'l'` at index **6** → replace with `'#'` | `arr[6]='#'`. |
| 6    | Scan for first `'o'` | `'o'` at index **5** → replace with `'#'` | `arr[5]='#'`. |
| 7    | Scan for second `'o'` | Next `'o'` at index **8** → replace with `'#'` | `arr[8]='#'`. |
| 8    | Scan for `'n'` | `'n'` at index **0** → replace with `'#'` | `arr[0]='#'`. |
| 9    | All 7 characters found → `ans++` → **`ans = 1`**. |
| 10   | Loop repeats (still `true`). |

Current state of `arr` after the first balloon has been removed:

```
arr = ['#','#','#','e','#','#','#','k','#']
```

(The only usable letters left are `'e'` and `'k'`.)

---

### **Second outer iteration** (`ans` is now `1`)

| Step | Action | Effect on `arr` | `found` | Result |
|------|--------|-----------------|----------|--------|
| 1    | `word = "balloon"` | – | – | Try to build a second balloon. |
| 2    | Scan for `'b'` | Scan across all positions, only `'#'`, `'e'`, `'k'` appear → **not found** | `false` | Exit inner `for` loop, go to `if (!found) return ans;`. |
| 3    | Since `found == false`, the method **returns** `ans = 1`. | – | – | End of algorithm. |

The function returns `1`, which matches the expected answer for `"nlaebolko"`.

---

### Summary of Variable Evolution  

| Iteration | `ans` before | Characters found & removed (indices) | `arr` after removal (non‑`#` shown) |
|-----------|--------------|--------------------------------------|-------------------------------------|
| 0 (start) | 0 | – | `n l a e b o l k o` |
| 1 | 0 → 1 | `b`@4, `a`@2, `l`@1, `l`@6, `o`@5, `o`@8, `n`@0 | `# # # e # # # k #` |
| 2 | 1 → (stop) | none ( `'b'` missing ) | same as above → return 1 |

---

## 3. Complexity Analysis  

### Time Complexity  

1. **Cost of a single outer iteration**  
   * The word “balloon” has **7** characters.  
   * For each character we perform a linear scan of the current `arr`.  
   * In the worst case we scan up to the current length of `arr` (which shrinks by 7 each iteration).  
   * Approximate cost ≈ `7 * (n + (n‑7) + (n‑14) + …)`, which is bounded by `7 * (n + (n‑7) + … + 1)`.  

   This series evaluates to **O(n²)** in the worst case (when `n` is large enough to form many balloons).  

2. **Effect of the outer loop**  
   The outer loop runs at most `⌊n/7⌋` times because each successful iteration consumes exactly 7 characters.  
   Therefore the overall worst‑case time is  

   \[
   O\big( (n/7) \times 7 \times n \big) = O(n^{2})
   \]

   *In practice the constant factor is small because the scan stops as soon as a match is found, but asymptotically we cannot guarantee better than quadratic.*

### Space Complexity  

* The algorithm uses only a few primitive variables (`ans`, loop indices, temporary `found`) and the mutable `char[] arr` that is built from the input string.  
* No additional data structures whose size depends on `n` are allocated.  

Hence the extra space usage is **O(1)** (constant auxiliary space).

---

### TL;DR  

* The code greedily removes one copy of “balloon” at a time by scanning the character array and marking used letters with `'#'`.  
* It repeats until a needed letter cannot be found, returning the number of successful removals.  
* Dry‑run on `"nlaebolko"` shows the algorithm removes one balloon and then fails to find a `'b'`, returning `1`.  
* **Time:** `O(n²)` in the worst case (quadratic due to repeated linear scans).  
* **Space:** `O(1)` auxiliary space.  

Even though the solution is accepted on LeetCode (because test data are modest), it is not the most efficient approach; a frequency‑counting solution runs in `O(n)` time and `O(1)` space. Nonetheless, the presented algorithm correctly solves the problem and illustrates a simple exhaustive‑removal strategy.
