import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

// We are going to create a Hangman Game with Java keywords :)
public class HangmanGame {

    // Java Keywords
    public static  String[] WORDS = {
            "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE",
            "CASE", "CATCH", "CHAR", "CLASS", "CONST",
            "CONTINUE", "DEFAULT", "DOUBLE", "DO", "ELSE",
            "ENUM", "EXTENDS", "FALSE", "FINAL", "FINALLY",
            "FLOAT", "FOR", "GOTO", "IF", "IMPLEMENTS",
            "IMPORT", "INSTANCEOF", "INT", "INTERFACE",
            "LONG", "NATIVE", "NEW", "NULL", "PACKAGE",
            "PRIVATE", "PROTECTED", "PUBLIC", "RETURN",
            "SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH",
            "SYNCHRONIZED", "THIS", "THROW", "THROWS",
            "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WHILE"
    };

    //Hangman's status showed as figure
    String[] HangmanWordBank;

    public static final Random RANDOM = new Random();
    // Max errors before user lose
    public static final int MAX_ERRORS = 8;
    // Word to find
    private String wordToFind;
    // Word found stored in a char array to show progression of user
    private char[] wordFound;
    private int nbErrors;
    // letters already entered by user
    private ArrayList <String> letters = new ArrayList <> ();
    //Current game status
    private String current = "";
    private int help = 0;

    private boolean hasEnded = false;

    // Method for starting a new game
    public HangmanGame()  {
        nbErrors = 0;
        letters.clear();
        wordToFind = nextWordToFind();

        // word found initialization
        wordFound = new char[wordToFind.length()];

        for (int i = 0; i < wordFound.length; i++) {
            wordFound[i] = '_';
        }
        this.current = wordFoundContent()+"\nEnter a letter : ";

        try{
            Path path = Paths.get("src/main/resources/HangmanWordBank.txt");
            this.HangmanWordBank = Files.lines(path).collect(Collectors.joining("\n")).split("\n0\n");
            path = Paths.get("src/main/resources/HangmanWords.txt");
            this.WORDS = Files.lines(path).collect(Collectors.joining("\n")).split("\n");
        } catch(IOException e){}

    }

    // Method returning randomly next word to find
    private String nextWordToFind() {
        return WORDS[RANDOM.nextInt(WORDS.length)];
    }


    // Method returning true if word is found by user
    public boolean wordFound() {
        return wordToFind.contentEquals(new String(wordFound));
    }

    // Method updating the word found after user entered a character
    private void enter(String c) {
        // we update only if c has not already been entered
        if (!letters.contains(c)) {
            // we check if word to find contains c
            if (wordToFind.contains(c)) {
                // if so, we replace _ by the character c
                int index = wordToFind.indexOf(c);

                while (index >= 0) {
                    wordFound[index] = c.charAt(0);
                    index = wordToFind.indexOf(c, index + 1);
                }
            } else {
                // c not in the word => error
                nbErrors++;
            }

            // c is now a letter entered
            letters.add(c);
        }
    }

    // Method returning the state of the word found by the user until by now
    private String wordFoundContent() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < wordFound.length; i++) {
            builder.append(wordFound[i]);

            if (i < wordFound.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    @Override
    public String toString(){
        return current;
    }

    public void hint(){
        int index = -1;
        for (int i = 0; i < wordFound.length; i++){
            if(wordFound[i] == '_'){
                index = i;
                break;
            }
        }
        if (index!=-1){
            enter(String.valueOf(wordToFind.charAt(index)));
            help++;
        }
        String a = "Enter a letter : ";
        // display current state
        a += "\n" + HangmanWordBank[nbErrors%(HangmanWordBank.length)];
        a += "\n" + wordFoundContent();
        if (wordFound()) {
            a += "\n" + "You win the game with the computer help!  (ᵔᴥᵔ) \n";
            a = a.substring(17);
            hasEnded = true;
        } else {
            // we display nb tries remaining for the user
            a += "\n" + "=> Nb tries remaining : " + (MAX_ERRORS - nbErrors);
        }
        current = a;
    }

    public String execute(String guess){
        String a ="";
        // we play while nbErrors is lower than max errors or user has found the word
        if(nbErrors < MAX_ERRORS) {
            a += "Enter a letter : ";
            // get next input from user
            String str = guess;

            // we keep just first letter
            if (str.length() > 1) {
                str = str.substring(0, 1);
            }

            // update word found
            enter(str);

            // display current state
            a += "\n" + HangmanWordBank[nbErrors%(HangmanWordBank.length)];
            a += "\n" + wordFoundContent() +"\n";

            // check if word is found
            if (wordFound()) {
                String b = +  help < 1? "You win!":"You win the game with the computer help!  (ᵔᴥᵔ) \n";
                a+=b;
                a = a.substring(17);
                hasEnded = true;
            } else {
                // we display nb tries remaining for the user
                a += "\n" + "=> Nb tries remaining : " + (MAX_ERRORS - nbErrors);
            }
        }

        if (nbErrors == MAX_ERRORS) {
            // user losed
            hasEnded = true;
            a += "\n" + "You lose!";
            a += "\n" + "=> Word to find was : " + wordToFind;
            a = a.substring(17);
        }
        current = a;
        return a;
    }

    public void quitGame(){
        current+= "\n=> Word to find was : " + wordToFind + "\nYou abandoned this game!";
        hasEnded = true;
    }

    public boolean gameEnded(){
        return hasEnded;
    }

}