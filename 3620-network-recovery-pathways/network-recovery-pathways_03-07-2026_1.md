# Submission details for network-recovery-pathways

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 184 ms (Better than 34.65% of submissions)
- **Memory:** 178.4 MB (Better than 57.43% of submissions)

## Solution Code
```java
class Solution {
    public int findMaxPathScore(int[][] edges, boolean[] online, long k) {
        int n = online.length;

        ArrayList<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();

        int[] indegree = new int[n];

        for (int[] e : edges) {
            graph[e[0]].add(new int[]{e[1], e[2]});
            indegree[e[1]]++;
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            if (indegree[i] == 0)
                q.offer(i);

        ArrayList<Integer> topo = new ArrayList<>();

        while (!q.isEmpty()) {
            int u = q.poll();
            topo.add(u);

            for (int[] edge : graph[u]) {
                if (--indegree[edge[0]] == 0)
                    q.offer(edge[0]);
            }
        }

        int left = 0, right = 1_000_000_000;
        int ans = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            long INF = Long.MAX_VALUE / 4;
            long[] dp = new long[n];
            Arrays.fill(dp, INF);
            dp[0] = 0;

            for (int u : topo) {

                if (dp[u] == INF)
                    continue;

                if (u != 0 && u != n - 1 && !online[u])
                    continue;

                for (int[] edge : graph[u]) {
                    int v = edge[0];
                    int w = edge[1];

                    if (w < mid)
                        continue;

                    if (v != n - 1 && !online[v])
                        continue;

                    dp[v] = Math.min(dp[v], dp[u] + w);
                }
            }

            if (dp[n - 1] <= k) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return ans;
    }
}
```

## Detailed Explanation
**1. Algorithm Analysis**

The problem can be re‑phrased as:

> *Given a directed acyclic graph (DAG) with edge weights `w[i]`, a boolean array `online[]` that tells us which intermediate nodes are “alive”, and a time budget `k`, find the largest integer `X` such that there is a path from node `0` to node `n‑1` whose  
> وث – the smallest weight among the edges on the path – is at least `X` and whose *sum of weights* does not exceed `k`.*

Notice the two constraints that make the problem non‑trivial:

1. **Only transitions that use edges of weight ≥ `X` are allowed.**  
   If the minimum edge on the path must be at least `X`, every edge used on the path must satisfy `w ≥ X`.

2. **Intermediate nodes must be online.**  
   Any node on the path except the source (0) and the destination (n‑1) must satisfy `online[node] nineteenth`.

The naïve approach would try every possible `'esprit`… That would be far too slow.  
Instead we use **binary search on the answer** (the minimal edge weight) and, for each candidate `mid`, we check whether a feasible path exists.  
Because we are working on a DAG we can efficiently find the *minimum total weight* that satisfies the constraints using a dynamic program in **topological order**.

---

### 1.1  Graph construction

* `graph[u]` is a list of `(v, w)` pairs, where `u → v` has weight `w`.
* `indegree[v]` tells how many incoming edges `v` has.
* All `Edge` objects are added to `graph`, and indegrees are updated.

The graph is guaranteed to be acyclic (this is implicit in the problem statement, otherwise the topological sort would fail).

---

### 1.2  Topological sorting

We perform a classic Kahn’s algorithm:

```
queue ← all vertices with indegree 0
while queue not empty:
    u ← queue.pop()
    topo.add(u)
    for each (v, w) in graph[u]:
        indegree[v]--
        if indegree[v] == 0: queue.push(v)
```

The resulting list `topo` contains all vertices in a gero.  
If the graph contains a cycle this algorithm would produce fewer than `n` vertices, but the problem guarantees a DAG.

---

### 1.3  Decision procedure for a fixed `mid`

We want to test whether there exists a path from 0 to n‑1 that:

* uses only edges with weight `≥ mid`, and
* goes through only online intermediate nodes, and
* has total weight `≤ k`.

The DP keeps the **minimum possible total weight** to reach each vertex under the above restrictions.

```
dp[v] = minimum total weight to reach v
        (INF if v is not reachable under restrictions)
dp[0] = 0
```

We process vertices in topological order:

```
for u in topo:
    if dp[u] == INF: continue          // u unreachable
    if u != 0 && u != n-1 && !online[u]: continue  // u is a forbidden intermediate node
    for each edge (u -> v, w):
        if w < mid: continue           // cannot use this edge
        if v != n-1 && !online[v]: continue  // v would be a forbidden intermediate node
        dp[v] = min(dp[v], dp[u] + w)
```

After the loop, `dp[n-1]` holds the cheapest feasible path.  
If `dp[n-1] ≤ k` the candidate `mid` is **feasible**; otherwise it is impossible.

---

###קום 1.4  Binary search

The answer is monotone:  
if a path exists with minimum edge weight `X Weg`, then it also exists for every smaller weight `≤ X`.  
Hence we binary‑search the maximum feasible weight between 0 and 10⁹ (the problem’s natural upper bound for weight).  

```
left  = 0
right = 1_000_000_000
ans   = -1
while left ≤ right:
    mid = (left + right) / 2
    if feasible(mid):
        ans  = mid  // mid works, try finding something larger
        left = mid + 1
    else:
        right = mid - 1
return ans
```

