(ns clojureprogs.number-reader-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [clojureprogs.number-reader :refer :all]))

(deftest test-evaluate-number
  (testing "Numbers in the map"
    (is (= (evaluate-number 11) "eleven"))
    (is (= (evaluate-number 90) "ninety"))
    (is (= (evaluate-number 6) "six")))

  (testing "Thousands and Millions"
    (is (= (evaluate-number 1000) "one thousand"))
    (is (= (evaluate-number 1005432) "one million five thousand four hundred and thirty-two"))
    (is (= (evaluate-number 123456789) "one hundred and twenty-three million four hundred and fifty-six thousand seven hundred and eighty-nine")))

  (testing "Bad input tests"
    (is (thrown? Exception (evaluate-number "123")))
    (is (thrown? Exception (evaluate-number 1000000000)))
    (is (thrown? Exception (evaluate-number -1)))))

(deftest test-eval-hundreds
  (testing "Evaluate Hundreds"
    (is (= (eval-hundreds 100) "one hundred"))
    (is (= (eval-hundreds 343) "three hundred and forty-three"))
    (is (= (eval-hundreds 999) "nine hundred and ninety-nine"))))

(deftest test-eval-tens-units
  (testing "Evaluating the final two digits of the chunks"
    (is (= (eval-tens-units 20) "twenty"))
    (is (= (eval-tens-units 99) "ninety-nine"))
    (is (= (eval-tens-units 0) "zero"))))

(deftest test-append-size
  (testing "Confirming the appropriate size is returned"
    (is (= (append-size 3) " million"))
    (is (= (append-size 2) " thousand"))
    (is (= (append-size 1) nil))))

(deftest test-number-chunker
  (testing "Testing the function that breaks numbers down into groups of three"
    (is (= (number-chunker 123456789) [123 456 789]))
    (is (= (number-chunker 123456) [0 123 456]))
    (is (= (number-chunker 123) [0 0 123]))
    (is (= (number-chunker 0) [0 0 0]))))

(deftest test-number-reader-main
  (testing "Output of number-reader-main, using trim because windows and linux terminal use different line ends (/n vs /r/n)"
    (is (= (str/trim (with-out-str (number-reader-main 8))) "eight"))
    (is (= (str/trim (with-out-str (number-reader-main 127))) "one hundred and twenty-seven"))
    (is (= (str/trim (with-out-str (number-reader-main 123456789))) "one hundred and twenty-three million four hundred and fifty-six thousand seven hundred and eighty-nine")))
  )

(deftest test-validate-input
  (testing "Main rejects bad inputs"
    (is (thrown-with-msg? Exception #"Only enter numerical values" (validate-input "123")))
    (is (thrown-with-msg? Exception #"Only positive numbers are supported." (validate-input -1)))
    (is (thrown-with-msg? Exception #"Program only supports up to nine digits!" (validate-input 1000000000)))))

(run-tests)