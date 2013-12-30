optimal-guitar
==============

Solves a [dynamic
programming](https://en.wikipedia.org/wiki/Dynamic_programming)
problem to determine the most efficient (easiest) way to play a song
on a guitar.

The file `guitar-hero-dp-algorithm.pdf` provides a proof of the
algorithm for Guitar Hero. This algorithm is implemented in the
`guitar-hero` directory. The file `guitar-hero-search-algorithm.ppt`
is an alternative formulation for the problem.

The directory `real-guitar` is an implementation for real
tablature. It runs slow (or at least did in 2009), so you might want
to only run the algorithm on a phrase at a time.

## Difficulty Model

The solver assumes a model of difficulty for (1) hand positions, and
(2) transitions.

## Project History

I built this project as part of an independent study at Washington
University in St. Louis in 2009. I recently re-discovered the code,
and wanted to open-source for others to build off of. When I get the
opportunity, I'll go over the code to make sure it works as intended.

