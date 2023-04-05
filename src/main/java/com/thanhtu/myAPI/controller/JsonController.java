package com.thanhtu.myAPI.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhtu.myAPI.helper.JsonHelper;
import com.thanhtu.myAPI.model.JsonPermissionModel;
import com.thanhtu.myAPI.model.JsonUpperCaseToCamelRequestModel;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class JsonController {
	public static Map<String, String> articleMapOne;
	static {
		articleMapOne = new HashMap<>();
		articleMapOne.put("ar01", "Intro to Map, Best");
		articleMapOne.put("ar02", "Some article");
	}

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

	@RequestMapping(value = "/json-uppercase-json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> JsonUppercaseToJson(@RequestBody JsonUpperCaseToCamelRequestModel request)
			throws Exception {
		List<String> listPropertiesCamel = JsonHelper.ListPropertiesFromJsonStringNoEscape(request.getJsonString());
		List<String> listPropertiesUppercase = JsonHelper
				.ListPropertiesFromJsonStringNoEscape(request.getUppercaseJsonString());
		String result = request.getUppercaseJsonString();
		Boolean isRemoveI = false;
		Boolean isRemoveJ = false;
		Integer sizePropertiesCamel = listPropertiesCamel.size();
		Integer sizePropertiesUppercase = listPropertiesUppercase.size();
		for (int i = 0; i < sizePropertiesCamel;) {
			isRemoveI = false;
			isRemoveJ = false;
			for (int j = 0; j < sizePropertiesUppercase;) {
				String uppercaseString = listPropertiesUppercase.get(j);
				String upperCamelCaseString = listPropertiesCamel.get(i).toUpperCase();
				if (upperCamelCaseString.equals(uppercaseString)) {
					String uppercase = listPropertiesUppercase.get(j);
					String camel = listPropertiesCamel.get(i);
					result = result.replace(uppercase, camel);
					listPropertiesCamel.remove(i);
					listPropertiesUppercase.remove(j);
					isRemoveI = true;
					isRemoveJ = true;
					sizePropertiesCamel = listPropertiesCamel.size();
					sizePropertiesUppercase = listPropertiesUppercase.size();
					break;
				}

				if (isRemoveJ != true && listPropertiesUppercase.size() > 0 && listPropertiesUppercase.size() > j) {
					j++;
				}
				if (sizePropertiesUppercase == 0) {
					break;
				}
			}
			if (isRemoveI != true && listPropertiesCamel.size() > 0 && listPropertiesCamel.size() > i) {
				i++;
			}
			if (sizePropertiesUppercase == 0) {
				break;
			}

		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/permissionToApplication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> convertPermissionToApplication(@RequestBody JsonPermissionModel model)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<LinkedHashMap<String, String>> linkedHashMaps = mapper.readValue(model.getJsonString(),
				new TypeReference<List<LinkedHashMap<String, String>>>() {
				});
		String config = "resources.permissions";
		Integer i = model.getFrom() + 1;
		for (LinkedHashMap<String, String> map : linkedHashMaps) {
			System.out.println(config + "[" + i + "].url" + "=" + map.get("url"));
			System.out.println(config + "[" + i + "].key" + "=" + map.get("key"));
			i++;
		}
		return ResponseEntity.ok("Success");
	}

	@RequestMapping(value = "/controllerToPermission", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> convertFileJsonToApplicationProperties(@RequestBody JsonPermissionModel request)
			throws Exception {
		List<String> listUrlAPI = JsonHelper.matchUrlAPI(request.getJsonString());
		for (String urlAPI : listUrlAPI) {
			System.out.println(",{");
			System.out.println("    \"url\": " + "\"" + urlAPI + "\",");
			System.out.println("    \"key\": " + "\"LIST_FORMS\"");
			System.out.println("}");
		}
		return ResponseEntity.ok("Success");
	}

	@RequestMapping(value = "/controllerToApplication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> convertControllerToApplicationProperties(@RequestBody JsonPermissionModel model)
			throws Exception {
		String config = "resources.permissions";
		List<String> listUrlAPI = JsonHelper.matchUrlAPI(model.getJsonString());
		Integer from = model.getFrom() + 1;
		for (String urlAPI : listUrlAPI) {
			System.out.println(config + "[" + from + "].url" + "=" + urlAPI);
			System.out.println(config + "[" + from + "].key" + "=" + "LIST_FORMS");
			from++;
		}
		return ResponseEntity.ok("Success");
	}

	@RequestMapping(value = "/testGetKeyIsVariableMap", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> testGetKeyIsVariableMap(@RequestParam(name = "key") String key) throws Exception {
		String value = "";
		if (articleMapOne.containsKey(key)) {
			value = articleMapOne.get(key);
		} else {
			value = "Key sai chết mọe";
		}
		return ResponseEntity.ok(value);
	}

	@RequestMapping(value = "/getRequestBodyFromDocAPI", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getRequestBodyFromDocAPI(@RequestBody JsonPermissionModel model) throws Exception {
		String strDoc=model.getJsonString();
		Map<String, String> mapAPILinkRequestBody=new HashMap<String, String>();
		mapAPILinkRequestBody.put("/ahihi", "kkk");
		JsonHelper.mapAPIAndRequestBody(strDoc,mapAPILinkRequestBody);
		return ResponseEntity.ok(mapAPILinkRequestBody);
	}
}
