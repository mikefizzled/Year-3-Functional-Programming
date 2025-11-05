;; src/clojureprogs/core.clj
(ns clojureprogs.core
  (:require [clojureprogs.morse-code :refer [morse-code-main]]
            [clojureprogs.number-reader :refer [number-reader-main]]
            [clojureprogs.palindrome :refer [palindrome-main]]))

(defn -main []

  ; just uncomment and follow required arguement pattern

  ; palindrome [starting-number lazy-take-amount]
  ; e.g, (palindrome-main 10000 10)

  (palindrome-main 6 3)

  ; number reader [number-to-convert]
  ; e.g, (number-reader-main 123456789)
  
  (number-reader-main 123456789)

  ; morse [function input]
  ; e.g, (morse-code-main "encode" "Inspector Morse")
  ; or   (morse-code-main "decode" ". -. -.. . .- ...- --- ..- .-.")
  
   (morse-code-main "encode" "Inspector Morse")
   (morse-code-main "decode" "-- --- .-. ... .       -.-. --- -.. .")
  )
