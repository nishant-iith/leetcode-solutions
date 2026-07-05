# Submission details for minimum-score-of-a-path-between-two-cities

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 23 ms (Better than 78.51% of submissions)
- **Memory:** 162.4 MB (Better than 61.62% of submissions)

## Solution Code
```java
class Solution {
    public int minScore(int n, int[][] roads) {
        List<int[]>[] g = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) g[i] = new ArrayList<>();

        for (int[] e : roads) {
            g[e[0]].add(new int[]{e[1], e[2]});
            g[e[1]].add(new int[]{e[0], e[2]});
        }

        boolean[] vis = new boolean[n + 1];
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(1);
        vis[1] = true;

        int ans = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int u = q.poll();

            for (int[] cur : g[u]) {
                int v = cur[0], w = cur[1];
                ans = Math.min(ans, w);

                if (!vis[v]) {
                    vis[v] = true;
                    q.offer(v);
                }
            }
        }

        return ans;
    }
}
```

## Detailed Explanation
## 1️⃣  Algorithm Analysis – What the code actually does  

| Step | What the code does | Why it matters for the problem |
|------|-------------------|--------------------------------|
| **1. Build the adjacency list** `g` | For each road `[u,v,w]` we add a bidirectional edge `u → (v,w)` and `v → (u,w)`. | The graph must be traversable, so we need to know every neighbour of a city together with the *score* (weight) of the connecting road. |
| **2. BFS from city 1** (queue `q`, visited array `vis`) | Starting from city 1 we repeatedly pop a node, look at all its outgoing edges, update the global `ans` with the minimum edge weight seen so far, and push any neighbour that has not been visited yet. | The key observation of the problem: **the minimum score you can ever achieve on a path that connects city 1 to city n is simply the smallest edge weight that belongs to the *same connected component* as city 1**. Because roads can be traversed any number of times, once you can reach a city you can also use any edge inside that component repeatedly. Therefore we never need to explicitly store paths – just explore the whole component reachable from city 1. |
| **3. Return `ans`** | After the BFS finishes, `ans` holds the smallest `w` among all edges that were encountered. | This is exactly the answer required by LeetCode: the *minimum possible score* of any path that starts at city 1 **and ends at city n** (or any path that connects the two components, because if city n is reachable, it shares the same component). |

### Why BFS (or DFS) is sufficient  

* The graph is **undirected** and **connected only within each component**.  
* If city n is reachable from city 1, they lie in the **same component**.  
* While exploring that component we will *inevitably* look at every edge that belongs to it (because we follow every adjacency).  
* The *minimum* edge weight among all those edges is the global minimum score that can ever appear on a path that starts at 1 and ends at any vertex (including `n`).  

Thus the algorithm reduces to:  

> **“Find the smallest edge weight inside the connected component that contains vertex 1.”**

The BFS is merely a convenient way to enumerate that component.



---

## 2️⃣  Step‑by‑Step Dry‑Run on the Sample  

**Input**

```
n = 4
roads = [[1,2,9],[2,3,6],[2,4,5],[1,4,7]]
```

### Initialisation  

| Variable | Value |
|----------|-------|
| `g` (adjacency list) | empty lists for indices 0 … 4 |
| `vis` (boolean)      | `false` for all 0 … 4 |
| `q` (queue)          | empty |
| `ans`                  | `Integer.MAX_VALUE` (= 2 147 483 647) |
| `i` (loop)           | starts at 1 |

### 1️⃣  Build adjacency list  

We iterate over the four edges and add each direction:

| Edge | Operation | Result (partial `g`) |
|------|-----------|----------------------|
| `[1,2,9]` | `g[1].add(2,9)`, `g[2].add(1,9)` | `g[1] = [(2,9)]`  <br> `g[2] = [(1,9)]` |
| `[2,3,6]` | `g[2].add(3,6)`, `g[3].add(2,6)` | `g[2] = [(1,9), (3,6)]` <br> `g[3] = [(2,6)]` |
| `[2,4,5]` | `g[2].add(4,5)`, `g[4].add(2,5)` | `g[2] = [(1,9), (3,6), (4,5)]` <br> `g[4] = [(2,5)]` |
| `[1,4,7]` | `g[1].add(4,7)`, `g[4].add(1,7)` | `g[1] = [(2,9), (4,7)]` <br> `g[4] = [(2,5), (1,7)]` |

