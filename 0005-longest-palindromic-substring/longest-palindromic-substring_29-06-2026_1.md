# Submission details for longest-palindromic-substring

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 19 ms (Better than 40.99% of submissions)
- **Memory:** 46.9 MB (Better than 28.43% of submissions)

## Solution Code
```java
class Solution {
    public String longestPalindrome(String s) {
        int n = s.length();
        String res = "";

        for(int i = 0;i<n;i++){
            int st = i,end = i;
            while(st>=0&&end<n&&s.charAt(st)==s.charAt(end)){
                st--;
                end++;
            }
            String temp = s.substring(st+1,end);
            if(temp.length()>res.length())res=temp;

            st = i;
            end = i+1;
            while(st>=0&&end<n&&s.charAt(st)==s.charAt(end)){
                st--;
                end++;
            }
            temp = s.substring(st+1,end);
            if(temp.length()>res.length())res=temp;
        }

        return res;
    }
}
```

## Detailed Explanation
##1. Algorithm Analysis  

### Core Idea – “Expand‑around‑center”

A palindrome reads the same forward and backward.  
If we know the **center** of a palindrome, we can grow it outward as long as the characters on the left and right match.  
Every palindrome of length ≥ 1 has one of two possible centers:

| Palindrome length | Center representation |
|-------------------|-----------------------|
| odd  (e.g. `aba`) | a single character (`i`) |
| even (e.g. `abba`) | the gap between two characters (`i` and `i+1`) |

Thus, for each index `i` in the string we try two expansions:

1. **Odd‑length case** – start with `left = i`, `right = i`.  
2. **Even‑length case** – start with `left = i`, `right = i+1`.

During each expansion we move `left` leftward (`--`) and `right` rightward (`++`) while the characters are equal and we stay inside the string bounds.  
When the loop stops, the substring `s[left+1 … right‑1]` (exclusive of the mismatching characters) is the longest palindrome having that centre.  
We keep the longest one seen so far in `res`.

Because every possible centre (there are `2·n‑1` of them) is examined once and each expansion moves the pointers at most `n` steps total, the algorithm runs in **O(n²)** time and **O(1)** extra space.

---

### Pseudocode (mirrors the given Java)

```
longestPalindrome(s):
    n ← length(s)
    best ← ""                     // current longest palindrome

    for i from 0 to n-1:
        // ----- odd length centre -----
        left ← i ; right ← i
        while left ≥ 0 and right < n and s[left] == s[right]:
            left  ← left  - 1
            right ← right + 1
        cand ← s.substring(left+1, right)   // Java: end exclusive
        if length(cand) > length(best): best ← cand

        // ----- even length centre -----
        left ← i ; right ← i+1
        while left ≥ 0 and right < n and s[left] == s[right]:
            left  ← left  - 1
            right ← right + 1
        cand ← s.substring(left+1, right)
        if length(cand) > length(best): best ← cand

    return best
```

---

## 2. Step‑by‑Step Walkthrough (Dry‑Run) on `"babad"`  

We will trace the execution of the algorithm for the input  

```
s = "b a b a d"
indices:0 1 2 3 4
n = 5
```

We maintain the following variables throughout the trace:

| Symbol | Meaning |
|--------|---------|
| `i`    | current centre index (outer loop) |
| `st`   | left pointer used during expansion |
| `end`  | right pointer used during expansion |
| `temp` | palindrome found by the current expansion (`s.substring(st+1, end)`) |
| `res`  | longest palindrome seen so far |

We'll show a table after each **inner while‑loop** (i.e., after each expansion) and after the possible update of `res`.

### Initialization  

```
res = ""        // length 0
```

---

### Iteration i = 0  

#### Odd‑length centre (st = 0, end = 0)  

| step | st | end | condition `s[st]==s[end]`? | action |
|------|----|-----|----------------------------|--------|
| start| 0  | 0   | s[0]=='b' == s[0]=='b' → true | st←-1, end←1 |
| loop | -1 | 1   | st<0 → break               | – |

`temp = s.substring(st+1, end) = s.substring(0,1) = "b"`  
`temp.length = 1 > res.length (=0)` → `res = "b"`

#### Even‑length centre (st = 0, end = 1)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 0  | 1   | s[0]=='b' vs s[1]=='a' → false | break immediately |
| –    | –  | –   | –          | – |

`temp = s.substring(st+1, end) = s.substring(0,1) = "b"` (same as before)  
Length 1 is **not** > current `res.length` (1), so `res` stays `"b"`.

**End of i=0:** `res = "b"`

---

### Iteration i = 1  

#### Odd‑length centre (st = 1, end = 1)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 1  | 1   | s[1]=='a' == s[1]=='a' → true | st←0, end←2 |
| 1    | 0  | 2   | s[0]=='b' == s[2]=='b' → true | st←-1, end←3 |
| 2    |-1  | 3   | st<0 → break | – |

