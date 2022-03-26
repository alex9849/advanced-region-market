package net.alex9849.arm;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Messages extends MessageVariables {
    public enum MessageLocale {
        EN("en"), DE("de"), FR("fr"), RU("ru"), ES("es"),
        ZH_CN("zh_cn");
        private String code;

        MessageLocale(String code) {
            this.code = code;
        }

        String code() {
            return code;
        }

        public static MessageLocale byCode(String code) {
            for (MessageLocale locale : MessageLocale.values()) {
                if (locale.code().equalsIgnoreCase(code)) {
                    return locale;
                }
            }
            return null;
        }
    }

    public static void reload(File savePath, MessageLocale locale) {
        YamlConfiguration config = updateAndWriteConfig(locale, savePath);
        load(config);
    }

    private static YamlConfiguration updateAndWriteConfig(MessageLocale locale, File savePath) {
        YamlConfiguration config;
        if (savePath.exists()) {
            config = YamlConfiguration.loadConfiguration(savePath);
        } else {
            config = new YamlConfiguration();
        }
        int configVersion = config.getInt("FileVersion");
        int newConfigVersion = configVersion;
        ConfigurationSection localeconfigMessages = null;
        int localeFileVersion = 0;
        if (MessageLocale.EN != locale) {
            InputStreamReader reader = new InputStreamReader(AdvancedRegionMarket.getInstance()
                    .getResource("messages_" + locale.code() + ".yml"), Charset.forName("UTF-8"));
            YamlConfiguration localeConfig = YamlConfiguration.loadConfiguration(reader);
            localeFileVersion = localeConfig.getInt("FileVersion");
            localeconfigMessages = localeConfig.getConfigurationSection("Messages");
        }

        ConfigurationSection configMessages = config.getConfigurationSection("Messages");
        if (configMessages == null) {
            configMessages = new YamlConfiguration();
        }

        boolean fileUpdated = false;
        for (Field field : Messages.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Message.class)) {
                continue;
            }
            int requestedVersion = getRequestedVersion(field);
            String serializedKey = getSerializedKey(field);
            if ((configVersion >= requestedVersion)
                    && configMessages.get(serializedKey) != null
                    && field.getType().isAssignableFrom(configMessages.get(serializedKey).getClass())) {
                continue;
            }
            Object replaceMessage = getMessage(field);
            if (localeconfigMessages != null) {
                Object localeConfigMessage = localeconfigMessages.get(serializedKey);
                if (localeConfigMessage != null && localeFileVersion >= requestedVersion
                        && field.getType().isAssignableFrom(localeConfigMessage.getClass())) {
                    replaceMessage = localeConfigMessage;
                }
            }

            configMessages.set(serializedKey, replaceMessage);
            newConfigVersion = Math.max(newConfigVersion, requestedVersion);
            fileUpdated = true;
        }

        if (fileUpdated) {
            config.set("FileVersion", newConfigVersion);
            config.set("Messages", configMessages);
            config.options().copyDefaults(true);
            try {
                config.save(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    private static void load(YamlConfiguration config) {
        ConfigurationSection cs = config.getConfigurationSection("Messages");
        if (cs == null) {
            return;
        }

        for (Field field : Messages.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Message.class)) {
                field.setAccessible(true);
                Object parsedOption = cs.get(getSerializedKey(field));
                if (parsedOption instanceof String) {
                    parsedOption = ChatColor.translateAlternateColorCodes('&', (String) parsedOption);

                } else if (parsedOption instanceof List<?>) {
                    List<String> parsedOptionList = new ArrayList<>();
                    for (Object listElement : (List<?>) parsedOption) {
                        if (listElement instanceof String) {
                            parsedOptionList.add(ChatColor.translateAlternateColorCodes('&', (String) listElement));
                        }
                    }
                    parsedOption = parsedOptionList;
                }

                try {
                    if (field.getType().isAssignableFrom(parsedOption.getClass())) {
                        field.set(Messages.class, parsedOption);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(false);
            }
        }
    }

    private static Object getMessage(Field field) {
        if (!field.isAnnotationPresent(Message.class)) {
            return null;
        }
        List<String> messageList = Arrays.asList(field.getAnnotation(Message.class).message());
        if (field.getType().isAssignableFrom(String.class)) {
            return getStringList(messageList, x -> x, "\n");
        }
        return messageList;
    }

    private static String getSerializedKey(Field field) {
        if (field.isAnnotationPresent(Message.class)) {
            String annotationValue = field.getAnnotation(Message.class).name();
            if (annotationValue.isEmpty()) {
                annotationValue = field.getName();
            }
            return annotationValue;
        }
        return null;
    }

    private static int getRequestedVersion(Field field) {
        if (field.isAnnotationPresent(Message.class)) {
            return field.getAnnotation(Message.class).version();
        }
        return 0;
    }

    public static String convertYesNo(Boolean bool) {
        if (bool) {
            return Messages.YES;
        } else {
            return Messages.NO;
        }
    }

    public static String convertEnabledDisabled(boolean bool) {
        if (bool) {
            return Messages.ENABLED;
        } else {
            return Messages.DISABLED;
        }
    }

    public static <X> String getStringList(Iterable<X> xList, StringGetter<X> stringGetter, String splitter) {
        StringBuilder sb = new StringBuilder();

        Iterator<X> iterator = xList.iterator();
        while (iterator.hasNext()) {
            X x = iterator.next();
            sb.append(stringGetter.get(x));
            if (iterator.hasNext()) {
                sb.append(splitter);
            }
        }
        return sb.toString();
    }

    public interface StringGetter<X> {
        String get(X x);
    }

    public static <X> String getStringValue(X object, StringGetter<X> stringGetter, String nullString) {
        if (object != null) {
            return stringGetter.get(object);
        } else {
            return nullString;
        }
    }

}
