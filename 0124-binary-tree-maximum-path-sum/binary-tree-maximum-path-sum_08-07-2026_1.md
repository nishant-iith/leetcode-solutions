# Submission details for binary-tree-maximum-path-sum

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 32.33% of submissions)
- **Memory:** 46.7 MB (Better than 37.66% of submissions)

## Solution Code
```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {

    int maxSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }

    private int maxGain(TreeNode node) {

        if(node == null){
            return 0;
        }

        int left = Math.max(0, maxGain(node.left));
        int right = Math.max(0, maxGain(node.right));

        int currentPath = node.val + left + right;
        maxSum = Math.max(maxSum, currentPath);

        return node.val + Math.max(left, right);
    }
}
```

## Detailed Explanation


### 1. **Algorithm Analysis**

The solution uses a **post-order traversal** approach to compute the maximum path sum in a binary tree. The key idea is to recursively calculate the maximum contribution a subtree rooted at a node can provide to a path (either left or right, but not both), while simultaneously updating a global maximum (`maxSum`) that considers paths passing through the current node and both subtrees.

#### **Key Steps:**
- **Recursive Function (`maxGain`)**:
  - For each node, compute the maximum gain from the left and right subtrees. If a subtree's gain is negative, it is ignored (set to 0) because including it would reduce the total sum.
  - Calculate the **current path sum** as the node's value plus the left and right gains. This represents a path that goes through the current node and both subtrees. Update `maxSum` if this path is larger.
  - Return the **maximum path that can extend upwards** (either left or right, not both) to parent nodes. This is `node.val + max(left, right)`.

- **Global Tracking (`maxSum`)**:
  - Tracks the maximum path sum encountered during traversal. The path considered here can start and end anywhere in the tree, not necessarily through the root.

#### **Why This Works:**
- By computing the maximum left/right gain at each node, the algorithm ensures that the path through the current node is optimally considered (including both subtrees if beneficial).
- The post-order traversal guarantees that all child nodes are processed before their parent, allowing correct accumulation of path sums.

---

### 2. **Step-by-Step Walkthrough (DRY Run)**

**Test Case:** `[1,2,3]` (root = 1, left = 2, right = 3)

| Step | Node | Left Gain | Right Gain | Current Path | maxSum | Return Value |
|------|------|-----------|------------|--------------|--------|--------------|
| 1    | 2 (left child of 1) | 0 (no children) | 0 | 2 + 0 + 0 = 2 | 2 | 2 (max(2,0) = 2) |
| 2    | 3 (right child of 1) | 0 | 0 | 3 + 0 + 0 = 3 | 3 | 3 |
| 3    | 1 (root) | 2 (from left subtree) | 3 (from right subtree) | 1 + 2 + 3 = 6 | 6 | 1 + max(2,3) = 4 |

**Final `maxSum` = 6**, which is the correct answer (path 2 → 1 → 3).

---

### 3. **Complexity Analysis**

#### **Time Complexity:**  
**O(n)**  
- **Reasoning:** Each node is visited exactly once during the post-order traversal. The operations at each node (comparisons, additions) are constant time.

#### **Space Complexity:**  
**O(h)**  
- **Reasoning:** The space is determined by the recursion stack, where `h` is the height of the tree. In the worst case (skewed tree), `h = n`, leading to O(n). For a balanced tree, `h = log n`.

---

### Summary
The algorithm efficiently computes the maximum path sum by leveraging post-order traversal and dynamic updates to a global maximum. It ensures optimal substructure (left/right subtree contributions) and overlapping subproblems (each node's contribution is computed once). The time and space complexities are optimal for this problem.

