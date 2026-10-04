package se.amir;

public class Library {
    private Book[] books;
    private Member[] borrowedBy;
    private int bookCount;

    private Member[] members;
    private int memberCount;
    private int nextMemberId;

    public Library(int bookCapacity, int memberCapacity) {
        books = new Book[bookCapacity];
        borrowedBy = new Member[bookCapacity];
        members = new Member[memberCapacity];
        bookCount = 0;
        memberCount = 0;
        nextMemberId = 1;
    }

    public void addBook(Book book) {
        if (bookCount == books.length) {
            growBookArrays();
        }
        books[bookCount] = book;
        borrowedBy[bookCount] = null;
        bookCount++;
    }

    public Member registerMember(String name) {
        if (memberCount == members.length) {
            growMemberArray();
        }
        Member member = new Member(nextMemberId, name);
        nextMemberId++;
        members[memberCount] = member;
        memberCount++;
        return member;
    }

    private void growBookArrays() {
        int newCapacity = books.length * 2;
        Book[] newBooks = new Book[newCapacity];
        Member[] newBorrowedBy = new Member[newCapacity];

        for (int i = 0; i < bookCount; i++) {
            newBooks[i] = books[i];
            newBorrowedBy[i] = borrowedBy[i];
        }

        books = newBooks;
        borrowedBy = newBorrowedBy;
    }

    private void growMemberArray() {
        Member[] newMembers = new Member[members.length * 2];

        for (int i = 0; i < memberCount; i++) {
            newMembers[i] = members[i];
        }

        members = newMembers;
    }

    public String borrowBook(String isbn, int memberId) {
        int index = findBookIndex(isbn);
        if (index == -1) {
            return "Det finns ingen bok med ISBN " + isbn + ".";
        }

        Member member = findMember(memberId);
        if (member == null) {
            return "Det finns ingen medlem med medlemsnummer " + memberId + ".";
        }

        if (borrowedBy[index] != null) {
            return "Boken är redan utlånad till " + borrowedBy[index].getName() + ".";
        }

        if (!member.canBorrowMore()) {
            return member.getName() + " har redan lånat max antal böcker (" + Member.getMaxLoans() + " st).";
        }

        borrowedBy[index] = member;
        member.addLoan();
        return member.getName() + " har nu lånat \"" + books[index].title() + "\".";
    }

    public String returnBook(String isbn) {
        int index = findBookIndex(isbn);
        if (index == -1) {
            return "Det finns ingen bok med ISBN " + isbn + ".";
        }

        Member member = borrowedBy[index];
        if (member == null) {
            return "Boken är inte utlånad.";
        }

        member.removeLoan();
        borrowedBy[index] = null;
        return member.getName() + " har lämnat tillbaka \"" + books[index].title() + "\".";
    }

    public void sortBooksByTitle() {
        for (int i = 0; i < bookCount - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < bookCount - 1 - i; j++) {
                if (books[j].title().compareToIgnoreCase(books[j + 1].title()) > 0) {
                    Book tempBook = books[j];
                    books[j] = books[j + 1];
                    books[j + 1] = tempBook;

                    Member tempMember = borrowedBy[j];
                    borrowedBy[j] = borrowedBy[j + 1];
                    borrowedBy[j + 1] = tempMember;

                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    public int[] searchBooks(String query) {
        String search = query.toLowerCase();

        int matches = 0;
        for (int i = 0; i < bookCount; i++) {
            if (matchesSearch(books[i], search)) {
                matches++;
            }
        }

        int[] result = new int[matches];
        int position = 0;
        for (int i = 0; i < bookCount; i++) {
            if (matchesSearch(books[i], search)) {
                result[position] = i;
                position++;
            }
        }
        return result;
    }

    private boolean matchesSearch(Book book, String search) {
        return book.title().toLowerCase().contains(search)
                || book.author().toLowerCase().contains(search);
    }

    public Member getMemberWithMostLoans() {
        Member top = null;
        for (int i = 0; i < memberCount; i++) {
            Member member = members[i];
            if (member.getActiveLoans() > 0
                    && (top == null || member.getActiveLoans() > top.getActiveLoans())) {
                top = member;
            }
        }
        return top;
    }

    public int findBookIndex(String isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].isbn().equalsIgnoreCase(isbn)) {
                return i;
            }
        }
        return -1;
    }

    public Member findMember(int id) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getId() == id) {
                return members[i];
            }
        }
        return null;
    }

    public int getBookCount() {
        return bookCount;
    }

    public Book getBook(int index) {
        return books[index];
    }

    public Member getBorrower(int index) {
        return borrowedBy[index];
    }

    public int getMemberCount() {
        return memberCount;
    }

    public Member getMember(int index) {
        return members[index];
    }
}
