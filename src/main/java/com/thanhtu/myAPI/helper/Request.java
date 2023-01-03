package com.thanhtu.myAPI.helper;

import java.util.List;

public class Request {
	private Long endDate;
	private List<Object> listGroupAttribute;
	private String typeName;
	private Long typeID;
	private String updatedUser;
	private Boolean isActive;
	private Long startDate;

	public Long getEndDate(){
	    return endDate;
	}
	public void setEndDate(Long endDate){
	    this.endDate = endDate;
	}
	public List<Object> getListGroupAttribute(){
	    return listGroupAttribute;
	}
	public void setListGroupAttribute(List<Object> listGroupAttribute){
	    this.listGroupAttribute = listGroupAttribute;
	}
	public String getTypeName(){
	    return typeName;
	}
	public void setTypeName(String typeName){
	    this.typeName = typeName;
	}
	public Long getTypeID(){
	    return typeID;
	}
	public void setTypeID(Long typeID){
	    this.typeID = typeID;
	}
	public String getUpdatedUser(){
	    return updatedUser;
	}
	public void setUpdatedUser(String updatedUser){
	    this.updatedUser = updatedUser;
	}
	public Boolean getIsActive(){
	    return isActive;
	}
	public void setIsActive(Boolean isActive){
	    this.isActive = isActive;
	}
	public Long getStartDate(){
	    return startDate;
	}
	public void setStartDate(Long startDate){
	    this.startDate = startDate;
	}

}
