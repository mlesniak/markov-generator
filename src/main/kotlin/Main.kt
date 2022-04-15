package com.mlesniak.main

class MarkovGenerator(source: String) {
    private val database = mutableMapOf<String, MutableList<String>>()
    private val startWords = mutableSetOf<String>()

    init {
        val words = wordlist(source)
        for (i in 0 until words.size - 2) {
            val cur = words[i]
            val next = words[i + 1]
            database.merge(cur, mutableListOf(next)) { a, b -> a.addAll(b); a }

            if (i == 0) {
                startWords.add(cur)
            }
            if (cur.endWord()) {
                startWords.add(next)
            }
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

    private fun String.endWord(): Boolean {
        val ends = listOf(".", ";", "?", "!")
        return ends.any { this.endsWith(it) }
    }

    fun run(numSentences: Int) {
        repeat(numSentences) {
            var currentWord = startWords.random()

            while (true) {
                print("$currentWord ")
                if (currentWord.endWord()) {
                    break
                }
                val nextWords = database[currentWord] ?: break
                currentWord = nextWords.random()
            }
            println()
        }
    }
}

fun main() {
    MarkovGenerator("/die_verwandlung.txt").run(3)
}
