package io.hexlet.typoreporter.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BundleSourceUtils {
    @Autowired
    private MessageSource messageSource;

    /**
     * @param key - Ключ из messages_LANG.properties, по которому необходимо получить значение
     *            Для RU необходимо использовать new Locale("ru")
     * @param locale - Параметр, по которому определяется язык (локализация)
     * @param isReplaceToQuot - Признак необходимости замены " на &quot;
     * @param args - Аргументы с типом String, которые необходимо подставить при интерполяции
     * @return Строка из resource bundle изменениями
     */
    public String getValueByKey(String key, Locale locale, boolean isReplaceToQuot, String[] args) {
        String value = messageSource.getMessage(key, null, locale);
        if (args != null) {
            Pattern pattern = Pattern.compile("(\\$\\{(\\w+)\\})|(\\{(\\w+)\\})");
            Matcher matcher = pattern.matcher(value);
            while (matcher.find()) {
                value = value.replace(matcher.group(), "%s");
            }
            value = isReplaceToQuot
                ? value.replace("\"", "&quot;")
                : value;
            return String.format(value, args);
        }
        return isReplaceToQuot ? value.replace("\"", "&quot;") : value;
    }

    /**
     * @param key - Ключ из messages_LANG.properties, по которому необходимо получить значение
     * @param isReplaceToQuot - Признак необходимости замены " на &quot;
     * @param args - Аргументы с типом String, которые необходимо подставить при интерполяции
     * @return - Строка из resource bundle изменениями, по умолчанию на английском языке
     */
    public String getValueByKey(String key, boolean isReplaceToQuot, String[] args) {
        return getValueByKey(key, Locale.ENGLISH, isReplaceToQuot, args);
    }

    /**
     * @param key - Ключ из messages_LANGUAGE.properties, по которому необходимо получить значение.
     * @param isReplaceToQuot - Признак необходимости замены " на &quot;
     * @return - Строка из resource bundle изменениями, по умолчанию на английском языке
     */
    public String getValueByKey(String key, boolean isReplaceToQuot) {
        return getValueByKey(key, isReplaceToQuot, null);
    }

    /**
     * @param key - Ключ из messages_LANG.properties, по которому необходимо получить значение
     * @param args - Аргументы с типом String, которые необходимо подставить при интерполяции
     * @return - Строка из resource bundle, по умолчанию на английском языке
     */
    public String getValueByKey(String key, String[] args) {
        return getValueByKey(key, false, args);
    }

    /**
     * @param key - Ключ из messages_LANG.properties, по которому необходимо получить значение
     * @return - Строка из resource bundle, по умолчанию на английском языке
     */
    public String getValueByKey(String key) {
        return getValueByKey(key, null);
    }
}
