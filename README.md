# Final Year – Functional Programming - Clojure

This project was developed as part of a final-year functional programming module.  
It demonstrates three independent Clojure programs, each solving a distinct problem using functional techniques.  
All three programs are accessible via a single entry point (`core.clj`).

## Overview

### Included Programs
| Program | Description |
|----------|-------------|
| **Palindrome Generator** | Generates a lazy sequence of numerical palindromes below a given starting value. |
| **Number Reader** | Converts integer values (up to 999,999,999) into English words. |
| **Morse Code Translator** | Encodes ASCII text into Morse code and decodes Morse code back into text. |


## Project Structure

| File / Folder | Purpose |
|----------------|----------|
| `src/clojureprogs/core.clj` | Entry point that calls each main function. |
| `src/clojureprogs/palindrome.clj` | Lazy palindrome generator and validator. |
| `src/clojureprogs/number_reader.clj` | Numeric to word converter. |
| `src/clojureprogs/morse_code.clj` | Morse encoder/decoder logic and specifications. |
| `test/clojureprogs/...` | Unit tests for each module. |
| `project.clj` | Leiningen configuration and metadata. |

## Usage

You can run the project directly from the terminal with:

`lein run`

Alternatively, if using Calva, you can load the namespace and run the main function interactively.

Each module call can be enabled or disabled by commenting or uncommenting the relevant function calls inside `core.clj`.

Examples of how the functions can be called from `core.clj`:

- **Palindrome Generator** — `(palindrome-main 10000 10)`  
  Generates the first 10 palindromes smaller than 10,000.

- **Number Reader** — `(number-reader-main 123456789)`  
  Converts the number 123456789 to “one hundred and twenty-three million four hundred and fifty-six thousand seven hundred and eighty-nine”.

- **Morse Code Translator** — `(morse-code-main "encode" "Inspector Morse")`  
  or `(morse-code-main "decode" "-- --- .-. ... .")`  
  Encodes and decodes Morse code respectively.

## Testing

Each namespace includes corresponding unit tests located in the `test/` directory.  
These tests cover expected input/output behaviour and exception handling.

To run the test suite, execute:

`lein test`

This will automatically instrument all functions defined with `clojure.spec`, ensuring that input and output specifications are verified during test execution.

## Development Notes

- First coursework written in a functional programming langauge

- Written in Clojure 1.11.1

- Specs (clojure.spec.alpha) used for function contracts

- stest/instrument enforces runtime spec validation

- Developed and tested using Calva in Visual Studio Code
