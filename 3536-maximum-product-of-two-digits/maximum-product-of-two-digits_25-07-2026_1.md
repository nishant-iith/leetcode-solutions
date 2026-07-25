# Submission details for maximum-product-of-two-digits

- **Status:** Accepted
- **Language:** Java
- **Runtime:** 1 ms (Better than 100.00% of submissions)
- **Memory:** 42.6 MB (Better than 58.74% of submissions)

## Solution Code
```java
class Solution {
    public int maxProduct(int n) {
        long m = 0;

        for (; n != 0; n /= 10)
            m += 1L << ((n % 10) << 2);

        int u = (64 - Long.numberOfLeadingZeros(m) - 1) >> 2;
        return u * ((64 - Long.numberOfLeadingZeros(m - (1L << (u << 2))) - 1) >> 2);
    }
}
```

## Detailed Explanation
User Safety: safe
