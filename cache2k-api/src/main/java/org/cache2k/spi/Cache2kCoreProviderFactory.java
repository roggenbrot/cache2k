package org.cache2k.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Cache2kCoreProviderFactory {

    static boolean OSGI_ENV = false;

    private static Cache2kCoreProvider PROVIDER;


    static void setProvider(Cache2kCoreProvider provider) {
        if(PROVIDER != null){
            // TODO: Is this correct? Do we need to close more resources?
            PROVIDER.close();
        }
        PROVIDER = provider;
    }

    public static Cache2kCoreProvider getProvider() {
        if (PROVIDER == null) {
            // TODO: Make thread safe (Semaphore ...)
            if (OSGI_ENV) {
                // OSGI will set the provider as soon as a Cache2kCoreProvider service is available
                return null;
            } else {
                final Iterator<Cache2kCoreProvider> it = ServiceLoader.load(Cache2kCoreProvider.class).iterator();
                if (!it.hasNext()) {
                    return null;
                }
                PROVIDER = it.next();
            }
        }
        return PROVIDER;
    }


}
