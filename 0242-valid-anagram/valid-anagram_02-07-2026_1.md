# Submission details for valid-anagram

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 26 ms (Better than 5.35% of submissions)
- **Memory:** 46.4 MB (Better than 33.58% of submissions)

## Solution Code
```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        HashMap<Character, Integer> sCount = new HashMap<>();
        HashMap<Character, Integer> tCount = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            sCount.put(s.charAt(i), 1 + sCount.getOrDefault(s.charAt(i), 0));
            tCount.put(t.charAt(i), 1 + tCount.getOrDefault(t.charAt(i), 0));
        }

        return sCount.equals(tCount);        
    }
}
```

## Detailed Explanation
## 1️⃣ Algorithm Analysis  

### Problem restatement  
Given two strings `s` and `t`, return `true` **iff** they are anagrams of each other – i.e. they contain exactly the same characters with the same frequencies, possibly in a different order.

### Core idea  
*If two strings are anagrams, their character‑frequency maps are identical.*  
So we:
1. Quickly reject strings of different length (they cannot be anagrams).  
2. Build a frequency map (`HashMap<Character,Integer>`) for each string while scanning it once.  
3. Compare the two maps for equality (`HashMap.equals` checks that both have the same key‑set and the same value for each key).  

If the maps are equal → the strings are anagrams; otherwise → not.

### Why this works  
* The frequency map captures *all* information needed to decide anagram‑ness:  
  - **Presence** of a character → key in the map.  
  - **Count** of that character → associated integer value.  
* Two strings are anagrams ⇔ for every character `c`, `count_s(c) == count_t(c)`.  
  This is precisely the condition checked by `sCount.equals(tCount)`.  

### Advantages of the chosen implementation  
* **Single pass** over each string → O(n) time.  
* Uses only the standard Java `HashMap`; no sorting (which would be O(n log n)).  
* Early length check saves work for obviously non‑anagram inputs.  

### Potential alternatives (brief mention)  
* **Array of size 26** (if we know input is only lowercase English letters) → O(1) extra space.  
* **Sorting** both strings and comparing → O(n log n) time, O(1) or O(n) space depending on sort.  
* **Single map** (increment for `s`, decrement for `t`, then verify all zero) → saves one map allocation.  

The presented solution trades a little extra space (two maps) for maximal clarity and symmetry.

---

## 2️⃣ Step‑by‑Step Walkthrough (Dry Run)  

We will trace the algorithm on the sample:

```
s = "anagram"
t = "nagaram"
```

Both strings have length 7 → we proceed.

### Initialization  

| Variable | Value |
|----------|-------|
| `s.length()` | 7 |
| `t.length()` | 7 |
| `sCount` | `{}` (empty HashMap) |
| `tCount` | `{}` (empty HashMap) |
| `i` (loop index) | 0 (about to start) |

### Loop iteration table  

| i | s.charAt(i) | t.charAt(i) | Action on `sCount` | `sCount` after put | Action on `tCount` | `tCount` after put |
|---|-------------|-------------|--------------------|--------------------|--------------------|--------------------|
| 0 | `'a'` | `'n'` | `getOrDefault('a',0) = 0` → new count = 1 | `{a=1}` | `getOrDefault('n',0) = 0` → new count = 1 | `{n=1}` |
| 1 | `'n'` | `'a'` | `getOrDefault('n',0) = 0` → new count = 1 | `{a=1, n=1}` | `getOrDefault('a',0) = 1` (from previous) → new count = 2 | `{n=1, a=2}` |
| 2 | `'a'` | `'g'` | `getOrDefault('a',0) = 2` → new count = 3 | `{a=3, n=1}` | `getOrDefault('g',0) = 0` → new count = 1 | `{n=1, a=2, g=1}` |
| 3 | `'g'` | `'a'` | `getOrDefault('g',0) = 0` → new count = 1 | `{a=3, n=1, g=1}` | `getOrDefault('a',0) = 3` → new count = 4 | `{n=1, a=4, g=1}` |
| 4 | `'r'` | `'r'` | `getOrDefault('r',0) = 0` → new count = 1 | `{a=3, n=1, g=1, r=1}` | `getOrDefault('r',0) = 1` → new count = 2 | `{n=1, a=4, g=1, r=2}` |
| 5 | `'a'` | `'a'` | `getOrDefault('a',0) = 3` → new count = 4 | `{a=4, n=1, g=1, r=1}` | `getOrDefault('a',0) = 4` → new count = 5 | `{n=1, a=5, g=1, r=2}` |
| 6 | `'m'` | `'m'` | `getOrDefault('m',0) = 0` → new count = 1 | `{a=4, n=1, g=1, r=1, m=1}` | `getOrDefault('m',0) = 0` → new count = 1 | `{n=1, a=5, g=1, r=2, m=1}` |

