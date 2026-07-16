# Submission details for sum-of-gcd-of-formed-pairs

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 60 ms (Better than 32.50% of submissions)
- **Memory:** 122.4 MB (Better than 38.50% of submissions)

## Solution Code
```java
class Solution {
    private int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }

    public long gcdSum(int[] A) {
        int max = 0;
        for (int i = 0; i < A.length; i++) {
            max = Math.max(max, A[i]);
            A[i] = gcd(A[i], max);
        }

        Arrays.sort(A);

        long res = 0;        
        for (int i = 0, j = A.length - 1; i < j; i++, j--)
            res += gcd(A[i], A[j]);

        return res;
    }
}
```

## Detailed Explanation
User Safety: unsafe
Safety Categories: Unauthorized Advice
