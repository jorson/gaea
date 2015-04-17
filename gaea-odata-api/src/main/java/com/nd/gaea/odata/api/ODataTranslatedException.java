package com.nd.gaea.odata.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Abstract superclass of all translatable server exceptions.
 */
public abstract class ODataTranslatedException extends ODataException {

    private static final long serialVersionUID = -1210541002198287561L;
    private static final Logger LOG = LoggerFactory.getLogger(ODataTranslatedException.class);
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    protected static final String DEFAULT_SERVER_BUNDLE_NAME = "server-core-exceptions-i18n";

    public static interface MessageKey {
        public String getKey();
    }

    private MessageKey messageKey;
    private Object[] parameters;

    protected ODataTranslatedException(String developmentMessage, MessageKey messageKey, String... parameters) {
        super(developmentMessage);
        this.messageKey = messageKey;
        this.parameters = parameters;
    }

    protected ODataTranslatedException(String developmentMessage, Throwable cause, MessageKey messageKey,
                                       String... parameters) {
        super(developmentMessage, cause);
        this.messageKey = messageKey;
        this.parameters = parameters;
    }

    @Override
    public String getLocalizedMessage() {
        return getTranslatedMessage(DEFAULT_LOCALE).getMessage();
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public MessageKey getMessageKey() {
        return messageKey;
    }

    public ODataErrorMessage getTranslatedMessage(final Locale locale) {
        if (messageKey == null) {
            return new ODataErrorMessage(getMessage(), DEFAULT_LOCALE);
        }
        ResourceBundle bundle = createResourceBundle(locale);
        if (bundle == null) {
            return new ODataErrorMessage(getMessage(), DEFAULT_LOCALE);
        }

        return buildMessage(bundle, locale);
    }

    /**
     * <p>Gets the name of the {@link java.util.ResourceBundle} containing the exception texts.</p>
     * <p>The key for an exception text is the concatenation of the exception-class name and
     * the {@link ODataTranslatedException.MessageKey}, separated by a dot.</p>
     *
     * @return the name of the resource bundle
     */
    protected abstract String getBundleName();

    private ResourceBundle createResourceBundle(final Locale locale) {
        try {
            return ResourceBundle.getBundle(getBundleName(), locale == null ? DEFAULT_LOCALE : locale);
        } catch (final MissingResourceException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    private ODataErrorMessage buildMessage(ResourceBundle bundle, Locale locale) {
        String message = null;

        try {
            message = bundle.getString(getClass().getSimpleName() + '.' + messageKey.getKey());
            StringBuilder builder = new StringBuilder();
            Formatter f = new Formatter(builder, locale);
            f.format(message, parameters);
            f.close();
            Locale usedLocale = bundle.getLocale();
            if (Locale.ROOT.equals(usedLocale)) {
                usedLocale = DEFAULT_LOCALE;
            }
            return new ODataErrorMessage(builder.toString(), usedLocale);
        } catch (MissingResourceException e) {
            return new ODataErrorMessage("Missing message for key '" + messageKey.getKey() + "'!", DEFAULT_LOCALE);
        } catch (MissingFormatArgumentException e) {
            return new ODataErrorMessage("Missing replacement for place holder in message '" + message +
                    "' for following arguments '" + Arrays.toString(parameters) + "'!", DEFAULT_LOCALE);
        }
    }

    public class ODataErrorMessage {
        String message;
        Locale locale;

        public ODataErrorMessage(String message, Locale usedLocale) {
            this.message = message;
            this.locale = usedLocale;
        }

        public String getMessage() {
            return message;
        }

        public Locale getLocale() {
            return locale;
        }
    }
}
