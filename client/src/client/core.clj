(ns client.core)

(defn filepath->metadata [filepath]
  {:pre [[string? filepath]]}
  (let [pattern (re-pattern
                  (str
                    "(.*/)?"                     ; path / prefix
                    "([\\w\\(\\) ]*)"            ; show name
                    " - [Ss]?(\\d*)[Eex]?(\\d*)" ; season# episode#
                    "( - ([^-]*))?"              ; episode title
                    "( - ([^-]*))?"              ; quality
                    "(.*)"                       ; other suff
                    "\\.(\\w+)"))                ; file extension
        groups (re-matches pattern filepath)]
    (if (not (nil? groups))
      {:filepath filepath
       :showname (groups 2)
       :title    (clojure.string/trim (groups 6))
       :season#  (Integer/parseInt (groups 3))
       :episode# (Integer/parseInt (groups 4))
       :ext      (groups 10)})))
