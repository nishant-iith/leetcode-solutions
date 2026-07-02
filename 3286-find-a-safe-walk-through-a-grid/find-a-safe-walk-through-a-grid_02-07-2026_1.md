# Submission details for find-a-safe-walk-through-a-grid

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 9 ms (Better than 91.39% of submissions)
- **Memory:** 46.4 MB (Better than 99.18% of submissions)

## Solution Code
```java
class Solution {
    public boolean findSafeWalk(List<List<Integer>> grid, int health) {
        int m = grid.size();
        int n = grid.get(0).size();

        int[][] dist = new int[m][n];
        for (int[] row : dist)
            Arrays.fill(row, Integer.MAX_VALUE);

        Deque<int[]> dq = new ArrayDeque<>();

        dist[0][0] = grid.get(0).get(0);
        dq.offerFirst(new int[]{0, 0});

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!dq.isEmpty()) {
            int[] curr = dq.pollFirst();
            int x = curr[0];
            int y = curr[1];

            if (x == m - 1 && y == n - 1)
                return dist[x][y] < health;

            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];

                if (nx < 0 || nx >= m || ny < 0 || ny >= n)
                    continue;

                int w = grid.get(nx).get(ny);

                if (dist[x][y] + w < dist[nx][ny]) {
                    dist[nx][ny] = dist[x][y] + w;

                    if (w == 0)
                        dq.offerFirst(new int[]{nx, ny});
                    else
                        dq.offerLast(new int[]{nx, ny});
                }
            }
        }

        return dist[m - 1][n - 1] < health;
    }
}
```

## Detailed Explanation
## 1️⃣  Algorithm Analysis  

| Aspect | Explanation |
|--------|-------------|
| **Core Idea** | Treat each cell as a node in a weighted graph where the weight of entering a cell is the value stored in that cell (`grid[r][c]`).  The task is to know whether there exists a path from **(0,0)** to **(m‑1,n‑1)** whose **total weight** is **strictly smaller than `health`**. |
| **Why Dijkstra?** | All weights are **non‑negative** (0 or 1 in the actual test data, but the algorithm works for any non‑negative integer).  We need the *minimum* sum of weights on a path from the source to the destination.  If that minimum sum < `health` we can walk safely; otherwise we cannot. |
| **How the code implements it** | The implementation uses **0‑1 BFS (double‑ended BFS)** – a specialization of Dijkstra that runs in **O(V+E)** when the edge‑weights are only **0 or 1**.  <br> • `dist[r][c]` stores the smallest accumulated weight known so far to reach `(r,c)`. <br> • A `Deque<int[]>` works as a queue where **0‑weight edges are inserted at the *front*** and **1‑weight edges (or any weight > 0) are inserted at the *back***.  This guarantees that nodes are popped from the deque in **non‑decreasing order of distance**, exactly the property Dijkstra needs, but without a priority queue. |
| **Cycle / Visited handling** | `dist` is initialised to `Integer.MAX_VALUE`.  When we discover a strictly smaller distance `dist[x][y] + w` we update it and push the neighbour.  Because the update is only performed when the new distance is *strictly* smaller, a node can be pushed at most a few times (once for each distinct distance that improves it).  In practice, for 0‑1 weights each node is popped **once**; later pushes are ignored. |
| **Termination condition** | As soon as we pop the target cell `(m‑1,n‑1)` we can decide: `dist[dest] < health` → safe walk exists → return `true`.  If the deque empties first, we return `false`. |
| **Correctness Sketch** | 1. `dist` always holds the **best known distance** for every visited cell.  <br>2. The deque is ordered by non‑decreasing distance, therefore the **first time we pop the destination**, `dist[dest]` is the *globally minimal* sum of cell values on any path to the destination (standard 0‑1 BFS invariant).  <br>3. If that minimal sum is `< health`, a safe walk exists; otherwise no path can satisfy the health constraint. |

---

## 2️⃣  Step‑by‑Step Walk‑through (DRY‑run)  

We will trace the algorithm on the **sample test case**:

```
grid = [
  [0, 1, 0, 0, 0],
  [0, 1, 0, 1, 0],
  [0, 0, 0, 1, 0]
]
health = 1
```

