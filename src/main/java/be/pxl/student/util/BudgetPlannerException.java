package be.pxl.student.util;

public class BudgetPlannerException extends Exception {
    public BudgetPlannerException(String message)
    {
        super(message);
    }

    public BudgetPlannerException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    public BudgetPlannerException(Throwable throwable)
    {
        super(throwable);
    }
}
