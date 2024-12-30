NFL Team Statistics Dashboard by Kareem Khalidi

- nfl_stats.py contains the NFLStats class for interacting with, filtering, and calculating pass/rush statistics for NFL play-by-play data.
- app.py contains the code for the Flask Web App Dashboard for querying and viewing NFL Data.
- The templates folder contains all the html code for the Dashboard.

- To run the app, simply run the app.py class and then click on the link that it prints to the console ("* Running on http://...").
- To query and view data, select the filters you want to apply to the data from the home page and then click the submit button.
- Please note that the first query after running the app may take a minute or two as it has to load the pbp data for the entire season
  - Once a seasons pbp data is loaded, querying that season again should be almost instantaneous
  - I recommend using ctrl+click to press submit so that it opens the results in a new tab, allowing you to reuse the same filters as well as the same pbp data without needing to reload it.