* `m = 3`, `n = 5`  
* Starting cell `(0,0)` has value `0`.  
* Destination cell `(2,4)` (bottom‑right) has value `0`.  

### Initialisation  

| Variable | Value |
|----------|-------|
| `dist` (3×5) | all `INF` except `dist[0][0]=0` |
| `deque` | `[ (0,0) ]` (distance 0) |
| `dx,dy` | `{-1,1,0,0}` , `{0,0,-1,1}` (4 directions) |

```
dist = [
  [0, INF, INF, INF, INF],
  [INF,INF, INF, INF, INF],
  [INF,INF, INF, INF, INF]
]
```

### Iteration 1 – pop (0,0)

* Current distance = `dist[0][0] = 0`.  
* It is **not** the target (`0,0 != 2,4`).  
* Explore its 4 neighbours:

| neighbour (nx,ny) | weight `w` | newDist = 0 + w | `newDist < dist[nx][ny]`? | Action |
|-------------------|-----------|-----------------|--------------------------|--------|
| (‑1,0) , (1,0) , (0,‑1) , (0,1) → only **(1,0)** is inside grid | `grid[1][0]=0` | `0` | Yes (`0 < INF`) | `dist[1][0]=0`; `w==0` → **offerFirst** → deque becomes `[ (1,0) ]` |

*Deque after processing (0,0):* `[ (1,0) ]`

---

### Iteration 2 – pop (1,0)

* Distance = `dist[1][0]=0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? | Action |
|---------|-----|-----------|---------|--------|
| (0,0) – already 0 (no) |
| (2,0) – `grid[2][0]=0` → `newDist=0` | **update** → `dist[2][0]=0`; push front → deque `[ (2,0), (1,0?) ]` (but (1,0) already popped, so only `(2,0)`) |
| (1,‑1) – out of bounds |
| (1,1) – `grid[1][1]=1` → `newDist=1` → `1 < INF` → update → `dist[1][1]=1`; push back → deque `[ (2,0), (1,1) ]` |

*Deque now:* `[ (2,0), (1,1) ]`

---

### Iteration 3 – pop (2,0)

* Distance = `0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (1,0) – already 0 |
| (3,0) – out of bounds |
| (2,‑1) – out |
| (2,1) – `grid[2][1]=0` → `newDist=0` → update → `dist[2][1]=0`; push front → deque `[ (2,1), (1,1) ]` |

*Deque:* `[ (2,1), (1,1) ]`

---

### Iteration 4 – pop (2,1)

* Distance = `0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (1,1) – `grid[1][1]=1` → `newDist=1` → currently `dist[1][1]=1` → **no improvement** |
| (3,1) – out |
| (2,0) – already 0 |
| (2,2) – `grid[2][2]=0` → `newDist=0` → update → `dist[2][2]=0`; push front → deque `[ (2,2), (1,1) ]` |

*Deque:* `[ (2,2), (1,1) ]`

---

### Iteration 5 – pop (2,2)

* Distance = `0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (1,2) – `grid[1][2]=0` → `newDist=0` → update → `dist[1][2]=0`; push front → deque `[ (1,2), (1,1) ]` |
| (3,2) – out |
| (2,1) – already 0 |
| (2,3) – `grid[2][3]=1` → `newDist=1` → improves from `INF` → `dist[2][3]=1`; push back → deque `[ (1,2), (1,1), (2,3) ]` |

*Deque:* `[ (1,2), (1,1), (2,3) ]`

---

### Iteration 6 – pop (1,2)

* Distance = `0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (0,2) – `grid[0][2]=0` → `newDist=0` → update → `dist[0][2]=0`; push front → deque `[ (0,2), (1,1), (2,3) ]` |
| (2,2) – already 0 |
| (1,1) – `w=1` → `newDist=1` → current `dist[1][1]=1` → **no update** |
| (1,3) – `grid[1][3]=1` → `newDist=1` → update → `dist[1][3]=1`; push back → deque `[ (0,2), (1,1), (2,3), (1,3) ]` |

*Deque:* `[ (0,2), (1,1), (2,3), (1,3) ]`

---

### Iteration 7 – pop (0,2)

* Distance = `0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (‑1,2) – out |
| (1,2) – already 0 |
| (0,1) – `grid[0][1]=1` → `newDist=1` → update → `dist[0][1]=1`; push back → deque `[ (1,1), (2,3), (1,3), (0,1) ]` |
| (0,3) – `grid[0][3]=0` → `newDist=0` → update → `dist[0][3]=0`; push front → deque `[ (0,3), (1,1), (2,3), (1,3), (0,1) ]` |

