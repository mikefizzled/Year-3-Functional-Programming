(ns clojureprogs.palindrome-test
  (:require [clojure.test :refer :all]
            [clojureprogs.palindrome :refer :all]))

(defn valid-length?
  "Custom predicate to confirm if the length is equal to or smaller than the specified take amount"
  [coll take-amount]
  (<= (count coll) take-amount))

(deftest test-palindrome?
  (testing "Palindrome check"
    (is (true? (palindrome? 121)))
    (is (true? (palindrome? 11)))
    (is (true? (palindrome? 90000009)))

    ; Test with non-palindromic numbers
    (is (false? (palindrome? 123)))
    (is (false? (palindrome? 10)))
    (is (false? (palindrome? 1002)))))

(deftest test-lazy-palindrome-gen
  (testing "Testing the lazy nature of the generator" 
    (is (= (take 5 (lazy-palindrome-gen 200)) [191 181 171 161 151]))
    (is (= (take 3 (lazy-palindrome-gen 100)) [99 88 77]))
    (is (= (take 10 (lazy-palindrome-gen 20)) [11 9 8 7 6 5 4 3 2 1]))))

(deftest test-palindrome-main
  (testing "Integration test of palindrome-main function"
    (let [start 200
          take-amount 3
          result (palindrome-main start take-amount)]
      (is (= result '(191 181 171)))
      (is (valid-length? result take-amount)))))

(deftest test-validate-palindrome-inputs
   (testing "Testing the inputs are thrown in main"
    (is (thrown-with-msg? Exception #"Invalid start input: must be a positive integer" (validate-palindrome-inputs -1 10)))
    (is (thrown-with-msg? Exception #"Invalid start input: must be a positive integer" (validate-palindrome-inputs "10" 10)))
    (is (thrown-with-msg? Exception #"Invalid start input: must be a positive integer" (validate-palindrome-inputs 0 10)))
    (is (thrown-with-msg? Exception #"Invalid take-amount input: must be a positive integer" (validate-palindrome-inputs 10 0)))
    (is (thrown-with-msg? Exception #"Invalid take-amount input: must be a positive integer" (validate-palindrome-inputs 10 -1)))))

(run-tests)