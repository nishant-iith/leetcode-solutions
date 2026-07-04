# Submission details for minimum-score-of-a-path-between-two-cities

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 31 ms (Better than 44.30% of submissions)
- **Memory:** 162.6 MB (Better than 52.41% of submissions)

## Solution Code
```java
class Solution {
    public int minScore(int n, int[][] roads) {

        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            int w = road[2];

            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});
        }

        boolean[] vis = new boolean[n + 1];
        Queue<Integer> q = new LinkedList<>();

        q.offer(1);
        vis[1] = true;

        int ans = Integer.MAX_VALUE;

        while (!q.isEmpty()) {

            int node = q.poll();

            for (int[] edge : adj.get(node)) {
                int nei = edge[0];
                int wt = edge[1];

                ans = Math.min(ans, wt);

                if (!vis[nei]) {
                    vis[nei] = true;
                    q.offer(nei);
                }
            }
        }

        return ans;
    }
}
```

## Detailed Explanation
### Algorithm Analysis

**Strategy Overview:**

The problem requires finding the **minimum score** of a path between city 1 and city n, where the score is defined as the **minimum edge weight** along that path. The key insight is that the minimum possible score is the **smallest edge weight in the connected component** containing both city 1 and city n. 

**Approach:**
1. **Graph Representation:** Use an adjacency list to represent the bidirectional roads.
2. **Breadth-First Search (BFS):** Start BFS from city 1 to traverse all reachable nodes (since the graph is connected between 1 and n, this covers the entire component).
3. **Track Minimum Edge:** During traversal, keep track of the smallest edge weight encountered. This is because any path from 1 to n must pass through edges in this component, and the smallest edge in the component will be the minimum possible score.

---

### Step-by-Step Walkthrough (Dry Run)

**Sample Input:**
```
n = 4
roads = [[1,2,9],[2,3,6],[2,4,5],[1,4,7]]
```

**Adjacency List Construction:**
- **Node 1:** `(2,9), (4,7)`
- **Node 2:** `(1,9), (3,6), (4,5)`
- **Node 3:** `(2,6)`
- **Node 4:** `(1,7), (2,5)`

**BFS Execution:**
1. **Initialization:**
   - `vis = [F, F, F, F, F]` (size 5, index 0 unused).
   - Queue starts with `[1]`, `vis[1] = T`.
   - `ans = Integer.MAX_VALUE (∞)`.

2. **Dequeue Node 1:**
   - **Edges to Process:**
     - **Edge to 2 (weight 9):**
       - Update `ans = min(∞, 9) = 9`.
       - Mark `vis[2] = T`, enqueue 2.
     - **Edge to 4 (weight 7):**
       - Update `ans = min(9, 7) = 7`.
       - Mark `vis[4] = T`, enqueue 4.
   - **Queue:** `[2, 4]`.

3. **Dequeue Node 2:**
   - **Edges to Process:**
     - **Edge to 1 (visited, skip).**
     - **Edge to 3 (weight 6):**
       - Update `ans = min(7, 6) = 6`.
       - Mark `vis[3] = T`, enqueue 3.
     - **Edge to 4 (weight 5):**
       - Update `ans = min(6, 5) = 5`.
       - Node 4 is already visited.
   - **Queue:** `[4, 3]`.

4. **Dequeue Node 4:**
   - **Edges to Process:**
     - **Edges to 1 and 2 (both visited, skip).**
   - **Queue:** `[3]`.

5. **Dequeue Node 3:**
   - **Edges to Process:**
     - **Edge to 2 (visited, skip).**
   - **Queue:** Empty.

**Final Result:** `ans = 5` (correct, as the smallest edge in the component).

---

### Complexity Analysis

**Time Complexity:**  
- **O(n + m)**, where `n` is the number of nodes and `m` is the number of roads (edges).
  - BFS traverses each node once (O(n)) and each edge twice (once for each direction, O(m)).
  - The adjacency list construction takes O(m) time.

**Space Complexity:**  
- **O(n + m)**:
  - **Adjacency List:** Stores all edges in two directions, requiring O(m) space per node, totaling O(n + m).
  - **Visited Array:** O(n).
  - **Queue:** In the worst case (e.g., a linear chain), it stores O(n) nodes.

---

### Summary Table: Dry Run States

| Step | Node Dequeued | Edges Processed          | Updated `ans` | Queue State       |
|------|-----------------|--------------------------|---------------|-------------------|
| 1    | 1               | 9 (to 2), 7 (to 4)       | 7             | [2,4]             |
| 2    | 2               | 6 (to 3), 5 (to 4)       | 5             | [4,3]             |
| 3    | 4               | None (all visited)       | 5             | [3]               |
| 4    | 3               | None (already visited)   | 5             | []                |

---

### Key Observations:
- The BFS ensures all nodes in the connected component are explored.
- The minimum edge weight encountered during traversal is the smallest possible score for any path between 1 and n.
- The approach guarantees correctness because the problem constraints ensure 1 and n are in the same connected component.
