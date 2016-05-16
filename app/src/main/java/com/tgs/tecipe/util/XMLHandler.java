package com.tgs.tecipe.util;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
	private Item item=null;
	
	private ArrayList<Item> itemList=new ArrayList<Item>();
	
	public ArrayList<Item> getItemList() {
		return itemList;
	}
	String cate_name="";
	 @Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		itemList.clear();
	}
	 @Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equalsIgnoreCase("cat-name"))
		{ 
			cate_name=attributes.getValue("name").toString();
		}
		
		if(localName.equalsIgnoreCase("item"))
		{
			item=new Item();
			item.setCategory(cate_name);
			item.setType(attributes.getValue("type").trim().toString());
			item.setName(attributes.getValue("name").trim().toString());
			item.setLink(attributes.getValue("link").trim().toString());
			
		}
		
	}
	 @Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}
	 @Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		
		if(localName.equalsIgnoreCase("item"))
		{
			itemList.add(item);
		}
	}
	 @Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
}
