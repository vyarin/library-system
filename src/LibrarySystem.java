/*
 * LibrarySystem.java
 *
 * Culminating Performance Task
 * 2023-08-10
 *
 * This program simulates a library system in which a user can set up their
 * own library and utilize several functionalities similar to a real library.
 *
 * The user would be able to:
 *  - Add their own books
 *  - Loan books
 *  - Return books
 *  - Search for books
 *  - Display all books or books on loan
 */

import java.util.Scanner;

public class LibrarySystem extends TestCases {
    // Allow for user input
    private static Scanner input = new Scanner(System.in);

    private static boolean booksSorted = false;
    private static int bookElements = 0;

    // Declare book arrays
    private static String[] bookTitles;
    private static String[] bookAuthors;
    private static int[] bookCopies;
    private static int[] booksOnLoan;

    private static int[] sortedBookTitleIndexes;
    private static int[] sortedBookAuthorIndexes;

    public static void main(String[] args) {
        displayMenu();
        String userInput = getInput(">").toUpperCase();
        String errorMessage = "";

        // Allow user to pick menu options
        while (!userInput.equals("X")) {
            // Check if setup process has been done
            if (bookElements < 1) {
                if (userInput.equals("S")) {
                    setUpBooks();
                    waitToExit();
                } else if (userInput.equals("H")) {
                    displayHelp();
                    waitToExitHelp();
                } else if ("FDOALR".contains(userInput)
                        && !userInput.isEmpty()) {
                    // Check if inputs that require book setup were entered
                    errorMessage = ">Set up books first! [S]\n";
                } else {
                    errorMessage = ">Please enter a valid letter.\n";
                }
            } else {
                switch (userInput) {
                    case "F" -> {
                        searchBooks();
                        waitToExit();
                    }
                    case "D" -> {
                        displayBooks();
                        waitToExit();
                    }
                    case "O" -> {
                        displayOnLoan();
                        waitToExit();
                    }
                    case "A" -> {
                        addBook();
                        waitToExit();
                    }
                    case "L" -> {
                        loanBook();
                        waitToExit();
                    }
                    case "R" -> {
                        returnBook();
                        waitToExit();
                    }
                    case "H" -> {
                        displayHelp();
                        waitToExitHelp();
                    }
                    default -> errorMessage =
                            ">Please enter a valid letter.\n";
                }
            }
            displayMenu();

            // Print and reset error message, if any
            System.out.print(errorMessage);
            errorMessage = "";

            userInput = getInput(">").toUpperCase();
        }
        System.out.println(">Thank you for visiting!");
    }

    /**
     * getInput
     *
     * Given a message, prompt the user.
     *
     * @param message message to be outputted
     * @return        string the user inputted
     */
    private static String getInput(String message) {
        System.out.print(message);
        return input.nextLine().strip();
    }

    /**
     * getChoice
     *
     * Given a message, prompt the user for a "yes" or "no" answer.
     *
     * @param message message to be outputted
     * @return        a boolean indicating yes (true) or no (false)
     */
    private static boolean getChoice(String message) {
        String userInput = getInput(message).toUpperCase();

        // Continue prompting for a yes or no answer
        while (!userInput.equals("Y") && !userInput.equals("N")) {
            System.out.println("Please enter 'Y' or 'N'.");
            userInput = getInput(message).toUpperCase();
        }
        return userInput.equals("Y");
    }

    /**
     * waitToExit
     *
     * Continually prompt user until they exit. Useful for when exiting
     * is the only option.
     */
    private static void waitToExit() {
        System.out.println("\n\n[X] EXIT\n");
        String userInput = getInput(">").toUpperCase();

        // Print input prompt until user exits
        while (!userInput.equals("X")) {
            System.out.println(">Enter 'X' to exit.");
            userInput = getInput(">").toUpperCase();
        }
    }

    /**
     * waitToExitHelp
     *
     * Continually prompt user until they exit the Help Menu.
     */
    private static void waitToExitHelp() {
        String userInput = getInput(">").toUpperCase();

        while (!userInput.equals("X")) {
            if (userInput.equals("E")) {
                System.out.println(">Good job!");
            } else {
                System.out.println(">Not quite!");
            }
            userInput = getInput(">").toUpperCase();
        }
    }

