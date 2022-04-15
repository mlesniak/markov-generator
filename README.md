# Overview

Generate random pseudo-gibberish sentences from a source file by randomly choosing subsequent elements of the sentence
based on initially randomly chosen starting words and occurences in the text.

Uses Kafka's _Die Verwandlung_ (obtained from Project Gutenberg) as a primary source.

## Build

    mvn clean package

## Run

    target/markov