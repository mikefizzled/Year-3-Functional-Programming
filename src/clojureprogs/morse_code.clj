(ns clojureprogs.morse-code
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.set :as set]
            [clojure.spec.test.alpha :as stest]))

(def morse-encode-values
  "Simple map of morse encoding values"
  {\A ".-"
   \B "-..."
   \C "-.-."
   \D "-.."
   \E "."
   \F "..-."
   \G "--."
   \H "...."
   \I ".."
   \J ".---"
   \K "-.-"
   \L ".-.."
   \M "--"
   \N "-."
   \O "---"
   \P ".--."
   \Q "--.-"
   \R ".-."
   \S "..."
   \T "-"
   \U "..-"
   \V "...-"
   \W ".--"
   \X "-..-"
   \Y "-.--"
   \Z "--.."
   \0 "-----"
   \1 ".----"
   \2 "..---"
   \3 "...--"
   \4 "....-"
   \5 "....."
   \6 "-...."
   \7 "--..."
   \8 "---.."
   \9 "----."
   \. ".-.-.-"
   \space ""})

; didn't even know this was possible until I read this Stack Overflow question
; https://stackoverflow.com/questions/15595986/swap-keys-and-values-in-a-map
 
(def morse-decode-values
  "Using a simple map inversion so only one map needs to be maintained"
  (set/map-invert morse-encode-values))

(defn split-on-spaces [s]
  (str/split s #" "))

(defn prep-text-to-morse
  "Using arrow macros for the steps to standardise the text input"
  [input]
  (-> input
      str/upper-case
      split-on-spaces))

(defn prep-morse-to-text
  "Splits morse code when it matches the regex 7 spaces expression"
  [input]
  (str/split input #"\s{7}"))

(defn valid-morse-chunks? [input]
  (let [chunks (split-on-spaces input)] 
    (every? #(contains? morse-decode-values %) chunks)))

(s/def ::valid-encode-string (s/and string? #(every? morse-encode-values (str/upper-case %))))
; This regex validation doesn't ensure its genuine Morse code 
; but at least limits input to morse-centric characters, tried to expand it but this is the first time ive ever tried to use them
; so ive opted to break them down into morse chunks, but only after performing simpler safer checks
(s/def ::valid-decode-string (s/and string? 
                                    #(re-matches #"[.\- ]*" %)
                                    valid-morse-chunks?))

(s/fdef morse-encoder
  :args (s/cat :input ::valid-encode-string)
  :ret ::valid-decode-string)

(s/fdef morse-decoder
  :args (s/cat :input ::valid-decode-string)
  :ret ::valid-encode-string)

(defn morse-encoder
  "Encodes the prepared ASCII string into Morse code"
  [input]
  (let [words (prep-text-to-morse input) 
        encode-word (fn [word]
                      (str/join " " (map #(morse-encode-values %) word)))]
    (str/join "       " (map encode-word words))))

(defn morse-decoder
  "Decodes the Morse code into ASCII text"
  [input]
  (let [words (prep-morse-to-text input)
        decode-word (fn [word]
                      (str/join "" (map #(morse-decode-values %)
                                        (str/split word #" "))))]
    (str/join " " (map decode-word words))))

(s/fdef morse-code-main
  :args (s/cat :function string? :input string?))

(defn morse-code-main
  "Main entry point that manages bad function inputs by throwing an exception with information, return the final result for tests"
  [function input]
  (let
   [result (cond
             (= (str/lower-case function) "encode") (morse-encoder input)
             (= (str/lower-case function) "decode") (morse-decoder input)
             :else (throw (ex-info "Invalid function type. Use either 'encode' or 'decode'." {:function function})))]
    ; removing some side effects
    ;(println (format "Preparing to %s \"%s\"" function input))
    (println result)
    result)) ; returns the result specificlly so we can do tests on main

(stest/instrument `morse-encoder)
(stest/instrument `morse-decoder)
(stest/instrument `morse-code-main)
(stest/instrument `valid-morse-chunks?)


; Earlier attempts at the problem:

; I removed the previous spec attempt in favour of using the :else throw with an exception as I could give much more descriptive feedback
; I am unsure if this is the secure/Clojurey approach

; (s/def ::function-keyword #{"encode" "decode"})
; (s/fdef morse-code-main
;  :args (s/cat :function ::function-keyword :input string?))


; Used a loop instead, replaced unknown chars with ?, had extra spaces on either side of encoded spaces (9, not 7)

;(defn morse-encoder [input]
;  (loop [message (str/upper-case input)
;         encoded []]
;    (if (empty? message)
;      (str/join "/" encoded) ; join the vector to return
;      (let [letter (first message)
;            morse-letter (get morse-values letter "?")] ; if the character is not in map, return ?
;       (recur (rest message) (conj encoded morse-letter))))))

; Original method of preparing input
; (defn prepare-morse-input [input]
; (str/split (str/upper-case input) #" "))