    /**
     * getBookIndex
     *
     * Prompt user to input the location (ID) of a book.
     *
     * @return an integer indicating the index of the book array.
     */
    private static int getBookIndex() {
        int index = -1;

        // Get the index of the book to be replaced
        while (index < 0 || index >= bookElements) {
            // Validate input
            try {
                index = Integer.parseInt(getInput(String.format(
                        "\nEnter the book ID (max: %d, starting from 1): ",
                        bookElements))) - 1;
            } catch (NumberFormatException e) {
                System.out.printf(
                        "Please enter an integer value from 1 to %d.\n",
                        bookElements);
                continue;
            }

            // Validate number input
            if (index < 0 || index >= bookElements) {
                System.out.printf(
                        "Please enter an integer value from 1 to %d.\n",
                        bookElements);
            }
        }
        return index;
    }

    /**
     * displayMenu
     *
     * Display a menu depending on the state of the program.
     */
    private static void displayMenu() {
        // Title ASCII art (displays "std.Library")
        String title = """
                     _      _   _     _ _
                 ___| |_ __| | | |   (_) |__  _ __ __ _ _ __ _   _
                / __| __/ _` | | |   | | '_ \\| '__/ _` | '__| | | |
                \\__ \\ || (_| |_| |___| | |_) | | | (_| | |  | |_| |
                |___/\\__\\__,_(_)_____|_|_.__/|_|  \\__,_|_|   \\__, |
                                                             |___/""";

        // Information text block for when books have not been set up
        String startInformation = """
                
                ENTER LETTER TO START
                
                [S] SET UP BOOKS
                
                [H] HELP
                [X] EXIT
                
                """;

        // Information text block for when books have been set up
        String information = """
                
                ENTER LETTER TO START
                
                [F] SEARCH BOOKS
                [D] DISPLAY ALL BOOKS
                [O] DISPLAY BOOKS ON LOAN
                
                [A] ADD BOOK
                [L] LOAN BOOK
                [R] RETURN BOOK
                
                [H] HELP
                [X] EXIT
                
                """;

        // Print menu based on if books have been set up
        System.out.print(title
                + (bookElements > 0 ? information : startInformation));
    }

    /**
     * displayHelp
     *
     * Display help message.
     */
    private static void displayHelp() {
        System.out.print("""
                HELP!
                
                WHAT DOES THIS PROGRAM DO?
                This program keeps track of books in a virtual library.
                
                HOW DO I NAVIGATE THE MENU?
                To interact with the library, type the key specified
                in the brackets, [] in the menu and click enter:
                
                    Type this! -> [E] EXAMPLE
                    (In this example, 'E' or 'e' should be typed)
                    
                    Try it out!
                
                
                [X] EXIT
                
                """);
    }

    /**
     * setUpBooks
     *
     * Given a user-specified amount of elements, set book array lengths.
     */
    private static void setUpBooks() {
        // Print header
        System.out.println("\nSET UP\n\n");

        // Get the amount of elements and set up length of book arrays
        while (true) {
            // Validate if input is an integer
            try {
                bookElements = Integer.parseInt(getInput(
                        "Enter the amount of unique titles in the library: "));
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer value over 0.\n");
                continue;
            }

            // Validate if array length is possible
            if (bookElements < 1) {
                System.out.println("Please enter a positive value.\n");
            } else {
                // Check if array length can be assigned the given value
                try {
                    bookTitles = new String[bookElements];
                    bookAuthors = new String[bookElements];
                    bookCopies = new int[bookElements];
                    booksOnLoan = new int[bookElements];
                } catch (OutOfMemoryError e) {
                    System.out.println("Too many books!\n");
                    continue;
                }
                break;
            }
        }

        // Assign names to books
        for (int i = 0; i < bookElements; i++) {
            // Print header
            System.out.printf("\nBOOK %d\n\n", i + 1);

            // Only add unique books
            String bookTitle = getInput("Enter the book's title: ");
            String bookAuthor = getInput("Enter the book's author: ");

            while (searchExistingBooks(bookTitle, bookAuthor, i)) {
                System.out.println("This book already exists!\n");
                bookTitle = getInput("Enter the book's title: ");
                bookAuthor = getInput("Enter the book's author: ");
            }

            bookTitles[i] = bookTitle;
            bookAuthors[i] = bookAuthor;

            // Validate if input is an integer
            while (true) {
                try {
                    bookCopies[i] = Integer.parseInt(getInput(
                            "Enter the amount of copies the library owns: "));
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Please enter an integer value over 0.\n");
                    continue;
                }

                // Validate if number of books are possible
                if (bookCopies[i] < 1) {
                    System.out.println("Please enter a positive value.\n");
                } else {
                    break;
                }
            }
        }
        System.out.println("\nBooks set up successfully!");
    }

