# Submission details for add-two-numbers

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 100.00% of submissions)
- **Memory:** 46.3 MB (Better than 75.99% of submissions)

## Solution Code
```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int digit1 = (l1 != null) ? l1.val : 0;
            int digit2 = (l2 != null) ? l2.val : 0;

            int sum = digit1 + digit2 + carry;
            int digit = sum % 10;
            carry = sum / 10;

            ListNode newNode = new ListNode(digit);
            tail.next = newNode;
            tail = tail.next;

            l1 = (l1 != null) ? l1.next : null;
            l2 = (l2 != null) ? l2.next : null;
        }

        ListNode result = dummyHead.next;
        dummyHead.next = null;
        return result;
    }
}
```

## Detailed Explanation
## 1️⃣ Algorithm Analysis  

| Aspect | Description |
|--------|-------------|
| **Problem** | The two input singly‑linked lists represent non‑negative integers in **reverse order** (least‑significant digit first). We must return a new linked list that represents the sum of those integers, also in reverse order. |
| **Core Idea** | Add the two numbers **digit by digit** starting from the heads (the least‑significant digits). Keep a **carry** (0 or 1) that propagates to the next more‑significant position. Because the lists may have different lengths, treat a missing digit as 0. Continue looping while at least one list still has nodes **or** there is a non‑zero carry. |
| **Data Structures** | - `ListNode` nodes for the result (created on the fly). <br> - `dummyHead` – a sentinel node that simplifies edge‑case handling (no need to treat the first node specially). <br> - `tail` – a pointer to the last node of the result list, used to append new nodes in O(1). |
| **Control Flow** | ```java\nwhile (l1 != null || l2 != null || carry != 0) { … }\n``` <br> The loop body extracts the current digits (`digit1`, `digit2`), adds them together with the carry, computes the new digit (`sum % 10`) and the new carry (`sum / 10`). A new `ListNode` holding `digit` is linked after `tail`, and `tail` is moved forward. Finally the list pointers (`l1`, `l2`) are advanced if possible. |
| **Why it works** | - **Reverse order** means the first node we process is the units place, then tens, hundreds, … – exactly the order required for elementary addition. <br> - The loop condition guarantees we process **all** digits and any leftover carry (e.g., 999 + 1 → 1000). <br> - Using a dummy head avoids special‑casing the first node; `tail.next = newNode` always appends to the growing list. |
| **Key Steps** | 1. Initialise `dummyHead` (value 0) and `tail` pointing to it. <br> 2. Initialise `carry = 0`. <br> 3. Loop while any operand or carry remains. <br> 4. Read current digits (0 if the corresponding list is exhausted). <br> 5. `sum = digit1 + digit2 + carry`. <br> 6. `digit = sum % 10` → node value. <br> 7. `carry = sum / 10` (0 or 1, because max sum = 9+9+1 = 19). <br> 8. Append a new node with `digit` to the result list. <br> 9. Advance `l1`/`l2` if they are not null. <br> 10. After the loop, `dummyHead.next` is the real head; detach the dummy and return it. |

---

## 2️⃣ Step‑by‑Step Dry Run (Sample Test)

**Input lists**

| List | Node values (from head) | Number represented (reverse) |
|------|------------------------|------------------------------|
| `l1` | 2 → 4 → 3             | 342 |
| `l2` | 5 → 6 → 4             | 465 |

**Expected result** → 342 + 465 = 807 → list: **7 → 0 → 8** (reverse order).

We will trace the variables inside the `while` loop.

| Iteration | `l1` (node) | `l2` (node) | `carry` (before) | `digit1` | `digit2` | `sum` | `digit` (new node) | `carry` (after) | `tail` (after linking) | Remarks |
|-----------|-------------|-------------|------------------|----------|----------|-------|--------------------|-----------------|------------------------|---------|
| **0** (initial) | `null` (not entered yet) | `null` | 0 | – | – | – | – | – | `dummyHead → 0`<br>`tail → dummyHead` | Setup |
| **1** | points to node **2** | points to node **5** | 0 | 2 | 5 | 2 + 5 + 0 = **7** | 7 % 10 = **7** | 7 / 10 = **0** | `newNode(7)` → `tail.next` → `dummyHead → 0 → 7`<br>`tail → node(7)` | First digit (units) |
| **2** | `l1` → node **4** | `l2` → node **6** | 0 | 4 | 6 | 4 + 6 + 0 = **10** | 10 % 10 = **0** | 10 / 10 = **1** | `newNode(0)` → `tail.next` → `dummyHead → 0 → 7 → 0`<br>`tail → node(0)` | Produce 0, keep carry = 1 |
| **3** | `l1` → node **3** | `l2` → node **4** | 1 | 3 | 4 | 3 + 4 + 1 = **8** | 8 % 10 = **8** | 8 / 10 = **0** | `newNode(8)` → `tail.next` → `dummyHead → 0 → 7 → 0 → 8`<br>`tail → node(8)` | Final digit, carry becomes 0 |
| **4** | `l1` → `null` (end of list) | `l2` → `null` | 0 | 0 | 0 | 0 + 0 + 0 = **0** | loop condition `l1 != null || l2 != null || carry != 0` → **false** (carry = 0) → exit loop | – | – | Loop stops; result list is `dummyHead.next` → **7 → 0 → 8** |

**Final list** (`result`):  

```
7 → 0 → 8 → null
```

which correctly represents 807 in reverse order.

---

## 3️⃣ Complexity Analysis  

| Metric | Value | Justification |
|--------|-------|---------------|
| **Time Complexity** | **O(max(N, M))** | Let `N` be the length of `l1`, `M` the length of `l2`. The loop iterates once per digit of the longer list, plus possibly one extra iteration for a leftover carry. Each iteration does O(1) work (reading values, arithmetic, node creation, pointer updates). |
| **Space Complexity** | **O(max(N, M))** (excluding input) | We allocate a new node for every digit of the result, which in the worst case is `max(N, M) + 1` (when a final carry creates an extra digit). The algorithm uses only a constant amount of extra variables (`dummyHead`, `tail`, `carry`, a few integers), so auxiliary space is O(1). The output list itself is required by the problem, so the overall space usage is linear in the size of the result. |

**Note on Big‑O**: In interview settings the space used for the output is usually *not* counted against the algorithm’s auxiliary space, so we often state **O(1) auxiliary space** and **O(max(N, M)) total space** (output). The table above reflects the total space required to hold the result.

---  

### TL;DR  

* The algorithm walks both lists once, adds digit‑wise with a carry, creates a new list on the fly using a dummy head, and stops when all digits and any carry are processed.  
* For the sample `[2,4,3]` + `[5,6,4]` the step‑by‑step trace shows the creation of nodes `7`, `0`, `8`.  
* Time = O(max(N,M)), auxiliary space = O(1) (output list = O(max(N,M))).
