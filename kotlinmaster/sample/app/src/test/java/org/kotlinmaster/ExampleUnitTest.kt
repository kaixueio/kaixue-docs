package org.kotlinmaster

import org.junit.Assert.assertEquals
import org.junit.Test
import org.kotlinmaster.basic_grammar_01.Dict

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testKotlin() {
        Dict().word = "hello"
        println(Dict().word)
    }
}
