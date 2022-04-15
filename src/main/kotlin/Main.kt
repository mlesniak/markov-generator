package com.mlesniak.main

class MarkovGenerator(source: String) {
    private val database: MutableMap<String, MutableList<String>> = mutableMapOf()

    init {
        val words = wordlist(source)
        for (i in 0 until words.size - 2) {
            val key = words[i]
            val entry = words[i + 1]
            database.merge(key, mutableListOf(entry)) { a, b -> a.addAll(b); a }
        }

        // TODO(mlesniak) beginning of sentence
    }

    private fun wordlist(source: String): List<String> {
        val stream = MarkovGenerator::class.java.getResourceAsStream(source)
            ?: throw IllegalArgumentException("Source $source not found")
        return stream
            .reader()
            .readLines() // Prevents special handling of \r.
            .joinToString("")
            .split(" ")
            .map { it.trim() }
            .map { it.replace("\n", "") }
            .filter { it.isNotBlank() }
    }

    fun run(startWord: String) {
        var currentWord = startWord

        while (true) {
            println("'$currentWord' ${currentWord.hexdump()}")
            if (currentWord.endWord()) {
               break
            }
            val nextWords = database[currentWord] ?: break
            currentWord = nextWords.random()
        }
    }

    private fun String.hexdump(): String {
        val sb = StringBuilder()

        for (c in this) {
            sb.append(String.format("%02X ", c.code))
        }
        sb.deleteCharAt(sb.length-1)

        return sb.toString()
    }

    private fun String.endWord(): Boolean {
       return this.endsWith(".") || this.endsWith(";")
    }
}

fun main() {
    MarkovGenerator("/die_verwandlung.txt").run("Er")
}
