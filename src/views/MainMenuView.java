package views;

import models.User;
import models.UsedHashtag;
import models.MentionedDriver;
import controllers.UserController;
import controllers.TweetController;
import controllers.DriverController;
import controllers.HashtagController;
import uy.edu.um.prog2.adt.collection.MyCollection;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainMenuView {
    private static final Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

    public static void main(String[] args) {
        LoadDataView.loadData();

        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        while (selection != 0) {
            System.out.println("====== Choose the one of the following reports ======");
            System.out.println("-----------------------------------------------------");
            System.out.println("1 - Top 10 most mentioned drivers in a month and year");
            System.out.println("2 - Top 15 users with more tweets");
            System.out.println("3 - Number of different hashtags for a given day");
            System.out.println("4 - Most used hashtag for a given day (excluding #F1)");
            System.out.println("5 - Top 7 users with most favorites");
            System.out.println("6 - Number of tweets with a specific word or phrase");
            System.out.println("0 - Exit");
            System.out.println("-----------------------------------------------------");
            System.out.print("Enter the selected option: ");


            try {
                selection = scanner.nextInt();
            } catch (NoSuchElementException ignored) {
                selection = -1;
            }
            switch (selection) {
                case 1 -> {
                    System.out.println("========== Listing most mentioned drivers! ==========\n");
                    getMostMentionedDrivers();
                }
                case 2 -> {
                    System.out.println("========== Listing users with more tweets! ==========\n");
                    getMostActiveUsers();
                }
                case 3 -> {
                    System.out.println("======= Getting number of different hashtags! =======\n");
                    getNumberOfDifferentHashtags();
                }
                case 4 -> {
                    System.out.println("======= Getting most used hashtag in the day! =======\n");
                    getMostUsedHashtag();
                }
                case 5 -> {
                    System.out.println("=========== Listing most favorited users! ===========\n");
                    getMostFavoritedUsers();
                }
                case 6 -> {
                    System.out.println("============= Getting number of tweets! =============\n");
                    getNumberOfTweetsWithString();
                }
                case 0 -> System.out.println("====================== Exiting! =====================\n");
                default -> System.out.println("================== Invalid option! ==================\n");
            }
        } 
    }

    private static void getMostMentionedDrivers() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Insert the month: ");
        byte month;
        try {
            month = scanner.nextByte();
        } catch (InputMismatchException ignored) {
            System.out.println("\nInvalid month!");
            waitForEnter();
            return;
        }

        System.out.print("Insert the year: ");
        short year;
        try {
            year = scanner.nextShort();
        } catch (InputMismatchException ignored) {
            System.out.println("\nInvalid year!");
            waitForEnter();
            return;
        }

        System.out.println();

        DriverController driverController = DriverController.getInstance();
        final long startTime = System.nanoTime();

        MyCollection<MentionedDriver> mostMentionedDrivers = driverController.getMostMentionedDriversByMonthAndYear(month, year, 10);
        final long elapsedTime = System.nanoTime() - startTime;

        for (MentionedDriver driver: mostMentionedDrivers) {
            System.out.println("Driver " + driver.getFullName() + " is mentioned " + driver.mentionsCount() + " times");
        }
        System.out.println("\nReport took " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " milliseconds!");

        waitForEnter();
    }
    
    private static void getMostActiveUsers() {
        UserController userController = UserController.getInstance();
        final long startTime = System.nanoTime();

        MyCollection<User> mostActiveUsers = userController.getMostActiveUsers(15);
        final long elapsedTime = System.nanoTime() - startTime;

        for (User user: mostActiveUsers) {
            System.out.println("User \"" + user.getName() + "\" has tweeted " + user.getTweetsCount() + " times and " + (user.isVerified() ? "is verified" : "is not verified"));
        }
        System.out.println("\nReport took " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " milliseconds!");

        waitForEnter();
    }
    
    private static void getNumberOfDifferentHashtags() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert the date (yyyy-mm-dd): ");
        String date;
        try {
            date = scanner.next();
        } catch (NoSuchElementException ignored) {
            System.out.println("\nEmpty date!");
            waitForEnter();
            return;
        }

        if (!datePattern.matcher(date).matches()) {
            System.out.println("\nInvalid date format!");
            waitForEnter();
            return;
        }

        byte day = Byte.parseByte(date.substring(8, 10));
        byte month = Byte.parseByte(date.substring(5, 7));
        short year = Short.parseShort(date.substring(0, 4));

        System.out.println();

        HashtagController hashtagController = HashtagController.getInstance();
        final long startTime = System.nanoTime();

        int numberOfDifferentHashtags = hashtagController.getNumberOfDifferentHashtagsInADay(day, month, year);
        final long elapsedTime = System.nanoTime() - startTime;

        System.out.println("In the day " + date + ", " + numberOfDifferentHashtags + " different hashtags were used");
        System.out.println("\nReport took " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " milliseconds!");

        waitForEnter();
    }
    
    private static void getMostUsedHashtag() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert the date (yyyy-mm-dd): ");
        String date;
        try {
            date = scanner.next();
        } catch (NoSuchElementException ignored) {
            System.out.println("\nEmpty date!");
            waitForEnter();
            return;
        }

        if (!datePattern.matcher(date).matches()) {
            System.out.println("\nInvalid date format!");
            waitForEnter();
            return;
        }

        byte day = Byte.parseByte(date.substring(8, 10));
        byte month = Byte.parseByte(date.substring(5, 7));
        short year = Short.parseShort(date.substring(0, 4));

        System.out.println();

        HashtagController hashtagController = HashtagController.getInstance();
        final long startTime = System.nanoTime();

        UsedHashtag mostUsedHashtag = hashtagController.getMostUsedHashtagInTheDay(day, month, year);
        final long elapsedTime = System.nanoTime() - startTime;

        if (mostUsedHashtag != null) {
            System.out.println("In the day " + date + ", the most used hashtag is #" + mostUsedHashtag.getHashtagText() + " with " + mostUsedHashtag.getUsesCount() + " uses");
        } else {
            System.out.println("In the day " + date + ", no hashtags were used");
        }
        System.out.println("\nReport took " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " milliseconds!");

        waitForEnter();
    }
    
    private static void getMostFavoritedUsers() {
        UserController userController = UserController.getInstance();
        final long startTime = System.nanoTime();

        MyCollection<User> mostFavoritedUsers = userController.getMostFavoritedUsers(7);
        final long elapsedTime = System.nanoTime() - startTime;

        for (User user: mostFavoritedUsers) {
            System.out.println("User \"" + user.getName() + "\" has " + user.getFavoritesCount() + " favorites");
        }
        System.out.println("\nReport took " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " milliseconds!");

        waitForEnter();
    }

    private static void getNumberOfTweetsWithString() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Insert the string: ");
        String searchString;
        try {
            searchString = scanner.next();
        } catch (NoSuchElementException ignored) {
            System.out.println("\nEmpty string!");
            waitForEnter();
            return;
        }

        System.out.print("Match case (y/n): ");
        boolean caseSensitive;
        try {
            String selectionString = scanner.next();
            caseSensitive = selectionString.equals("y");
        } catch (NoSuchElementException ignored) {
            System.out.println("\nEmpty string!");
            waitForEnter();
            return;
        }

        System.out.println();

        TweetController tweetController = TweetController.getInstance();
        final long startTime = System.nanoTime();

        int numberOfTweets = tweetController.getNumberOfTweetsWithString(searchString, caseSensitive);
        final long elapsedTime = System.nanoTime() - startTime;

        switch (numberOfTweets) {
            case 0 -> System.out.println("There are no tweets that contain the string \"" + searchString + "\"");
            case 1 -> System.out.println("There is one tweet that contain the string \"" + searchString + "\"");
            default -> System.out.println("There are " + numberOfTweets + " tweets that contain the string \"" + searchString + "\"");
        }
        System.out.println("\nReport took " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " milliseconds!");

        waitForEnter();
    }

    private static void waitForEnter() {
        System.out.print("Press enter to go back to the menu...");
        try {
            int ignored = System.in.read();
            System.out.println();
        } catch (IOException ignored) {}
    }
}
