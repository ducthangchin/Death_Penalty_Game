# Design Document

Detailed description of the project.





## Functional Details and Project Structure

### 1. Class HangmanBot

#### This class extends `TelegramLongPollingBot` Class

- Class variables:
  
  | Variable    | Function                           | Type                       |
  | :--------   | :----------------------------------| :------------------------- |
  | `games`  | Save game information of each user | `Map<Long, HangmanGame>`    |
  |`lastgames`  | Save last game information of each user | `Map<Long, HangmanGame>`  |


- Class methods:
  
  | Name         | Function                           | Type                                                                |
  | :--------    | :-------                           | :------------------------- |
  | `getBotUsername()`   | This method must always return **Bot username**.                                       | `String` |
  | `getBotToken()`   | This method must always return **Bot Token**.                                             | `String` |
  | `onUpdateReceived()`   | This method will manage incoming messages, send messages to users, control the game, mainly responsible for various logical events. | `Void` |

### 2. Class HangmanGame

#### The Game class store information about the player's game.
- Class variables:

  | Variable    | Function                           | Type                       |
  | :--------   | :----------------------------------| :------------------------- |
  | `WORDS`      |  The dictionary where words stored       | `String[]`               |
  | `HangmanFigures`      | Figures as text art to illustrate hanging man by neck      | `String[]`               |
  | `RANDOM`  | Generate a random word from 'WORDS' |`Random`  |
  | `MAX_ERRORS`    |  Max errors before user lose(`MAX_ERRORS = 8`)   | `final int`  |
  | `wordFound`      | Word found stored in a char array to show progression of user       | `char[]`               |
  | `wordToFind`      | Word to  find       | `String`               |
  | `nbErrors`    | Number of noted errors   | `int`               |
  | `letters`     | Letters already entered by user   | `ArrayList<String>`   |
  | `current`      | Current game's state      | `String`    |
  | `help`        | To count how many times the command */hint* used | `int`|
  | `hasEnded`    | Get value true if the game finished | `boolean` | 



- Class methods:

  | Name         | Function                           | Type                                                                |
  | :--------    | :-------                           | :------------------------- |
  | `HangmanGame()`   | Method for starting a new game                                    | `Void` |
  | `nextWordToFind`   | Method returning randomly next word to find                                   | `String` |
  | `wordFound`   | Method returning true if word is found by user                                          | `boolean` |
  | `enter(String c)`   |    Method updating the word found after user entered a character | `void` |
  | `wordFoundContent()`   | Method returning the state of the word found by the user until by now | `String` |
  | `toString()`   | Method returning current game state                                            | `String` |
  | `hint()`   | Open the first unknown letter and update game state  | `void` |
  | `execute()`   | To execute the game   | `void`  |
  | `quitgame()`  | To quit the game  | `void`  |
  | `gameEnded()` | Method returning true if the game finished | `boolean` |



## Execution plan

### Create a Telegram Bot

- Get the library and add it to project.
- Register bot.
- For each command, implement code to set a proper responding message.

### Build the game

- Create new Game with a random word to be guessed, store user's answers and update game's state regularly.
- If the nb errores haven't reached Maximum number then continue recieve letter from user's guess until the word found
- If the nb errores, end the game.
- And some more features to handle and respond user's requests like: quit the game, show the last game. 
- Since the game is built on the Telegram platform, so the game interface is created with only unicode characters and emoji.
