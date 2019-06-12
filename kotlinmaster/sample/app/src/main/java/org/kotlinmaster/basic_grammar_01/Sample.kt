package org.kotlinmaster.basic_grammar_01


var count: Int = 1

lateinit var countStr: String

var word: String = "hello"
    get() {
        return "world"
    }
    set(value) {
        field = value + " hahaha"
    }

fun incrementCount() {
    count += 1

    var nullableWord: String? = null
    nullableWord?.length

}
