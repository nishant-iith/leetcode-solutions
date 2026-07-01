# Submission details for find-the-safest-path-in-a-grid

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 180 ms (Better than 34.29% of submissions)
- **Memory:** 121.2 MB (Better than 48.25% of submissions)

## Solution Code
```java
class Solution {
    int n;
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, 1, -1};

    public int maximumSafenessFactor(List<List<Integer>> grid) {
        n = grid.size();

        Queue<int[]> q = new LinkedList<>();
        int[][] dist = new int[n][n];
        boolean[][] vis = new boolean[n][n];

        // Step 1: Push all thief cells
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (grid.get(r).get(c) == 1) {
                    vis[r][c] = true;
                    q.offer(new int[]{r, c});
                }
            }
        }

        // Multi-source BFS
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int r = curr[0], c = curr[1];

            for (int d = 0; d < 4; d++) {
                int nr = r + dx[d];
                int nc = c + dy[d];

                if (nr < 0 || nc < 0 || nr >= n || nc >= n) continue;
                if (vis[nr][nc]) continue;

                dist[nr][nc] = dist[r][c] + 1;
                vis[nr][nc] = true;
                q.offer(new int[]{nr, nc});
            }
        }

        // Step 2: Max heap
        PriorityQueue<int[]> store = new PriorityQueue<>(
            (a, b) -> b[0] - a[0]
        );

        boolean[][] vis2 = new boolean[n][n];
        store.offer(new int[]{dist[0][0], 0, 0});

        while (!store.isEmpty()) {
            int[] curr = store.poll();
            int safeE = curr[0];
            int r = curr[1];
            int c = curr[2];

            if (vis2[r][c]) continue;
            vis2[r][c] = true;

            if (r == n - 1 && c == n - 1) return safeE;

            for (int d = 0; d < 4; d++) {
                int nr = r + dx[d];
                int nc = c + dy[d];

                if (nr < 0 || nc < 0 || nr >= n || nc >= n) continue;
                if (vis2[nr][nc]) continue;

                int newSafe = Math.min(safeE, dist[nr][nc]);
                store.offer(new int[]{newSafe, nr, nc});
            }
        }

        return 0;
    }
}
```

## Detailed Explanation
## 1️⃣  Algorithm Overview  

The solution solves the problem in **two separate phases** that are combined into one class.

| Phase | What is computed? | How is it computed? | Why is it needed? |
|------|------------------|---------------------|------------------|
| **1 – Multi‑source BFS** | `dist[r][c]` = *minimum number of moves* a thief needs to reach cell `(r,c)` (using 4‑directional moves). | • Put **every cell that contains a thief (`grid[r][c]==1`)** into a queue. <br>• Run a normal BFS, expanding to the four neighbours and writing `dist[nr][nc] = dist[r][c] + 1`. <br>• `vis` (a boolean matrix) prevents revisiting cells. | The value `dist[r][c]` is exactly the **safeness factor** of cell `(r,c)` – the larger the distance to the nearest thief, the safer the cell is. |
| **2 – “Maximum‑minimum” path search** | Find a path from `(0,0)` to `(n‑1,n‑1)` whose **minimum** `dist` value is as large as possible. | • Use a **max‑heap** (priority queue ordered by the current safeness factor). <br>• Pull the node with the **largest** currently‑available safeness factor. <br>• From a node `(r,c, safeE)` push each un‑visited neighbour `(nr,nc)` with `newSafe = min(safeE, dist[nr][nc])`. <br>• Stop as soon as the destination is popped – the popped `safeE` is the optimal answer. | This is the classic “bottleneck‑shortest‑path” problem.  By always expanding the node that currently has the **largest** guaranteed minimum, the first time we reach the goal we are guaranteed to have the maximal possible minimum. |

The two phases together give the required answer.

---

## 2️⃣  Step‑by‑Step Dry‑Run on the Sample  

**Input**  

```text
grid = [[1,0,0],
        [0,0,0],
        [0,0,1]]
n = 3
```

Thieves are at `(0,0)` and `(2,2)`.  
Goal is to go from `(0,0)` (the start) to `(2,2)` (the destination).

