package com.thirdpart.YsMe7xF0;

import com.h3c.iot.codec.CodecConstant;
import com.thirdpart.YsMe7xF0.codec.CodecServiceImpl;
import com.h3c.iot.codec.CodecService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;

/*该类无需改动*/
public class Activator implements BundleActivator {

	private ServiceRegistration serviceRegistration;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Dictionary<String,String> dic = new Hashtable<>(1);
		dic.put(CodecConstant.SERVICE_NAME, PropertyResourceBundle.getBundle("service-info").getString(CodecConstant.SERVICE_NAME));
		serviceRegistration = bundleContext.registerService(CodecService.class.getName(), new CodecServiceImpl(), dic);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		serviceRegistration.unregister();
	}
}