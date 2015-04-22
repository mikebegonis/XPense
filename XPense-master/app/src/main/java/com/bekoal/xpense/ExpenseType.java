package com.bekoal.xpense;

public enum ExpenseType
{
    Food,
    Lodging,
    Gas,
    CarRental,
    Other;

    @Override
    public String toString() {
        switch (this) {
            case Food:
                return "Food";
            case Lodging:
                return "Lodging";
            case Gas:
                return "Gas";
            case CarRental:
                return "CarRental";
            case Other:
                return "Other";
            default:
                return "Expense";
        }
    };
}