*Deque:* `[ (0,3), (1,1), (2,3), (1,3), (0,1) ]`

---

### Iteration 8 – pop (0,3)

* Distance = `0`.  
* Not target.  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (‑1,3) – out |
| (1,3) – `grid[1][3]=1` → `newDist=1` → current `dist[1][3]=1` → **no update** |
| (0,2) – already 0 |
| (0,4) – `grid[0][4]=0` → `newDist=0` → update → `dist[0][4]=0`; push front → deque `[ (0,4), (1,1), (2,3), (1,3), (0,1) ]` |

*Deque:* `[ (0,4), (1,1), (2,3), (1,3), (0,1) ]`

---

### Iteration 9 – pop (0,4)

* Distance = `0`.  
* **Not** target yet (`0,4 != 2,4`).  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (‑1,4) – out |
| (1,4) – `grid[1][4]=0` → `newDist=0` → update → `dist[1][4]=0`; push front → deque `[ (1,4), (1,1), (2,3), (1,3), (0,1) ]` |
| (0,3) – already 0 |
| (0,5) – out |

*Deque now:* `[ (1,4), (1,1), (2,3), (1,3), (0,1) ]`

---

### Iteration 10 – pop (1,4)

* Distance = `0`.  
* Not target (`1,4 != 2,4`).  
* Neighbours:

| (nx,ny) | `w` | `newDist` | update? |
|---------|-----|-----------|----------|
| (0,4) – already 0 |
| (2,4) – **destination** → `grid[2][4]=0` → `newDist=0` → currently `dist[2][4]=INF` → **update** → `dist[2][4]=0`; push front → deque `[ (2,4), (1,1), (2,3), (1,3), (0,1) ]` |
| (1,3) – already 1 (no improvement) |
| (1,5) – out |

*Deque after push:* `[ (2,4), (1,1), (2,3), (1,3), (0,1) ]`

---

### Iteration 11 – pop (2,4)  

* Current distance = `0`.  
* **This is the target cell** (`x==m‑1 && y==n‑1`).  

Now the algorithm evaluates:

```java
return dist[x][y] < health;   // 0 < 1  → true
```

So the method returns **`true`** – a safe walk exists.

> **Summary of the walk‑through** – the algorithm discovers a zero‑cost path that reaches the bottom‑right corner before any positive‑cost cell is ever popped, guaranteeing that the minimal accumulated weight is `0`. Because `0 < health (1)`, the answer is `true`.

---

## 3️⃣  Complexity Analysis  

| Metric | Value | Reason |
|--------|-------|--------|
| **Time Complexity** | **`O(m·n)`** | Each cell is **popped from the deque at most once** (once its minimal distance is known). When a cell is popped we examine its 4 neighbours → constant work per edge. The total number of edges in the grid graph is `4·m·n` → `O(m·n)`. |
| **Space Complexity** | **`O(m·n)`** | `dist` stores an `int` for every cell (`m·n`). The deque can hold at most `m·n` positions in the worst case (e.g., when many equal‑distance cells are waiting). No other large structures are allocated. |
| **Additional Notes** | The algorithm is a **0‑1 BFS** variant of Dijkstra. It relies on the fact that edge weights are non‑negative; if they were arbitrary non‑negative integers larger than 1, the same deque logic would no longer guarantee the correct order and we would need a priority queue (`O(m·n log(m·n))`). |

---

### TL;DR  

* The solution treats the grid as a weighted graph and finds the **minimum sum of cell values** from start to end using **0‑1 BFS** (a linear‑time Dijkstra for 0/1 weights).  
* A dry‑run on the sample grid shows that the algorithm discovers a zero‑cost path, leading to the correct answer `true`.  
* The algorithm runs in **`O(m·n)` time** and uses **`O(m·n)` extra space**, which is optimal for this grid‑traversal problem.
