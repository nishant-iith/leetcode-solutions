# Submission details for number-of-strings-that-appear-as-substrings-in-word

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 11 ms (Better than 3.88% of submissions)
- **Memory:** 47.2 MB (Better than 8.19% of submissions)

## Solution Code
```java
class Solution {
    class Node {
        Node[] c = new Node[26];
        Node f;
        Node l;
        int n = 0;
    }

    public int numOfStrings(String[] patterns, String word) {
        Node root = new Node();
        
        for (int i = 0; i < patterns.length; i++) {
            Node curr = root;
            String p = patterns[i];
            
            for (int j = 0; j < p.length(); j++) {
                int x = p.charAt(j) - 97;
                
                if (curr.c[x] == null)
                    curr.c[x] = new Node();
                    
                curr = curr.c[x];
            }
            
            curr.n++;
        }
        
        Node[] q = new Node[10005];
        int hd = 0;
        int tl = 0;
        
        for (int i = 0; i < 26; i++) {
            if (root.c[i] == null) {
                root.c[i] = root;
                continue;
            }
            
            root.c[i].f = root;
            root.c[i].l = null;
            q[tl] = root.c[i];
            tl++;
        }
        
        while (hd < tl) {
            Node curr = q[hd];
            hd++;
            
            for (int i = 0; i < 26; i++) {
                if (curr.c[i] == null) {
                    curr.c[i] = curr.f.c[i];
                    continue;
                }
                
                curr.c[i].f = curr.f.c[i];
                
                if (curr.c[i].f.n == 0)
                    curr.c[i].l = curr.c[i].f.l;
                else
                    curr.c[i].l = curr.c[i].f;
                    
                q[tl] = curr.c[i];
                tl++;
            }
        }
        
        Node curr = root;
        int res = 0;
        
        for (int i = 0; i < word.length(); i++) {
            int x = word.charAt(i) - 97;
            curr = curr.c[x];
            
            Node m = curr;
            
            while (m != null) {
                res += m.n;
                m.n = 0;
                
                Node tmp = m.l;
                m.l = null;
                m = tmp;
            }
        }
        
        return res;
    }
}
```

## Detailed Explanation
## 1️⃣  Algorithm Overview  

The solution solves **LeetCode 2223 – “Number of Strings That Appear as Substrings in Word”** with an **Aho‑Corasick automaton** (a.k.a. *dictionary‑matching* automaton).  

The problem statement (shortened) is  

*Given an array `patterns[]` of lowercase strings and a single lowercase string `word`, return how many strings in `patterns` appear **at least once** as a contiguous substring of `word`.*  

The naïve way would be `O(|patterns|·|word|·L)` (try every pattern at every position).  
The automaton builds a **trie** of all patterns, augments it with **failure links** (`f`) and **output links** (`l`), then walks through `word` once, following the automaton transitions.  
Whenever the walk lands on a node that corresponds to the *end* of one or more patterns we add the count of those patterns to the answer and *disable* them (set `n = 0`) so that each pattern is counted only once.

The code can be divided into three phases:

| Phase | What is done | Data structures | Main loop |
|------|--------------|-----------------|-----------|
| **Trie construction** | Insert every pattern into a plain trie; each terminal node stores `n = #patterns that end here`. | `Node { Node[26] c; Node f; Node l; int n; }` | `for each pattern → for each char`. |
| **Failure & output link building** (BFS) | Compute the Aho‑Corasick failure links (`f`) for every trie node and a *shortcut* output link (`l`) that jumps to the next node on the failure chain that actually ends a pattern (`n>0`). Also fill missing transitions with the transition of the failure node (`curr.c[i] = curr.f.c[i]`). | Same `Node` objects, an array‑based queue `q[]` for BFS. | `while (head < tail)`. |
| **Scanning `word`** | Walk through the automaton character by character (`curr = curr.c[x]`). Whenever we land on a node, follow the output chain (`m = curr; while (m != null) …`) adding `m.n` to the answer and zero‑ing it (so the same pattern is never counted again). | No extra structures; only pointers `curr`, `m`, `tmp`. | `for each char in word`. |

### Why it works

* **Trie** lets us share common prefixes of the patterns, so building it costs `O(total length of patterns)`.
* **Failure links** give us the longest proper suffix of the current path that is also a trie node – exactly the state we must jump to when the next character does not continue the current path. This makes the scan **linear** in the length of `word`.
* **Output links** (`l`) let us skip over intermediate failure nodes that do **not** correspond to a pattern end. Without them we would have to walk up the failure chain one step at a time, which would still be linear overall but a little slower; the link makes the inner loop O(number of matches) instead of O(depth).
* Setting `n = 0` after a match guarantees each pattern contributes **once** even if it appears many times in `word`.

The whole algorithm is the classic Aho‑Corasick multiple‑pattern matcher, just written in a very compact (and a bit “clever”) Java style.

