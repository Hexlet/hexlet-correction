package io.hexlet.typoreporter.utils;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public final class TextUtils {

    public static String toLowerCaseData(final String inputData) {
        return inputData.toLowerCase(Locale.ROOT);
    }

}
