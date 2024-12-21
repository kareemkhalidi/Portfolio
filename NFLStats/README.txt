This project processes play-by-play data from NFL games to calculate and extract a variety of advanced statistics.

Features:
- Compare matchups between two teams by season, offensive team, defensive team, and stat type.
- Dynamic calculations for team-specific performance metrics using the nfl_stats class.

Usage:
- To analyze a matchup, run nfl_stats.py and provide the following inputs:
  - Season
  - Offensive team
  - Defensive team
  - Desired stat type (pass, rush, rec)
- The script will calculate and display the requested statistics.
- The nfl_stats class is designed for versatility and can support a wide range of additional use cases.

Potential Future Additions:
- Player-Specific Statistics: Add functionality to work with stats for individual players more easily.
- Position-Based Analysis: Enable stats calculation for specific player positions (e.g., tar, rec, rec_yd, and rec_yd allowed to wide receivers only).
- Advanced Metrics: Introduce performance evaluations for quarterbacks, running backs, and receivers against different coverage types or metrics like yards per route run.
- Enhanced Modularity: Increase abstraction to allow integration of the NFLStats class into various projects with minimal adjustments.