### 2.1 Phase 1 – Multi‑source BFS  

| Step | Queue (front→back) | Visited (`vis`) – `true` cells | `dist` values (only changed cells) | Comment |
|------|--------------------|--------------------------------|-----------------------------------|---------|
| 0 | **[(0,0) , (2,2)]** | `(0,0)=T , (2,2)=T` | all `0` (initial) | All thief cells are inserted. |
| 1 | pop `(0,0)` → neighbours `(1,0)` , `(0,1)` | mark them `T` | `dist[1][0]=1 , dist[0][1]=1` | First expansion from a thief. |
| 2 | pop `(2,2)` → neighbours `(1,2)` , `(2,1)` | mark them `T` | `dist[1][2]=1 , dist[2][1]=1` | Second expansion from the other thief. |
| 3 | pop `(1,0)` → neighbours `(2,0)` , `(1,1)` | mark them `T` | `dist[2][0]=2 , dist[1][1]=2` | Distance grows to 2 for cells two steps away. |
| 4 | pop `(0,1)` → neighbour `(0,2)` (others already visited) | mark `(0,2)=T` | `dist[0][2]=2` | Continue expanding. |
| 5 | pop `(1,2)` → neighbour `(0,2)` (already visited) – nothing new. |
| 6 | pop `(2,1)` → nothing new. |
| 7 | pop `(2,0)` → nothing new. |
| 8 | pop `(1,1)` → nothing new. |
| 9 | pop `(0,2)` → nothing new. |

All cells are now visited. The final `dist` matrix is

```
dist = [
  [0, 1, 2],
  [2, 2, 1],
  [2, 1, 0]
]
```

*Interpretation*:  
- The safest cells (largest distance) are the four “2” cells.  
- The start cell `(0,0)` (a thief) has distance `0`; the destination `(2,2)` also has distance `0`.

### 2.2 Phase 2 – Max‑Heap Path Search  

We now run a Dijkstra‑style search that always expands the node with the **largest** current safeness factor (`safeE`).  
The heap stores triples `{safeE, row, col}`; larger `safeE` comes out first.

| Iteration | Heap (top→bottom)                              | `vis2` (visited so far)   | Action taken (popped cell) | Reason for pushing neighbours |
|-----------|-----------------------------------------------|---------------------------|----------------------------|--------------------------------|
| 0 | `[(0,0,0)]` | all `F` | **pop** `(0,0,0)` <br> `safeE = 0` <br> `r=0,c=0` <br> Not the goal (`(2,2)` not reached). | Neighbours: `(1,0)` (`dist=2`) → `newSafe = min(0,2)=0` → push `(0,1,0)` <br> `(0,1)` (`dist=1`) → push `(0,0,1)` |
| 1 | Heap now contains `[(0,1,0) , (0,0,1)]` | `(0,0)=T` | **pop** `(0,1,0)` – cell `(1,0)` <br> `safeE = 0` <br> Not goal. | From `(1,0)` neighbours:<br>  `(2,0)` (`dist=2`) → push `(0,2,0)`<br>  `(1,1)` (`dist=2`) → push `(0,1,1)`<br>  `(0,0)` already visited.<br>  `(1,-1)` out of bounds. |
| 2 | Heap `[(0,0,1) , (0,2,0) , (0,1,1)]` | `(0,0),(1,0)=T` | **pop** `(0,0,1)` – cell `(0,1)` <br> `safeE = 0` | Neighbours:<br>  `(0,0)` visited.<br>  `(1,1)` (`dist=2`) → push `(0,1,1)` (already there).<br>  `(0,2)` (`dist=2`) → push `(0,0,2)`.<br>  `( -1,1)` invalid. |
| 3 | Heap `[(0,2,0) , (0,1,1) , (0,0,2)]` | `(0,0),(1,0),(0,1)=T` | **pop** `(0,2,0)` – cell `(2,0)` <br> `safeE = 0` | Neighbours:<br>  `(1,0)` visited.<br>  `(3,0)` invalid.<br>  `(2,1)` (`dist=1`) → push `(0,2,1)`.<br>  `(2,-1)` invalid. |
| 4 | Heap `[(0,1,1) , (0,0,2) , (0,2,1)]` | `(0,0),(1,0),(0,1),(2,0)=T` | **pop** `(0,1,1)` – cell `(1,1)` <br> `safeE = 0` | Neighbours of `(1,1)` (all have `dist` = 2 or 1) → all produce `newSafe = 0` and are either visited or will be pushed again (`(0,1)`, `(2,1)`, `(1,0)`, `(1,2)`). |
| 5 | Heap `[(0,0,2) , (0,2,1) , …]` | now also `(1,1)=T` | **pop** `(0,0,2)` – cell `(0,2)` <br> `safeE = 0` | Neighbours `(0,1)` visited, `(1,2)` (`dist=1`) → push `(0,1,2)`. |
| 6 | Heap `[(0,2,1) , (0,1,2) , …]` | `(0,2)=T` | **pop** `(0,2,1)` – cell `(2,1)` <br> `safeE = 0` | Neighbours `(2,0)` visited, `(2,2)` **goal** (`dist=0`) → `newSafe = min(0,0)=0` → push `(0,2,2)`. |
| 7 | Heap `[(0,2,2) , (0,1,2) , …]` | `(2,1)=T` | **pop** `(0,2,2)` – cell `(2,2)` (the destination) <br> `safeE = 0` | Because we have reached the goal, the algorithm **returns** `safeE` → **0**. |

