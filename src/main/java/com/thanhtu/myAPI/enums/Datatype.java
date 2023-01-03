package com.thanhtu.myAPI.enums;

public enum Datatype {
	STRING(1,"String","private String"),
	INTEGER(2,"Integer","private Integer"),
	TIMESTAMP(3,"Timestamp","private Timestamp"),
	DOUBLE(4,"Double","private Double"),
	FLOAT(5,"float","private float");
	private Integer type;
	private String datatype;
	private String javaClass;
	Datatype(int id, String TypeString, String dataTypeString) {
		// TODO Auto-generated constructor stub
		this.type=id;
		this.datatype=TypeString;
		this.datatype=dataTypeString;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getJavaClass() {
		return javaClass;
	}
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}
	
}
