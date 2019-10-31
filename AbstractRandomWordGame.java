import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public abstract class AbstractRandomWordGame {
    protected List<String> words;
    protected Random random;
    protected Scanner input;
    protected PrintStream output;
    protected static final Path defaultWordsFilePath = Paths.get("gamedictionary.txt");
    protected static final Charset defaultWordsFileCharset = Charset.forName("UTF-8");
    protected Path wordsFilePath;
    protected Charset wordsFileCharset;

    public AbstractRandomWordGame(InputStream input, PrintStream output) {
        this(input, output, null, null);
    }

    public AbstractRandomWordGame(InputStream input, PrintStream output, Path wordsFilePath) {
        this(input, output, wordsFilePath, null);
    }

    public AbstractRandomWordGame(InputStream input, PrintStream output, Path wordsFilePath, Charset wordsFileCharset) {
        this.input = new Scanner(input);
        this.output = output;
        this.wordsFilePath = wordsFilePath;
        this.wordsFileCharset = wordsFileCharset;
        random = new Random();
        loadWords();
    }

    //TODO: protected or private?
    //TODO: rename to tell of side effects
    private void loadWords() {
        if (wordsFilePath == null) wordsFilePath = defaultWordsFilePath;
        if (wordsFileCharset == null) wordsFileCharset = defaultWordsFileCharset;

        try {
            words = Files.readAllLines(wordsFilePath, wordsFileCharset);
        } catch (IOException e) {
            System.err.println("Error reading file '" + wordsFilePath + "'");
            words = getHardcodedWordList();
        }
    }

    protected String getRandomWord() {
        int r = random.nextInt(words.size());
        return words.get(r);
    }

    abstract void play();
    
    public List<String> getHardcodedWordList() {
    	List<String> defaultWordList;
    	return defaultWordList = Arrays.asList("compilation", "popstar", "symphony");
    }
}
