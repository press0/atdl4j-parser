package br.com.investtools.fix.atdl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.atdl4j.atdl.xmlbeans.core.ParameterT;
import org.atdl4j.atdl.xmlbeans.core.StrategiesDocument;
import org.atdl4j.atdl.xmlbeans.core.StrategyT;
import org.atdl4j.atdl.xmlbeans.layout.StrategyLayoutT;
import org.atdl4j.atdl.xmlbeans.layout.StrategyPanelT;

public class DumpXmlBeans {

	/**
	 * Parse stream using XmlBeans parser. Prints the name of each strategy
	 * found.
	 * 
	 * @param is
	 */
	public static void parseXmlBeans(File f) {
		try {
			System.out.println("Parsing using XmlBeans");
			InputStream is = new FileInputStream(f);
			StrategiesDocument doc = StrategiesDocument.Factory.parse(is);
			System.out
					.println("Found "
							+ doc.getStrategies().sizeOfStrategyArray()
							+ " strategies");

			String ident = Dump.IDENT;
			for (StrategyT s : doc.getStrategies().getStrategyArray()) {
				System.out.println(s.getName());
				Iterator<Object> it = new StrategyIterator(s);
				while (it.hasNext()) {
					Object obj = it.next();
					if (obj instanceof ParameterT) {
						System.out.println(ident + "parameter ["
								+ ((ParameterT) obj).getName() + "]");
					} else if (obj instanceof StrategyLayoutT) {
						StrategyLayoutT l = (StrategyLayoutT) obj;
						System.out.println(ident + "layout ");
						for (StrategyPanelT p : l.getStrategyPanelArray()) {
							dump(p, ident + Dump.IDENT);
						}
					}
				}

			}
		} catch (XmlException e) {
			System.err.println("Error parsing file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		}
	}

	private static void dump(StrategyPanelT panel, String ident) {
		Iterator<Object> itp = new StrategyPanelIterator(panel);
		System.out.println(ident + "panel [" + panel.getTitle() + "]");
		while (itp.hasNext()) {
			Object o = itp.next();
			if (o instanceof ParameterT) {
				ParameterT p = (ParameterT) o;
				System.out.println(ident + Dump.IDENT + "parameter ["
						+ p.getName() + "]");
			} else if (o instanceof StrategyPanelT) {
				dump((StrategyPanelT) o, ident + Dump.IDENT);
			}
		}
	}

}
