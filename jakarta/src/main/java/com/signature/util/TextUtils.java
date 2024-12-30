package com.signature.util;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.NumberFormat;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    public static String generateRandomAlphanumeric(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        return sb.toString();
    }

    public static String generateSecureRandomAlphanumeric(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        return sb.toString();
    }

    public static boolean isEmpty(String value) {
        return StringUtils.isEmpty(value);
    }

    public static Long parseLong(String value) {
        if (isEmpty(value)) {
            return null;
        }
        return Long.parseLong(value);
    }

    public static Integer parseInteger(String value) {
        if (isEmpty(value)) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public static boolean hasValue(String string) {
        return string != null && string.trim().length() != 0;
    }

    private static Map<Character, Character> chars;

    static {
        chars = new HashMap<>();
        chars.put('\u0643', '\u06a9');
        chars.put('\ufef1', '\u06cc');
        chars.put('\u064a', '\u06cc');
    }

    public static String sanitizeNumber(String textWithNumber, Locale targetLocale) throws ParseException {
        NumberFormat format = NumberFormat.getNumberInstance(targetLocale);
        format.setGroupingUsed(false);
        String regex = "([\u06600]*)([\u0660-\u06690-9]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(textWithNumber);
        String number;
        String oneToNineDigits;
        String zeros;
        while (m.find()) {
            number = m.group(0);
            zeros = m.group(1);
            oneToNineDigits = m.group(2);

            MessageFormat messageFormat = new MessageFormat("{0,number,#}", targetLocale);
            Number parsedOneToNineDigits = format.parse(oneToNineDigits);
            oneToNineDigits = messageFormat.format(new Number[]{parsedOneToNineDigits});

            StringBuilder builder = new StringBuilder();
            if (!"".equals(zeros)) {
                Number parsedZeros = format.parse(zeros);
                int zeroCount = zeros.length();
                zeros = messageFormat.format(new Number[]{parsedZeros});
                for (int i = 0; i < zeroCount; i++) {
                    builder.append(zeros);
                }
            }

            builder.append(oneToNineDigits);
            textWithNumber = textWithNumber.replaceFirst(number, builder.toString());
        }

        return textWithNumber;
    }

    public static String sanitizeNumber(String number) throws ParseException {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        format.setGroupingUsed(false);
        String regex = "([\u06600]*)([\u0660-\u06690-9]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(number);
        String numbers;
        if (m.find()) {
            numbers = m.groupCount() == 2 ? m.group(2) : m.group(1);
            Number n = format.parse(numbers);
            number = String.format("%0" + number.length() + "d", n);
        }

        return number;
    }

    public static String sanitizeString(String text) {
        for (char c : chars.keySet()) {
            text = text.replaceAll(String.valueOf(c), String.valueOf(chars.get(c)));
        }

        return text;
    }
}