---

## 2️⃣  Dry‑Run on the Sample  

```
patterns = ["a","abc","bc","d"]
word     = "abc"
```

### 2.1  Phase 1 – Build the Trie  

We start with a single root node (`root`).  
`Node.n` = number of patterns that **end** exactly at that node.

| Inserted pattern | Nodes created / visited | `n` values after insertion |
|------------------|--------------------------|-----------------------------|
| **"a"** | root → 'a' (new) | node('a').n = 1 |
| **"abc"** | root → 'a' (existing) → 'b' (new) → 'c' (new) | node('c').n = 1 |
| **"bc"** | root → 'b' (new) → 'c' (existing) | node('c' after “bc”).n = 1 (now there are two different **c** nodes, one under `'a'` and one under `'b'`; each gets its own `n`). |
| **"d"** | root → 'd' (new) | node('d').n = 1 |

Resulting trie (only letters that exist are shown, `*` marks an end node):

```
root
 ├─ a (n=1)                 <-- "a"
 │   └─ b
 │       └─ c (n=1)        <-- "abc"
 ├─ b
 │   └─ c (n=1)            <-- "bc"
 └─ d (n=1)                <-- "d"
```

All other `c[i]` entries are `null`.

---

### 2.2  Phase 2 – Build failure (`f`) and output (`l`) links  

We use a BFS queue `q[]`.  
First we initialise direct children of the root:

| Letter | Action |
|--------|--------|
| a | `root.c[a].f = root` ; `root.c[a].l = null` ; enqueue node('a') |
| b | `root.c[b].f = root` ; `root.c[b].l = null` ; enqueue node('b') |
| c … z (except d) | `root.c[i] = root`   (missing transitions point back to root) |
| d | `root.c[d].f = root` ; `root.c[d].l = null` ; enqueue node('d') |

Now the queue contains three nodes: **A** = node('a'), **B** = node('b'), **D** = node('d').

We process them one by one.

---

#### Process node **A** (represents prefix “a”)  

For each character `i` (0…25) we look at `A.c[i]`.

| i | char | A.c[i] exists? | Action |
|---|------|----------------|--------|
| a (0) | 'a' | **null** | `A.c[a] = A.f.c[a] = root.c[a]` (which is **A** itself, a self‑loop). |
| b (1) | 'b' | **node('b' under A)** → this is the `'b'` that continues “ab”.<br>Set `A.c[b].f = A.f.c[b] = root.c[b] = **B**`.<br>Since `B.n == 0`, `A.c[b].l = B.l = null`.<br>Enqueue the new node (call it **AB**). |
| c … z (others) | null | `A.c[i] = A.f.c[i] = root.c[i]` (most of them become `root`). |

After this step we have created node **AB** (prefix “ab”).

---

#### Process node **B** (prefix “b”)  

| i | char | B.c[i] exists? | Action |
|---|------|----------------|--------|
| a | null | `B.c[a] = B.f.c[a] = root.c[a] = **A**` |
| b | null | `B.c[b] = B.f.c[b] = root.c[b] = **B**` |
| c | **node('c' under B)** (end of “bc”) | `B.c[c].f = B.f.c[c] = root.c[c] = root` (because root.c[c] was set to root earlier).<br>Now `B.c[c].f.n == 0` (root.n = 0), so `B.c[c].l = B.c[c].f.l = null`.<br>Enqueue node **BC** (prefix “bc”). |
| d … z | null → point to root |

Node **BC** is a terminal node (`n = 1`).

---

#### Process node **D** (prefix “d”)  

All children are `null`, so they become copies of the root’s transitions. No new nodes are enqueued.

---

#### Process node **AB** (prefix “ab”)  

Only child that exists is `'c'` (completes “abc”).

* `AB.c[c].f = AB.f.c[c] = B.c[c]` (the node that ends “bc”).  
* `AB.c[c].f.n == 1` (because “bc” ends there), therefore `AB.c[c].l = AB.c[c].f` (i.e. `l` points to the “bc” node).  
* Enqueue node **ABC** (prefix “abc”).

All other missing children are filled with `AB.f.c[i]` (which are either **B** or **root**).

---

#### Process node **BC** (prefix “bc”)  

It has no children, so missing transitions are filled with its failure’s transitions (`BC.f = root`). No new nodes.

---

#### Process node **ABC** (prefix “abc”)  

It is a terminal node (`n = 1`). All its children are missing, they become copies of its failure’s children (`ABC.f = B.c[c]`). No further enqueues.

The BFS finishes. At this point every `c[i]` entry is a **deterministic transition** (no `null` left), `f` links are correct, and `l` points to the **nearest node on the failure chain that ends a pattern** (or `null`).

A compact view of the relevant part of the automaton (only non‑root transitions shown) is:

