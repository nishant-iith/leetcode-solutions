# Submission details for implement-queue-using-stacks

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 0 ms (Better than 100.00% of submissions)
- **Memory:** 42.9 MB (Better than 26.38% of submissions)

## Solution Code
```java
class MyQueue {
    private Stack<Integer> input;
    private Stack<Integer> output;

    public MyQueue() {
        input = new Stack<>();
        output = new Stack<>();
    }

    public void push(int x) {
        input.push(x);
    }

    public int pop() {
        peek();
        return output.pop();
    }

    public int peek() {
        if (output.isEmpty()) {
            while (!input.isEmpty()) {
                output.push(input.pop());
            }
        }
        return output.peek();
    }

    public boolean empty() {
        return input.isEmpty() && output.isEmpty();
    }
}

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */
```

## Detailed Explanation
### 1. **Algorithm Analysis**  
The "implement-queue-using-stacks" problem leverages two stacks (`input` and `output`) to simulate queue behavior. The key insight is that **each element is pushed onto `input` and popped from `output` in reverse order**, ensuring FIFO (First-In-First-Out) order. This dual-stack approach allows efficient queue operations with O(1) time per operation (push/pop) and O(n) space complexity due to the interplay of stacks.  

---

### 2. **Step-by-Step Walkthrough (DRY Run)**  
**Sample Test Case**:  
```  
["MyQueue","push","push","peek","pop","empty"]  
Steps:  
1. `MyQueue obj = new MyQueue();`  
   - `input` = [], `output` = []  
2. `obj.push(1)` → `input = [1]`  
3. `obj.push(2)` → `input = [1, 2]`  
4. `obj.peek()` → `output` is empty, so pop from `input` and push to `output`:  
   - `[1] → pop(1) → push(1)` to `output` → `output = [1]`  
   - `input = [2]`  
5. `obj.peek()` → `output = [1]` → return `1`  
6. `obj.pop()` → Deschoolce: `output.pop()` → `output = []`, return `1`  
7. `obj.empty()` → `output.isEmpty()` → true (since `input = [2]`).  

Final State: `output = [], input = [2]` → `empty()` returns `true`.  

---

### 3. **Complexity Analysis**  
- **Time Complexity**: O(1) for `push`, `pop`, and `peek`, but O(n) overall for operations due to potential underflow checks (e.g., when `peek()` detects underflow during `pop`).  
- **Space Complexity**: O(n), where n is insertions (height of `input` stack + output stack).  

Breakdown:  
- **Algebraic Analysis**:  
  - `push(x)`: Adds 1 element to `input`.  
  - `pop()`, `peek()`: Involve O(k) operations where k is the length of `output`, which fluctuates with insertions.  
- **Justification**: While stacks themselves are O(n) space, the combined usage leads to linear space and average time complexity.  

---

### Final Notes  
- **Correctness**: The solution correctly simulates FIFO behavior by reversing intermediate outputs.  
- **Efficiency**: Optimal tradeoff between space and time for queue simulation.  
- **Edge Cases**: Handles underflow via `output.isEmpty()` checks.
