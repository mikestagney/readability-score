package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int wordTotal = 0;
        int sentenceTotal = 0;
        int characterTotal = 0;
        int syllableTotal = 0;
        int pollySyllableTotal = -1;

        Scanner userInput = new Scanner(System.in);

        File fileName = new File(args[0]);

        System.out.println("The text is: ");

        try (Scanner input = new Scanner(fileName)) {
            while (input.hasNextLine()) {
                String text = input.nextLine();
                System.out.print(text);

                sentenceTotal += text.split("[.?!]").length;
                String[] words = text.split("[\\s]");

                for (String word : words) {
                    characterTotal += word.toCharArray().length;

                    int syllable = countSyllables(word);
                    syllableTotal += syllable;
                    if (syllable > 2) {
                        pollySyllableTotal++;
                    }
                }
                wordTotal += words.length;
            }

        } catch (FileNotFoundException e) {
            System.out.println("Can't find that file. Sorry!");
        }

        double ARIscore = autoReadIndex(characterTotal, wordTotal, sentenceTotal);
        int ARIlevel = levelText(ARIscore);

        double fleshKincadScore = felschKincaid(wordTotal, sentenceTotal, syllableTotal);
        int fKLevel = levelText(fleshKincadScore);

        double smogIndexScore = smogIndex(pollySyllableTotal, sentenceTotal);
        int smogLevel = levelText(smogIndexScore);

        double coleLiauscore = colemanLiauIndex(characterTotal, wordTotal, sentenceTotal);
        int coleLiauLevel = levelText(coleLiauscore);

        double averageScore = (double)(ARIlevel + fKLevel + smogLevel + coleLiauLevel) / 4.0;

        System.out.println();
        System.out.println();
        System.out.printf("Words: %d%n", wordTotal);
        System.out.printf("Sentences: %d%n", sentenceTotal);
        System.out.printf("Characters: %d%n", characterTotal);
        System.out.printf("Syllables: %d%n", syllableTotal);
        System.out.printf("Polysyllables: %d%n", pollySyllableTotal);
        System.out.println();
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        String userChoice = userInput.nextLine();

         switch (userChoice) {
            case "ARI":
                System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", ARIscore, ARIlevel);
                break;
            case "FK":
                System.out.printf("Flesch-Kincaid readability tests: %.2f (about %d year olds).%n", fleshKincadScore, fKLevel);
                break;
            case "SMOG":
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", smogIndexScore, smogLevel);
                break;
            case "CL":
                System.out.printf("Coleman-Liau index: %.2f (about %d year olds).%n", coleLiauscore, coleLiauLevel);
                break;
            default:
                System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", ARIscore, ARIlevel);
                System.out.printf("Flesch-Kincaid readability tests: %.2f (about %d year olds).%n", fleshKincadScore, fKLevel);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", smogIndexScore, smogLevel);
                System.out.printf("Coleman-Liau index: %.2f (about %d year olds).%n", coleLiauscore, coleLiauLevel);
                System.out.println();
                System.out.printf("This text should be understood in average by %.2f year olds.%n", averageScore);
        }


    }
    static int levelText(double score) {
        int scoreInt = (int)Math.round(score);
        int level = scoreInt + 6;
        //adjustments to match Automated Readability Index table
        if (scoreInt <= 2) {
            level -= 1;
        }
        if (score >= 13) {
            level = 24;
        }
        return level;
    }
    public static int countSyllables(String word) {
        int syllables = 0;
        int len = word.length();

        for (int i = 0; i < len; i++) {
            if (word.substring(i, i + 1).matches("[aeiouy]")) {
                syllables++;
                if (i + 1 < len && word.substring(i + 1, i + 2).matches("[aeiouy]")) {
                    syllables--;
                }
            }
        }
        if (word.charAt(len - 1) == 'e') {
            syllables--;        }
        if (syllables == 0) {
            syllables = 1;
        }
        return syllables;
    }

    static double autoReadIndex(int characterTotal, int wordTotal, int sentenceTotal) {
        return 4.71 * (double)characterTotal / (double)wordTotal
                + 0.5 * (double)wordTotal / (double)sentenceTotal - 21.43;

    }
    static double felschKincaid(int wordTotal, int sentenceTotal, int syllableTotal) {
        return 0.39 * (double)wordTotal / (double)sentenceTotal
                + 11.8 * (double)syllableTotal / (double)wordTotal - 15.59;

    }
    static double smogIndex(int polysyllables, int sentenceTotal) {
        return 1.043 * Math.sqrt(polysyllables * 30.0 / sentenceTotal) + 3.1291;

    }
    static double colemanLiauIndex(int characterTotal, int wordTotal, int sentenceTotal) {
        return 0.0588 * characterTotal / wordTotal * 100.0 - 0.296 * sentenceTotal / wordTotal * 100.0 - 15.8;
    }


}
