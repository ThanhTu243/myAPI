package com.thanhtu.myAPI.controller;

import java.util.ArrayList;
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
import com.thanhtu.myAPI.request.RequestResponse;

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
			result = result.replace("\"" + entryItem.getKey().toString() + "\"",
					"\"" + entryItem.getValue().toString() + "\"");
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
	public ResponseEntity<Object> testGetKeyIsVariableMap() throws Exception {
		String value = "";
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

	@RequestMapping(value = "/test", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> testremovemap() throws Exception {
		List<RequestResponse> listResponses = new ArrayList<>();
		String strCustomerId = "[\"1076222062\",\n" + "\"1076237332\",\n" + "\"1076237772\",\n" + "\"1076238224\",\n"
				+ "\"1076238582\",\n" + "\"1076239023\",\n" + "\"1076239177\",\n" + "\"1076241635\",\n"
				+ "\"1076242435\",\n" + "\"1076242437\",\n" + "\"1076244191\",\n" + "\"1076244338\",\n"
				+ "\"1076244690\",\n" + "\"1076244766\",\n" + "\"1076244829\",\n" + "\"1076244857\",\n"
				+ "\"1076244902\",\n" + "\"1076244932\",\n" + "\"1076245420\",\n" + "\"1076245427\",\n"
				+ "\"1076245526\",\n" + "\"1076245550\",\n" + "\"1076245551\",\n" + "\"1076245597\",\n"
				+ "\"1076245603\",\n" + "\"1076245645\",\n" + "\"1076245647\",\n" + "\"1076245651\",\n"
				+ "\"1076245898\"]";
		List<String> listCustomerId = new ObjectMapper().readValue(strCustomerId, new TypeReference<List<String>>() {
		});
		for (String cutomerId : listCustomerId) {
			RequestResponse requestResponse = new RequestResponse();
			requestResponse.setCustomerId(cutomerId);
			listResponses.add(requestResponse);
		}
		String strValue = "[4902994.0,\n" + "5233054.0,\n" + "5425059.0,\n" + "8277828.0,\n" + "5967833.0,\n"
				+ "7478176.0,\n" + "5919502.0,\n" + "4051729.0,\n" + "17899109,\n" + "5070176.0,\n" + "15147458,\n"
				+ "9464596.0,\n" + "2188731.0,\n" + "7827786.0,\n" + "737556.0,\n" + "11386989,\n" + "7231566.0,\n"
				+ "897254.0,\n" + "1269543.0,\n" + "16430785,\n" + "2738438.0,\n" + "9421341.0,\n" + "5980062.0,\n"
				+ "4071090.0,\n" + "6704386.0,\n" + "3049880.0,\n" + "10699238,\n" + "14776821,\n" + "7593173.0]";
		List<Double> listValue = new ObjectMapper().readValue(strValue, new TypeReference<List<Double>>() {
		});
		for (int i = 0; i < listValue.size(); i++) {
			RequestResponse requestResponse = listResponses.get(i);
			requestResponse.setValue(listValue.get(i));
		}
		System.out.println("[");
		for (RequestResponse requestResponse : listResponses) {
			System.out.println("{");
			System.out.println("\"customerId\":" + requestResponse.getCustomerId() + ",");
			System.out.println("\"value\":" + requestResponse.getValue() + ",");
			System.out.println("\"recentDate\":" + requestResponse.getRecentDate());
			System.out.println("},");
		}
		System.out.println("]");
		return ResponseEntity.ok(0);
	}
}
