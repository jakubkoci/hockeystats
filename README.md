# Hockeystats

Simple experimental pet project to learn and practice Clojure and ClojureScript languages.

# Features

Basic goal is based on comparing standings of ice hockey teams during whole season after each round.

### Input

Data about ice hockey games (rivals, final score, ...) from Czech ice hockey top league for current season on hokej.cz saved as CSV file.

### Expected output

Chart or some graphics of standings for each round and its changes during these rounds.

# Build


## Web client

Build JS file which is linked from `index.html`:

`lein cljsbuild once`

or

`lein cljsbuild auto`

for auto rebuild after change.

# Run

Run server which contains web app files as static assets:

`lein run`

# License

Copyright © 2016

