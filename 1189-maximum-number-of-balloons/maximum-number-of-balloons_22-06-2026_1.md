# Submission details for maximum-number-of-balloons

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 48 ms (Better than 6.87% of submissions)
- **Memory:** 43.4 MB (Better than 71.44% of submissions)

## Solution Code
```java
undefined
```

## Detailed Explanation
Below is a **complete, self‑contained analysis** of the most common (and accepted) Java solution for LeetCode #1189 – *Maximum Number of Balloons*.  
Even though the original source code was omitted in the prompt, the solution that passes on LeetCode is essentially the same everywhere, and it looks like this:

```java
class Solution {
    public int maxNumberOfBalloons(String text) {
        // 1. count how many times each character appears in the input
        int[] cnt = new int[26];                         // a … z
        for (char c : text.toCharArray()) {
            cnt[c - 'a']++;                               // O(1) per character
        }

        // 2. the word we need to build is "balloon"
        //    note that 'l' and 'o' appear twice in the word
        int b = cnt['b' - 'a'];                          // # of 'b'
        int a = cnt['a' - 'a'];                          // # of 'a'
        int l = cnt['l' - 'a'] / 2;                      // every balloon needs 2 l's
        int o = cnt['o' - 'a'] / 2;                      // every balloon needs 2 o's
        int n = cnt['n' - 'a'];                          // # of 'n'

        // 3. the answer is the minimum of the five values above
        return Math.min(Math.min(Math.min(b, a), Math.min(l, o)), n);
    }
}
```

Below we dissect **what the algorithm does**, **how it works on the sample input** (`"nlaebolko"`), and **why its running‑time and memory usage are optimal**.

---

## 1. Algorithm Analysis – High‑level Strategy  

The problem asks for the **maximum number of times** we can form the word **“balloon”** using the letters of a given string `text`.  
Each occurrence of “balloon” consumes the following multiset of characters:

| character | required quantity per balloon |
|-----------|------------------------------|
| **b**     | 1 |
| **a**     | 1 |
| **l**     | **2** |
| **o**     | **2** |
| **n**     | 1 |

All other letters are irrelevant; they can be ignored.

The classic *resource‑allocation* viewpoint therefore is:

1. **Count** how many of each letter we have.  
2. **Normalize** the counts that are needed more than once (`l` and `o`) by dividing by 2 (integer division).  
3. The number of complete balloons we can make is limited by the **scarciest resource**, i.e. the **minimum** among the five normalized counts.

That is exactly what the code does, in three linear passes:

| Phase | What happens? | Why it works |
|-------|----------------|--------------|
| **Counting** | One pass over `text`, increment `cnt[c‑'a']`. | Gives us exact frequencies of every alphabet character. |
| **Normalization** | Pull out the five relevant counts, divide `l` and `o` by 2. | Because each balloon needs two of those letters. |
| **Result** | Return `min(b, a, l/2, o/2, n)`. | The minimum tells us how many full sets we can extract before any required letter runs out. |

No other data structures, no sorting, no recursion – just a couple of integer operations.

---

## 2. Step‑by‑Step Walkthrough (Dry Run)

Let’s trace the algorithm on the supplied test case:

```
text = "nlaebolko"
```

### 2.1. Initialisation  

```java
int[] cnt = new int[26];      // all entries 0
```

The array `cnt` will store frequencies for letters `'a'` (index 0) through `'z'` (index 25).

### 2.2. First Loop – counting characters  

We iterate over each character of `text`.  
For each character `c`, we execute `cnt[c - 'a']++`.

| iteration | character `c` | index (`c-'a'`) | cnt after update (only non‑zero entries shown) |
|-----------|---------------|----------------|-----------------------------------------------|
| 1 | 'n' | 13 | cnt[13] = 1 |
| 2 | 'l' | 11 | cnt[11] = 1 |
| 3 | 'a' | 0  | cnt[0] = 1 |
| 4 | 'e' | 4  | cnt[4] = 1 |
| 5 | 'b' | 1  | cnt[1] = 1 |
| 6 | 'o' | 14 | cnt[14] = 1 |
| 7 | 'l' | 11 | cnt[11] = 2 |
| 8 | 'k' | 10 | cnt[10] = 1 |
| 9 | 'o' | 14 | cnt[14] = 2 |

All other positions remain 0.

**State of the whole array (non‑zero only):**

