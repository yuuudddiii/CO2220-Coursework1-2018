import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class WordSleuth extends AbstractRandomWordGame{
	
	Random rand = new Random();
	String wordToGuess;
	int highestPossibleScore;
	int guessesLeft;
	List<Character> revealedLetters;
	String unrevealedLetters;
	boolean gameOver;
	String wordWithRevealedLetters;
	
	public WordSleuth(InputStream input, PrintStream output) {
	        this(input, output, null, null);
	    }

	public WordSleuth(InputStream input, PrintStream output, Path wordsFilePath) {
	        this(input, output, wordsFilePath, null);
	    }

	public WordSleuth(InputStream input, PrintStream output, Path wordsFilePath, Charset wordsFileCharset) {
	        super(input, output, wordsFilePath, wordsFileCharset);
	    }
	
	public void play() {
		displayRules();
		initialGameState();
		mainLoop();
	}	
	
	private void displayRules() {
		System.out.println("\n----------------W E L C O M E    T O    W O R D  S L E U T H-----------------------\n");
		System.out.println("Rules: ");
		System.out.println("1. You are tasked with guessing a word randomly selected from a dictionary");
		System.out.println("2. You will have a limited number of guesses depending on the size of the word");
		System.out.println("3. The first letter of the word, length of the word, and number of guesses you have, will be revealed to you");
		System.out.println("4. After every wrong guess, a letter from the word will be revealed without giving away its position in the word");
		System.out.println("5. After using up all your guesses, you will be given one last chance to guess with the revealed letters displayed in the correct order");
		System.out.println("\n G O O D  L U C K    A N D    H A V E  F U N!! \n" );
	}
	
	private void showGameBoard() {
		
		System.out.println("The word you are trying to guess starts with the letter '" + wordToGuess.charAt(0) + "'");
		System.out.println("The word contains " + wordToGuess.length() + " letters");
		System.out.print("|| You have " + guessesLeft + " guesses left || ");
		System.out.print("The revealed letter(s) are: ");
		showRevealedLetters();
		System.out.print(" ||");
		
	}
	
	private void mainLoop() {
		while (!gameOver) {
			showGameBoard();
			askUserToGuess(); 
			String guess = getUserGuess();
			updateGameState(guess);
			checkForEndState(guess);
			}
	}
	
	private void initialGameState() {
		wordToGuess = getRandomWord();
		unrevealedLetters = wordToGuess;
		highestPossibleScore = wordToGuess.length() * 2;
		guessesLeft = (int) Math.ceil(((wordToGuess.length() / 2)) + 1);
		revealedLetters = new ArrayList<Character>();
		gameOver = false;
		wordWithRevealedLetters = "";
	}
	
	private void askUserToGuess() {
		output.println();
        output.print("Enter next guess: ");
    }
	
	private String getUserGuess() {
        String s = input.nextLine();
        return s;
    }
	
	private boolean isGoodGuess(String guess) {
        return wordToGuess.equalsIgnoreCase(guess); 
    }
	
	private void updateGameState(String guess) {
		if (isGoodGuess(guess)) return;
		
		else {
			deductHighestPossibleScore(1);
			revealNewLetter();
			deductGuessesLeft(1);
		}
	}
	
	private void revealNewLetter() {
		// Select a random letter from unrevealedLetters to reveal 
		char temp = unrevealedLetters.charAt(rand.nextInt(unrevealedLetters.length())); 
		// Make sure the letter to be revealed is not the first letter of wordToGuess as that it already given
		if(temp == unrevealedLetters.charAt(0)) revealNewLetter();
		else {
		// Reveal all occurrences of that letter in unrevealedLetters
		for (int i = 0; i<unrevealedLetters.length(); i++) if (unrevealedLetters.charAt(i) == temp) revealedLetters.add(temp);
		
		// Remove all occurrences of the letter that has been added into revealedLetters from unrevealedLetters
		StringBuilder sb = new StringBuilder(unrevealedLetters);
		for (int i = 0; i<sb.length(); i++) if (sb.charAt(i) == temp) sb.deleteCharAt(i);
		unrevealedLetters = sb.toString();
		}
	}
	
	private void showRevealedLetters() {
		Character [] revealedLettersArray = new Character [revealedLetters.size()] ;
		revealedLetters.toArray(revealedLettersArray);
		for (int i = 0; i<revealedLettersArray.length; i++) {
			if (revealedLettersArray[i] != null) System.out.print(revealedLettersArray[i] + " ");
		}
	}
 	
	private void showArrangedRevealedLetters() {
		for (int i = 0; i<wordToGuess.length(); i++) {
			if(revealedLetters.contains(wordToGuess.charAt(i))) System.out.print(wordToGuess.charAt(i));
		}
	}
	
	private void checkForEndState(String guess) {
		if (isGoodGuess(guess)) {
			gameOver = true;
			playerWonMessage();
		}
		
		else {
			if (guessesLeft == -1) {
				deductHighestPossibleScore(2);
				System.out.println("You have used up all your guesses, you will be given one final chance to guess the word");
				System.out.print("The word to be guessed with the revealed letters in the correct order is: ");
				showArrangedRevealedLetters();
				askUserToGuess();
				String finalGuess = getUserGuess();
				if (!isGoodGuess(finalGuess)) deductGuessesLeft(1);
				checkForEndState(finalGuess);
			}
			
			else if (guessesLeft>=0) System.out.println("That was not the word, please try again \n");
			
			else { 
				gameOver = true;
				playerLostMessage();
			}
		}
	}
	
	private void playerWonMessage() {
		System.out.println("Congratulations, you have guessed the word correctly!!");
		System.out.println("Your score is: " + highestPossibleScore);
		System.out.println("The word was: " + wordToGuess);
	}
	
	private void playerLostMessage() {
		System.out.println("You have failed to guess the word!! :( ");
		System.out.println("The word was: " + wordToGuess);
	}
	
	private void deductHighestPossibleScore(int n) {
		highestPossibleScore -= n;
	}
	
	private void deductGuessesLeft(int n) {
		guessesLeft -= n;
	}
	
}