> **Note:** The `put` operation replaces the previous value for the key if it already exists; the expression `1 + map.getOrDefault(ch,0)` computes the new count.

### Final maps after the loop  

```
sCount = {a=4, n=1, g=1, r=1, m=1}
tCount = {a=5, n=1, g=1, r=2, m=1}
```

At this point we can already see they are **not** equal (`a` and `r` counts differ).  
But let’s continue with the algorithm as written:

```java
return sCount.equals(tCount);
```

`HashMap.equals` returns `false` because the key‑value sets differ.  

**Result:** `false`? Wait – the sample strings *are* anagrams. Something went wrong in our trace.

Let’s double‑check the strings:

- s = "a n a g r a m" → letters: a(3), n(1), g(1), r(1), m(1)  
- t = "n a g a r a m" → letters: a(3), n(1), g(1), r(1), m(1)

Our trace inadvertently **over‑counted** because we mistakenly processed the characters incorrectly. Let’s redo the trace carefully, this time writing the exact characters at each index.

#### Correct indexing  

| Index | s[i] | t[i] |
|-------|------|------|
| 0 | a | n |
| 1 | n | a |
| 2 | a | g |
| 3 | g | a |
| 4 | r | r |
| 5 | a | a |
| 6 | m | m |

Now redo the map updates step‑by‑step.

| i | sChar | tChar | sCount update | sCount after | tCount update | tCount after |
|---|-------|-------|---------------|--------------|---------------|--------------|
| 0 | a | n | a: 0→1 | {a=1} | n: 0→1 | {n=1} |
| 1 | n | a | n: 0→1 | {a=1, n=1} | a: 1→2 | {n=1, a=2} |
| 2 | a | g | a: 1→2 | {a=2, n=1} | g: 0→1 | {n=1, a=2, g=1} |
| 3 | g | a | g: 0→1 | {a=2, n=1, g=1} | a: 2→3 | {n=1, a=3, g=1} |
| 4 | r | r | r: 0→1 | {a=2, n=1, g=1, r=1} | r: 0→1 | {n=1, a=3, g=1, r=1} |
| 5 | a | a | a: 2→3 | {a=3, n=1, g=1, r=1} | a: 3→4 | {n=1, a=4, g=1, r=1} |
| 6 | m | m | m: 0→1 | {a=3, n=1, g=1, r=1, m=1} | m: 0→1 | {n=1, a=4, g=1, r=1, m=1} |

Final maps:

```
sCount = {a=3, n=1, g=1, r=1, m=1}
tCount = {a=4, n=1, g=1, r=1, m=1}
```

Oops – still off by one on `a`. Let’s verify the strings again:  

- s = "a n a g r a m" → a appears at indices 0,2,5 → **3** times.  
- t = "n a g a r a m" → a appears at indices 1,3,5 → **3** times.  

So both should have `a=3`. Our trace shows `tCount` ending with `a=4`. Where did the extra `a` come from? Look at the steps for `t` :

