package com.thanhtu.myAPI.helper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

import com.sun.codemodel.JCodeModel;

public class JsonHelper {
	public static List<String> ListPropertiesFromJsonString(String jsonString) {
		List<String> listProperties = new ArrayList<String>();
		String strPattern = "\\\"(\\w+)\\\\\":";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(jsonString);

		while (matcher.find()) {
			listProperties.add(matcher.group(1));
		}
		return listProperties;
	}

	public static List<String> ListPropertiesFromJsonStringNoEscape(String jsonStringNoEscape) {
		List<String> listProperties = new ArrayList<String>();
		String strPattern = "\"\\s*(\\w+)\\s*\\\"\\s*:";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(jsonStringNoEscape);

		while (matcher.find()) {
			listProperties.add(matcher.group(1));
		}
		return listProperties;
	}

	public static List<String> matchUrlAPI(String jsonString) {
		List<String> listUrlAPI = new ArrayList<String>();
		String strPattern = "value\\s*=\\s*\"([\\w\\/]+)\"";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(jsonString);

		while (matcher.find()) {
			listUrlAPI.add(matcher.group(1));
		}
		return listUrlAPI;
	}

	public static void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName,
			String javaClassName) throws IOException {
		JCodeModel jcodeModel = new JCodeModel();

		GenerationConfig config = new DefaultGenerationConfig() {

			@Override
			public SourceType getSourceType() {
				return SourceType.JSONSCHEMA;
			}

			@Override
			public boolean isIncludeGeneratedAnnotation() {
				return false;
			}

			@Override
			public boolean isIncludeHashcodeAndEquals() {
				return false;
			}

			@Override
			public boolean isIncludeToString() {
				return false;
			}

			@Override
			public boolean isInitializeCollections() {
				return false;
			}

			@Override
			public boolean isIncludeAllPropertiesConstructor() {
				return false;
			}

			@Override
			public boolean isIncludeAdditionalProperties() {
				return false;
			}

		};

		SchemaMapper mapper = new SchemaMapper(
				new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
		mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

		jcodeModel.build(outputJavaClassDirectory);
	}

	public static void jsonToJavaClass(String fileName, JSONObject jsonObj,StringBuilder result) throws Exception {
		List<String> attributeList = new ArrayList<String>();
		List<String> listGetSetAttribute = new ArrayList<String>();
		for (Object key : jsonObj.keySet()) {
			// based on you key types
			String keyStr = String.valueOf(key);
			Object keyvalue = jsonObj.get(keyStr);
			String attribute = "";
			String strGetterSetter = "";
			if (keyvalue instanceof Integer) {
				attribute = generateAttribute("Integer", keyStr);
				strGetterSetter = generateGetterSetter("Integer", keyStr);
			} else if (keyvalue instanceof Long) {
				attribute = generateAttribute("Long", keyStr);
				strGetterSetter = generateGetterSetter("Long", keyStr);
			} else if (keyvalue instanceof Double) {
				attribute = generateAttribute("Double", keyStr);
				strGetterSetter = generateGetterSetter("Double", keyStr);
			} else if (keyvalue instanceof String) {
				attribute = generateAttribute("String", keyStr);
				strGetterSetter = generateGetterSetter("String", keyStr);
			} else if (keyvalue instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(keyvalue.toString());
				Object objType = jsonArray.get(0);
				String type = checkDataType(objType);
				attribute = generateAttribute("List<" + type + ">", keyStr);
				strGetterSetter = generateGetterSetter("List<" + type + ">", keyStr);
			} else if (keyvalue instanceof Boolean) {
				attribute = generateAttribute("Boolean", keyStr);
				strGetterSetter = generateGetterSetter("Boolean", keyStr);
			} else {
				attribute = generateAttribute("Object", keyStr);
				strGetterSetter = generateGetterSetter("Object", keyStr);
			}
			attributeList.add(attribute);
			listGetSetAttribute.add(strGetterSetter);

			// for nested objects iteration if required
			if (keyvalue instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(keyvalue.toString());
				Object objType = jsonArray.get(0);
				String type = checkDataType(objType);
				if (type == "Object") {
//					ObjectMapper mapper = new ObjectMapper();
//					mapper.setSerializationInclusion(Include.ALWAYS);
					JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
					jsonToJavaClassUppercase(keyStr, jsonObject,result);
				}
			}
		}
		System.out.println(fileName);
		attributeList.stream().forEach(s -> System.out.println(s));
		FileHelper.writeFileJavaClass(fileName, attributeList, listGetSetAttribute,result);
	}

	public static void jsonToJavaClassUppercase(String fileName, JSONObject jsonObj,StringBuilder result) throws Exception {
		List<String> attributeList = new ArrayList<String>();
		List<String> listGetSetAttribute = new ArrayList<String>();
		List<String> upperCaseAttributeList = new ArrayList<String>();
		List<String> listGetSetAttributeUppercase = new ArrayList<String>();
		for (Object key : jsonObj.keySet()) {
			// based on you key types
			String keyStr = String.valueOf(key);
			Object keyvalue = jsonObj.get(keyStr);
			String attribute = "";
			String strGetterSetter = "";
			String upperCaseAttribute = "";
			String strUpperAttributeGetSet = "";

			if ((keyvalue instanceof Integer
					&& (keyStr.toLowerCase().contains("date") || keyStr.toLowerCase().contains("time"))
					&& (!keyStr.toLowerCase().contains("list") || !keyStr.toLowerCase().contains("lst")))
					|| (keyvalue instanceof Long
							&& (keyStr.toLowerCase().contains("date") || keyStr.toLowerCase().contains("time"))
							&& (!keyStr.toLowerCase().contains("list") || !keyStr.toLowerCase().contains("lst")))) {
				attribute = generateAttribute("Timestamp", keyStr);
				strGetterSetter = generateGetterSetter("Timestamp", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Timestamp", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Timestamp", keyStr);
			} else if (keyvalue instanceof Integer) {
				attribute = generateAttribute("Integer", keyStr);
				strGetterSetter = generateGetterSetter("Integer", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Integer", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Integer", keyStr);
			} else if (keyvalue instanceof Long) {
				attribute = generateAttribute("Long", keyStr);
				strGetterSetter = generateGetterSetter("Long", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Long", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Long", keyStr);
			} else if (keyvalue instanceof Double) {
				attribute = generateAttribute("Double", keyStr);
				strGetterSetter = generateGetterSetter("Double", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Double", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Double", keyStr);
			} else if (keyvalue instanceof String || keyStr.contains("word") || keyStr.contains("Word")) {
				attribute = generateAttribute("String", keyStr);
				strGetterSetter = generateGetterSetter("String", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("String", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("String", keyStr);
			} else if (keyvalue instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(keyvalue.toString());
				String type;
				if (jsonArray.isEmpty()) {
					type = "Object";
				} else {
					Object objType = jsonArray.get(0);
					List<Integer> listIntegers = getElemntEQUALLISTORLST(keyStr.toString());
					if (listIntegers.size() == 0) {
						type = checkDataType(objType).equals("Object")
								? keyStr.substring(0, 1).toUpperCase() + keyStr.substring(1) + "Request"
								: checkDataType(objType);
					} else {
						String stringRemoveStringList = keyStr.toString().substring(0, listIntegers.get(0))
								+ keyStr.toString().substring(listIntegers.get(1) + 1);
						type = checkDataType(objType).equals("Object")
								? stringRemoveStringList.substring(0, 1).toUpperCase()
										+ stringRemoveStringList.substring(1) + "Request"
								: checkDataType(objType);
					}
				}

				attribute = generateAttribute("List&lt;" + type + "&gt;", keyStr);
				strGetterSetter = generateGetterSetter("List&lt;" + type + "&gt;", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("List&lt;" + type + "&gt;", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("List&lt;" + type + "&gt;", keyStr);
			} else if (keyvalue instanceof JSONObject) {
				String type = keyStr.substring(0, 1).toUpperCase() + keyStr.substring(1);
				attribute = generateAttribute(type, keyStr);
				strGetterSetter = generateGetterSetter(type, keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties(type, keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet(type, keyStr);

			} else if (keyvalue instanceof Boolean) {
				attribute = generateAttribute("Boolean", keyStr);
				strGetterSetter = generateGetterSetter("Boolean", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Boolean", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Boolean", keyStr);
			} else {
				attribute = generateAttribute("Object", keyStr);
				strGetterSetter = generateGetterSetter("Object", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Object", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Object", keyStr);
			}
			attributeList.add(attribute);
			listGetSetAttribute.add(strGetterSetter);
			upperCaseAttributeList.add(upperCaseAttribute);
			listGetSetAttributeUppercase.add(strUpperAttributeGetSet);
			// for nested objects iteration if required
			if (keyvalue instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(keyvalue.toString());
				String type;
				if (jsonArray.isEmpty()) {
					type = "ObjectEmpty";
				} else {
					Object objType = jsonArray.get(0);
					type = checkDataType(objType);
				}

				if (type == "Object") {
//					ObjectMapper mapper = new ObjectMapper();
//					mapper.setSerializationInclusion(Include.ALWAYS);
					JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
					jsonToJavaClassUppercase(keyStr, jsonObject,result);
				} else if (type == "ObjectEmpty") {

				}
			}
			if (keyvalue instanceof JSONObject) {
				jsonToJavaClassUppercase(keyStr, new JSONObject(keyvalue.toString()),result);
			}
		}
		System.out.println(fileName);
		attributeList.stream().forEach(s -> System.out.println(s));
		FileHelper.writeFileJavaClassAndUpperCase(fileName, attributeList, upperCaseAttributeList, listGetSetAttribute,
				listGetSetAttributeUppercase,result);
	}

	public static String generateAttribute(String dataType, String keyStr) {
		return "private " + dataType + " " + keyStr + ";";
	}

	public static String generateGetterSetter(String dataType, String keyStr) {
		String strGet = "public " + dataType + " get" + keyStr.substring(0, 1).toUpperCase() + keyStr.substring(1)
				+ "(){" + System.lineSeparator() + "    return " + keyStr + ";" + System.lineSeparator() + "}";
		String strSet = "public void set" + keyStr.substring(0, 1).toUpperCase() + keyStr.substring(1) + "(" + dataType
				+ " " + keyStr + "){" + System.lineSeparator() + "    this." + keyStr + " = " + keyStr + ";"
				+ System.lineSeparator() + "}";
		return strGet + System.lineSeparator() + strSet;
	}

	private static String generateUpperAttributeAndJsonProperties(String dataType, String keyStr) {
		keyStr.toUpperCase();
		String strJsonProperties = "@JsonProperty(value = \"" + keyStr.toUpperCase() + "\")" + System.lineSeparator()
				+ "private " + dataType + " " + "req" + keyStr + ";";
		return strJsonProperties;
	}

	private static String generateUpperAttributeGetSet(String dataType, String keyStr) {
		keyStr = "req" + keyStr;
		String strGet = "public " + dataType + " get" + keyStr.substring(0, 1).toUpperCase() + keyStr.substring(1)
				+ "(){" + System.lineSeparator() + "    return " + keyStr + ";" + System.lineSeparator() + "}";
		String strSet = "public void set" + keyStr.substring(0, 1).toUpperCase() + keyStr.substring(1) + "(" + dataType
				+ " " + keyStr + "){" + System.lineSeparator() + "    this." + keyStr + " = " + keyStr + ";"
				+ System.lineSeparator() + "    this." + keyStr.substring(3) + " = " + keyStr + ";"
				+ System.lineSeparator() + "}";
		return strGet + System.lineSeparator() + strSet;
	}

	private static String checkDataType(Object obj) {
		if (obj instanceof Integer) {
			return "Integer";
		} else if (obj instanceof Long) {
			return "Long";
		} else if (obj instanceof Double) {
			return "Double";
		} else if (obj instanceof String) {
			return "String";
		} else if (obj instanceof Boolean) {
			return "Boolean";
		} else {
			return "Object";
		}
	}

	public static String upperCaseAllProperties(String jsonString) {
		String strPattern = "\\\"(\\w+)\\s*\"\\s*:";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(jsonString);

		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(result, matcher.group().toUpperCase());
		}

		matcher.appendTail(result);
		return result.toString();
	}

	public static List<Integer> getElemntEQUALLISTORLST(String string) {
		List<Integer> list = new ArrayList<Integer>();
		if (string.toUpperCase().contains("LIST")) {
			Integer from = string.toUpperCase().indexOf("LIST");
			list.add(from);
			list.add(from + 3);
		} else if (string.toUpperCase().contains("LST")) {
			Integer from = string.toUpperCase().indexOf("LST");
			list.add(from);
			list.add(from + 2);
		}
		return list;
	}

	public static void mapAPIAndRequestBody(String jsonString, Map<String, String> mapAPILinkRequestBody) {
		// TODO Auto-generated method stub
//		String strPattern = "/(\\/[\\w\\/]+)[\\\\\":\\]\\$#,\\/{\\s\\w[-]+schemas\\/(\\w+)/gm";
		String strPattern = "\"(\\/[\\w\\/]+)[\": \\],\\/$#{\\s\\w\\[-]+\\/schemas\\/(\\w+)\"";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(jsonString);

		while (matcher.find()) {
			mapAPILinkRequestBody.put(matcher.group(1), matcher.group(2));
		}
	}
}
