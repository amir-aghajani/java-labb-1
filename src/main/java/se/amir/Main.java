package se.amir;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Library library = new Library(5, 5);

    public static void main(String[] args) {
        addSampleData();
        boolean running = true;

        while (running) {
            clearScreen();
            printMenu();
            String choice = scanner.nextLine().trim().toLowerCase();
            clearScreen();

            switch (choice) {
                case "1":
                    addBook();
                    break;
                case "2":
                    registerMember();
                    break;
                case "3":
                    borrowBook();
                    break;
                case "4":
                    returnBook();
                    break;
                case "5":
                    searchBooks();
                    break;
                case "6":
                    showAllBooks();
                    break;
                case "7":
                    showAllMembers();
                    break;
                case "8":
                    showTopBorrower();
                    break;
                case "9":
                    showMemberLoans();
                    break;
                case "e":
                    running = false;
                    System.out.println("Hej då!");
                    break;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
            }

            if (running) {
                waitForEnter();
            }
        }
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void waitForEnter() {
        System.out.println();
        System.out.print("Tryck Enter för att gå tillbaka till menyn...");
        scanner.nextLine();
    }

    private static void addSampleData() {
        library.addBook(new Book("9789100119451", "Bröderna Lejonhjärta", "Astrid Lindgren"));
        library.addBook(new Book("9780261102217", "The Hobbit", "J.R.R. Tolkien"));
        library.addBook(new Book("9780441172719", "Dune", "Frank Herbert"));
        library.addBook(new Book("9789129688310", "Pippi Långstrump", "Astrid Lindgren"));
        library.addBook(new Book("9780451524935", "1984", "George Orwell"));
        library.addBook(new Book("9780132350884", "Clean Code", "Robert C. Martin"));

        library.registerMember("Anna Svensson");
        library.registerMember("Erik Johansson");
    }

    private static void printMenu() {
        System.out.println("Bibliotekshanteraren");
        System.out.println("====================");
        System.out.println("1. Lägg till bok");
        System.out.println("2. Registrera medlem");
        System.out.println("3. Låna bok");
        System.out.println("4. Lämna tillbaka bok");
        System.out.println("5. Sök bok (titel eller författare)");
        System.out.println("6. Visa alla böcker och status");
        System.out.println("7. Visa alla medlemmar");
        System.out.println("8. Visa medlem med flest aktiva lån");
        System.out.println("9. Visa lån för en medlem");
        System.out.println("e. Avsluta");
        System.out.print("Ditt val: ");
    }

    private static void addBook() {
        String isbn = readIsbn();
        if (library.findBookIndex(isbn) != -1) {
            System.out.println("Det finns redan en bok med det ISBN-numret.");
            return;
        }
        String title = readText("Titel: ");
        String author = readText("Författare: ");

        library.addBook(new Book(isbn, title, author));
        System.out.println("Boken har lagts till.");
    }

    private static void registerMember() {
        String name = readText("Namn: ");
        Member member = library.registerMember(name);
        System.out.println("Medlemmen är registrerad och har medlemsnummer " + member.getId() + ".");
    }

    private static void borrowBook() {
        int available = 0;
        System.out.println("Tillgängliga böcker:");
        for (int i = 0; i < library.getBookCount(); i++) {
            if (library.getBorrower(i) == null) {
                System.out.println("- " + library.getBook(i));
                available++;
            }
        }

        if (available == 0) {
            System.out.println("Det finns inga lediga böcker att låna just nu.");
            return;
        }

        System.out.println();
        String isbn = readText("Bokens ISBN: ");
        int memberId = readNumber("Medlemsnummer: ");
        System.out.println(library.borrowBook(isbn, memberId));
    }

    private static void returnBook() {
        String isbn = readText("Bokens ISBN: ");
        System.out.println(library.returnBook(isbn));
    }

    private static void searchBooks() {
        String query = readText("Sök på titel eller författare: ");
        int[] matches = library.searchBooks(query);

        if (matches.length == 0) {
            System.out.println("Inga träffar på \"" + query + "\".");
            return;
        }

        if (matches.length == 1) {
            System.out.println("1 träff:");
        } else {
            System.out.println(matches.length + " träffar:");
        }
        for (int i = 0; i < matches.length; i++) {
            printBookWithStatus(matches[i]);
        }
    }

    private static void showAllBooks() {
        if (library.getBookCount() == 0) {
            System.out.println("Det finns inga böcker i biblioteket ännu.");
            return;
        }

        library.sortBooksByTitle();
        for (int i = 0; i < library.getBookCount(); i++) {
            printBookWithStatus(i);
        }
    }

    private static void showAllMembers() {
        if (library.getMemberCount() == 0) {
            System.out.println("Det finns inga medlemmar ännu.");
            return;
        }

        for (int i = 0; i < library.getMemberCount(); i++) {
            System.out.println("- " + library.getMember(i));
        }
    }

    private static void showTopBorrower() {
        Member top = library.getMemberWithMostLoans();

        if (top == null) {
            System.out.println("Ingen medlem har några lån just nu.");
        } else {
            System.out.println(top.getName() + " har flest lån just nu ("
                    + top.getActiveLoans() + " st).");
        }
    }

    private static void showMemberLoans() {
        int memberId = readNumber("Medlemsnummer: ");
        Member member = library.findMember(memberId);

        if (member == null) {
            System.out.println("Det finns ingen medlem med medlemsnummer " + memberId + ".");
            return;
        }

        if (member.getActiveLoans() == 0) {
            System.out.println(member.getName() + " har inga lån just nu.");
            return;
        }

        System.out.println(member.getName() + " har lånat:");
        for (int i = 0; i < library.getBookCount(); i++) {
            if (library.getBorrower(i) == member) {
                System.out.println("- " + library.getBook(i));
            }
        }
    }

    private static void printBookWithStatus(int index) {
        Book book = library.getBook(index);
        Member borrower = library.getBorrower(index);

        String status;
        if (borrower == null) {
            status = "Tillgänglig";
        } else {
            status = "Utlånad till " + borrower.getName();
        }
        System.out.println("- " + book + " - " + status);
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Fältet får inte vara tomt.");
        }
    }

    private static String readIsbn() {
        while (true) {
            String isbn = readText("ISBN (10 eller 13 siffror): ");
            if (isValidIsbn(isbn)) {
                return isbn;
            }
            System.out.println("Ogiltigt ISBN, det ska vara 10 eller 13 siffror.");
        }
    }

    private static boolean isValidIsbn(String isbn) {
        if (isbn.length() != 10 && isbn.length() != 13) {
            return false;
        }
        for (int i = 0; i < isbn.length(); i++) {
            if (!Character.isDigit(isbn.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static int readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Du måste skriva ett nummer, t.ex. 1.");
            }
        }
    }
}
