# Overview

Generate random pseudo-gibberish sentences from a source file by randomly choosing subsequent elements of the sentence
based on initially randomly chosen starting words and occurences in the text. Uses Kafka's _Die Verwandlung_ (obtained from Project Gutenberg) as a primary source.

I'm not completely happy with the result, but this code is just a playground for this idea and maybe future work based on that.

## Build

    mvn clean package

## Run

    target/markov

## Example

    ❯ target/markov
    sagte der mittlere der Herren etwas bestürzt und lächelte süßlich.
    Die Enttäuschung über das Kanapee hin und her, aber weiter reichten die Kräfte der Familie ein großes Nein, und es wurde nicht mehr ergeben.
    Werden Sie alles wahrheitsgetreu berichten?