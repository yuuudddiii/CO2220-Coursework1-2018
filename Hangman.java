/* 1) It is redundant to have 2 methods to check for duplicates (whether the guess is right or wrong) 
 * Since all guesses are added to the guesses list, there is no need to check the next guess against 
 * the wordProgress for correct guesses. To check if the guess is a duplicate, checking against the guesses list
 * is enough. This way, 3 methods; isDuplicateGuess, isDuplicateWrongGuess and isDuplicateCorrectGuess, can be combined
 * into one method : 
 * 
 * public boolean is DuplicateGuess(guess) {
 * 	return (guesses.contains(Character.toUpper(guess) || Character.toLower(guess))
 * }
 * 
 * 
 * 2) In the UpdateWordProgress method :
 * Instead of calling the temporary variable, 'lowerCaseAnswer', it could be called lowerCaseWordToGuess to avoid confusion
 * between wordToGuess and Answer since the variable lowerCaseGuess is used as a temporary variable for 'guess'
 * The temporary variable answerCharacter seems redundant as it is obvious what it is for, which is to obtain 
 * the character that is in the specified index in wordToGuess. Hence the temporary variable can be voided and 
 * the builder.setCharAt can use wordToGuess.charAt(index) as 2nd parameter instead. 
 * Creating too many variables may cause confusion in understand the logical flow of certain code.
 * 
 * 3) In the UpdateGameState method: 
 * Temporary variable 'goodGuess' is not needed as it is only used once and in the next if loop
 * isGoodGuess(guess) can be used as a guard instead.
 * 
 * 
 * 
 */


