package generic.exceptions;

public class SeedWordsFormatException extends Exception {
    public SeedWordsFormatException(String str) {
        super(str+" is wrong seed format, the correctly word is looks like part1-part2-part3");
    }
}
