# Submission details for binary-tree-postorder-traversal

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 13.22% of submissions)
- **Memory:** 43.3 MB (Better than 33.31% of submissions)

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
    public List<Integer> postorderTraversal(TreeNode root) {
        // Create an array list to store the solution result...
        List<Integer> sol = new ArrayList<Integer>();
        // Return the solution answer if the tree is empty...
        if(root==null) return sol;
        // Create an empty stack and push the root node...
        Stack<TreeNode> bag = new Stack<TreeNode>();
        bag.push(root);
        // Loop till stack is empty...
        while(!bag.isEmpty()) {
            // set peek a node from the stack...
            TreeNode node = bag.peek();
            // If the subtrees of that node are null, then pop & store the pop value into solution result...
            if(node.left==null && node.right==null) {
                TreeNode pop = bag.pop();
                sol.add(pop.val);
            }
            else {
                // Push the right child of the popped node into the stack...
                if(node.right!=null) {
                    bag.push(node.right);
                    node.right = null;
                }
                // Push the left child of the popped node into the stack...
                if(node.left!=null) {
                    bag.push(node.left);
                    node.left = null;
                }
            }
        }
        return sol;     // Return the solution list...
    }
}
```

## Detailed Explanation
## 1.  Algorithm Analysis  

The class implements a **iterative post‑order traversal** of a binary tree using only a single explicit stack (`java.util.Stack`).  
The high‑level idea is:

1. **Push the root** onto the stack.  
2. While the stack is not empty, look at the node on the top (`peek`).  
3. If the node has **no children left that are still un‑visited** (i.e. both `left` and `right` are `null`), the node is ready to be added to the answer because all of its descendants have already been processed. We `pop` it and record its value.  
4. Otherwise the node still has at least one child that has not been processed.  
   * We **push the children onto the stack** in the order *right first, then left*.  
   * Immediately after pushing a child we **null‑out the corresponding reference** (`node.right = null; node.left = null;`).  
   * Null‑ing the reference guarantees that the child will never be considered again as a “still‑un‑visited” subtree of its parent; it also prevents the parent from being pushed onto the stack via a mistaken recursive call.

Because we always push children **right before left**, the next node that will be popped is the **leftmost leaf**, then its ancestors are processed only after both sub‑trees have been popped – exactly the order required by *post‑order*: **left → right → root**.

The algorithm never uses recursion, so the call‑stack is replaced by the data‑structure `Stack<TreeNode>`.  
When the stack becomes empty we have visited every node exactly once, and `sol` holds the values in post‑order.

---

## 2.  Step‑by‑Step Dry‑Run  

We will trace the algorithm on the sample input ` [1,null,2,3] `.

```
        1
       / \
     null 2
          / \
         3   null
