package com.example.android.pets.utils

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class StringUtilsTest {
    @Test
    fun newLine_ShouldBeNewLine() {
        assertThat(StringUtils.NEW_LINE, equalTo(System.getProperty("line.separator")))
    }
}