import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Hangman extends AbstractRandomWordGame {
    private List<Character> guesses; //list of guesses the user has made
    private String wordToGuess; //word that the user has to guess
    private int badGuesses; //number of times the user has guessed wrongly
    private String wordProgress; //the current number of letters of the word that the user has guessed
    private boolean gameRunning; // is the game running?
    
    // Game skeleton
    
    private static final String[] hangman = { 
            "",
            "_________%n",
            " |%n |%n |%n |%n_|_______%n",
            " ______%n |/%n |%n |%n |%n_|_______%n",
            " ______%n |/   |%n |%n |%n |%n_|_______%n",
            " ______%n |/   |%n |    O%n |%n |%n_|_______%n",
            " ______%n |/   |%n |    O%n |    |%n |%n_|_______%n",
            " ______%n |/   |%n |    O%n |   /|%n |%n_|_______%n",
            " ______%n |/   |%n |    O%n |   /|\\%n |%n_|_______%n",
            " ______%n |/   |%n |    O%n |   /|\\%n |   /%n_|_______%n",
            " ______%n |/   |%n |    O%n |   /|\\%n |   / \\%n_|_______%n"
    };
    
    
    public Hangman(InputStream input, PrintStream output) {
        this(input, output, null, null);
    }

    public Hangman(InputStream input, PrintStream output, Path wordsFilePath) {
        this(input, output, wordsFilePath, null);
    }

    public Hangman(InputStream input, PrintStream output, Path wordsFilePath, Charset wordsFileCharset) {
        super(input, output, wordsFilePath, wordsFileCharset);
    }
    
    // Play the game
    
    public void play() {
        initialiseGameState();
        mainLoop();
    }
    
    // Start the game with initialized conditions
    
    private void initialiseGameState() {
        guesses = new ArrayList<Character>(); // create the user guesses list
        wordToGuess = getRandomWord(); // obtain the word for the user to guess
        badGuesses = 0; // number of bad guesses start at 0 as user hasn't made a guess
        wordProgress = getCensoredAnswer(wordToGuess);
        gameRunning = true; // game is running
    }

    private void mainLoop() {
        while (gameRunning) {
            showGameBoard(); //displays the current game with all variables
            askUserToGuess(); // ask user for a guess
            char guess = getUserGuess(); // temporary variable for user guess
            updateGameState(guess); // update game state for the guess given
            checkForEndState();// check if it is time to end the game
        }
        showGameBoard();//once the loop has ended show the final state to the user
        showEndGameMessage();
    }
    
    //update the state of the game
    
    // Temporary variable goodGuess is not needed as it is only used once and in the incident if loop
    // isGoodGuess(guess) can be used as a guard instead.
    
    private void updateGameState(char guess) {
        if (isDuplicateGuess(guess)) {
            return;
        }

        boolean goodGuess = isGoodGuess(guess); // his line is not needed
        if (goodGuess) { // change to if(isGoodGuess(guess) {
            wordProgress = updateWordProgress(guess);
        } else {
            badGuesses++; // add to the number of bad guesses made
            guesses.add(guess); // add the guessed letter to the list of guessed letters
        }
    }
    
    // Check if the guess is a duplicate guess
    
    private boolean isDuplicateGuess(char guess) {
        return isDuplicateWrongGuess(guess) || isDuplicateCorrectGuess(guess);
    }
    
    // Check if the wrongly guessed letter has been guessed before
    
    private boolean isDuplicateWrongGuess(char guess) {
        char upper = Character.toUpperCase(guess);
        char lower = Character.toLowerCase(guess);
        return guesses.contains(upper) || guesses.contains(lower);
    }
    
    // Check if the correctly guessed letter has been checked before
    
    private boolean isDuplicateCorrectGuess(char guess) {
        char candidate = Character.toLowerCase(guess);

        //the letters in wordProgress are always lower case. This is enforced in the updateWordProgress() method
        return wordProgress.indexOf(candidate) != -1;
    }
    
    // Asks user for next guess input
    private void askUserToGuess() {
		output.println();
        output.print("Enter next guess: ");
    }

    private String getCensoredAnswer(String answer) {
        return answer.replaceAll(".", "-");
    }
    
    
    // Show the conditions of the game
    
    private void showGameBoard() {
        String string = "WORD:\t\t\tWRONG GUESSES:%n%s\t\t\t%s%n%n" + hangman[badGuesses];
        output.format(string, wordProgress, guesses);
    }
    
    //Get the user's next guess
    
    private char getUserGuess() {
        String s = input.nextLine();
        return s.charAt(0);
    }
    
    // Checks if the letter guessed is inside the word to be guessed
    private boolean isGoodGuess(char guess) {
        String candidate = "" + guess; // create temporary variable containing guess letter to be verified
        return wordToGuess.toLowerCase().contains(candidate.toLowerCase()); // is the guess letter in the word to be guessed?
    }
    
    // 
    private String updateWordProgress(char guess) {
        String lowerCaseAnswer = wordToGuess.toLowerCase(); // New String variable lowerCaseAnswer = "apple"
        char lowerCaseGuess = Character.toLowerCase(guess); // New char variable lowerCaseGuess = lower case of player's guess
        StringBuilder builder = new StringBuilder(wordProgress); // new StringBuilder variable builder = the word progress
        int index = lowerCaseAnswer.indexOf(lowerCaseGuess); // New integer variable index = index of player's guess in "apple"
        while(index >= 0) { // if the guessed letter is in "apple" :
            char answerCharacter = wordToGuess.charAt(index);  // New char variable answerCharacter = index of guess in "apple"
            builder.setCharAt(index, answerCharacter); // set the word progress' index of the guessed letter to the guessed letter
            index = lowerCaseAnswer.indexOf(lowerCaseGuess, index+1); //  check if the letter appears again in "apple"
        } // if letter appears again, index >= 0 , if letter does not appear again, index = -1 and the loop ends
        
        return builder.toString(); // word progress is updated to be the newly updated string
    }
    
    // Checks whether it is time to end the game
    
    private void checkForEndState() {
        if (userHasGuessedTheWord() || userHasUsedAllTheirGuesses()) {
            gameRunning = false;
        }
    }
    
    // Checks if the user has guessed the word
    
    private boolean userHasGuessedTheWord() {
        return wordToGuess.equalsIgnoreCase(wordProgress);
    }

    // Checks if the user has used up all their lives
    
    private boolean userHasUsedAllTheirGuesses() {
        return badGuesses == hangman.length-1;
    }

    // Message displayed at the end of the game
    
    private void showEndGameMessage() {
        if (wordToGuess.equalsIgnoreCase(wordProgress)) {
            output.println("You won! The word was " + wordToGuess + ". Congratulations.");
        } else {
            output.println("You lose. Better luck next time!");
        }
    }

    public static void main(String[] args) {
        Hangman hangman = new Hangman(System.in, System.out);
        hangman.play();
    }
}

