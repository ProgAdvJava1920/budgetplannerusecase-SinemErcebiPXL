package be.pxl.student.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetPlannerImporterTest {
    Path csvFilePath = Paths.get("src/test/resources/account_payments_test.csv");

    @Test
    void read_csv_file_should_return_non_empty_list() throws BudgetPlannerException {
        assertFalse(BudgetPlannerImporter.readCsvFile(csvFilePath).isEmpty());
    }

    @Test
    void read_csv_file_should_return_list_of_size_3() throws BudgetPlannerException {
        assertEquals(3, BudgetPlannerImporter.readCsvFile(csvFilePath).size());
    }

    @Test
    void read_csv_file_should_throw_exception_when_csv_file_does_not_exist() {
        assertThrows(BudgetPlannerException.class, () -> {
            BudgetPlannerImporter.readCsvFile(Paths.get("noFileToBeFoundHere"));
        });
    }

    @Test
    void read_csv_file_should_throw_exception_when_passing_null() {
        assertThrows(BudgetPlannerException.class, () -> {
            BudgetPlannerImporter.readCsvFile(null);
        });
    }
}
