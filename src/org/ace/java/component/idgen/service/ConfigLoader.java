package org.ace.java.component.idgen.service;

import java.io.CharArrayWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ace.insurance.process.interfaces.IUserProcessService;
import org.ace.insurance.system.common.branch.Branch;
import org.ace.insurance.system.common.branch.persistence.interfaces.IBranchDAO;
import org.ace.java.component.idgen.service.interfaces.IDConfigLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Service("IDConfigLoader")
public class ConfigLoader extends DefaultHandler implements IDConfigLoader {
	@javax.annotation.Resource(name = "IDConfigLoader")
	private IDConfigLoader idConfigLoader;
	@javax.annotation.Resource(name = "UserProcessService")
	private IUserProcessService userProcessService;
	@javax.annotation.Resource(name = "BranchDAO")
	private IBranchDAO branchDAO;
	private CharArrayWriter text = new CharArrayWriter();
	private Map<String, String> idConfigMap = new HashMap<String, String>();
	private String branchCode;
	private boolean centralized;

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("branch-code")) {
			String stBoolean = attributes.getValue("centralized");
			centralized = Boolean.parseBoolean(stBoolean);
			branchCode = attributes.getValue("code");
		} else if (qName.equalsIgnoreCase("class")) {
			idConfigMap.put(attributes.getValue("name"), attributes.getValue("prefix"));
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		text.write(ch, start, length);
	}

	@PostConstruct
	public void load() {
		try {
			Resource configFile = new ClassPathResource("id-format.xml");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(configFile.getFile(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFormat(String className) {
		return idConfigMap.get(className);
	}

	public String getBranchCode() {
		return branchCode;
	}

	public String getBranchId() {
		Branch branch = null;
		String branchCode = null;
		if (idConfigLoader.isCentralizedSystem()) {
			branch = userProcessService.getLoginUser().getBranch();
		} else {
			branchCode = idConfigLoader.getBranchCode();
			branch = branchDAO.findByCode(branchCode);
		}

		return branch != null ? branch.getId() : null;
	}

	public boolean isCentralizedSystem() {
		return centralized;
	}
}
