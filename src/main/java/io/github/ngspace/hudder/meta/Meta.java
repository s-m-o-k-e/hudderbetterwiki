package io.github.ngspace.hudder.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.ngspace.hudder.compilers.CompileException;
import io.github.ngspace.hudder.compilers.CompileResult;
import io.github.ngspace.hudder.config.ConfigManager;

public class Meta {

	public static final String TOPLEFT = "topleft";
	public static final String BOTTOMLEFT = "bottomleft";
	public static final String TOPRIGHT = "topright";
	public static final String BOTTOMRIGHT = "bottomright";
	public static final String MUTE = "mute";
	
	public String pos;
	public String TLText = "";
	public String BLText = "";
	public String TRText = "";
	public String BRText = "";
	public float TLScale = 1;
	public float BLScale = 1;
	public float TRScale = 1;
	public float BRScale = 1;
	public List<Element> elements = new ArrayList<Element>();

	public Meta(String string) {setTextLocation(string, ConfigManager.getConfig().scale);}
	public void addString(String txt, boolean cleanup) throws CompileException {addString(txt,pos,cleanup);}
	
	protected void addString(String txt, String pos, boolean cleanup) throws CompileException {
		String text = txt;
		if (cleanup) {
			int buffer = ConfigManager.getConfig().metaBuffer;
			if (buffer<10)
				for (int i = 0; i<buffer;i++)
					try {
						if (text.startsWith("\r\n")) text = text.substring(2);
						if (text.endsWith("\r\n")) text = text.substring(0, text.length() - 2);
					} catch (StringIndexOutOfBoundsException e) {
						throw new CompileException("Empty section \"" + pos + "\"");
					}
			else text = text.trim();
		}
		switch (pos) {
			case TOPLEFT: TLText+=text; break;
			case BOTTOMLEFT: BLText+=text; break;
			case TOPRIGHT: TRText+=text; break;
			case BOTTOMRIGHT: BRText+=text; break;
			case MUTE: break;
			default: throw new CompileException("Unidentifiable meta state \"" + pos + "\"");
		}
	}
	
	public void setTextLocation(String text, float d) {
		this.pos = text.toLowerCase();
		switch (pos) {
			case TOPLEFT: TLScale = d; break;
			case BOTTOMLEFT: BLScale = d; break;
			case TOPRIGHT: TRScale = d; break;
			case BOTTOMRIGHT: BRScale = d; break;
			default:break;
		}
	}
	
	public CompileResult toResult() {
		return new CompileResult(TLText, TLScale, BLText, BLScale, TRText, TRScale, BRText, BRScale,
				elements.toArray(new Element[elements.size()]));
	}

	public void combineWithResult(CompileResult compile, boolean combineText) throws CompileException {
		if (combineText) {
			addString(compile.TopLeftText, TOPLEFT, false);        TLScale = compile.TLScale;
			addString(compile.BottomLeftText, BOTTOMLEFT, false);  BLScale = compile.BLScale;
			addString(compile.TopRightText, TOPRIGHT, false);      TRScale = compile.TRScale;
			addString(compile.BottomRightText, BOTTOMRIGHT, false);BRScale = compile.BRScale;
		}
		Collections.addAll(elements, compile.elements);
	}
	
}
