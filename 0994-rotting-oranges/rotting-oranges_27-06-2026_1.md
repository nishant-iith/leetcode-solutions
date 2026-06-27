# Submission details for rotting-oranges

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 2 ms (Better than 90.73% of submissions)
- **Memory:** 44 MB (Better than 71.69% of submissions)

## Solution Code
```java
class Solution {
    class Pair{
        int row,col,time;
        Pair(int r, int c, int t){
            row = r;
            col = c;
            time = t;
        }
    }

    public int orangesRotting(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        Queue<Pair> q = new LinkedList<>();
        int freshCnt = 0;

        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                if(grid[i][j] == 2){
                    q.add(new Pair(i,j,0));
                }
                if(grid[i][j] == 1){
                    freshCnt++;
                }
            }
        }
        int dRow[] = {-1,1,0,0};
        int dCol[] = {0,0,-1,1};

        int rc =0,maxTime =0;

        while(!q.isEmpty()){
            Pair curr = q.poll();
            int row = curr.row;
            int col = curr.col;
            int time = curr.time;
            maxTime = Math.max(maxTime,time);
            for(int k=0;k<4;k++){
                int newR = row+dRow[k];
                int newC = col+dCol[k];
                if(newR >=0 && newR < n && newC >= 0 && newC < m && grid[newR]
                [newC] == 1){
                    grid[newR][newC] = 2;
                    q.add(new Pair(newR,newC,time+1));
                    rc++;
                }
            }
        }
        return rc == freshCnt ? maxTime:-1;
    }
}
```

## Detailed Explanation
##1. Algorithm Analysis

**Problem Statement Recap:**  
Given an `m x n` grid where each cell can be:
*   `0` = Empty cell
*   `1` = Fresh orange
*   `2` = Rotten orange

Every minute, any fresh orange adjacent (4-directionally) to a rotten orange becomes rotten. Return the minimum minutes until no fresh oranges remain. Return `-1` if impossible.

---

### Strategy: Multi-Source Breadth-First Search (BFS)

This solution uses **Multi-Source BFS**. Instead of starting BFS from a single source, we enqueue *all* initially rotten oranges (value `2`) simultaneously with `time = 0`. This simulates the parallel spread of rot from all starting points.

**Why BFS?**
*   BFS explores nodes layer-by-layer. The first time a fresh orange is reached corresponds to the *minimum* time required for rot to reach it.
*   Since multiple rotten oranges rot neighbors simultaneously, processing them level-by-level (using a queue) perfectly models the discrete time steps (minutes).

---

### Key Components

1.  **`Pair` Class**: Encapsulates `(row, col, time)`. `time` represents the minute this orange became rotten.
2.  **Initialization Phase**:
    *   Scan grid to count `freshCnt` (total fresh oranges).
    *   Enqueue all initial rotten oranges (`grid[i][j] == 2`) into `Queue<Pair> q` with `time = 0`.
3.  **BFS Loop**:
    *   Dequeue current rotten orange `curr`.
    *   Update `maxTime = max(maxTime, curr.time)`.
    *   Check 4 neighbors (Up, Down, Left, Right).
    *   If neighbor is valid (`in bounds`) and **Fresh (`1`)**:
        *   Mark it Rotten (`grid[newR][newC] = 2`) → **Visited marker** (prevents re-processing).
        *   Enqueue neighbor with `time = curr.time + 1`.
        *   Increment `rc` (rotted count).
4.  **Termination Check**:
    *   After BFS finishes, compare `rc` (oranges rotted during BFS) with `freshCnt` (initial fresh oranges).
    *   If equal → All fresh oranges reached. Return `maxTime`.
    *   Else → Unreachable fresh oranges exist. Return `-1`.

---

## 2. Step-by-Step Walkthrough (Dry Run)

**Sample Test Case:**
```text
Grid:
[[2, 1, 1],
 [1, 1, 0],
 [0, 1, 1]]
```
`n = 3`, `m = 3`

### Initialization Scan
| i | j | Val | Action | `freshCnt` | Queue State (row, col, time) |
|---|---|-----|--------|------------|------------------------------|
| 0 | 0 | 2   | Enqueue | 0          | `[(0,0,0)]`                  |
| 0 | 1 | 1   | Count   | 1          |                              |
| 0 | 2 | 1   | Count   | 2          |                              |
| 1 | 0 | 1   | Count   | 3          |                              |
| 1 | 1 | 1   | Count   | 4          |                              |
| 1 | 2 | 0   | Skip    | 4          |                              |
| 2 | 0 | 0   | Skip    | 4          |                              |
| 2 | 1 | 1   | Count   | 5          |                              |
| 2 | 2 | 1   | Count   | 6          |                              |

**Initial State:**
*   `freshCnt = 6`
*   `rc = 0`
*   `maxTime = 0`
*   `Queue = [(0,0,0)]`
*   `Grid` unchanged initially.

---

### BFS Execution

**Directions:** `dRow = [-1, 1, 0, 0]`, `dCol = [0, 0, -1, 1]` (Up, Down, Left, Right)

#### **Iteration 1: Process (0, 0, 0)**
*   **Dequeue:** `curr = (0,0,0)`. `maxTime = max(0,0) = 0`.
*   **Neighbors:**
    *   **Up (-1,0):** Invalid (Row -1).
    *   **Down (1,0):** Valid. `grid[1][0] == 1` (Fresh).
        *   Mark `grid[1][0] = 2`.
        *   Enqueue `(1,0,1)`. `rc = 1`.
    *   **Left (0,-1):** Invalid (Col -1).
    *   **Right (0,1):** Valid. `grid[0][1] == 1` (Fresh).
        *   Mark `grid[0][1] = 2`.
        *   Enqueue `(0,1,1)`. `rc = 2`.
