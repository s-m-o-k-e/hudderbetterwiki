package io.github.ngspace.hudder.data_management;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;

public class Advanced {private Advanced() {}
	public static double gpuUsage = 0;
    
	
	
	public static String OS = null; static{
		if (OS == null) {
			String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
			if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) OS = "mac";
			else if (OS.indexOf("win") >= 0) OS = "windows";
			else if (OS.indexOf("nux") >= 0) OS = "linux";
			else OS = "other";
		}
	}
	
	
	
	/* FPS */
	
	
	
    private static final List<Integer> fpshistory = new ArrayList<Integer>();

    public static int getFPS(MinecraftClient ins) {
    	int fps = ins.getCurrentFps();
        fpshistory.add(fps);
        if (fpshistory.size()>800) fpshistory.remove(0);
        return fps;
    }
    public static int getMinimumFPS() {int max = fpshistory.get(0);for (int i:fpshistory) if (i<max) max=i;return max;}
    public static int getMaximumFPS() {int max = 0;for (int i:fpshistory) if (i>max) max=i;return max;}
    public static int getAverageFPS() {int sum = 0;for (int fps : fpshistory) sum+=fps;return sum/fpshistory.size();}
    
    
    
    /* Keyboard */
    
    
    
    public static final Map<Integer,Integer> keysheld = new HashMap<Integer,Integer>();

    //Kinda cheating?
    static HashMap<String, Integer> keys = new HashMap<String, Integer>(); static {
	    for (Field field : GLFW.class.getFields()) {
	    	try {
	    		if (field.getName().startsWith("GLFW_KEY_")&&field.canAccess(null))
	    			keys.put(field.getName().substring(9).toLowerCase(),field.getInt(null));
			} catch (Exception e) {e.printStackTrace();}
	    }
    }
	public static int isKeyHeld(String key) {
		if (key.length()>4&&key.length()<18&&key.startsWith("key_")) {
			int keynum = keys.get(key.substring(4));
			if (keynum==0) return 0;
			return keysheld.containsKey(keynum)?2:1;
		}
		return 0;
	}
}