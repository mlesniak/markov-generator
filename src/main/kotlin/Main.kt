package com.mlesniak.main

class MarkovGenerator(lookahead: Int, source: String) {
    private val database = mutableMapOf<List<String>, MutableList<String>>()
    private val startWords = mutableSetOf<List<String>>()

    init {
        val words = wordlist(source)
        for (i in 0 until words.size - 2) {
            val cur = mutableListOf<String>()
            for (j in i until i+lookahead) {
                cur.add(words[j])
            }

            val next = words[i + lookahead]
            database.merge(cur, mutableListOf(next)) { a, b -> a.addAll(b); a }

            if (i == 0) {
                startWords.add(cur)
            }
            // TODO(mlesniak) fix this
            // if (cur.last().endWord()) {
            //     startWords.add(next)
            // }
        }
    }

    private fun wordlist(source: String): List<String> {
        val stream = MarkovGenerator::class.java.getResourceAsStream(source)
            ?: throw IllegalArgumentException("Source $source not found")
        return stream
            .reader()
            .readLines() // Prevents special handling of \r.
            .joinToString(" ")
            .split(" ")
            .map { it.trim() }
            .map { it.replace("\n", "") }
            .filter { it.isNotBlank() }
    }

    private fun List<String>.endWord(): Boolean {
        val ends = listOf(".", "?", "!")
        val last = this.last()
        return ends.any { last.endsWith(it) }
    }

    fun run(numSentences: Int) {
        repeat(numSentences) {
            var currentWords = startWords.random()
            print("${currentWords.joinToString(" ")} ")

            while (!currentWords.endWord()) {
                val nextWords = database[currentWords] ?: break
                val nextWord = nextWords.random()
                currentWords = currentWords.subList(1, currentWords.size) + nextWord
                print("$nextWord ")
            }
            println()
        }
    }
}

fun main() {
    MarkovGenerator(2, "/die_verwandlung.txt").run(3)
}
