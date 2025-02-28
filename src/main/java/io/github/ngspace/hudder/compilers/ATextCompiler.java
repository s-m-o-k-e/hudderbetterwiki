package io.github.ngspace.hudder.compilers;

import java.util.HashMap;
import java.util.Map;

import io.github.ngspace.hudder.config.ConfigInfo;

public abstract class ATextCompiler {
	
	public static Map<String, String> compilers = new HashMap<String, String>();
	
	public static final String EMPTYCOM = "io.github.ngspace.hudder.compilers.EmptyCompiler";
	public static final String DEFAULT_COMPILER = "io.github.ngspace.hudder.compilers.DefaultCompiler";
	
	public static void registerCompiler(String name, String classname) {compilers.put(name, classname);}
	
	static {
		/* Default */
		registerCompiler("defaultcompiler", DEFAULT_COMPILER);
		registerCompiler("default compiler", DEFAULT_COMPILER);
		registerCompiler("default", DEFAULT_COMPILER);
		registerCompiler("hudder", DEFAULT_COMPILER);
		
		
		/* JavaScript */
		registerCompiler("js", "io.github.ngspace.hudder.compilers.JavaScriptCompiler");
		registerCompiler("javascript", "io.github.ngspace.hudder.compilers.JavaScriptCompiler");
		registerCompiler("javascriptcompiler", "io.github.ngspace.hudder.compilers.JavaScriptCompiler");
		
		
		registerCompiler("empty",EMPTYCOM);
		registerCompiler("none", EMPTYCOM);
		registerCompiler("null", EMPTYCOM);
		registerCompiler( null , EMPTYCOM);
	}

	protected Map<String, Object> variables = new HashMap<String, Object>();
	
	public abstract CompileResult compile(ConfigInfo info, String text) throws CompileException;
	public abstract Object getVariable(String key) throws CompileException;
	
	public void put(String key, Object value) {variables.put(key, value);}
	public Object get(String key) {return variables.get(key);}
}