`temp = s.substring(0,3) = "bab"` (indices 0,1,2)  
Length 3 > `res.length` (1) → `res = "bab"`

#### Even‑length centre (st = 1, end = 2)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 1  | 2   | s[1]=='a' vs s[2]=='b' → false | break |

`temp = s.substring(2,2) = ""` (empty string) – length 0, no update.

**End of i=1:** `res = "bab"`

---

### Iteration i = 2  

#### Odd‑length centre (st = 2, end = 2)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 2  | 2   | s[2]=='b' == s[2]=='b' → true | st←1, end←3 |
| 1    | 1  | 3   | s[1]=='a' == s[3]=='a' → true | st←0, end←4 |
| 2    | 0  | 4   | s[0]=='b' == s[4]=='d' → false | break |

`temp = s.substring(1,4) = "aba"` (indices 1,2,3)  
Length 3 is **equal** to current `res.length` (3). The code uses `>` only, so `res` stays `"bab"` (first longest found).

#### Even‑length centre (st = 2, end = 3)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 2  | 3   | s[2]=='b' vs s[3]=='a' → false | break |

`temp = s.substring(3,3) = ""` – no update.

**End of i=2:** `res = "bab"`

---

### Iteration i = 3  

#### Odd‑length centre (st = 3, end = 3)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 3  | 3   | s[3]=='a' == s[3]=='a' → true | st←2, end←4 |
| 1    | 2  | 4   | s[2]=='b' vs s[4]=='d' → false | break |

`temp = s.substring(3,4) = "a"` (single char) – length 1 < 3 → no update.

#### Even‑length centre (st = 3, end = 4)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 3  | 4   | s[3]=='a' vs s[4]=='d' → false | break |

`temp = s.substring(4,4) = ""` – no update.

**End of i=3:** `res = "bab"`

---

### Iteration i = 4  

#### Odd‑length centre (st = 4, end = 4)  

| step | st | end | condition? | action |
|------|----|-----|------------|--------|
| start| 4  | 4   | s[4]=='d' == s[4]=='d' → true | st←3, end←5 |
| 1    | 3  | 5   | end≥n (5≥5) → break | – |

`temp = s.substring(4,5) = "d"` – length 1 < 3 → no update.

#### Even‑length centre (st = 4, end = 5)  

`end` starts out of bounds (`5 == n`), so the while condition fails immediately.  
`temp = s.substring(5,5) = ""` – no update.

**End of i=4:** `res = "bab"` (final answer)

---

**Result:** The algorithm returns `"bab"`. (Note that `"aba"` is also a valid longest palindrome of length 3; the implementation returns the first one encountered.)

---

## 3. Complexity Analysis  

### Time Complexity  

*The outer loop* runs `n` times (once per index `i`).  
Inside each iteration we perform **two** expansions (odd and even).  

During an expansion, the pointers `st` and `end` move monotonically outward; each step either decrements `st` or increments `end`.  
Across **all** expansions for a fixed centre, the total number of character comparisons cannot exceed `2·n` (worst‑case: we walk from the centre to the left end and to the right end).  

Summed over all `n` centres, the worst‑case number of comparisons is  

```
O( n * n ) = O(n²)
\]

Formally:  
For each `i`, the odd‑length while loop does at most `min(i, n-1-i)+1` iterations, and the even‑length loop does at most `min(i, n-2-i)+1`. Adding both bounds yields ≤ `2n`. Summing over `i = 0 … n‑1` gives ≤ `2n²`. Hence **T(n) = Θ(n²)**.

### Space Complexity  

The algorithm uses only a constant number of primitive variables (`i`, `st`, `end`, `n`, plus the strings `res` and `temp`).  
`temp` is a view (via `substring`) of the original string; in Java it creates a new `String` object whose length is at most `n`, but this allocation happens **inside** the loop and is immediately discarded if not longer than `res`.  
Even counting the temporary strings, the auxiliary space beyond the input and output does not depend on `n` in an asymptotic sense—it is bounded by the length of the current palindrome being examined, which is at most `n`, but we never store more than one such string at a time.  

Therefore, **extra space = O(1)** (ignoring the output string itself).  

---

### Summary  

| Aspect        | Details |
|---------------|---------|
| **Strategy**  | Expand around every possible centre (odd & even) to find the longest palindrome. |
| **Correctness** | Every palindrome has a unique centre; the algorithm checks all centres, and for each centre it records the maximal palindrome reachable by expansion, guaranteeing the global maximum is found. |
| **Time**      | `O(n²)` – each of the `n` centres may cause up to `O(n)` character comparisons. |
| **Space**     | `O(1)` auxiliary (constant extra variables). |

The provided Java code is a direct, clean implementation of this expand‑around‑center technique.
