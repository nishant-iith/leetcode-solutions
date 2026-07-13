# Submission details for binary-search-tree-iterator

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 17 ms (Better than 95.45% of submissions)
- **Memory:** 50.1 MB (Better than 60.63% of submissions)

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

public class BSTIterator {
    
    private Stack<TreeNode> stack;
    public BSTIterator(TreeNode root) {
        stack = new Stack<>();
        TreeNode cur = root;
        while(cur != null){
            stack.push(cur);
            if(cur.left != null)
                cur = cur.left;
            else
                break;
        }
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }

    public int next() {
        TreeNode node = stack.pop();
        TreeNode cur = node;
        if(cur.right != null){
            cur = cur.right;
            while(cur != null){
                stack.push(cur);
                if(cur.left != null)
                    cur = cur.left;
                else
                    break;
            }
        }
        return node.val;
    }
}

/**
 * Your BSTIterator object will be instantiated and called as such:
 * BSTIterator obj = new BSTIterator(root);
 * int param_1 = obj.next();
 * boolean param_2 = obj.hasNext();
 */
```

## Detailed Explanation
# Binary Search Tree Iterator - Algorithm Analysis

## 1. Algorithm Analysis

This solution implements an iterative in-order traversal of a Binary Search Tree (BST) using an explicit stack. The key insight is that in-order traversal of a BST visits nodes in ascending order.

### Strategy:
- Use a **stack** to simulate the recursive call stack that would normally be used for depth-first traversal
- In the **constructor**, preload the stack with all left descendants of the root (going as far left as possible)
- In **next()**, pop the top element, then push all left descendants of its right subtree
- This ensures we always get the next smallest element in BST order

### Key Insight:
In-order traversal follows the pattern: Left → Root → Right
The stack maintains the "path" to the next node to visit, always keeping track of nodes whose left subtrees have been fully processed.

## 2. Step-by-Step Walkthrough (DRY Run)

Let's trace through the sample test case with tree `[7,3,15,null,null,9,20]`:

```
        7
       / \
      3   15
         /  \
        9   20
```

### Initial Setup (Constructor):
Starting with `root = 7`

| Step | Action | Stack State (top→bottom) | Current Node |
|------|--------|---------------------------|--------------|
| 1 | Push 7, go left | [7] | 3 |
| 2 | Push 3, no left child | [7,3] | null |

**Constructor completes**: Stack = [7,3]

### Iteration 1: `next()`
| Step | Action | Stack State | Returned Value |
|------|--------|-------------|----------------|
| 1 | Pop 3 | [7] | 3 |
| 2 | 3 has no right child | [7] | Return 3 |

**Result**: Returns 3

### Iteration 2: `next()`
| Step | Action | Stack State | Returned Value |
|------|--------|-------------|----------------|
| 1 | Pop 7 | [] | 7 |
| 2 | 7 has right child (15) | [15] | |
| 3 | Go left from 15 (no left child) | [15] | |
| 4 | | [15] | Return 7 |

**Result**: Returns 7

### Iteration 3: `hasNext()`
Stack is not empty, so return `true`

### Iteration 4: `next()`
| Step | Action | Stack State | Returned Value |
|------|--------|-------------|----------------|
| 1 | Pop 15 | [] | 15 |
| 2 | 15 has right child (20) | [20] | |
| 3 | Go left from 20 (no left child) | [20] | |
| 4 | | [20] | Return 15 |

**Result**: Returns 15

### Iteration 5: `hasNext()`
Stack is not empty, so return `true`

### Iteration 6: `next()`
| Step | Action | Stack State | Returned Value |
|------|--------|-------------|----------------|
| 1 | Pop 20 | [] | 20 |
| 2 | 20 has no right child | [] | |
| 3 | | [] | Return 20 |

**Result**: Returns 20

### Iteration 7: `hasNext()`
Stack is empty, so return `false`

## 3. Complexity Analysis

### Time Complexity: O(1) amortized per operation

**Analysis**:
- Each node is pushed onto the stack exactly once and popped exactly once
- Over n operations, total work is O(n)
- Therefore, each `next()` and `hasNext()` operation takes O(1) amortized time
- The apparent O(h) cost of pushing left descendants is amortized over subsequent pops

### Space Complexity: O(h) where h is the height of the tree

**Analysis**:
- The stack stores at most one path from root to leaf
- In worst case (skewed tree): O(n)
- In balanced tree: O(log n)
- Generally expressed as O(h) where h is tree height

**Justification**:
- We only store nodes along a single path from root to current position
- Maximum stack size equals the maximum depth of any node from root
- This is fundamentally limited by the height of the BST

## Summary Table

| Operation | Time Complexity | Space Complexity |
|-------------|----------------|------------------|
| Constructor | O(h) | O(h) |
| hasNext() | O(1) | O(1) |
| next() | O(1) amortized | O(1) |
| **Overall** | **O(1) amortized** | **O(h)** |

Where h = height of the tree, making this an efficient iterator for BST applications requiring sorted access.
