package br.com.investtools.fix.atdl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.xmlbeans.XmlException;

import br.com.investtools.fix.atdl.core.jaxb.Strategies;
import br.com.investtools.fix.atdl.core.xmlbeans.ParameterT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyLayoutDocument.StrategyLayout;
import br.com.investtools.fix.atdl.layout.xmlbeans.StrategyPanelDocument.StrategyPanel;

public class Dump {
	private static final String IDENT = "    ";

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Dump <filename>");
			return;
		}
		String pathname = args[0];
		File f = new File(pathname);
		// parseJAXB(f);
		parseXmlBeans(f);
	}

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

			String ident = IDENT;
			for (StrategyT s : doc.getStrategies().getStrategyArray()) {
				System.out.println(s.getName());
				Iterator<Object> it = new StrategyIterator(s);
				while (it.hasNext()) {
					Object obj = it.next();
					if (obj instanceof ParameterT) {
						System.out.println(ident + "parameter ["
								+ ((ParameterT) obj).getName() + "]");
					} else if (obj instanceof StrategyLayout) {
						StrategyLayout l = (StrategyLayout) obj;
						System.out.println(ident + "layout ");
						for (StrategyPanel p : l.getStrategyPanelArray()) {
							dump(p, ident + IDENT);
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

	private static void dump(StrategyPanel panel, String ident) {
		Iterator<Object> itp = new StrategyPanelIterator(panel);
		System.out.println(ident + "panel [" + panel.getTitle() + "]");
		while (itp.hasNext()) {
			Object o = itp.next();
			if (o instanceof ParameterT) {
				ParameterT p = (ParameterT) o;
				System.out.println(ident + IDENT + "parameter [" + p.getName() + "]");
			} else if (o instanceof StrategyPanel) {
				dump((StrategyPanel) o, ident + IDENT);
			}
		}
	}

	/**
	 * Parse stream using JAXB parser. Prints the name of each strategy found.
	 * 
	 * @param is
	 */
	public static void parseJAXB(File f) {
		try {
			System.out.println("Parsing using JAXB");
			InputStream is = new FileInputStream(f);
			JAXBContext jc = JAXBContext
					.newInstance("br.com.investtools.fix.atdl.core.jaxb");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Strategies doc = (Strategies) unmarshaller.unmarshal(is);
			System.out.println("Found " + doc.getStrategy().size()
					+ " strategies");
			for (br.com.investtools.fix.atdl.core.jaxb.StrategyT s : doc
					.getStrategy()) {
				System.out.println(s.getName());
			}
		} catch (JAXBException e) {
			System.err.println("JAXB error");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}

	}
}
