(ns clojureprogs.number-reader 
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

"Write and demonstrate a function that receives a single number as argument and returns a string representing it in words.
For example, pass in 8 and receive eight, pass in 127 and get back either one hundred and twenty seven or one hundred twenty seven.
Your function should work for numbers with up to nine digits (e.g. 123456789) and only for whole numbers."

(def number-map {0 "zero"
                 1 "one"
                 2 "two"
                 3 "three"
                 4 "four"
                 5 "five"
                 6 "six"
                 7 "seven"
                 8 "eight"
                 9 "nine"
                 10 "ten"
                 11 "eleven"
                 12 "twelve"
                 13 "thirteen"
                 14 "fourteen"
                 15 "fifteen"
                 16 "sixteen"
                 17 "seventeen"
                 18 "eighteen"
                 19 "nineteen"
                 20 "twenty"
                 30 "thirty"
                 40 "forty"
                 50 "fifty"
                 60 "sixty"
                 70 "seventy"
                 80 "eighty"
                 90 "ninety"})

(s/def ::assignment-constraints (s/and int? #(<= 0 % 999999999)))

(s/fdef number-chunker
  :args (s/cat :input ::assignment-constraints)
  :ret (s/coll-of int? :kind vector? :count 3)) ; does it return a vector with three integers

(s/fdef eval-hundreds
  :args (s/cat :number ::assignment-constraints)
  :ret string?)

(s/fdef eval-tens-units
  :args (s/cat :number int?)
  :ret string?)

(s/fdef append-size
  :args (s/cat :size int?)
  :ret string?)

(s/fdef evaluate-number
  :args (s/cat :input ::assignment-constraints)
  :ret string?)


(defn number-chunker 
  "Breaks numbers into three digit chunks and returns it as a vector; millions, thousands, and hundreds."
  [input]
  (let [millions (quot input 1000000)
        thousands (quot (mod input 1000000) 1000)
        hundreds (mod input 1000)]
    [millions thousands hundreds]))

(defn eval-tens-units
  "Evaluates the final two digits of a chunk.
   Checks if its in the number map, otherwise creating the response by concatenation."
  [number]
  (let [value (mod number 100)]
    (cond
      (contains? number-map value) (number-map value)
      (= 0 value) nil ; return nothing 
      :else (let [units (mod value 10)
                  tens (- value units)]
              (str (number-map tens) "-" (number-map units))))))

(defn eval-hundreds 
  "Evaluates the hundreds column of the number.
   Evalautes whether 'and' should be appended.
   Finally, passes the remainder to the tens and units evaluator, if needed, to complete the chunk"
  [number]
  (let [digit (quot number 100)
        remainder (mod number 100)]
    (cond
      (and (> remainder 0) (> digit 0)) (str (number-map digit) " hundred and " (eval-tens-units remainder))
      (> digit 0) (str (number-map digit) " hundred")
      :else (eval-tens-units remainder))))



(defn append-size 
  "Depending on the size of vector left, append the appropriate figure"
  [size]
  (cond
    (= size 3) (str " million")
    (= size 2) (str " thousand")))

(defn evaluate-number 
  "Evaluates a number into its word equivalent.
   Recursively evaluates the three digit vector chunks until the vector is emptied.
   Once completed, join the words vector together for final result."
  [input]
  (let [parts (number-chunker input)]
    (if (= 0 (reduce + parts)) "zero") ; if the sum of the parts is zero, return that, otherwise complete the program 
    (loop [remaining parts
           words []
           size (count parts)]
      (if (empty? remaining)
        (str/join " " (remove empty? words)) ; remove empties to avoid extra spaces for 1 or 2 chunk numbers  
        (let [part (first remaining)
              word (when (pos? part)
                     (str (eval-hundreds part) (append-size size)))]
          (recur (rest remaining) (conj words word) (dec size)))))))


; this function is effectively redundant due adding the spec instruments, leaving for now
(defn validate-input 
  "Validate that the input being passed to main is within the boundaries of 
   being a positive whole number between 0 and 999,999,999"
  [input] 
  (cond (not (int? input)) (throw (ex-info "Only enter numerical values" {:input input})) 
        (neg? input) (throw (ex-info "Only positive numbers are supported." {:input input})) 
        (> input 999999999) (throw (ex-info "Program only supports up to nine digits!" {:input input})) 
        :else input))

(defn number-reader-main 
  "Takes the input arguement and converts it to its "
  [input]
  (try (validate-input input)
       (println (evaluate-number input))
    (catch Exception e
      (println "Error:" (.getMessage e)))))


(stest/instrument `number-chunker)
(stest/instrument `eval-hundreds)
(stest/instrument `eval-tens-units)
(stest/instrument `append-size)
(stest/instrument `evaluate-number)