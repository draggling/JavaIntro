import java.util.Random;
import java.util.Scanner;

public class Exercise1 {
    public static void main(String[] args) {
        guessRandomNumber();
    }

    public static void guessRandomNumber() {
        Random r = new Random();
        Scanner s = new Scanner(System.in);
        int randNum = r.nextInt(100) + 1;
        for(int guesses = 5; guesses > 0; guesses--) {
            if(guess(s, randNum)) {
                System.out.println("The answer is " + randNum);
                break;
            } else if(guesses > 1) {
                System.out.println("Keep Trying");
            } else {
                System.out.println("Sorry");
            }
        }
    }
    public static boolean guess(Scanner s, int ran) {
        System.out.println("Guess a number from 1 to 100:");
        int input = s.nextInt();
        return (Math.abs(input - ran) <= 10) ? true: false;
    }
}
