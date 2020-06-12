package org.pensatocode.simplicity.generator.util;

import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;

import java.util.HashMap;
import java.util.Map;

public class ServiceBinder {

    private static class ServiceHolder {
        private static final Map<String, Object> instance = new HashMap<>();
    }

    private enum ServiceName {
        DIRECTORY_SERVICE(DirectoryService.SINGLETON),
        JAVA_CLASS_SERVICE(JavaClassService.SINGLETON);

        ServiceName(Object serviceInstance) {
            ServiceHolder.instance.put(this.name(), serviceInstance);
        }
    }

    public static DirectoryService getDirectoryService() {
        return (DirectoryService) ServiceHolder.instance.get(ServiceName.DIRECTORY_SERVICE.name());
    }

    public static JavaClassService getJavaClassService() {
        return (JavaClassService) ServiceHolder.instance.get(ServiceName.JAVA_CLASS_SERVICE.name());
    }

}
