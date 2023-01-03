package com.thanhtu.myAPI.helper;

import java.util.ArrayList;
import java.util.List;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;

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

	public static void jsonToJavaClass(String fileName, JSONObject jsonObj) throws Exception {
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
					String subFileName = "D:\\MyAPI\\JsonToClass\\" + keyStr + ".txt";
					jsonToJavaClassUppercase(subFileName, jsonObject);
				}
			}
		}
		System.out.println(fileName);
		attributeList.stream().forEach(s -> System.out.println(s));
		FileHelper.writeFileJavaClass(fileName, attributeList, listGetSetAttribute);
	}

	public static void jsonToJavaClassUppercase(String fileName, JSONObject jsonObj) throws Exception {
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
			if (keyvalue instanceof Integer && keyStr.contains("Date") || keyvalue instanceof Long &&keyStr.contains("Date") ||keyStr.contains("Date")) {
				attribute = generateAttribute("Timestamp", keyStr);
				strGetterSetter = generateGetterSetter("Timestamp", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("Timestamp", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("Timestamp", keyStr);
			} else if (keyvalue instanceof Integer || keyStr.toLowerCase().contains("id")) {
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
			} else if (keyvalue instanceof String) {
				attribute = generateAttribute("String", keyStr);
				strGetterSetter = generateGetterSetter("String", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("String", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("String", keyStr);
			} else if (keyvalue instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(keyvalue.toString());
				Object objType = jsonArray.get(0);
				String type = checkDataType(objType).equals("Object")
						? keyStr.substring(4).substring(0, 1).toUpperCase() + keyStr.substring(4).substring(1)
								+ "Request"
						: checkDataType(objType);
				attribute = generateAttribute("List<" + type + ">", keyStr);
				strGetterSetter = generateGetterSetter("List<" + type + ">", keyStr);
				upperCaseAttribute = generateUpperAttributeAndJsonProperties("List<" + type + ">", keyStr);
				strUpperAttributeGetSet = generateUpperAttributeGetSet("List<" + type + ">", keyStr);
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
				Object objType = jsonArray.get(0);
				String type = checkDataType(objType);
				if (type == "Object") {
//					ObjectMapper mapper = new ObjectMapper();
//					mapper.setSerializationInclusion(Include.ALWAYS);
					JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
					String subFileName = "D:\\MyAPI\\JsonToClassUppercase\\" + keyStr + ".txt";
					jsonToJavaClassUppercase(subFileName, jsonObject);
				}
			}
		}
		System.out.println(fileName);
		attributeList.stream().forEach(s -> System.out.println(s));
		FileHelper.writeFileJavaClassAndUpperCase(fileName, attributeList, upperCaseAttributeList, listGetSetAttribute,
				listGetSetAttributeUppercase);
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
		String strPattern = "\\\"(\\w+)\":";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(jsonString);

		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(result, matcher.group().toUpperCase());
		}

		matcher.appendTail(result);
		return result.toString();
	}
}
