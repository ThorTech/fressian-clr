;; Copyright (c) Metadata Partners, LLC.
;; All rights reserved.

(ns org.fressian.generators
  (:refer-clojure :exclude [symbol keyword])
  (:import org.fressian.TaggedObject)
  (:require [clojure.test.generative.generators :as gen]
            [clojure.core :as core]))

;;FF
;; (defn uuid
;;   []
;;   (java.util.UUID. (gen/long) (gen/long)))

(defn uuid
  []
  #_(Guid. (gen/int) (gen/short) (gen/short) (gen/byte-array 8))
  (Guid/NewGuid))

;;FF
;; (defn regex
;;   []
;;   (java.util.regex.Pattern/compile
;;    (gen/string
;;     #(char (gen/rand-nth (range 65 91))))))

(defn regex
  []
  (System.Text.RegularExpressions.Regex.
   (gen/string
    #(char (gen/rand-nth (range 65 91))))))

;;FF
;; (defn uri
;;   []
;;   ;; examples from http://labs.apache.org/webarch/uri/rfc/rfc3986.html
;;   (java.net.URI. (gen/rand-nth
;;                   ["ftp://ftp.is.co.za/rfc/rfc1808.txt"
;;                    "http://www.ietf.org/rfc/rfc2396.txt"
;;                    "ldap://[2001:db8::7]/c=GB?objectClass?one"
;;                    "mailto:John.Doe@example.com" 
;;                    "news:comp.infosystems.www.servers.unix"
;;                    "tel:+1-816-555-1212"
;;                    "telnet://192.0.2.16:80/"
;;                    "urn:oasis:names:specification:docbook:dtd:xml:4.1."])))

(defn uri
  []
  ;; examples from http://labs.apache.org/webarch/uri/rfc/rfc3986.html
  (System.Uri. (gen/rand-nth
                  ["ftp://ftp.is.co.za/rfc/rfc1808.txt"
                   "http://www.ietf.org/rfc/rfc2396.txt"
                   "ldap://[2001:db8::7]/c=GB?objectClass?one"
                   "mailto:John.Doe@example.com" 
                   "news:comp.infosystems.www.servers.unix"
                   "tel:+1-816-555-1212"
                   "telnet://192.0.2.16:80/"
                   "urn:oasis:names:specification:docbook:dtd:xml:4.1."])))

(defn big-int
  []
  (System.Numerics.BigInteger. (gen/byte-array gen/byte)))

;;FF - not supported
;; (defn big-decimal
;;   []
;;   (BigDecimal. (big-int) (gen/geometric 0.01)))

;;FF
;; (defn date
;;   []
;;   (java.util.Date. (long (gen/long))))

(def epoch (DateTime. 1970 1 1 0 0 0 DateTimeKind/Utc))

(defn- rand-range
  [min max]
  (rand-nth (concat (range min (inc max)))))

(defn date
  []
  (let [y (rand-range 1 10000)
        m (rand-range 1 12)
        d (rand-range 1 28)
        h (rand-range 0 12)
        mm (rand-range 0 59)
        ss (rand-range 1 59)
        ms (rand-range 0 999)]
    (DateTime. y m d h mm ss ms DateTimeKind/Utc)))

(defn single-char-string
  []
  (str (char (gen/uniform 1 65536))))

(defn symbol
  []
  (gen/one-of
   (fn [] (core/symbol (@#'gen/name) (@#'gen/name)))
   (fn [] (core/symbol (@#'gen/name)))))

(defn keyword
  []
  (gen/one-of
   (fn [] (core/keyword (@#'gen/name) (@#'gen/name)))
   (fn [] (core/keyword (@#'gen/name)))))

(defn symbolic
  []
  (gen/one-of symbol keyword))

(defn tagged-symbol
  "Returns symbol as tagged object."
  []
  (let [s (symbol)]
    (TaggedObject. "sym" (to-array [(namespace s) (name s)]))))

(defn tagged-keyword
  "Returns symbol as tagged object."
  []
  (let [k (keyword)]
    (TaggedObject. "key" (to-array [(namespace k) (name k)]))))

(defn scalar
  "Generate a random fressian-serializable scalar."
  []
  (gen/one-of uuid
              regex
              uri
              ;;FF - not supported - big-decimal
              big-int
              date
              tagged-symbol
              tagged-keyword
              gen/long
              gen/float
              gen/double))
;;FF
;; (defn nested
;;   "Generate a nested collection of items, where each item
;;    is generated by gen-item. Shallower nestings are much
;;    more likely."
;;   ([max-depth max-length gen-item]
;;      (nested 0 max-depth max-length gen-item))
;;   ([depth max-depth max-length gen-item]
;;      (if (< (.nextDouble gen/*rnd*) (/ depth max-depth))
;;        (gen-item)
;;        (gen/reps #(gen/uniform 0 max-length) #(nested (inc depth) max-depth max-length gen-item)))))

(defn nested
  "Generate a nested collection of items, where each item
   is generated by gen-item. Shallower nestings are much
   more likely."
  ([max-depth max-length gen-item]
     (nested 0 max-depth max-length gen-item))
  ([depth max-depth max-length gen-item]
     (if (< (.NextDouble gen/*rnd*) (/ depth max-depth))
       (gen-item)
       (gen/reps #(gen/uniform 0 max-length) #(nested (inc depth) max-depth max-length gen-item)))))

;;FF
;; (defn fressian-builtin
;;   "Generate a random instance of a fressian builtin type."
;;   []
;;   (gen/one-of gen/string
;;               gen/boolean
;;               #(gen/list gen/long)
;;               #(gen/byte-array gen/byte)
;;               #(gen/int-array (fn []  (gen/uniform Integer/MIN_VALUE (inc Integer/MAX_VALUE))))
;;               #(gen/float-array gen/float)
;;               #(gen/double-array gen/double)
;;               #(gen/boolean-array gen/boolean)
;;               #(object-array (gen/list gen/string))
;;               #(gen/long-array gen/long)
;;               #(nested 8 8 gen/long)
;;               gen/long
;;               gen/float
;;               gen/double
;;               nil))

(defn fressian-builtin
  "Generate a random instance of a fressian builtin type."
  []
  (gen/one-of gen/string
              gen/boolean
              #(gen/list gen/long)
              #(gen/byte-array gen/byte)
              #(gen/int-array (fn []  (gen/uniform Int32/MinValue (inc Int32/MaxValue))))
              #(gen/float-array gen/float)
              #(gen/double-array gen/double)
              #(gen/boolean-array gen/boolean)
              #(object-array (gen/list gen/string))
              #(gen/long-array gen/long)
              #(nested 8 8 gen/long)
              gen/long
              gen/float
              gen/double
              nil))

(defn cache-session
  "Generate a sequence of [fressianable cache?] args for testing
   caching. Lots of duplication of fressianable. "
  [gen-item]
  ;; explicit gen/vec sizer ensures non-empty collection
  (let [objs (gen/vec gen-item #(gen/geometric 0.02))]
    (repeatedly (gen/geometric 0.01)
                (fn [] [(gen/rand-nth objs) (gen/boolean)]))))

(def longs-near-powers-of-2
  "Longs close to powers of two, used to stress fressian encoding
   rules, which have boundary conditions near power-of-2 boundaries."
  (->> (range 0 63)
       (map #(long (Math/Pow 2 %)))
       (mapcat (fn [x] [(- x 2) (dec x) x (inc x) (+ x 2)]))))