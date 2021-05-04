package com.tokopedia.minimumpathsum

import android.util.Log

object Solution {
    fun minimumPathSum(matrix: Array<IntArray>): Int {
        // TODO, find a path from top left to bottom right which minimizes the sum of all numbers along its path, and return the sum
        // below is stub
        val rows = matrix.size
        if (rows == 0) return 0

        val cols = matrix[0].size

        var dp = Array(rows) { IntArray(cols) { 0 } }

        //nilai pada dp 0,0 sama dengan matrix 0,0
        dp[0][0] = matrix[0][0]

        //mengisi nilai baris pertama pada dp
        for (i in 1 until cols){
            dp[0][i] = dp[0][i - 1] + matrix[0][i]
        }

        //mengisi nilai kolom pertama pada dp
        for (i in 1 until rows){
            dp[i][0] = dp[i-1][0] + matrix[i][0];
        }

        for (i in 1 until rows){
            for (j in 1 until cols){
                dp[i][j] = matrix[i][j] + min(dp[i-1][j],dp[i][j-1]);
            }
        }

        Log.d("TAG", "hasil = ${dp[rows-1][cols-1]}")

        return dp[rows-1][cols-1];

    }


    private fun min(a : Int, b : Int) : Int {
        return if (a<b) a else b
    }

}
