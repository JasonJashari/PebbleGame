CONTENTS OF THIS FILE
---------------------

 * Introduction
 * Requirements


INTRODUCTION
------------

PebbleGame is a game involving multiple competing players using multi-threading. There are three black bags (X, Y, and Z) that are associated 
with three white bags (A, B, and C). At the start of the game three files are loaded to fill the black bags with the pebbles of weight specified 
in the files.
 
Each thread selects ten pebbles from a random bag to create their starting hand, then the threads will continuously discard a pebble to the white 
bag and draws a new pebble from a random black bag. The game continues until one thread gains a hand with total pebble weights of 100. 

Pebbles will be discarded to the white bag associated with the black bag the last pebble(s) was/were drawn from (black bag X corresponds with 
white bag A, and so forth....).

The command-line program will prompt for a number of players (above zero) and the three locations of the files to load. These files must be 
comma-separated natural integer values in the form of a .txt or .csv file.


REQUIREMENTS
------------

For the testing suite and classes, two files are required:

testBag.txt  --  file containing a list of positive non-zero integers as comma-separated values.
			- these values represent the weight of each pebble in the bag. 
			- there must be 55 or more items in the list.

tinyBag.csv  --  file containing a list of positive non-zero integers.
			- these values represent the weight of each pebble in the bag.
			- there must be less than 55 items in the list.

