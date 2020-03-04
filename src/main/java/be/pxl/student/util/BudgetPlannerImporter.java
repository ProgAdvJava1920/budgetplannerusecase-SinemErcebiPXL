package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Util class to import csv file
 */
public class BudgetPlannerImporter {
    public static List<String> readCsvFile(Path path) throws BudgetPlannerException {
        List<String> fileLines = new ArrayList<>();
        BufferedReader reader = null;

        try
        {
            reader = Files.newBufferedReader(path);
            String line = reader.readLine(); // ignore first line

            while ((line = reader.readLine()) != null)
            {
                fileLines.add(line);
            }
        }
        catch (IOException | NullPointerException ex)
        {
            throw new BudgetPlannerException("Something went wrong reading csv file", ex);
        }

        return fileLines;
    }
}