At this point the heap is empty if we kept popping, but the moment the destination is removed from the heap the function returns.  
The returned value `0` matches the expected answer because the start cell itself is a thief (its safeness factor is 0, and no path can have a larger minimum).

> **Key observation during the dry‑run** – Every push introduces a `safeE` of **0** because the starting cell’s distance is 0, and the operation `newSafe = min(currentSafe, dist[neighbor])` never can increase the value once the current safe factor is already 0. Consequently the algorithm explores the whole grid but never discovers a larger `safeE` before the destination is popped.

---

## 3️⃣  Complexity Analysis  

### 3.1 Time Complexity  

| Phase | Work per cell / edge | Number of cells | Overall |
|-------|----------------------|-----------------|---------|
| **BFS (Phase 1)** | Each cell is enqueued **once** and each of its 4 edges is examined at most once. | `n²` cells ⇒ `O(n²)` operations. | `O(n²)` |
| **Max‑Heap path search (Phase 2)** | A cell can be **pushed** up to 4 times (once from each neighbour) but it is **popped and processed at most once** (the `vis2` guard). Each pop/push costs `O(log k)` where `k` ≤ total nodes in heap ≈ `n²`. | Up to `n²` pops, each causing up to 4 pushes. | `O(n² log n²) = O(n² log n)` |

**Total:** `O(n² + n² log n) = O(n² log n)` time.

### 3.2 Space Complexity  

| Data structure | Size |
|----------------|------|
| `dist` matrix | `n × n` integers → `O(n²)` |
| `vis` (first BFS) | `n × n` booleans → `O(n²)` |
| `vis2` (second BFS) | `n × n` booleans → `O(n²)` |
| Max‑heap | In the worst case stores all cells → `O(n²)` |

All together we need `O(n²)` auxiliary space.

---

## 4️⃣  Recap  

1. **Phase 1** builds a distance field that tells, for every cell, how many steps the nearest thief needs to reach it – this is the *safety score* of that cell.  
2. **Phase 2** runs a variant of Dijkstra that always expands the node whose current **minimum** safety along the path is the largest possible. Because the heap is ordered by this safety value, the first time the destination cell is removed from the heap we have discovered the *maximum achievable safeness factor*.  
3. On the sample `[[1,0,0],[0,0,0],[0,0,1]]` the algorithm correctly returns `0` – the start cell itself is a thief, so its safeness factor cannot be improved.  
4. The algorithm runs in **`O(n² log n)` time** and uses **`O(n²)` extra space**, which easily satisfies LeetCode’s constraints (`n ≤ 30` in the original problem, but the analysis holds for any `n`).  

Feel free to adapt the same two‑phase idea to other languages or to embed additional optimizations (e.g., early exit when the current popped `safeE` is already larger than any undiscovered cell’s `dist`).
