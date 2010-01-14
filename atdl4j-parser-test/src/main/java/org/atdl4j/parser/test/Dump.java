package org.atdl4j.parser.test;

import java.io.File;

public class Dump {
	public static final String IDENT = "    ";

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Dump <filename>");
			return;
		}
		String pathname = args[0];
		File f = new File(pathname);
		// DumpJAXB.parseJAXB(f);
		DumpXmlBeans.parseXmlBeans(f);
	}

}