    /**
     * insertionSort
     *
     * Sort a given array alphabetically. Used to make the array compatible
     * with the binarySearch method.
     *
     * @param array the array to be sorted
     * @return      an array of indexes pertaining to the indexes of the
     *              sorted array
     */
    private static int[] insertionSort(String[] array) {
        // Set up array containing the indexes of a book array
        int[] indexArray = new int[bookElements];
        for (int i = 0; i < indexArray.length; i++) {
            indexArray[i] = i;
        }

        // Insertion sort
        for (int i = 1; i < indexArray.length; i++) {
            int j = i;
            while (j > 0 && (array[j - 1].compareTo(array[j]) > 0)) {
                // Swap elements in book array
                String temp =  array[j - 1];
                array[j - 1] = array[j];
                array[j] = temp;

                // Swap elements in index array
                int indexTemp =  indexArray[j - 1];
                indexArray[j - 1] = indexArray[j];
                indexArray[j] = indexTemp;

                j--;
            }
        }
        return indexArray;
    }

    /**
     * sortBooks
     *
     * Sort and get the indexes of sorted bookTitles and bookAuthors arrays.
     */
    private static void sortBooks() {
        // Pass copies to sort so object references of arrays are not passed
        sortedBookTitleIndexes = insertionSort(bookTitles.clone());
        sortedBookAuthorIndexes = insertionSort(bookAuthors.clone());

        booksSorted = true;
    }

    /**
     * binarySearch
     *
     * Given a query, search for a matching book or author. Used for when user
     * is performing an exact, case-sensitive search for more consistent
     * results.
     *
     * @param indexes     an array of indexes of a sorted array
     * @param searchArray the array to be searched
     * @param query       the search term/query
     * @return            an array containing the indexes of search results
     */
    private static int[] binarySearch(int[] indexes, String[] searchArray,
              String query) {
        int arrayLength = searchArray.length;

        // Initial starting points
        int midpoint = arrayLength / 2;
        int start = 0;
        int end = arrayLength - 1;

        // Keep track of search results
        int[] searchResultIndexes = new int[arrayLength];

        // Keep track of final index of searchResultIndexes
        int index = 0;

        // Binary search
        while (end >= start) {
            // Search term found
            if (searchArray[indexes[midpoint]].compareTo(query) == 0) {
                searchResultIndexes[index] = indexes[midpoint];
                index++;
                break;
            }

            if (searchArray[indexes[midpoint]].compareTo(query) > 0) {
                // Search term is before the midpoint
                end = midpoint - 1;
            } else if (searchArray[indexes[midpoint]].compareTo(query) < 0) {
                // Search term is after the midpoint
                start = midpoint + 1;
            }
            midpoint = ((start + end) / 2);
        }

        // If search term found, search neighbouring terms for duplicates
        if (end >= start) {
            int foundIndex = midpoint;

            // Search array left
            while (foundIndex > 0
                    && searchArray[indexes[foundIndex - 1]]
                    .compareTo(query) == 0) {
                searchResultIndexes[index] = indexes[--foundIndex];
                index++;
            }

            // Search array right
            foundIndex = midpoint;
            while (foundIndex < arrayLength - 1
                    && searchArray[indexes[foundIndex + 1]]
                    .compareTo(query) == 0) {
                searchResultIndexes[index] = indexes[++foundIndex];
                index++;
            }
        }

        // Indicate where searchResultIndexes ends if not full
        if (index < searchResultIndexes.length) {
            searchResultIndexes[index] = -1;
        }
        return searchResultIndexes;
    }

    /**
     * substringSearch
     *
     * Given a query, search if any string in the given array contains it.
     *
     * @param searchArray the array to be searched
     * @param query       the search term/query
     * @return            an array containing the indexes of search results
     */
    private static int[] substringSearch(String[] searchArray, String query) {
        // Convert to uppercase to ignore case when searching
        query = query.toUpperCase();

        // Keep track of search results
        int[] searchResultIndexes = new int[bookElements];

        // Keep track of final index of searchResultIndexes
        int index = 0;

        // Search if element in search array contains the query
        for (int i = 0; i < searchArray.length; i++) {
            if (searchArray[i].toUpperCase().contains(query)) {
                searchResultIndexes[index] = i;
                index++;
            }
        }

        // Indicate where searchResultIndexes ends if not full
        if (index < searchResultIndexes.length) {
            searchResultIndexes[index] = -1;
        }
        return searchResultIndexes;
    }

