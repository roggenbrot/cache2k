package org.cache2k.spi;

import org.osgi.framework.*;

public class Activator implements BundleActivator, ServiceListener {

    private final static String PROVIDER_SERVICE_FILTER = "(objectclass=" + Cache2kCoreProvider.class.getName() + ")";

    private BundleContext bundleContext;

    @Override
    public void start(BundleContext bundleContext) throws InvalidSyntaxException {
        Cache2kCoreProviderFactory.OSGI_ENV = true;
        bundleContext.addServiceListener(this, PROVIDER_SERVICE_FILTER);
        this.bundleContext = bundleContext;
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
        Cache2kCoreProviderFactory.OSGI_ENV = false;
        this.bundleContext = null;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {

        // We are just interested in REGISTERED/UNREGISTERED event. We don't care
        // about modification of service properties (yet)
        if (event.getType() == ServiceEvent.REGISTERED) {
            if (bundleContext != null) {
                // TODO: It's possible to have multiple Cache2kCoreProvider services deployed (e.g. multiple core jars with different/same version)
                // We have to think about a way to prevent that existing provider will be overwritten:
                // - Based on a service property (Version ...)
                // - Based on current provider value
                // - Add to documentation that this is a unsupported scenario
                final Cache2kCoreProvider provider = (Cache2kCoreProvider) bundleContext.getService(event.getServiceReference());
                Cache2kCoreProviderFactory.setProvider(provider);
            }
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            if (bundleContext != null) {
                final Cache2kCoreProvider provider = (Cache2kCoreProvider) bundleContext.getService(event.getServiceReference());
                // Only unregister the provider if object is the same as currently registered
                if (provider == Cache2kCoreProviderFactory.getProvider()) {
                    Cache2kCoreProviderFactory.setProvider(null);
                }
            } else {
                // Illegal bundle state. Should never happen since bundle is not running anymore
                Cache2kCoreProviderFactory.setProvider(null);
            }
        }
    }


}
