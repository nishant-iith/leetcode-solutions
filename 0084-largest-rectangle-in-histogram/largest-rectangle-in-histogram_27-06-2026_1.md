# Submission details for largest-rectangle-in-histogram

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 71 ms (Better than 38.16% of submissions)
- **Memory:** 75.4 MB (Better than 97.55% of submissions)

## Solution Code
```java
class Solution {
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int maxArea = 0;

        for (int i = 0; i < heights.length; i++) {
            while (stack.peek() != -1 && heights[i] <= heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }

        while (stack.peek() != -1) {
            int height = heights[stack.pop()];
            int width = heights.length - stack.peek() - 1;
            maxArea = Math.max(maxArea, height * width);
        }

        return maxArea;
    }
}
```

## Detailed Explanation
### 1. Algorithm Analysis

The provided Java solution efficiently computes the largest rectangle in a histogram using a **stack-based approach** that leverages the concept of **next smaller element** to the left and right for each bar. Here's the detailed breakdown:

#### **Strategy**:
1. **Stack Initialization**: A stack is initialized with `-1` to handle edge cases where the first bar is the smallest.
2. **Iterate Through Bars**: Traverse each bar in the histogram. For each bar:
   - **While** the current bar's height is less than or equal to the height of the bar at the top of the stack:
     - Pop the stack to get the index of the bar (`height`) that forms the top of the rectangle.
     - Calculate the rectangle's **width** as the distance between the current index `i` and the new top of the stack minus 1.
     - Update `maxArea` if this rectangle's area is larger.
   - Push the current index `i` onto the stack.
3. **Remaining Bars**: After processing all bars, any remaining indices in the stack (except `-1`) are processed similarly by using the end of the array as the boundary.

#### **Key Insight**:
- The stack maintains indices of bars in **increasing order of height**. When a smaller bar is encountered, it acts as the right boundary for the rectangle formed by the popped bar, whose left boundary is the new top of the stack.

---

### 2. Step-by-Step Walkthrough (Dry Run)

**Sample Input**: `[2, 1, 5, 6, 2, 3]`

| Step | `i` | Stack State          | `heights[i]` | Action                     | MaxArea After |
|------|-----|----------------------|--------------|----------------------------|---------------|
| 0    | 0   | `[-1]`               | 2            | Push `0`                   | 0             |
| 1    | 1   | `[ -1, 0 ]`          | 1            | **Pop** 0 (height=2): width = 1 - (-1) -1 = 1 → area=2. Push 1. | maxArea=2     |
| 2    | 2   | `[ -1, 1 ]`          | 5            | Push 2                     | 2             |
| 3    | 3   | `[ -1, 1, 2 ]`       | 6            | Push 3                     | 2             |
| 4    | 4   | `[ -1, 1, 2, 3 ]`    | 2            | **Pop** 3 (height=6): width = 4 - 2 -1 = 1 → area=6. Max=6. Pop 2 (height=5): width = 4 -1 -1 = 2 → area=10. Push 4. | maxArea=10    |
| 5    | 5   | `[ -1, 1, 4 ]`       | 3            | **Pop** 4 (height=2): width = 5 -1 -1 = 3 → area=6. Push 5. | maxArea=10    |
| 6    | Post-iter | `[ -1, 1, 5 ]`       | –            | **Pop** 5 (height=3): width = 6 -1 -1 = 4 → area=12. Pop 1 (height=1): width = 6 - (-1) -1 = 6 → area=6. | maxArea=12    |

**Final Output**: `12`.

---

### 3. Complexity Analysis

#### **Time Complexity**:
- **O(n)**: Each bar is pushed and popped from the stack exactly once. The total operations for the `for` loop and the final `while` loop are linear in `n`.

#### **Space Complexity**:
- **O(n)**: The stack stores up to `n+1` elements (including `-1`), resulting in linear space usage.

#### **Justification**:
- **Time**: The algorithm ensures every bar is processed in constant time during push/pop operations.
- **Space**: The stack's maximum size corresponds to the number of bars in the worst case (e.g., strictly increasing heights).

---

### Summary
The stack-based algorithm efficiently finds the largest rectangle in a histogram by tracking boundaries of rectangles using the next smaller elements. The time and space complexities are optimal, making this a robust solution.

