package org.cache2k.core.osgi;

import org.cache2k.core.Cache2kCoreProviderImpl;
import org.cache2k.spi.Cache2kCoreProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Hashtable;

public class Activator implements BundleActivator {

    private ServiceRegistration<Cache2kCoreProvider> cache2kCoreProviderService;

    @Override
    public void start(BundleContext bundleContext) {
        bundleContext.registerService(
                Cache2kCoreProvider.class.getName(),
                new Cache2kCoreProviderImpl(),
                new Hashtable<>());
    }

    @Override
    public void stop(BundleContext bundleContext)  {
        if (cache2kCoreProviderService != null) {
            cache2kCoreProviderService.unregister();
            cache2kCoreProviderService = null;
        }

    }
}
