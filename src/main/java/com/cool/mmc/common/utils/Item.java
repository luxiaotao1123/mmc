package com.cool.mmc.common.utils;

import java.util.*;

public class Item
{
	private String name;
	private String value;
	private String cData;
	private List<Item> itemList=new ArrayList<Item>();
	private Map<String,Item> attributes=new Hashtable();
	private Item parent=null;
	
	public Item()
	{
		
	}
	
	public Item(String name)
	{
		this.name=name;
	}
	
	public Item(String name, String value)
	{
		this.name=name;
		this.value=value;
	}
	
	public Item(String name, String value, String cData)
	{
		this.name=name;
		this.value=value;
		this.cData = cData;
	}

	public String toString()
	{
		String strString="";
		strString+=nbsp();
		strString+="<"+name;
		Iterator<String> itr = attributes.keySet().iterator();
		while(itr.hasNext())
		{
			String key=itr.next();
			Item attribute = attributes.get(key);
			strString+=" ";
			strString+=attribute.getName()+"=\""+formatXml(attribute.getValue())+"\"";
		}
		strString+=">";
		if(itemList.size()>0)
			strString+="\n";
		if(value!=null && !value.equals(""))
		{
			strString+=formatXml(value);
			if(itemList.size()>0)
				strString+="\n";
		}
		if(cData!=null && !cData.equals("")){
			strString+="<![CDATA["+(cData)+"]]>";
		}
		for(Item item:itemList)
		{
			strString+=item.toString();
		}
		if(itemList.size()>0)
			strString+=nbsp();
		strString+="</"+name+">\n";
		return strString;
	}
	
	public Item $(String name){
		if(name==null)
			return null;
		for(Item item:itemList)
		{
			if(name.equals(item.getName()))
				return item;
		}
		return null;
	}
	
	public String nbsp()
	{
		String whitespace="";
		int desp=this.getDesp();
		for(int i=0;i<desp;i++)
			whitespace+="  ";
		return whitespace;
	}	
	
	private int getDesp()
	{
		if(this.parent!=null)
			return this.parent.getDesp()+1;
		else
			return 0;
	}

	public String getName()
	{
		return name;
	}
	public Item setName(String name)
	{
		this.name = name;
		return this;
	}
	public String getValue()
	{
		return value;
	}
	public String getValue(String childName)
	{
		if(childName==null)
			return null;
		for(Item item:itemList){
			if(childName.equals(item.getName()))
				return item.getValue();
		}
		return null;
	}
	public Item setValue(String value)
	{
		this.value = value;
		return this;
	}
	public List<Item> getItemList()
	{
		return itemList;
	}
	public void setItemList(List<Item> itemList)
	{
		this.itemList = itemList;
	}
	public Item addChild(Item childItem)
	{
		childItem.setParent(this);
		itemList.add(childItem);	
		return this;
	}	
	public Item setAttribute(String nodeName, String nodeValue)
	{
		attributes.put(nodeName,new Item(nodeName,nodeValue));	
		return this;
	}
	
	public String getAttribute(String strName){
		if(strName==null)
			return null;
		Item item = attributes.get(strName);
		if(item==null)
			return null;
		return item.getValue();		
	}

	public Item getParent()
	{
		return parent;
	}

	public void setParent(Item parent)
	{
		this.parent = parent;
	}
	
	public static String formatXml(String string){	
		if(string==null || string.trim().equals(""))
			return "";
		string=string.replace("&", "&amp;");
		string=string.replace("<", "&lt;");
		string=string.replace(">", "&gt;");
		string=string.replace("\"", "&quot;");		
		return string;
	}

	
	public Item getChild(String childName)
	{
		if(childName==null)
			return null;
		for(Item item:itemList){
			if(childName.equals(item.getName()))
				return item;
		}
		return null;
	}
	

	public String getCData() {
		return cData;
	}
	
	public String getCData(String childName)
	{
		if(childName==null)
			return null;
		for(Item item:itemList){
			if(childName.equals(item.getName()))
				return item.getCData();
		}
		return null;
	}


	public Item setCData(String data) {
		cData = data;
		return this;
	}	
}
