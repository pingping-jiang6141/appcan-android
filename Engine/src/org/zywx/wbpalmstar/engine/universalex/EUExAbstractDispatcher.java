package org.zywx.wbpalmstar.engine.universalex;

public abstract class EUExAbstractDispatcher {
	
	   public static final String JS_OBJECT_NAME="uexDispatcher";
	   
	   public abstract void dispatch(String pluginName,String methodName,String[] params);

}
