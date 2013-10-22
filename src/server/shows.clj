(ns server.shows)

(defn select-episodes
  [shows showname season episode]
  (->> shows
       vals
       flatten
       (filter #(= showname
                   (% "showname")))
       (filter #(= season
                   (% "season#")))
       (filter #(= episode
                   (% "episode#")))))

(defn contains-episode?
  [shows showname season episode]
  (->> (select-episodes shows showname season episode)
       empty?
       not))
