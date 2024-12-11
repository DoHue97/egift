package com.crm.egift.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    static public String customFormat(double value ) {
        Locale locale = new Locale("en", "EN");
       // Locale locale = new Locale("el", "GR");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getNumberInstance(locale);

        decimalFormat.applyPattern(pattern);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(value);

    }
    static public String customFormatPriceForGr(double value ) {
        //Locale locale = new Locale("en", "EN");
         Locale locale = new Locale("el", "GR");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getNumberInstance(locale);

        decimalFormat.applyPattern(pattern);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(value);

    }


    private static ObjectMapper initMapper() {
        ObjectMapper mapper = new ObjectMapper();

        //mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }

    public  static String secretCardNumber(String number){
        if (number == null)
            return number;
        String cardNumber = number.replace(" ","");
        if (cardNumber.length() < 8)
            return cardNumber;

        String maskedCard = "";
        String firstSet = cardNumber.substring(0,4)+ " " + cardNumber.substring(4,6) + "**" ;
        maskedCard = maskedCard + firstSet;
        String lastSet = "";
        for (int i=8; i < cardNumber.length();) {
            if (i+4 >= cardNumber.length()) {
                lastSet = cardNumber.substring(i, cardNumber.length());
                maskedCard += " " + lastSet;
            } else {
                maskedCard += " ****";
            }
            i = i+4;
        }
        return maskedCard;
    }

    public static String maskCardNumber(String cardNumber, String mask) {

        // format the number
        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(cardNumber.charAt(index));
                index++;
            } else if (c == '*') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }
        return maskedNumber.toString();
    }
}
