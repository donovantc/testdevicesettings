package com.adi.plugin;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * Secure storage component
 */
public final class SecureStorage
{
    private static final String TAG = SecureStorage.class.getSimpleName();
    private final KeyStorage keyStorage;
    private final Context context;
    private final String repositoryName;
    private final Map<String, String> valueCache = new HashMap<String, String>();

    private final static Map<String, SecureStorage> repositoryMap = new HashMap<String, SecureStorage>();

    /**
     * Static factory method to create or return singleton object
     *
     * @param context           The Android Context
     * @param repositoryName    The name used for the Keystore alias and shared preference file
     * @return                  The singleton instance of the SecureStorage class
     */
    public synchronized static SecureStorage getInstance(final Context context,
                                                         final String repositoryName,
                                                         final boolean encryptionRequired)
    {
        final SecureStorage instance;
        if (!repositoryMap.containsKey(repositoryName))
        {
            instance = new SecureStorage(context, repositoryName, encryptionRequired);
            repositoryMap.put(repositoryName, instance);
        }
        else
        {
            instance = repositoryMap.get(repositoryName);
        }
        return instance;
    }

    /**
     * Private constructor to ensure singleton only instanciation
     *
     * @param context           The Android Context
     * @param repositoryName    The name used for the Keystore alias and shared preference file
     */
    private SecureStorage(final Context context, final String repositoryName, final boolean encryptionRequired)
    {
        this.context = context;
        this.repositoryName = repositoryName;
        try
        {
            keyStorage = new KeyStorage(context, repositoryName, encryptionRequired);
        }
        catch (CertificateException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (KeyStoreException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (UnrecoverableEntryException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (NoSuchProviderException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            throw new IllegalArgumentException(e);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the string value for the given key
     *
     * Will return unecrypted value from local cache if present,
     * otherwise retrieves encrypted value from shared preferences and unencrypts before returning it
     *
     * @param key   The key of the value to return
     * @return      The unencrypted value or null if not found
     */
    public String getValue(final String key)
    {
        if (!valueCache.containsKey(key))
        {
            final SharedPreferences prefs = context.getSharedPreferences(repositoryName, Context.MODE_PRIVATE);
            final String val = prefs.getString(key, null);
            if (val != null)
            {
                valueCache.put(key, keyStorage.decryptString(val));
            }
        }
        return valueCache.get(key);
    }

    /**
     * Sets the string value for the given key
     *
     * Encrypts the value and stores in shared preferences and stores the un-encrypted value
     * in the local cache
     *
     * @param key   The key of the value to set
     * @param value The value to set
     */
    public void setValue(final String key, final String value)
    {
        final SharedPreferences prefs = context.getSharedPreferences(repositoryName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        final String encrypted = keyStorage.encryptString(value);
        editor.putString(key, encrypted);
        editor.apply();
        valueCache.put(key, value);
    }

    /**
     * Remove the value for the given key from shared preferences and the local cache
     *
     * @param key   The key of the value to remove
     */
    public void removeValue(final String key)
    {
        final SharedPreferences prefs = context.getSharedPreferences(repositoryName, Context.MODE_PRIVATE);
        if (valueCache.containsKey(key) || prefs.contains(key))
        {
            final SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key);
            editor.apply();
            valueCache.remove(key);
        }
    }
}