    /**
     * searchExistingBooks
     *
     * Check if a book already exists in library arrays (case-sensitive).
     *
     * @param title    the title of the book
     * @param author   the author of the book
     * @param endpoint where the book names end
     * @return         a boolean: true if the book exists, else false
     */
    private static boolean searchExistingBooks(String title, String author,
            int endpoint) {
        for (int i = 0; i < endpoint; i++) {
            if (bookTitles[i].equals(title) && bookAuthors[i].equals(author)) {
                return true;
            }
        }
        return false;
    }

    /**
     * searchBooks
     *
     * Given a query, search for a title or author and display the results.
     */
    private static void searchBooks() {
        String query = getInput("\nEnter a search query (use double quotes to "
                + "search an exact title or author): ");

        // Query is a full name of a title or author if surrounded in quotes
        boolean specificSearch = false;
        if (query.length() > 1) {
             specificSearch = query.charAt(0) == '"'
                    && query.charAt(query.length() - 1) == '"';
        }

        // Keep track of indexes of search results
        int[] bookIndexes;

        // Prompt user to search titles or authors
        String userInput = getInput(
                "\nDo you want to search book titles [T] or authors [A]: ")
                .toUpperCase();
        while (!"TA".contains(userInput) || userInput.isEmpty()) {
            System.out.println("Please enter 'T' or 'A'.");
            userInput = getInput(
                    "\nDo you want to search book titles [T] or authors [A]: ")
                    .toUpperCase();
        }

        // Store user's choice
        boolean searchTitlesFirst = userInput.equals("T");

        // Perform search
        if (specificSearch) {
            if (!booksSorted) {
                sortBooks();
            }

            // Remove outside double quotes from specific search term
            String queryCopy = query;
            query = "";

            for (int i = 1; i < queryCopy.length() - 1; i++) {
                query += queryCopy.substring(i, i + 1);
            }

            // Use binarySearch
            if (searchTitlesFirst) {
                bookIndexes = binarySearch(sortedBookTitleIndexes,
                        bookTitles, query);
            } else {
                bookIndexes = binarySearch(sortedBookAuthorIndexes,
                        bookAuthors, query);
            }
        } else {
            // Use substringSearch
            if (searchTitlesFirst) {
                bookIndexes = substringSearch(bookTitles, query);
            } else {
                bookIndexes = substringSearch(bookAuthors, query);
            }
        }

        // Count search results
        int searchResults = 0;
        for (int i = 0; i < bookIndexes.length && bookIndexes[i] != -1; i++) {
            searchResults++;
        }

        // Print search results
        System.out.printf("\nSEARCH RESULTS (%d)\n", searchResults);
        if (searchResults > 0) {
            for (int i = 0; i < searchResults; i++) {
                int j = bookIndexes[i];
                System.out.printf("""
                    
                    - "%s" by %s
                    \tID: %d
                    \tCopies owned by library: %d
                    \tCopies out: %d
                    """, bookTitles[j],  bookAuthors[j], j + 1,
                        bookCopies[j], booksOnLoan[j]);
            }
        } else {
            System.out.println("\nNo books found!");
        }
    }

    /**
     * displayBooks
     *
     * Print all books.
     */
    private static void displayBooks() {
        // Print header
        System.out.printf("\nBOOK LIST (%d)\n", bookElements);

        // Print list of all books
        for (int i = 0; i < bookElements; i++) {
            System.out.printf("""
                    
                    - "%s" by %s
                    \tID: %d
                    """, bookTitles[i], bookAuthors[i],
                    i + 1);
        }
    }

    /**
     * displayOnLoan
     *
     * Print all books out on loan.
     */
    private static void displayOnLoan() {
        int loaned = 0;

        // Count books on loan
        for (int books : booksOnLoan) {
            loaned += books;
        }

        // Print header
        System.out.printf("\nBOOKS ON LOAN (%d)\n", loaned);

        if (loaned > 0) {
            // Print list of all books on loan
            for (int i = 0; i < booksOnLoan.length; i++) {
                if (booksOnLoan[i] > 0) {
                    System.out.printf("""
                            
                            - "%s" by %s
                            \tID: %d
                            \tCopies out: %d
                            """, bookTitles[i], bookAuthors[i],
                            i + 1, booksOnLoan[i]);
                }
            }
        } else {
            System.out.println("\nThere are no books out on loan.");
        }
    }

