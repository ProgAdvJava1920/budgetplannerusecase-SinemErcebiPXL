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
        ArrayList<String> output = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine(); // ignore header line

            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException | NullPointerException e) {
            throw new BudgetPlannerException("Something went wrong reading csv file", e);
        }
        return output;
    }
}