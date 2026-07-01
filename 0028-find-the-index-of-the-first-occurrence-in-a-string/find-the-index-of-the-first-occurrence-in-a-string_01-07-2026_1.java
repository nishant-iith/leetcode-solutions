
class Solution {
    public int strStr(String haystack, String needle) {
        if (
            haystack == null || needle == null || 
            needle.length() > haystack.length()
        ) return -1;

        // Step 1: build lps[] array for needle
        int[] lps = buildLPSArray(needle);

        // Step 2: KMP Sliding Window
        int hIndex = 0;
        int nIndex = 0;

        while (hIndex < haystack.length()) {
            // case 1: haystack[hIndex] == needle[nIndex]
            if (haystack.charAt(hIndex) == needle.charAt(nIndex)) {
                hIndex++;
                nIndex++;
            } else { // haystack[hIndex] != needle[nIndex]
                if (nIndex > 0) nIndex = lps[nIndex]; // case 2.1
                else hIndex++;                        // case 2.2
            }

            // check if window is valid
            if (nIndex == needle.length()) return hIndex - needle.length();
        }

        return -1;
    }
    private int[] buildLPSArray(String s) {
        int[] lps = new int[s.length()];
        lps[0] = -1;
        if (1 < s.length()) lps[1] = 0;
        if (s.length() <= 2) return lps;
        int prefixLen = 0;
        int i = 2;
        while (i < lps.length) {
            if (s.charAt(i - 1) == s.charAt(prefixLen)) lps[i++] = ++prefixLen;
            else if (prefixLen > 0) prefixLen = lps[prefixLen];
            else lps[i++] = 0;
        }

        return lps;
    }
}