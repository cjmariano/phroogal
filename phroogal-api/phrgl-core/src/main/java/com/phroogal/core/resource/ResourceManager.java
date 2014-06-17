package com.phroogal.core.resource;

import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * <b>This is a class that manages several resource bundles in use within the
 * POS system. The intention of this class is to provide a single point of
 * access to common resource files used in this Application. <br>
 * Since common resource files are expected to be static (i.e., not being
 * modified), there is no need to create several instances of
 * <code>ResourceBundle</code> each time a resource file needs to be accessed.
 * The class uses a single Hashtable to store a single copy of each resource
 * file being loaded. </b>
 *
 * @author Christopher Mariano
 * @version 0.01
 */
public class ResourceManager {
    @SuppressWarnings({ "rawtypes" })
    private static final Hashtable htRes = new Hashtable();

    /**
     * This method returns a specific ResourceBundle given the base name. If the
     * resource has already been created previously, the returned value is a
     * cached version of the resource. Otherwise, a new resource bundle is
     * created added to the cache.
     * 
     * @param baseName
     *            Fully qualified base name of the properties file. For example
     *            "com.dotspatial.kryptaura.common.resources.KryptauraResources"
     * @return ResourceBundle instance of the requested resource bundle.
     */
    @SuppressWarnings("unchecked")
    public synchronized static ResourceBundle getResource(String baseName) {
        ResourceBundle bundle = (ResourceBundle) htRes.get(baseName);
        try{
            if (bundle != null) {
                return bundle;
            }
            else {
                bundle = ResourceBundle.getBundle(baseName);
                htRes.put(baseName, bundle);
            }           
        }catch( Exception ex ){
            System.out.println(ex.getMessage());
        }
        return bundle;
    }
}