All other indices keep an empty list.

### 2️⃣  BFS initialisation  

* `q.offer(1)` → queue = **[1]**  
* `vis[1] = true` → visited[1] = **true**  
* `ans` stays at `MAX_VALUE`.

### 3️⃣  Main while‑loop – first iteration  

| Current step | `u` (polled) | Process each neighbour of `u` |
|--------------|--------------|--------------------------------|
| **while queue not empty** → `u = 1` |
| • neighbour `(2,9)` → `v = 2, w = 9` <br> &nbsp;&nbsp;• `ans = min(MAX,9) = 9` <br> &nbsp;&nbsp;• `vis[2]` is false → set `vis[2]=true`, `q.offer(2)` |
| • neighbour `(4,7)` → `v = 4, w = 7` <br> &nbsp;&nbsp;• `ans = min(9,7) = 7` <br> &nbsp;&nbsp;• `vis[4]` is false → set `vis[4]=true`, `q.offer(4)` |

**State after first iteration**

| Variable | Value |
|----------|-------|
| `ans`    | **7** |
| `q`      | **[2, 4]** (order preserved) |
| `vis`    | `vis[1]=true, vis[2]=true, vis[4]=true` (others false) |

---

| Step | Queue (front→back) | `u` popped | Action on each edge | Updated `ans` | New vertices enqueued |
|------|--------------------|-----------|----------------------|--------------|-----------------------|
| **2** | **[2, 4]** | `u = 2` | *Edge (1,9)* → `v=1` already visited → only update `ans = min(7,9) = 7` (no change). <br>*Edge (3,6)* → `v=3, w=6` → `ans = min(7,6) = 6`. <br> `vis[3]` is false → mark true, `q.offer(3)`. <br>*Edge (4,5)* → `v=4` already visited → update `ans = min(6,5) = 5`. (no new enqueue) | `ans` becomes **5** after examining edge `(4,5)` | `q` becomes **[4, 3]** (4 was already there) |
| **3** | **[4, 3]** | `u = 4` | *Edge (2,5)* → `v=2` visited → `ans = min(5,5) = 5`. <br>*Edge (1,7)* → `v=1` visited → `ans = min(5,7) = 5`. No new vertices. | `ans` stays **5** | Queue now **[3]** |
| **4** | **[3]** | `u = 3` | *Edge (2,6)* → `v=2` visited → `ans = min(5,6) = 5`. No new vertices. | `ans` stays **5** | Queue becomes **[]** → loop ends |

### Final state  

* `ans = 5`  
* `vis` marks all four cities as visited (component containing city 1 includes all of them).  
* The function returns **5**.

**Interpretation:** The smallest weight among all edges that belong to the component reachable from city 1 (and therefore also reachable from city 4) is `5`, which is precisely the answer.

---

## 3️⃣  Complexity Analysis  

| Metric | Formal expression | Reasoning |
|--------|-------------------|-----------|
| **Time Complexity** | **O(N + M)** where `N = n` (number of cities) and `M = roads.length` | *Building the adjacency list* scans each of the `M` edges once → **O(M)**. *BFS* visits each vertex at most once and examines each adjacency list entry exactly once → each edge (both directions) is processed once → **O(M)**. The loop over vertices adds a linear `O(N)` term (initial queue insertion). Overall: `O(N + M)`. |
| **Space Complexity** | **O(N + M)** | *Adjacency list* stores `2 × M` directed entries → **O(M)**. *Visited array* and *queue* together need at most `N` entries → **O(N)**. Hence total **O(N + M)**. |

### Quick sanity check with the sample  

* `N = 4`, `M = 4`.  
* We allocated 4 `ArrayList`s, each holding a few pairs, plus a boolean array of size 5 and a queue that used at most 2 elements.  
* The constants line up with the asymptotic bounds above.

---

## 4️⃣  TL;DR – What you should remember  

* The problem is **not** about finding a path of minimum total weight; it’s about the **smallest single edge** that lives in the **same connected component** as city 1.  
* A simple BFS/DFS that explores that component is enough: every time we look at an edge we can update a running minimum.  
* After the traversal finishes, that minimum **is** the answer.  
* Runtime is linear in the size of the input graph (`O(N+M)`) and uses the same amount of extra space.  

Feel free to ask follow‑up questions—whether you want to see a DFS version, a union‑find alternative, or how to adapt the solution if the graph were directed. Happy coding!
