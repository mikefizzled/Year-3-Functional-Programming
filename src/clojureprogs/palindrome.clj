(ns clojureprogs.palindrome
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

; only allow positive numerical inputs
(s/def ::positive-int (s/and int? pos?))

; spec the i/o of palindrome predicate function
(s/fdef palindrome? 
  :args (s/cat :input ::positive-int)
  :ret boolean?)

(defn palindrome? 
  "Compares if a string is the same as the reversed string"
  [input]
  (let [stringput (str input)]
    (= stringput (str/reverse stringput))))

(s/fdef lazy-palindrome-gen
  :args (s/cat :start ::positive-int)
  :ret (s/coll-of ::positive-int))

(defn lazy-palindrome-gen
  "Generates a lazy sequence of palindromes in descending order, excluding the starting value.
   The starting number is decremented, evaluated and then added to the sequence,
   until either zero is reached or the requested number of palindromes is fullfilled."
  [start]
  (let [current (dec start)]
    (lazy-seq
     (when (pos? current)
       (if (palindrome? current)
         (cons current (lazy-palindrome-gen current))
         (lazy-palindrome-gen current))))))

;; removed the args parameter to make the custom validator utilising the specs
(s/fdef palindrome-main 
  :ret (s/coll-of ::positive-int))

(defn validate-palindrome-inputs [start take-amount] 
  (when-not (s/valid? ::positive-int start) 
    (throw (ex-info "Invalid start input: must be a positive integer" {:input start}))) 
  (when-not (s/valid? ::positive-int take-amount) 
    (throw (ex-info "Invalid take-amount input: must be a positive integer" {:input take-amount}))))

(defn palindrome-main 
  "Prints and returns the requested number of palindromes beneath the starting value"
  [start take-amount]
  (try (validate-palindrome-inputs start take-amount)
       (println "Finding the first" take-amount "palindromes smaller than" start) 
       (let [result (take take-amount (lazy-palindrome-gen start))] 
         (doseq [num result] 
           (println num)) 
         result) ; return result for easier testing
   (catch Exception e 
     (println "Error:" (.getMessage e))))) 

; spec instruments for enforcing s/fdefs - never implemented stest/check as i struggled to understand how it worked
(stest/instrument `palindrome?) 
(stest/instrument `lazy-palindrome-gen) 
(stest/instrument `palindrome-main)


; I looked into using doseq and side effects.
; The consensus is that because println is effectively a side effect,
; it's better to use doseq for printing rather than including the print in the generator itself.

; Earlier attempts at the problem:

; Original marginally slower palidrome predicate
;(defn palindrome? [input]
;  (= (str input) (str/reverse (str input))))


; Original non-lazy generator
;(defn generator [start]
;  (loop [palindromes []
;         current (dec (int start))]
;    (if (= next 0)
;      palindromes
;      (recur (if (palindrome? current) 
;               (conj palindromes current) palindromes) (dec current)))))


