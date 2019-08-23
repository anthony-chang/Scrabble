# Scrabble
 Single player Scrabble game complete with an AI player with multiple difficultues.
 Created for an ICS project in grade 11. 
 Developer guide found [here](https://docs.google.com/document/d/1t2rW5CoKK5A7XOS7FrIgK37NX_cXJJmIM9Lxq-ZIHJ4/edit?usp=sharing)

## User Guide 
### Getting Started

To play the game, first download the zipped folder containing the program and images used. Extract the contents of this zipped file, double-click the file called “Scrabble.jar”, and then run the program.When the game runs, click ‘Play’ and then choose a difficulty level for the Computer player to proceed to the game. The game is a single player game; you versus the computer. Note that for the Hard difficulty, the computer will always play the highest scoring play.  

### The Board

Your tiles can be placed in any of the legal positions on a 15 by 15 playing area. 60 of those positions contain a score multiplier. The most rare, and most potent score multiplier is the red Triple Word Score which take up 8 of those positions around the edges and corners of the board. The other score multipliers, in order of ascending rarity are: the dark blue Triple Letter Score (12 positions), the pink Double Word Score (16 positions), and the light blue Double Letter Score (24 positions). The first word must be placed on the centre square, which doubles the word score.

### The Letters

There are 98 lettered tiles in the game, each with a unique distribution and score value. During your turn, the tiles may be dragged and dropped from using your mouse from the rack, to the desired position on the playing board. The tiles can also be be recalled back to your rack from the board by clicking the ‘Recall’ button if the word they make up has not yet been played. 

### Buttons

Before you finalize a move, you can recall the tiles you have placed on the board back to your rack by clicking the ‘Recall’ button. Once you have placed your tiles onto the board to to form the desired word, you must click ‘Play’ to carry out the move. If your Play is not valid, the tiles placed will return to your rack. The ‘Pass’ button allows you to pass a turn without scoring. Finally, the three buttons ‘Exchange’, ‘Submit Exchange’, and ‘Cancel Exchange’ are involved with exchanging a tile from your rack. To exchange a selected tile from your rack with a different one from the rack, first click the ‘Exchange’ button. Next, click on a tile in your rack to select it. Then, click ‘Submit Exchange’. In case you do not wish to exchange the tile you selected, click ‘Cancel Exchange’. In the case that you choose to pass or exchange, you will not be able to make a move and score points.
 
### Scoring

Each letter is assigned a specific score value (shows in the bottom right corner of each tile). At the beginning of the game, each player starts with 0 points. At the end of each turn, the score from that turn is added to the player’s current score. The score for each turn is determined by the sum of the played letters’ score, for all of the words completed that turn. Thus, if a player’s move completes multiple words, all of those words are counted in the score. If any of the played tiles occupy a score multiplier, the corresponding bonus is then added. If a letter occupies a score multiplier, and the letter completes multiple words, the score multiplier will apply to all of the words. Note that the centre square is a double word score multiplier. 

### Sequence of Play

The game is played in a turn-by-turn basis, that is, turns will alternate between you and the Computer. At the start of the game, seven tiles will automatically be distributed to your rack, and you will get the first turn. The first move must occupy the centre square, marked with a star. Subsequent moves must either add on to existing words, or create new legal words. The game will reject any illegal moves (outlined in the ‘Legal Moves’ section). After each move, the rack is replenished so that it contains seven tiles once more. During your turn, you may shuffle or recall tiles. You may also pass your turn, or exchange a tile instead of playing a word in your turn (these features are explained in the ‘Buttons’ section). The game will end when there are no more tiles left in the bag, and either you or the Computer use up all the letters in your/its rack, or when there are three consecutive scoreless turns.
Legal MovesEach tile placed must either occupy the same row, or the same column. With the exception of the first move, every tile placed must be connected to another tile. Hence, there should be no empty spaces between the tiles placed in a turn. 
A word is defined as two or more horizontally-adjacent letters, reading from left to right, or vertically-adjacent letters, reading from top to bottom (no diagonal words). The word must also be listed in the official English Scrabble dictionary to be valid.
As long as at the end of each turn every single word formed on the board is a legal and valid word, you may add letters to an existing word, play perpendicular to an existing word (by either adding a tile or using an existing one to form the word), or play parallel to a word (and so forming short perpendicular words).

### Limitations and Bugs

For an optimal experience, the minimum screen resolution should be 750 x 750. This version of Scrabble also does not contain blank tiles, does not support Pass and Play (multiplayer). Note that during the computer’s turn, it takes some time (about 25 seconds) to think of a play, and during that time, the game will if unresponsive. If you wish to exit the game while the computer is thinking, you must force it to close in Task Manager, or terminate the program in an IDE. 
The computer will sometimes play extremely obscure words, or words that are simply not in the Scrabble dictionary. Unfortunately, the official Scrabble dictionary costs money, so a random sketchy dictionary is used instead, meaning that there will be some possibly non-english words that count as a valid word in our dictionary. 



