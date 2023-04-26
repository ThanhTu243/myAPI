package com.thanhtu.myAPI.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
		articleMapOne.put("key1", "value1 nè");
		articleMapOne.put("key2", "value2 nè");
		articleMapOne.put("key3", "value3 nè");
		articleMapOne.put("key4", "value4 nè");
		articleMapOne.put("key5", "value5 nè");
		articleMapOne.put("key6", "value6 nè");

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
		Set<String> setPropertiesCamel = listPropertiesCamel.stream().collect(Collectors.toSet());
		List<String> listPropertiesUppercase = JsonHelper
				.ListPropertiesFromJsonStringNoEscape(request.getUppercaseJsonString());
		Set<String> setPropertiesUppercase = listPropertiesUppercase.stream().collect(Collectors.toSet());
		Map<String, String> mapPropertiesCamel = setPropertiesCamel.stream().collect(Collectors.toMap(s -> s, s -> s));
		Map<String, String> mapPropertiesUppercase = setPropertiesUppercase.stream()
				.collect(Collectors.toMap(s -> s, s -> s));
		for (Map.Entry<String, String> entry : mapPropertiesCamel.entrySet()) {
			String key = entry.getKey().toUpperCase();
			if (mapPropertiesUppercase.containsKey(key)) {
				mapPropertiesUppercase.put(key, entry.getKey());
			}
		}
		String result = request.getUppercaseJsonString();
		for (Map.Entry<String, String> entryItem : mapPropertiesUppercase.entrySet()) {
			result = result.replace("\""+entryItem.getKey().toString()+"\"", "\""+entryItem.getValue().toString()+"\"");
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
		String strDoc = model.getJsonString();
		Map<String, String> mapAPILinkRequestBody = new HashMap<String, String>();
		mapAPILinkRequestBody.put("/ahihi", "kkk");
		JsonHelper.mapAPIAndRequestBody(strDoc, mapAPILinkRequestBody);
		return ResponseEntity.ok(mapAPILinkRequestBody);
	}

	@RequestMapping(value = "/testremovemap", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> testremovemap() throws Exception {
		Map<String, String> mapTest = new HashMap<String, String>();
		mapTest = new HashMap<>();
		mapTest.put("key1", "value1 nè");
		mapTest.put("key2", "value2 nè");
		mapTest.put("key3", "value3 nè");
		mapTest.put("key4", "value4 nè");
		mapTest.put("key5", "value5 nè");
		mapTest.put("key6", "value6 nè");

		for (Iterator<Map.Entry<String, String>> it = mapTest.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			if (!key.equals("key3")) {
				it.remove();
			}
		}
		return ResponseEntity.ok(mapTest);
	}
}
