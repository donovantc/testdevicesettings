package com.adi.plugin;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Android implementation of Keychain Cordova Plugin
 */
public class Keychain extends CordovaPlugin
{

    private static final String TAG = Keychain.class.getSimpleName();

    private static final String GET_VALUE = "getForKey";
    private static final String SET_VALUE = "setForKey";
    private static final String REMOVE_VALUE = "removeForKey";
    private static final String TRUE = "true";

    public Keychain()
    {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */

    public void initialize(final CordovaInterface cordova, final CordovaWebView webView)
    {
        super.initialize(cordova, webView);
        Log.v(TAG, "Init KeyChain");
    }

    /**
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return true if action method found and false if not
     * @throws JSONException
     */
    public boolean execute(final String action, final JSONArray args,
                           final CallbackContext callbackContext) throws JSONException
    {
        Log.v(TAG, "Keychain Plugin received: " + action + " args: " + args.toString());

        if (GET_VALUE.equals(action))
        {
            final String key = args.getString(0);
            final String service = args.getString(1);
            final boolean encryptionRequired = args.length() == 2 || TRUE.equals(args.getString(2));
            processGet(key, service, encryptionRequired, callbackContext);
            return true;
        }
        else if (SET_VALUE.equals(action))
        {
            final String key = args.getString(0);
            final String service = args.getString(1);
            final String value = args.getString(2);
            final boolean encryptionRequired = args.length() == 3 || TRUE.equals(args.getString(3));
            processSet(key, service, value, encryptionRequired, callbackContext);
            return true;
        }
        else if (REMOVE_VALUE.equals(action))
        {
            final String key = args.getString(0);
            final String service = args.getString(1);
            final boolean encryptionRequired = args.length() == 2 || TRUE.equals(args.getString(2));
            processRemove(key, service, encryptionRequired, callbackContext);
            return true;
        }

        return false;
    }

    /**
     * Retrieve and un-encrypt a stored stored value
     *
     * @param key               The key used to identify the value
     * @param serviceName       Identifier to allow grouping of values
     * @param callbackContext   The callback
     */
    private void processGet(final String key, final String serviceName,
                            final boolean encryptionRequired,
                            final CallbackContext callbackContext)
    {
        cordova.getThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final SecureStorage secureStorage = SecureStorage.getInstance(
                            cordova.getActivity(), serviceName, encryptionRequired);
                    final String value = secureStorage.getValue(key);
                    callbackContext.success(value);
                }
                catch (IllegalStateException e)
                {
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    /**
     * Encrypt and store a value
     *
     * @param key               The key used to identify the value
     * @param serviceName       Identifier to allow grouping of values
     * @param value             The value to store
     * @param callbackContext   The callback
     */
    private void processSet(final String key, final String serviceName, final String value,
                            final boolean encryptionRequired,
                            final CallbackContext callbackContext)
    {
        cordova.getThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final SecureStorage secureStorage = SecureStorage.getInstance(
                            cordova.getActivity(), serviceName, encryptionRequired);
                    secureStorage.setValue(key, value);
                    callbackContext.success();
                }
                catch (IllegalStateException e)
                {
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    /**
     * Remove a stored key/value pair
     *
     * @param key               The key used to identify the value
     * @param serviceName       Identifier to allow grouping of values
     * @param callbackContext   The callback
     */
    private void processRemove(final String key, final String serviceName,
                               final boolean encryptionRequired,
                               final CallbackContext callbackContext)
    {
        cordova.getThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final SecureStorage secureStorage = SecureStorage.getInstance(
                            cordova.getActivity(), serviceName, encryptionRequired);
                    secureStorage.removeValue(key);
                    callbackContext.success();
                    }
                catch (IllegalStateException e)
                {
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }
}