*   **Queue:** `[(1,0,1), (0,1,1)]`
*   **Grid State:**
    ```text
    [[2, 2, 1],
     [2, 1, 0],
     [0, 1, 1]]
    ```

#### **Iteration 2: Process (1, 0, 1)**
*   **Dequeue:** `curr = (1,0,1)`. `maxTime = max(0,1) = 1`.
*   **Neighbors:**
    *   **Up (0,0):** `grid[0][0] == 2` (Rotten). Skip.
    *   **Down (2,0):** `grid[2][0] == 0` (Empty). Skip.
    *   **Left (1,-1):** Invalid.
    *   **Right (1,1):** Valid. `grid[1][1] == 1` (Fresh).
        *   Mark `grid[1][1] = 2`.
        *   Enqueue `(1,1,2)`. `rc = 3`.
*   **Queue:** `[(0,1,1), (1,1,2)]`
*   **Grid State:**
    ```text
    [[2, 2, 1],
     [2, 2, 0],
     [0, 1, 1]]
    ```

#### **Iteration 3: Process (0, 1, 1)**
*   **Dequeue:** `curr = (0,1,1)`. `maxTime = max(1,1) = 1`.
*   **Neighbors:**
    *   **Up (-1,1):** Invalid.
    *   **Down (1,1):** `grid[1][1] == 2` (Just rotted). Skip.
    *   **Left (0,0):** `grid[0][0] == 2`. Skip.
    *   **Right (0,2):** Valid. `grid[0][2] == 1` (Fresh).
        *   Mark `grid[0][2] = 2`.
        *   Enqueue `(0,2,2)`. `rc = 4`.
*   **Queue:** `[(1,1,2), (0,2,2)]`
*   **Grid State:**
    ```text
    [[2, 2, 2],
     [2, 2, 0],
     [0, 1, 1]]
    ```

#### **Iteration 4: Process (1, 1, 2)**
*   **Dequeue:** `curr = (1,1,2)`. `maxTime = max(1,2) = 2`.
*   **Neighbors:**
    *   **Up (0,1):** `grid[0][1] == 2`. Skip.
    *   **Down (2,1):** Valid. `grid[2][1] == 1` (Fresh).
        *   Mark `grid[2][1] = 2`.
        *   Enqueue `(2,1,3)`. `rc = 5`.
    *   **Left (1,0):** `grid[1][0] == 2`. Skip.
    *   **Right (1,2):** `grid[1][2] == 0`. Skip.
*   **Queue:** `[(0,2,2), (2,1,3)]`
*   **Grid State:**
    ```text
    [[2, 2, 2],
     [2, 2, 0],
     [0, 2, 1]]
    ```

#### **Iteration 5: Process (0, 2, 2)**
*   **Dequeue:** `curr = (0,2,2)`. `maxTime = max(2,2) = 2`.
*   **Neighbors:**
    *   **Up (-1,2):** Invalid.
    *   **Down (1,2):** `grid[1][2] == 0`. Skip.
    *   **Left (0,1):** `grid[0][1] == 2`. Skip.
    *   **Right (0,3):** Invalid.
*   **Queue:** `[(2,1,3)]`
*   **Grid State:** Unchanged.

#### **Iteration 6: Process (2, 1, 3)**
*   **Dequeue:** `curr = (2,1,3)`. `maxTime = max(2,3) = 3`.
*   **Neighbors:**
    *   **Up (1,1):** `grid[1][1] == 2`. Skip.
    *   **Down (3,1):** Invalid.
    *   **Left (2,0):** `grid[2][0] == 0`. Skip.
    *   **Right (2,2):** Valid. `grid[2][2] == 1` (Fresh).
        *   Mark `grid[2][2] = 2`.
        *   Enqueue `(2,2,4)`. `rc = 6`.
*   **Queue:** `[(2,2,4)]`
*   **Grid State:**
    ```text
    [[2, 2, 2],
     [2, 2, 0],
     [0, 2, 2]]
    ```

#### **Iteration 7: Process (2, 2, 4)**
*   **Dequeue:** `curr = (2,2,4)`. `maxTime = max(3,4) = 4`.
*   **Neighbors:** All invalid, empty, or rotten. No new additions.
*   **Queue:** `[]` (Empty)

---

### Final Check
*   Loop terminates.
*   `rc = 6`
*   `freshCnt = 6`
*   `rc == freshCnt` → **True**
*   **Return `maxTime = 4`.**

**Matches Expected Output: 4**

---

## 3. Complexity Analysis

### Time Complexity: **O(N * M)**
*   **Initialization Scan:** We iterate every cell once → `O(N * M)`.
*   **BFS Loop:** Each cell is enqueued and dequeued **at most once**.
    *   A cell is only enqueued if it is Fresh (`1`) and becomes Rotten (`2`).
    *   Since we immediately mark it `2` upon enqueuing, it is never enqueued again.
    *   Processing a cell involves checking 4 neighbors → `O(1)` work per cell.
    *   Total BFS work → `O(N * M)`.
*   **Total:** `O(N * M) + O(N * M) = O(N * M)`.

### Space Complexity: **O(N * M)**
*   **Queue:** In the worst case (e.g., grid full of rotten oranges initially, or the "wavefront" spans the diagonal), the queue can hold up to `O(N * M)` elements.
*   **Pair Objects:** Each element in queue is a `Pair` object (3 ints) → `O(N * M)` space.
*   **Grid Modification:** Done in-place (input array used as visited array). No extra `visited` matrix allocated.
*   **Auxiliary Variables:** `O(1)`.

**Note:** If modifying input is prohibited, a `boolean[][] visited` array would be needed, keeping Space Complexity at `O(N * M)`.
