package org.atdl4j.parser.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.atdl4j.atdl.core.StrategiesT;
import org.atdl4j.atdl.core.StrategyT;

public class DumpJAXB {

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
			StrategiesT doc = (StrategiesT) unmarshaller.unmarshal(is);
			System.out.println("Found " + doc.getStrategy().size()
					+ " strategies");
			for (StrategyT s : doc.getStrategy()) {
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
