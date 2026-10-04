package se.amir;

public class Member {
    private static final int MAX_LOANS = 3;

    private int id;
    private String name;
    private int activeLoans;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
        this.activeLoans = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActiveLoans() {
        return activeLoans;
    }

    public boolean canBorrowMore() {
        return activeLoans < MAX_LOANS;
    }

    public void addLoan() {
        activeLoans++;
    }

    public void removeLoan() {
        if (activeLoans > 0) {
            activeLoans--;
        }
    }

    public static int getMaxLoans() {
        return MAX_LOANS;
    }

    @Override
    public String toString() {
        return "#" + id + " " + name + " (" + activeLoans + " av " + MAX_LOANS + " lån)";
    }
}