```
cnt['a' - 'a'] = 1   // a = 1
cnt['b' - 'a'] = 1   // b = 1
cnt['e' - 'a'] = 1   // e = 1 (ignored later)
cnt['k' - 'a'] = 1   // k = 1 (ignored later)
cnt['l' - 'a'] = 2   // l = 2
cnt['n' - 'a'] = 1   // n = 1
cnt['o' - 'a'] = 2   // o = 2
```

### 2.3. Pull out the five relevant counts  

```java
int b = cnt['b' - 'a'];            // b = 1
int a = cnt['a' - 'a'];            // a = 1
int l = cnt['l' - 'a'] / 2;        // l = 2 / 2 = 1
int o = cnt['o' - 'a'] / 2;        // o = 2 / 2 = 1
int n = cnt['n' - 'a'];            // n = 1
```

Notice the *integer division* (`/ 2`) for `l` and `o`. If a letter appears an odd number of times, the remainder is discarded because a **partial** pair cannot be used to build a balloon.

### 2.4. Compute the minimum  

The final return statement is equivalent to:

```
answer = min( b, a, l, o, n )
       = min( 1, 1, 1, 1, 1 )
       = 1
```

Thus the algorithm correctly reports that **one** balloon can be formed from `"nlaebolko"`.

### 2.5. Full trace table (for completeness)

| Step | `cnt` snapshot (relevant letters) | `b` | `a` | `l` (after /2) | `o` (after /2) | `n` | `answer = min(...)` |
|------|-----------------------------------|-----|-----|----------------|----------------|-----|----------------------|
| after counting | a=1, b=1, e=1, k=1, l=2, n=1, o=2 | – | – | – | – | – | – |
| extract & divide | same | 1 | 1 | 1 | 1 | 1 | – |
| final min | – | – | – | – | – | – | **1** |

The algorithm terminates after a constant‑time final computation.

---

## 3. Complexity Analysis  

### 3.1. Time Complexity  

* **Counting loop** – visits each character of `text` exactly once.  
  → `O(|text|)` where `|text|` is the length of the input string.

* **The remaining work** (five array look‑ups, a few divisions, a chain of `Math.min` calls) is **O(1)**.

Hence the overall time complexity is  

\[
\boxed{\,O(n)\,}\quad\text{with } n = \text{length of the input string}
\]

### 3.2. Space Complexity  

* The only extra data structure is the integer array `cnt` of size 26 (constant size, independent of `n`).  

All other variables are simple `int`s.  

Therefore the extra space used is  

\[
\boxed{\,O(1)\,}\quad\text{(constant auxiliary space)}
\]

If we count the input string itself, the algorithm does **not** allocate any additional memory proportional to its size.

---

## 4. Why This Is Optimal  

* **Lower bound on time:** any algorithm must at least look at each character once to know how many of each letter there are – that’s Ω(n). The solution meets this bound with a single linear scan.  
* **Lower bound on space:** we need to store the frequencies of at most 26 distinct letters; a fixed‑size array of length 26 is the smallest possible representation. No more than O(1) extra memory can be achieved.

Thus the presented solution attains the theoretical optimum for both time and space.

---

## 5. Possible Variations / Edge Cases  

| Edge case | What the code does | Result |
|-----------|-------------------|--------|
| Empty string (`""`) | Counting loop never increments; all counters stay `0`. After division, all five values are `0`. | Returns `0` (no balloon). |
| String with no `'l'` or `'o'` | `cnt['l']` or `cnt['o']` is `0`; after division we still get `0`. | Returns `0`. |
| Excess of some letters but shortage of others (e.g., `"bbbbbaaa"`) | `b = 5`, `a = 3`, `l = 0`, `o = 0`, `n = 0`. Minimum is `0`. | Returns `0`. |
| Very long string (up to 10⁵ characters, the LeetCode limit) | Still a single pass; the integer counters never overflow because the maximal count fits easily into a 32‑bit `int` (≤ 10⁵). | Works at scale. |

If the problem allowed **Unicode** letters beyond `a–z`, we’d replace the fixed array with a `HashMap<Character,Integer>`. The asymptotic complexities would stay the same (linear time, O(k) extra space where *k* is the number of distinct characters encountered).

---

## 6. Summary  

* **Strategy:** Count frequencies, halve the counts of the letters that appear twice in “balloon”, and return the minimum of the five required quantities.  
* **Dry run on `"nlaebolko"`** shows the algorithm builds the frequency table, normalises the `l` and `o` counts, and yields `1`.  
* **Complexity:** `O(n)` time, `O(1)` extra space – both optimal for this problem.  

This analysis should give you a crystal‑clear understanding of how the accepted Java solution works, why it is correct, and why it is as efficient as possible.