`feasible(mid)` is exactly the DP described in §1.3.

---

**2. Step‑by‑Step Walkthrough (DRY Run)**  

Sample input  
```
edges   = [[0,1,5],[1,3,10],[0,2,3],[2,3,4]]
online  = [true,true,true,true]
k       = 10
```

Graph size: `n = 4`.

| Step | Description | Variables |
|------|-------------|-----------|
| **Graph building** | `graph[0] = {(1,5),(2,3)}`<br>`graph[1] = {(3,10)}`<br>`graph[2] = {(3,4)}`<br>`graph[3] = {}`<br>`indegree = [0,1,1,2]` | – |
| **Topological sort** | Queue init: `[0]`<br>Pop 0 → `topo=[0]` → indegree[1]=0, indegree[2]=0 → queue=[1,2] <br>Pop 1 → `topo=[0,1]` → indegree[3]=1 <br>Pop 2 → `topo=[0,1,2]` → indegree[3]=0 → queue=[3] <br>Pop 3 → `topo=[0,1,2,3]` | `topo = [0,1,2,3]` |

Now we perform binary search; we’ll skip the high‑weight iterations (because no path昌 can use an edge with weight > `10`), and focus on the decisive steps that decide the answer.

| Iteration | `mid` | Feasible? | `dp` after DP | Reason |
|-----------|-------|-----------|---------------|--------|
| 1 | 500,000,000 | **No** | all `INF` except `dp[0]=0`; no edges ≥ mid → all unreachable | no edges large enough |
| … | … | **No** | … | still no edges |
| After many “No” iterationslæg | `mid = 5` | **No** | `dp[0]=0` → edges ≥5: (0→1,5),(1→3,10). <br>dp[1] = 5<br>dp[3] = 15 | 15 > k |
| Next step | `mid = 4` | **No** | edges ≥4: (0→1,5),(1→3,10),(2→3,4).<br>dp[1]=5, dp[3] via 1 = 15 | still 15>k |
| Next step | `mid = 3` | **Yes** | edges ≥3: all edges. <br>Processing: <br>dp[0]=0 → dp[1]=5, dp[2]=3 <br>dp[1]→dp[3]=15 <br>dp[2]→_PKT=7 <br>dp[3] final = min(15,7)=7 | 7 ≤ k, so 3 works |
| Binary search adjusts left = mid+1 → left=4, right=3 → exit loop | – | – | Answer = `mid = 3` |

Thus the algorithm returns `3`, which matches the expected output.

---

**3. Complexity Analysis**

Let  

* `n` = number of nodes (`online.length`)  
* `m` = number of directed edges (`edges.length`)

---

### 3.1  Time Complexity

* **Graph construction**: `O(m + n)`  
  (one pass over the edges to build adjacency lists & indegree).
* **Topological sort**: `O(m + n)`  
  Each vertex is enqueued/dequeued once, each edge processed once.

* **Binary search**:  
  We search in the range `[0, 10⁹]` → `⌈log₂(10⁹)⌉ ≤ 30` iterations.

* **Each feasibility check**:  
  `O(m + n)` (topological order loop + edge relaxations).

Therefore:

```
Total Time = O( (m + n) * log(10⁹) ) ≈ O( (m + n) * 30 ) = O(m + n)
```

The logarithmic factor is a constant bounded by 30, so asymptotically the algorithm runs in linear time in the size of the input.

---

### 3.2  Space Complexity

* `	current` graph `ArrayList<int[]>[]` – stores `m` pairs → `O(m)`.  
* `indegree` array – `n` integers → `O(n)`.  
* `topo` list – `n` integers → `O(n)`.  
* `dp` array – `n` longs → `O(n)`.  
* Queue for Kahn’s algorithm – up to `n` vertices → `O(n)`.

Hence:

```
Space = O(m + n)
```

Theҗанspace overhead is linear and unavoidable because we must keep the graph structure itself.

---

### 3.3  Correctness Sketch

1. **Monotonicity**:  
   If a path of minimal edge weight `X` is feasible, any `Y < X` is also feasible (just keep the same path). Therefore the set of feasible weights is an interval `[0, answer]`. This property lets us binary‑search.

2. **Feasibility Test**:  
   For a fixed `mid`, the DP produces the *minimum total weight* among all paths that respect  
   * edges ≥ mid,  
   * online intermediate nodes.  
   The DP runs in topological order, ensuring that when relaxing an outgoing edge `(u→v)` we already know the optimal cost to `u`. This is the classic shortest‑path computation on a DAG and is guaranteed to give the true minimum.

3. **Decision**:  
   `mid` is feasible iff the computed `dp[n‑1]` ≤ `k`.  
   If that holds, a path of minimal edge weight `mid` and total length ≤ `k` exists.  
   Conversely, if `dp[n‑1]` > `k`, no such path can exist, because `dp[n‑1]` is the *shortest* possible total weight under the restrictions.

4. **Binary Search Outcome**:  
   By always keeping the “best‑found” answer and moving the search bounds accordingly, the algorithm converges to the maximum feasible weight.

Therefore the algorithm returns the optimal minimal edge weight that satisfies all constraints.