```

In level‑order notation this is:

- root = `1`  
- `root.left = null`  
- `root.right = 2`  
- `2.left = 3`  
- `2.right = null`  
- `3` is a leaf.

### Data structures before the loop

| Variable | Value / Type |
|----------|--------------|
| `sol`    | `[]` (empty `ArrayList<Integer>`) |
| `bag`    | `[1]` (contains the root node) |
| `root`   | reference to node `1` |

---

### Iteration 1  

- `bag.isEmpty()` → `false` → enter loop.  
- `node = bag.peek()` → `node` points to the `TreeNode(1)`.  
- Check `node.left == null && node.right == null` → `true && false` → **false**. (Only both children must be null to pop; here the right child exists.)  
- **Enter the else‑branch**:  

  1. `node.right != null` → `true` → `bag.push(node.right)` → push node `2`.  
     Stack becomes `[1, 2]`.  
     Then set `node.right = null` → node `1.right` becomes `null`.  
  2. `node.left != null` → `false` (already null) → nothing pushed / set.  

- After the block `node` is still the original object `1`, but now its `right` reference is cleared.

**State after iteration 1**

| Variable | Value |
|----------|-------|
| `bag`    | `[1, 2]` (top = 2) |
| `node`   | *still refers to* node `1` (its `right` field is now `null`) |
| `sol`    | `[]` |

---

### Iteration 2  

- `bag.isEmpty()` → `false`.  
- `node = bag.peek()` → now points to **node 2** (top of stack).  
- Evaluate `node.left == null && node.right == null` → current node `2` has `left = 3`, `right = null`. So condition is **false**.  
- **Enter else‑branch** again:  

  1. `node.right != null` → `false` (right is `null`).  
  2. `node.left != null` → `true` (left child is node 3).  

     * Push `node.left` (node 3) onto the stack → `bag` becomes `[1, 2, 3]`.  
     * Null‑out the reference `node.left` → node 2.left becomes `null`.  

- End of block.

**State after iteration 2**

| Variable | Value |
|----------|-------|
| `bag`    | `[1, 2, 3]` (top = 3) |
| `node`   | still refers to node 2 (its `left` field now `null`) |
| `sol`    | `[]` |

---

### Iteration 3  

- `bag.isEmpty()` → `false`.  
- `node = bag.peek()` → points to **node 3** (a leaf).  
- Check `node.left == null && node.right == null` → both children are `null`, so **true**.  

  1. Pop the node: `pop = bag.pop()` → removes node 3 from the stack, stack becomes `[1, 2]`.  
  2. `sol.add(pop.val)` → `sol` gets `3`.  

- Loop continues because stack is not empty.

**State after iteration 3**

| Variable | Value |
|----------|-------|
| `bag`    | `[1, 2]` |
| `sol`    | `[3]` |
| `node`   | not used (will be overwritten next iteration) |

---

### Iteration 4  

- Stack not empty → loop again.  
- `node = bag.peek()` → now points to **node 2** (still on top).  
- Evaluate `node.left == null && node.right == null`: both children are `null` now (we cleared them earlier), **true**.  

  1. `pop = bag.pop()` → removes node 2, stack becomes `[1]`.  
  2. `sol.add(pop.val)` → `sol` becomes `[3, 2]`.  

- Continue.

**State after iteration 4**

| Variable | Value |
|----------|-------|
| `bag`    | `[1]` |
| `sol`    | `[3, 2]` |

---

### Iteration 5  

- Stack not empty → loop.  
- `node = bag.peek()` → points to **node 1** (the root).  
- Both of its children have been cleared: `node.left == null`, `node.right == null`. Condition is **true**.  

  1. `pop = bag.pop()` → removes node 1, stack becomes `[]`.  
  2. `sol.add(pop.val)` → `sol` becomes `[3, 2, 1]`.  

- Now `bag.isEmpty()` → `true`; loop exits.

**Final result**

```java
return sol; // [3, 2, 1]
```

The output matches the expected post‑order traversal: left‑subtree (`3`), right‑subtree (`2`), then root (`1`).

---

## 3.  Complexity Analysis  

### Time Complexity  

- **Each tree node is pushed onto the stack exactly once** (the push occurs when its parent decides to explore it).  
- **Each node is popped exactly once** (when all its children have been processed).  
- All operations inside the loop (`peek`, `push`, `pop`, `null` checks, `add`) are **O(1)**.  

Therefore the total work is proportional to the number of nodes `n`:

\[
\boxed{O(n)}
\]

### Space Complexity  

- The **stack** holds at most the nodes on the current root‑to‑leaf path that have been reached but not yet finished.  
- In the worst case (a completely skewed tree that is actually a linked list), the depth of recursion equals `n`, and the stack will contain `n` nodes at the deepest moment.  
- The **output list** `sol` also stores `n` integers, but it is required output, not auxiliary extra space.  

Hence auxiliary (excluding output) space is bounded by the maximum depth of the tree, i.e.:

\[
\boxed{O(h)} \quad\text{where } h \le n
\]

If we count the output list as part of space usage, the total space is `O(n)` because we must store all `n` values.  

**Summary**

| Metric | Complexity | Reason |
|--------|------------|--------|
| Time   | **O(n)**   | One constant‑time operation per node (push/pop) |
| Space  | **O(h)** auxiliary (`h` = tree height) → worst‑case **O(n)** | Stack holds at most height many nodes; output list stores `n` integers. |

The algorithm is optimal for a single‑pass iterative traversal of a binary tree. It also avoids recursion and therefore prevents stack‑overflow on deep trees (though the explicit `Stack` can still overflow for extremely deep trees, the same limitation exists for recursion).
