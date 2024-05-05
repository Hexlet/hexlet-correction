package io.hexlet.typoreporter.utils;

import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@UtilityClass
public final class TextUtils {

    public static String toLowerCaseData(final String inputData) {
        return inputData.toLowerCase(Locale.ROOT);
    }
    public static String trimUrl(final String inputUrl) {
        URL url = null;
        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            //TODO: write custom exception
            throw new RuntimeException(e);
        }
        return url.getProtocol() + "://" + url.getHost();
    }
}
