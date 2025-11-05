(ns clojureprogs.morse-code-test  
  (:require [clojure.string :as str]
   [clojure.test :refer :all]
            [clojureprogs.morse-code :refer :all]))

(deftest test-morse-encoder 
  (testing "Morse code encoder with good inputs"
    (is (= (morse-encoder "morse and norse") "-- --- .-. ... .       .- -. -..       -. --- .-. ... ."))
    (is (= (morse-encoder "hello world") ".... . .-.. .-.. ---       .-- --- .-. .-.. -..")) 
    (is (= (morse-encoder "SOS") "... --- ...")))
  
  (testing "Morse code encoder with bad inputs"
    (is (thrown? Exception (morse-encoder "hello world!"))) ; bad char, no exclamation marks
    (is (thrown? Exception (morse-encoder "... --- ..."))) ; bad chars/morse input to encoder rather than decoder
    (is (thrown? Exception (morse-encoder "S-O-S"))))) ; bad char, no dashes

(deftest test-morse-decoder
  (testing "Morse decoder with good inputs"
    (is (= (morse-decoder "-- --- .-. ... .       .- -. -..       -. --- .-. ... .") "MORSE AND NORSE"))
    (is (= (morse-decoder ".... . .-.. .-.. ---       .-- --- .-. .-.. -..") "HELLO WORLD"))
    (is (= (morse-decoder "... --- ...") "SOS")))
  
  (testing "Morse decoder with bad inputs"
    (is (thrown? Exception (morse-decoder ".... . .-.. .-.. ---!"))) ; bad char
    (is (thrown? Exception (morse-decoder "......-...-..---"))) ; sos without spaces, therefore invalid morse
    (is (thrown? Exception (morse-decoder "ascii characters"))))) ; bad chars

(deftest test-prep-text-to-morse
  (testing "Preparing text to Morse"
    (is (= (prep-text-to-morse "inspector Morse") ["INSPECTOR" "MORSE"]))
    (is (= (prep-text-to-morse "0800 00 1066") ["0800" "00" "1066"]))))

(deftest test-prep-morse-to-text
  (testing "Preparing Morse to text"
    (is (= (prep-morse-to-text "-- --- .-. ... .       -.-. --- -.. .") ["-- --- .-. ... ." "-.-. --- -.. ."]))))

(deftest test-integration
  (testing "Testing encoding to decoding and vice versa"
    (let [original "INTEGRATION TEST"
          encoded (morse-encoder original)
          decoded (morse-decoder encoded)]
      (is (= decoded "INTEGRATION TEST"))) 
    (let [original ".. -. - . --. .-. .- - .. --- -.       - . ... -"
          decoded (morse-decoder original)
          encoded (morse-encoder decoded)]
      (is (= encoded ".. -. - . --. .-. .- - .. --- -.       - . ... -")))))


(deftest test-main-function
  (testing "Main with valid inputs, catching the output side effects using with-out-str"
    (let [encode-output (morse-code-main "encode" "HELLO")
          decode-output (morse-code-main "decode" ".... . .-.. .-.. ---")] 
      (is (= encode-output ".... . .-.. .-.. ---")) 
      (is (= decode-output "HELLO"))))) 
    
  (testing "Main with invalid function input"
    (is (thrown? Exception (morse-code-main "becode" "HELLO"))))

(run-tests)
