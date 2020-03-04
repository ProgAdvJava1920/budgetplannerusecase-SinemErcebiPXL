package be.pxl.student.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetPlannerImporterTest
{
    Path testCsvFile = Paths.get("src/test/resources/account_payments_test.csv");

    @Test
    void read_Csv_File_should_return_none_empty_list() throws BudgetPlannerException {
        assertFalse(BudgetPlannerImporter.readCsvFile(testCsvFile).isEmpty());
    }

    @Test
    void read_Csv_File_should_return_list_of_size_2() throws BudgetPlannerException {
        assertEquals(2, BudgetPlannerImporter.readCsvFile(testCsvFile).size());
    }

    @Test
    void read_Csv_File_should_throw_our_own_exception_when_csv_file_does_not_exist()
    {
        assertThrows(BudgetPlannerException.class, () -> {
            BudgetPlannerImporter.readCsvFile(Paths.get("none existing path"));
        });
    }

    @Test
    void read_Csv_File_should_throw_our_own_exception_when_passing_null()
    {
        assertThrows(BudgetPlannerException.class, () -> {
            BudgetPlannerImporter.readCsvFile(null);
        });
    }
}