package com.thanhtu.myAPI.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thanhtu.myAPI.helper.JsonHelper;
import com.thanhtu.myAPI.helper.StringHelper;

@RestController
public class JsonController {
	@RequestMapping(value = "/json-javaclass", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> JsonToJavaClass(@RequestBody String jsonString) throws Exception {
		JSONObject jsonObj = new JSONObject(jsonString);
		String fileName = "D:\\MyAPI\\JsonToClass\\request.txt";
		JsonHelper.jsonToJavaClass(fileName, jsonObj);
		return ResponseEntity.ok("Thành công");
	}

	@RequestMapping(value = "/json-javaclass-uppercase", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> JsonToJavaClassUppercase(@RequestBody String jsonString) throws Exception {
		JSONObject jsonObj = new JSONObject(jsonString);
		String fileName = "D:\\MyAPI\\JsonToClassUppercase\\request.txt";
		JsonHelper.jsonToJavaClassUppercase(fileName, jsonObj);
		return ResponseEntity.ok("Thành công");
	}

	@RequestMapping(value = "/json-json-uppercase", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> JsonToJsonUppercase(@RequestBody String jsonString) throws Exception {
		String upperCaseJsonString = null;
		try {
			upperCaseJsonString = JsonHelper.upperCaseAllProperties(jsonString);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseEntity.ok(upperCaseJsonString);
	}

	@RequestMapping(value = "/json-json-javaclass", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> JsonToJsonJavaClass(@RequestBody String jsonString) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonString);
		int count = jsonObject.getInt("count");
		jsonObject.remove("count");
		List<String> listProperties = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			String strContent = jsonObject.getString("file" + i);
			StringHelper.matchPropertiesJavaClass(strContent, listProperties);
			jsonObject.remove("file" + i);
		}
		String jsonString1=jsonObject.toString();
		for(int i=0;i<listProperties.size();i++)
		{
			jsonString1=jsonString1.replace(listProperties.get(i).toUpperCase(),listProperties.get(i));
		}
		return ResponseEntity.ok(jsonString1);
	}

}
