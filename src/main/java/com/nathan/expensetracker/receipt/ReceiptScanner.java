package com.nathan.expensetracker.receipt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class ReceiptScanner {
    private static final Pattern TOTAL = Pattern.compile(
            "^(?:grand\\s+total|total\\s+(?:due|paid)|amount\\s+(?:due|paid)|total)\\s*[:=]?\\s*\\$?\\s*(\\d{1,3}(?:,\\d{3})+\\.\\d{2}|\\d+\\.\\d{2})\\s*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern DATE = Pattern.compile(
            "\\b(\\d{1,2})/(\\d{1,2})/(\\d{4})\\b");

    public Result scan(Path image) throws IOException, InterruptedException {
        Path output = Files.createTempFile("receipt-ocr-", ".txt");
        try {
            Process process = new ProcessBuilder(executable(), image.toAbsolutePath().toString(),
                    "stdout", "-l", "eng")
                    .redirectOutput(output.toFile())
                    .redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start();
            try {
                if (!process.waitFor(20, TimeUnit.SECONDS)) {
                    throw new IOException("Receipt scan timed out. Try a smaller image.");
                }
                if (process.exitValue() != 0) {
                    throw new IOException("Tesseract could not read this image.");
                }
                return parse(new String(Files.readAllBytes(output), java.nio.charset.StandardCharsets.UTF_8));
            } finally {
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
            }
        } finally {
            Files.deleteIfExists(output);
        }
    }

    private String executable() {
        for (String path : new String[]{"/opt/homebrew/bin/tesseract", "/usr/local/bin/tesseract"}) {
            if (Files.isExecutable(java.nio.file.Paths.get(path))) {
                return path;
            }
        }
        return "tesseract";
    }

    static Result parse(String text) {
        String merchant = "";
        String amount = "";
        boolean conflictingTotals = false;
        for (String rawLine : text.split("\\R")) {
            String line = rawLine.trim();
            var match = TOTAL.matcher(line);
            if (match.matches()) {
                String candidate = new java.math.BigDecimal(match.group(1).replace(",", "")).toPlainString();
                conflictingTotals |= !amount.isEmpty() && !amount.equals(candidate);
                amount = candidate;
            } else if (merchant.isEmpty() && line.matches("[\\p{L}][\\p{L} &'.-]{1,59}")
                    && !line.toLowerCase(Locale.ROOT).matches(".*\\b(receipt|invoice|subtotal|tax|total|thank you)\\b.*")) {
                merchant = line;
            }
        }
        return new Result(merchant, conflictingTotals ? "" : amount, parseReceiptDate(text));
    }

    static LocalDate parseReceiptDate(String text) {
        var match = DATE.matcher(text);
        LocalDate date = null;
        while (match.find()) {
            try {
                int month = Integer.parseInt(match.group(1));
                int day = Integer.parseInt(match.group(2));
                int year = Integer.parseInt(match.group(3));
                LocalDate candidate = LocalDate.of(year, month, day);
                if (date != null && !date.equals(candidate)) {
                    return null;
                }
                date = candidate;
            } catch (DateTimeException e) {
                return null;
            }
        }
        return date;
    }

    public static final class Result {
        public final String merchant;
        public final String amount;

        public final LocalDate date;

        private Result(String merchant, String amount, LocalDate date) {
            this.merchant = merchant;
            this.amount = amount;
            this.date = date;
        }
    }
}
