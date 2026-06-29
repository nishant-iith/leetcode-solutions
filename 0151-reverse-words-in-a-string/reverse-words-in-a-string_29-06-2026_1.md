# Submission details for reverse-words-in-a-string

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 8 ms (Better than 36.51% of submissions)
- **Memory:** 44.5 MB (Better than 48.05% of submissions)

## Solution Code
```java
class Solution {
    public String reverseWords(String s) {
        String[] arr = s.trim().split("\\s+");
        int i=0,j=arr.length-1;
        while(i<j) {
            String t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
            i++;
            j--;
        }
        return String.join(" ", arr);
    }
}
```

## Detailed Explanation
Algorithm Analysis:The solution processes the input string by first trimming leading/trailing whitespace and splitting into words using `\\s+` (one or more spaces), which correctly handles multiple spaces. This creates a clean array of words. The core strategy is reversing the array in-place using a two-pointer technique (i starting at 0, j at end), swapping elements until pointers meet. Finally, the reversed array is joined into a single string with single spaces between words. This approach efficiently isolates the reversal logic from parsing/string reconstruction.

Step-by-Step DRY Run (Sample: "the sky is blue"):  
Initial state:  
- `s = "the sky is blue"`  
- `s.trim()` → `"the sky is blue"` (no leading/trailing spaces)  
- `arr = ["the", "sky", "is", "blue"]` (after `split("\\s+")`)  
- `i = 0`, `j = 3`  

Iteration 1:  
- `i < j` (0 < 3) → true  
- `t = arr[0] = "the"`  
- `arr[0] = arr[3] = "blue"`  
- `arr[3] = t = "the"`  
- State: `arr = ["blue", "sky", "is", "the"]`, `i=1`, `j=2`  

Iteration 2:  
- `i < j` (1 < 2) → true  
- `t = arr[1] = "sky"`  
- `arr[1] = arr[2] = "is"`  
- `arr[2] = t = "sky"`  
- State: `arr = ["blue", "is", "sky", "the"]`, `i=2`, `j=1`  

Loop ends (2 < 1 is false).  
Final result: `String.join(" ", arr)` → `"blue is sky the"`.

Complexity Analysis:  
- **Time Complexity: O(n)**  
  `split()` processes each character once to form words (O(n)). The two-pointer swap runs in O(m) where m is the number of words (m ≤ n), and `join()` reconstructs the string in O(n). Overall linear in input size.  
- **Space Complexity: O(n)**  
  The `arr` array stores all words, requiring O(n) space (Java strings are immutable, so `split()` creates new char[] arrays). No additional space beyond input storage.
