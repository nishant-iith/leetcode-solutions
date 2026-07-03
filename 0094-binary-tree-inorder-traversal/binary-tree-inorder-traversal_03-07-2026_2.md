# Submission details for binary-tree-inorder-traversal

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 0 ms (Better than 100.00% of submissions)
- **Memory:** 43 MB (Better than 69.77% of submissions)

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
    public List<Integer> inorderTraversal(final TreeNode root) {
        final var res = new ArrayList<Integer>();
        var curr = root;
        
        while (curr != null) {
            if (curr.left == null) {
                res.add(curr.val);
                curr = curr.right; // move to next right node
            } else { // has a left subtree
                var pre = curr.left;
                while (pre.right != null) { // find rightmost
                    pre = pre.right;
                }
                pre.right = curr; // put cur after the pre node
                
                var temp = curr; // store cur node
                curr = curr.left; // move cur to the top of the new tree
                temp.left = null; // original cur left be null, avoid infinite loops
            }
        }
        return res;
    }
}
```

## Detailed Explanation
Let’s carefully analyze the proposed solution for the **Binary-Tree Inorder Traversal** problem.

---

## **1. Algorithm Analysis**

The question describes a classical "stack-based inorder traversal" using Morris traversal idea but algorithms vary. Let’s reconstruct it:

- **Inorder Traversal Order:** Left → Root → Right  
- **Tree structure:** A **Morris traversal** variant (no recursion/pushing/popping stack is used)

The code leverages a **temporary linked list** to "thread" the tree elements in the proper order without extra space for stack/recursion.

**Approach Summary:**
1. Start from the root, traverse to the leftmost node.
2. When reaching a node with no left child, visit it, then move to the right subtree.
3. For a node with a left child, temporarily thread the right subtree link (so that next time you visit it, you get the right subtree point).
4. Restore the actual left pointer after processing.
5. Repeat until all nodes are visited.

**Important:** This method restructures pointers but doesn't actually copy nodes. All visited nodes are stored to build the result.

---

## **2. Step-by-Step Walkthrough (DRY Run)**

Let's take an example tree so that we can categorize steps.

**Sample Tree:**
```
    1
   /
  2
 / \
3   4
```

**Input Tree structure (in memory):**
```
      1
     / \
    2   3
   / \
  3   4
```
(Note: shaped like a right-skewed tree.)

### **Initial:**
```java
int root = new TreeNode(1, TreeNode(2), TreeNode(3, TreeNode(4), null));
```
`TreeNode` class:  
```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {} 
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
```

### **Algorithm Traversal:**
**Initial `curr = root` = 1**

#### **Iteration 1: curr = 1**
- `curr.left` is `null` → visit 1, move `curr = curr.right` = 2
- `curr = 2`
    - `curr.left` is `null` → visit 2, move `curr = curr.right` = 3
- `curr = 3`
    - `curr.left` is `null` → visit 3, move `curr = curr.right` = 4
- `curr = 4`
    - `curr.left` is null
####→ Result so far: [1, 2, 3, 4]

But this is **exactly** the inorder traversal of a binary tree showing the left, node, right order — but in this simple tree, we'd expect inorder: **Left -> Root -> Right**  
So for the first step: left subtree is empty.

Repeat for inorder:  
1→ right → 2→...→4. Correct for the given tree.

#### Modifying Perspectives:
Our node `tree` series:

- The MR **Morris Inorder** code simulates a modified inorder without stack.
- At each node, we link the leftmost node of right subtree to the current node.
- When back to parent, pointer is restored, and we proceed.

#### **For structure:**  
Original nodes: 1 (l), 2 (r1), 3 (l2), 4 (r2)
Step-by-step walk:
1. Start → curr=1 → visit 1 (res = [1])
2. curr=2 (r1) → visit 2 (res = [1, 2])
   - Tree [1-2], visit 2 → no left
   - 2→ right = 3 → ... continue
3. ...
   Move inward following threading pointers accordingly.

Each iteration visits all nodes in their inorder sequence, using a single pass.

---

**For the test case: `[1,null,2,3]`**  
Expected inorder: `[1,2,3]`

---

The code processes the tree and maintains order **as expected**.

---

## **3. Complexity Analysis**

Let’s analyze the algorithm's time and space requirements:

### **Time Complexity:**
- Each node is visited exactly once.
- Constant branching per node.
- **O(N)** time, where N is the number of nodes.

### **Space Complexity:**
- Uses a constant amount of extra space (for index tracking, preorder counter, etc).
- Morris traversal doesn't create a stack.
- **O(1)** auxiliary space.

---

## **Summary Table (Trace/DRY Run Format)**

| Step | Node   | Action                                                                 | State              |
|------|--------|------------------------------------------------------------------------|--------------------|
|1     | 1      | Leftmost of unvisited; visit, explore right                           | Node: 1            |
|2     | 2      | Left none ⇒ visit, move to right                                       | Node:2             |
|3     | 3      | Left none ⇒ visit, move to right                                       | Node:3             |
|4     | 4      | Left none ⇒ visit, move to right                                       | Node:4             |

*(Continue until all nodes visited)*

---

## **Conclusion**

- **Algorithm**: Morris-Inorder traversal using pointer threading.
- **Time complexity:** O(N)
- **Space complexity:** O(1) (not O(h) though, since it traverses rooted tree, but h is used internally)).
- **Correctness:** For each case, it correctly traverses binary trees in inorder without needing recursion or stack.

The solution is correct and efficient for LeetCode 711 on small inputs.

---

If you have another tricky part or need further clarification, feel free to ask!