- i=0: t[0] = 'n' → n=1  
- i=1: t[1] = 'a' → a becomes 2 (starting from 0? Actually we had a from previous? No, we haven't seen 'a' before in t yet, so should be 1). Wait, we incorrectly carried over a count from s? Let's reset: At i=1, before processing t[1] we have tCount = {n=1}. So `getOrDefault('a',0)` = 0, new count = 1 → tCount becomes {n=1, a=1}. Not 2.

Thus the error is in the table where we used the previous a-count from s incorrectly. Let's rebuild the table cleanly, keeping sCount and tCount completely separate.

### Clean dry‑run (two independent maps)

We'll maintain two columns: **sCount** and **tCount** and update them independently.

Initialize: `sCount = {}`, `tCount = {}`.

| i | sChar | sCount before | sCount after (inc) | tChar | tCount before | tCount after (inc) |
|---|-------|---------------|--------------------|-------|---------------|--------------------|
| 0 | a | {} | {a=1} | n | {} | {n=1} |
| 1 | n | {a=1} | {a=1, n=1} | a | {n=1} | {n=1, a=1} |
| 2 | a | {a=1, n=1} | {a=2, n=1} | g | {n=1, a=1} | {n=1, a=1, g=1} |
| 3 | g | {a=2, n=1} | {a=2, n=1, g=1} | a | {n=1, a=1, g=1} | {n=1, a=2, g=1} |
| 4 | r | {a=2, n=1, g=1} | {a=2, n=1, g=1, r=1} | r | {n=1, a=2, g=1} | {n=1, a=2, g=1, r=1} |
| 5 | a | {a=2, n=1, g=1, r=1} | {a=3, n=1, g=1, r=1} | a | {n=1, a=2, g=1, r=1} | {n=1, a=3, g=1, r=1} |
| 6 | m | {a=3, n=1, g=1, r=1} | {a=3, n=1, g=1, r=1, m=1} | m | {n=1, a=3, g=1, r=1} | {n=1, a=3, g=1, r=1, m=1} |

Now both maps are identical:

```
sCount = {a=3, n=1, g=1, r=1, m=1}
tCount = {a=3, n=1, g=1, r=1, m=1}
```

` sCount.equals(tCount) ` → **true**.

Thus the algorithm correctly reports that `"anagram"` and `"nagaram"` are anagrams.

---

## 3️⃣ Complexity Analysis  

Let **n** = length of the strings (the algorithm first checks `if (s.length() != t.length()) return false;`, so we can assume `n = s.length() = t.length()` for the remaining work).

### Time Complexity  

* The `for` loop runs **n** iterations.  
* Inside each iteration we perform:
  - Two `charAt` calls (O(1) each).  
  - Two `getOrDefault` look‑ups on a `HashMap` (average‑case O(1)).  
  - Two `put` operations (average‑case O(1)).  
* After the loop, `HashMap.equals` walks through the entries of one map and checks the other; in the worst case it examines **k** entries where *k* is the number of distinct characters ( ≤ n). This is also O(n).  

Hence total time = O(n) + O(n) = **O(n)**.

### Space Complexity  

* We allocate two `HashMap` objects.  
* In the worst case each map stores a distinct entry for every character that appears. The number of distinct characters is bounded by the size of the character set.  
  - If the input is unrestricted Unicode, the worst‑case distinct count is O(n) (every character different).  
  - If we assume only lowercase English letters (as in many LeetCode variants), the map size is bounded by 26 → O(1).  

Thus, **auxiliary space** = O(min(n, |Σ|)) where Σ is the alphabet. In big‑O notation we simply write **O(n)** (worst case) and note that for the typical lowercase‑only constraint it is O(1).

### Summary  

| Metric | Bound |
|--------|-------|
| Time   | **O(n)** |
| Space  | **O(n)** worst‑case (O(1) for fixed alphabet) |

---

## TL;DR  

*The solution builds frequency maps for both strings in a single pass, then checks map equality.*  
A dry run on `"anagram"` / `"nagaram"` shows the maps end up identical, yielding `true`.  
The algorithm runs in linear time and uses linear (or constant, for a bounded alphabet) extra space.
