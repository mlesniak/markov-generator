package com.mlesniak.main

/**
 * Generate random pseudo-gibberish sentences from a source file by randomly
 * choosing subsequent elements of the sentence based on initially randomly
 * chosen starting words and occurences in the text.
 */
class MarkovGenerator(lookahead: Int, source: String) {
    private val database = mutableMapOf<List<String>, MutableList<String>>()
    private val startWords = mutableSetOf<List<String>>()

    /**
     * Read the provided source file and generate the markov chain elements.
     */
    init {
        val words = wordlist(source)
        for (i in 0 until words.size - lookahead) {
            val cur = mutableListOf<String>()
            for (j in i until i + lookahead) {
                cur.add(words[j])
            }

            val nextWord = words[i + lookahead]
            database.merge(cur, mutableListOf(nextWord)) { a, b -> a.addAll(b); a }

            if (i == 0) {
                startWords.add(cur)
            }
            if (cur.endsSentence() && words.size >= i + 2 * lookahead) {
                val newSentence = words.subList(i + lookahead, i + 2 * lookahead)
                startWords.add(newSentence)
            }
        }
    }

    /**
     * Create a list of words from a text file on the classpath while removing
     * special characters.
     */
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
            .map { it.replace("«", "") }
            .map { it.replace("»", "") }
            .filter { it.isNotBlank() }
    }

    /**
     * True, if the list of words ends a sentence.
     */
    private fun List<String>.endsSentence(): Boolean {
        val ends = listOf(".", "?", "!")
        val last = this.last()
        return ends.any { last.endsWith(it) }
    }

    /**
     * Generate a random sentence.
     */
    fun generateSentence(): String {
        val sb = StringBuilder()
        var currentWords = startWords.random()
        sb.append(currentWords.joinToString(" "))
        sb.append(' ')

        while (!currentWords.endsSentence()) {
            val nextWords = database[currentWords] ?: break
            val nextWord = nextWords.random()
            currentWords = currentWords.subList(1, currentWords.size) + nextWord
            sb.append(nextWord)
            sb.append(' ')
        }
        return sb.toString()
    }
}

fun main() {
    val markovSource = MarkovGenerator(
        lookahead = 2,
        source = "/die_verwandlung.txt"
    )

    repeat(3) {
        println(markovSource.generateSentence())
    }
}
