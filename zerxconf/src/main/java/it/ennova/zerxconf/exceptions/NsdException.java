package it.ennova.zerxconf.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.util.Locale;

/**
 *
 */
public class NsdException extends IllegalStateException{

    private final String messageIncipit;

    public static final String START_DISCOVERY = "starting discovery";
    public static final String STOP_DISCOVERY = "stopping discovery";

    @StringDef({START_DISCOVERY, STOP_DISCOVERY})
    public @interface FAILURE {}

    public NsdException(@FAILURE String failure,
                        @NonNull String serviceName, int errorCode) {

        messageIncipit = String.format(Locale.getDefault(),
                "ZeRXconf failed %s on service %s with error code %d - ", failure, serviceName, errorCode);
    }

    @Override
    public String getMessage() {
        return messageIncipit + super.getMessage();
    }
}
