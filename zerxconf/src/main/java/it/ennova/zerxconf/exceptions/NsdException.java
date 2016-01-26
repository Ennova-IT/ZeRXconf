package it.ennova.zerxconf.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.util.Locale;

/**
 * This class is the one used in order to notify the developer of potential errors in implementing
 * this library
 */
public class NsdException extends IllegalStateException{

    private final String messageIncipit;
    @FAILURE
    private final String failureReason;

    public static final String START_DISCOVERY = "starting discovery";
    public static final String STOP_DISCOVERY = "stopping discovery";
    public static final String SERVICE_RESOLUTION = "service resolution";
    public static final String INVALID_PROTOCOL = "validating protocol";
    public static final String INVALID_ARGUMENT = "invalid argument";

    @StringDef({START_DISCOVERY, STOP_DISCOVERY, SERVICE_RESOLUTION, INVALID_PROTOCOL, INVALID_ARGUMENT})
    public @interface FAILURE {}

    /**
     * This constructor is the one that shall be used in order to invoke the {@link #INVALID_PROTOCOL}
     * error
     */
    public NsdException() {
        this(INVALID_PROTOCOL, "", 0);
    }

    public NsdException(@FAILURE String failure,
                        @NonNull String serviceName, int errorCode) {

        failureReason = failure;
        messageIncipit = buildMessageFrom(failure, serviceName, errorCode);
    }

    private String buildMessageFrom(@FAILURE String failure,
                                    @NonNull String serviceName, int errorCode) {

        if (failure.equals(INVALID_PROTOCOL)) {
            return String.format(Locale.getDefault(), "The given protocol (%s) could not be validated. " +
                    "As per standard, a valid protocol shall be in the form \"_ServiceType._TransportProtocolName.\", " +
                    "where TransportProtocolName can be either tcp or udp.");
        } else if (failure.equals(INVALID_ARGUMENT)) {
            return String.format(Locale.getDefault(), "You shall not use the constant ZeRXconf.ALL_AVAILABLE_SERVICES or the " +
                    "value \"_services._dns-sd._udp\" with the ZeRXconf.startDiscovery(Context, String) method. This value will " +
                    "not allow the services to be resolved. If you need to list all the available services, use the ZeRXconf.startDiscovery(Context) " +
                    "or provide a valid protocol to the ZeRXconf.startDiscovery(Context, String) call.");
        } else {
            return String.format(Locale.getDefault(),
                    "ZeRXconf failed %s on service %s with error code %d - ", failure, serviceName, errorCode);
        }

    }

    @Override
    public String getMessage() {
        return messageIncipit + super.getMessage();
    }

    @FAILURE
    public String getFailureReason() {
        return failureReason;
    }
}