    /**
     * addBook
     *
     * Add a book to the book arrays by replacing an existing one.
     */
    private static void addBook() {
        // Only add unique books
        String title = getInput("\nEnter the title of the book: ");
        String author = getInput("Enter the author of the book: ");

        while (searchExistingBooks(title, author, bookElements)) {
            System.out.println("This book already exists!\n");
            title = getInput("Enter the title of the book: ");
            author = getInput("Enter the author of the book: ");
        }

        // Print header and opening information
        System.out.println("\nADD A BOOK\n\n");
        System.out.printf("You are adding the book: \"%s\" by %s.\n", title,
                author);

        int index;
        do {
            // Prompt user on if they want to search for a book
            if (getChoice("\nDo you want to search for a book to replace? "
                    + "[Y/N]: ")) {
                searchBooks();
            }

            // Get book array index
            index = getBookIndex();
        } while (!getChoice(String.format("""
                
                You are replacing "%s" by %s with "%s" by %s.
                Are you sure this is the book you want to replace? [Y/N]:\s""",
                bookTitles[index], bookAuthors[index], title, author)));

        // Replace book with new book
        bookTitles[index] = title;
        bookAuthors[index] = author;
        booksOnLoan[index] = 0;

        // Validate if input is an integer
        while (true) {
            try {
                bookCopies[index] = Integer.parseInt(getInput(
                        "\nEnter the amount of copies the library owns of this"
                        + " book: "));
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer value over 0.");
                continue;
            }

            // Validate if number of books are possible
            if (bookCopies[index] < 1) {
                System.out.println("Please enter a positive value.");
            } else {
                break;
            }
        }

        // Books are most likely unsorted and would need to be sorted again
        booksSorted = false;

        System.out.println("\nBook added successfully!");
    }

    /**
     * loanBook
     *
     * Allow a user to loan a book. Add a book to the booksOnLoan array by
     * prompting the user to loan a book.
     */
    private static void loanBook() {
        boolean loaningPossible = false;

        // Check if there is at least one book available to loan
        for (int i = 0; i < bookElements; i++) {
            if (bookCopies[i] > booksOnLoan[i]) {
                loaningPossible = true;
                break;
            }
        }

        // Prompt to loan if books are available
        if (loaningPossible) {
            int index;
            do {
                // Prompt user on if they want to search for a book
                if (getChoice("\nDo you want to search for a book to "
                        + "loan? [Y/N]: ")) {
                    searchBooks();
                }

                // Get book array index
                index = getBookIndex();

                // Check if book can be loaned
                while ((bookCopies[index] - booksOnLoan[index]) < 1) {
                    System.out.println(
                            "No books available to loan! Please choose "
                            + "another book.");
                    index = getBookIndex();
                }
            } while (!getChoice(String.format("""
                    
                    You are loaning "%s" by %s.
                    Are you sure this is the book you want to loan? [Y/N]:\s""",
                    bookTitles[index], bookAuthors[index])));

            booksOnLoan[index]++;
            System.out.println("\nBook loaned successfully!");
        } else {
            System.out.println("\nNo books available to loan!");
        }
    }

    /**
     * returnBook
     *
     * Allow the user to return a book. Remove a book from the booksOnLoan
     * array by prompting the user to return a book.
     */
    private static void returnBook() {
        boolean returningPossible = false;

        // Check if there is at least one book available to return
        for (int books : booksOnLoan) {
            if (books > 0) {
                returningPossible = true;
                break;
            }
        }

        // Prompt to return if books are available
        if (returningPossible) {
            int index;
            do {
                // Prompt user on if they want to display books on loan
                if (getChoice("\nDo you want to display the books on "
                        + "loan? [Y/N]: ")) {
                    displayOnLoan();
                }

                // Get book array index
                index = getBookIndex();

                // Check if book can be replaced
                while (booksOnLoan[index] < 1) {
                    System.out.println(
                            "No books available to return! Please choose "
                            + "another book.");
                    index = getBookIndex();
                }

                System.out.printf("\nYou are returning \"%s\" by %s.",
                        bookTitles[index], bookAuthors[index]);
            } while (!getChoice("\nAre you sure this is the book you want "
                    + "to return? [Y/N]: "));

            booksOnLoan[index]--;
            System.out.println("\nBook returned successfully!");
        } else {
            System.out.println("\nNo books available to return!");
        }
    }
}