```
state 0 (root)
   a → 1
   b → 2
   d → 3
   other → 0

state 1 ("a")          n=1
   b → 4
   other → 0

state 2 ("b")          n=0
   c → 5
   other → 0

state 3 ("d")          n=1
   other → 0

state 4 ("ab")         n=0
   c → 6   (failure = state 5)
   other → 0

state 5 ("bc")         n=1   (output link l = null)
   other → 0

state 6 ("abc")        n=1   (output link l = state 5)
   other → 0
```

*`l` links*:  
- state 6 → state 5 (because “bc” also ends at the failure node).  
- all other states have `l = null`.

---

### 2️⃣ 3️⃣  Phase 3 – Scan the word `"abc"`  

Variables:  

* `curr` – current automaton state (starts at `root`).  
* `res` – answer accumulator (starts at `0`).  

We also use the **output chain** (`m = curr; while (m != null) …`) that traverses `l` links.

| i | character | `x = char - 'a'` | `curr = curr.c[x]` | `curr` description | Output chain walk (`m`) | `res` after walk | `n` values after walk |
|---|-----------|-----------------|-------------------|---------------------|--------------------------|------------------|------------------------|
| 0 | 'a' | 0 | root → **state 1** (`"a"`) | terminal, `n=1` | `m = state 1` → `res+=1` (now 1), `state 1.n=0`; `m.l = null` → stop | **1** | `state 1.n = 0` |
| 1 | 'b' | 1 | state 1 → **state 4** (`"ab"`) | not terminal (`n=0`) | `m = state 4` → `n=0` → nothing added, `m.l = null` (no output) | **1** (unchanged) | unchanged |
| 2 | 'c' | 2 | state 4 → **state 6** (`"abc"`) | terminal, `n=1` | `m = state 6` → `res+=1` (now 2), `state 6.n=0`.<br> `tmp = m.l = state 5`.<br> `m.l = null` (break link to avoid revisiting).<br> Continue with `m = state 5` → `res+=1` (now 3), `state 5.n=0`.<br> `tmp = m.l = null` → stop. | **3** | `state 6.n = 0`, `state 5.n = 0` |

End of word. `res = 3`, which is exactly the number of distinct patterns that appear in `"abc"` (`"a"`, `"abc"`, `"bc"`).

The algorithm never counted `"d"` because its node was never visited.

---

## 3️⃣  Complexity Analysis  

Let  

* `P = total number of characters in all patterns`  (`P = Σ |patterns[i]|`).  
* `W = |word|`.  
* `A = alphabet size = 26` (fixed, lower‑case English letters).  

### Building the trie  

* Each character of each pattern creates (or follows) one edge → **O(P)** time.  
* One `Node` per distinct prefix → at most `P + 1` nodes → **O(P)** extra memory.

### Building failure and output links (BFS)  

* Every node is dequeued exactly once, and for each node we iterate over the whole alphabet (`26`).  
* Hence **O(A·(#nodes)) = O(26·(P+1)) = O(P)** time.  
* No additional asymptotic memory beyond the queue (`O(#nodes) = O(P)`).

### Scanning `word`  

* For each character we perform a constant‑time transition (`curr = curr.c[x]`).  
* Then we follow the output chain. The chain visits **each terminal node at most once**, because after a node is visited we set `n = 0` and break its `l` link (`m.l = null`).  
* Therefore total work of all inner `while` loops is **O(number of pattern occurrences) ≤ O(P)**.  
* Overall scanning time is **O(W + number_of_matches) = O(W + P)**, which is bounded by **O(W + P)**.

### Overall  

| Phase | Time | Space |
|-------|------|-------|
| Trie construction | **O(P)** | **O(P)** nodes |
| Failure / output link BFS | **O(P)** | **O(P)** (queue) |
| Word scan | **O(W + P)** (worst‑case `O(W + P)`) | **O(1)** extra (just a few pointers) |
| **Total** | **O(P + W)** | **O(P)** |

Because the alphabet size is a constant (26), the factor `A` disappears in Big‑O notation.

---

## 4️⃣  Key Take‑aways  

* The solution is a **compact implementation of Aho‑Corasick**.  
* It uses three pointers per node:  
  * `c[26]` – deterministic transition table (filled during BFS, so never `null` after that).  
  * `f` – failure link (standard in Aho‑Corasick).  
  * `l` – **output shortcut** to the next node on the failure chain that actually ends a pattern (`n>0`). This makes the match‑reporting loop fast.  
* By zero‑ing `n` and clearing `l` after a match, each pattern contributes **once** to the answer, satisfying the problem requirement “appear at least once”.  
* The algorithm runs in linear time with respect to the total input size and uses linear extra memory, which is optimal for this problem.  

Feel free to ask for any part of the code to be rewritten more verbosely, or for variations (e.g., counting occurrences instead of distinct patterns).
