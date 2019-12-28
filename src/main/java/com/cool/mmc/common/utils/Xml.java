package com.cool.mmc.common.utils;


import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Xml
{
	private Item root=new Item();
	
	private String encoding="GB2312";
	
	public Xml()
	{	
		
	}
	
	public Xml(String encoding, Item item)
	{
		this.encoding=encoding;
		root=item;
	}
	
	public Xml(String encoding)
	{
		this.encoding=encoding;
	}
	
	
	/**
	 * 根据字符串构造Xml
	 * @param xmlString
	 * @throws Exception 
	 */
	public void Parse(String xmlString) throws Exception
	{
		if(xmlString==null || xmlString.equals(""))
			throw new Exception("<Xml> 不能以空的字符串构造XML!");
		InputStream   input =new ByteArrayInputStream(xmlString.getBytes());
		Parse(input);
	}
	
	
	/**
	 * 根据字符串构造Xml
	 * @param xmlString
	 * @throws Exception 
	 */
	public void Parse(String xmlString,String encode) throws Exception
	{
		if(xmlString==null || xmlString.equals(""))
			throw new Exception("<Xml> 不能以空的字符串构造XML!");
		InputStream   input =new ByteArrayInputStream(xmlString.getBytes(encode));
		Parse(input);
	}
	
	public void Parse(InputStream input)throws Exception
	{
		DocumentBuilderFactory domfac=DocumentBuilderFactory.newInstance();
		domfac.setValidating(false);
		DocumentBuilder dombuilder=null;
		Document doc=null;
	    dombuilder=domfac.newDocumentBuilder();
	    doc=dombuilder.parse(input);	    
	    Node node = doc.getDocumentElement();
	    Resolve(node,root);
	}
	
	/**
	 * 把Node转为Item
	 * @param node
	 * @param item
	 */
	private void Resolve(Node node,Item item)
	{
		if(node==null || item==null)
			return;
		
		NamedNodeMap attributes = node.getAttributes();		
		for(int i=0;i<attributes.getLength();i++)
		{
			Node attribute = attributes.item(i);
			item.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
		}
		
		item.setName(node.getNodeName());
		
		NodeList nodelist = node.getChildNodes();
		if(nodelist!=null)
		{
			for(int i=0;i<nodelist.getLength();i++)
			{
				Node childNode=nodelist.item(i);
				if(childNode.getNodeType()==Node.ELEMENT_NODE)
				{
					Item childItem=new Item();
					Resolve(childNode,childItem);
					item.addChild(childItem);
				}
				if(childNode.getNodeType()==Node.TEXT_NODE)
				{
					item.setValue(childNode.getNodeValue());
				}
				if(childNode.getNodeType()==Node.CDATA_SECTION_NODE)
				{
					CDATASection dataNode = (CDATASection) childNode;
					item.setCData(dataNode.getData());  
				}
			}
		}
	}

	public String getToString()
	{
		return this.toString();
	}
	
	public String toString()
	{
		String xmlHead="<?xml version=\"1.0\" encoding=\""+this.encoding+"\"?>\n";
		return xmlHead+root.toString();
	}
	
	public Item getRoot()
	{
		return root;
	}
	public void setRoot(Item root)
	{
		this.root = root;
	}
	
}
