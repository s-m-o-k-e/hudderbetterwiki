package io.github.ngspace.hudder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class CachedTextReader {
	HashMap<String, String> savedfiles = new HashMap<String, String>();
	
	public String getFile(String file) {
		if (!savedfiles.containsKey(file))
			try {
				savedfiles.put(file,new String(Files.readAllBytes(new File(file).toPath()), StandardCharsets.UTF_8));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "\u00A7aError while reading file: " + e.getLocalizedMessage();
			} catch (IOException e) {
				e.printStackTrace();
				return "\u00A7cNo such file file: " + new File(file).getName() + "   " + e.getMessage();
			}
		return savedfiles.get(file);
	}
	
	public boolean readFile(String file) {
		try {
			File f = new File(file);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				if (!f.createNewFile()) return false;
			}
			savedfiles.put(file,new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
