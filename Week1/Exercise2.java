import java.util.Scanner;

public class Exercise2 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        /* Ask for Player Names */
        System.out.print("What is the name of the first player? ");
        String p1 = s.next();
        System.out.print("\nWhat is the name of the second player? ");
        String p2 = s.next();
        while(p1.equalsIgnoreCase(p2)) {
            System.out.print("\nBoth players cannot be named " + p2 + ". Enter a different name: ");
            p2 = s.next();
        }
        /* Ask for # of tokens */
        int chips = 0;
        while (chips < 3 || chips % 2 == 0) {
            System.out.print("\nHow many chips does the pile contain? (must be odd and greater than 1) ");
            chips = s.nextInt();
            if(chips < 3) {
                System.out.println("\nYou have to start with at least 3 chips");
            } else if (chips % 2 == 0) {
                System.out.println("\nYou have to start with an odd number of chips");
            }
        }
        startGame(p1, p2, chips);
    }
    public static void startGame(String p1, String p2, int chips) {
        int pile = chips;
        int p1Chips = 0;
        int p2Chips = 0;
        boolean first = true;
        while(pile > 0) {
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * \n");
            System.out.printf("%s has %d chips.\n", p1, p1Chips);
            System.out.printf("%s has %d chips.\n", p2, p2Chips);
            if(first) {
                int pull = getChips(p1, pile);
                p1Chips += pull;
                pile -= pull;
            } else {
                int pull = getChips(p2, pile);
                p2Chips += pull;
                pile -= pull;
            }
            first = !first;
        }
        System.out.printf("%s has %d chips.\n", p1, p1Chips);
        System.out.printf("%s has %d chips.\n", p2, p2Chips);
        if(p1Chips % 2 == 1) {
            System.out.printf("%s wins!\n", p1);
        } else {
            System.out.printf("%s wins!\n", p2);
        }
        System.out.print("\n\nPlay another game? (y/n) ");
        Scanner s = new Scanner(System.in);
        String replay = s.next();
        if(replay.equalsIgnoreCase("y")) {
            startGame(p1, p2, chips);
        }
    }
    public static int getChips(String player, int pile) {
        Scanner s = new Scanner(System.in);
        int max = ((pile - (pile % 2)) / 2);
        System.out.println("It is your turn, " + player + ".");
        if(pile == 1) {
            System.out.println("There is 1 chip remaining");
        } else {
            System.out.println("There are " + pile + " chips remaining.");
        }
        if(max == 1 || pile == 1) {
            System.out.println("\nyou must take 1 chip, " + player);
            return 1;
        } else {
            int pull = 0;
            while(pull <= 0 || pull > max) {
                System.out.print("\nYou may take any number of chips from 1 to "+ max + ".  How many will you take, " + player + "? ");
                pull = s.nextInt();
                if(pull <= 0) {
                    System.out.println("\nIllegal move: you must take at least one chip");
                } else if(pull > max) {
                    System.out.println("\nIllegal move: you may not take more than " + max + " chips.");
                }
            }
            return pull;
        }
    }
}
