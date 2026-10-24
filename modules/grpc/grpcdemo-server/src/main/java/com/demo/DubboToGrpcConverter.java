package com.demo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dubbo 接口批量转换为 gRPC Proto 定义工具
 *
 * 功能：
 * 1. 扫描指定包下的 Dubbo 接口
 * 2. 解析接口方法、参数、返回值
 * 3. 生成对应的 .proto 文件
 *
 * 使用示例：
 * <pre>
 * DubboToGrpcConverter converter = new DubboToGrpcConverter();
 * converter.setSourcePackage("com.dmall.aftersale.si");
 * converter.setOutputDir("D:/proto/output");
 * converter.convert();
 * </pre>
 */
@Slf4j
public class DubboToGrpcConverter {

    private String sourcePackage = "com.dmall.aftersale.si";
    private String outputDir = "target/proto";
    private String protoPackage = "com.dfgd.aftersale.si";
    private Map<String, Set<String>> importedPackages = new HashMap<>();

    public DubboToGrpcConverter() {
    }

    public void convert() throws Exception {
        log.info("开始转换 Dubbo 接口为 gRPC Proto 定义");
        log.info("源包: {}", sourcePackage);
        log.info("输出目录: {}", outputDir);

        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            outputDirFile.mkdirs();
        }

        List<Class<?>> interfaces = scanInterfaces(sourcePackage);
        log.info("扫描到 {} 个接口", interfaces.size());

        for (Class<?> iface : interfaces) {
            try {
                convertInterface(iface);
            } catch (Exception e) {
                log.error("转换接口 {} 失败", iface.getName(), e);
            }
        }

        log.info("转换完成！Proto 文件已保存到: {}", outputDir);
    }

    private List<Class<?>> scanInterfaces(String packageName) throws Exception {
        List<Class<?>> interfaces = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        java.net.URL url = classLoader.getResource(packagePath);

        if (url != null) {
            File dir = new File(url.toURI());
            if (dir.exists()) {
                scanDirectory(dir, packageName, interfaces);
            }
        }

        return interfaces;
    }

    private void scanDirectory(File dir, String packageName, List<Class<?>> interfaces) {
        File[] files = dir.listFiles(file -> {
            if (file.isDirectory()) {
                return true;
            }
            return file.getName().endsWith(".class");
        });

        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), interfaces);
            } else {
                String className = packageName + "." +
                        file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isInterface() && !clazz.getName().contains("$")) {
                        interfaces.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.warn("无法加载类: {}", className);
                }
            }
        }
    }

    private void convertInterface(Class<?> iface) throws IOException {
        String interfaceName = iface.getSimpleName();
        String protoFileName = toSnakeCase(interfaceName).replace("_service", "_service.proto");
        File protoFile = new File(outputDir, protoFileName);

        StringBuilder sb = new StringBuilder();

        writeHeader(sb, interfaceName);
        writePackage(sb);
        writeImports(sb, iface);
        writeServiceDefinition(sb, iface);
        writeMessages(sb, iface);

        writeFile(protoFile, sb.toString());
        log.info("生成 Proto 文件: {}", protoFile.getAbsolutePath());
    }

    private void writeHeader(StringBuilder sb, String interfaceName) {
        sb.append("syntax = \"proto3\";\n\n");
        sb.append("// Auto-generated from Dubbo interface: ").append(interfaceName).append("\n");
        sb.append("// DO NOT EDIT MANUALLY\n\n");
    }

    private void writePackage(StringBuilder sb) {
        sb.append("package ").append(protoPackage).append(";\n\n");
        sb.append("option java_package = \"com.dmall.aftersale.si.grpc\";\n");
        sb.append("option java_multiple_files = true;\n\n");
    }

    private void writeImports(StringBuilder sb, Class<?> iface) {
        Set<String> imports = new TreeSet<>();
        imports.add("google/protobuf/wrappers.proto");

        Method[] methods = iface.getMethods();
        for (Method method : methods) {
            analyzeType(method.getReturnType(), imports);
            for (Class<?> paramType : method.getParameterTypes()) {
                analyzeType(paramType, imports);
            }
        }

        for (String imp : imports) {
            if (!imp.equals("google/protobuf/wrappers.proto") || needImportWrappers(imports)) {
                sb.append("import \"").append(imp).append("\";\n");
            }
        }

        if (!imports.isEmpty()) {
            sb.append("\n");
        }
    }

    private boolean needImportWrappers(Set<String> imports) {
        return imports.stream().anyMatch(imp -> imp.contains("wrappers"));
    }

    private void writeServiceDefinition(StringBuilder sb, Class<?> iface) {
        String serviceName = iface.getSimpleName();
        if (!serviceName.endsWith("Service")) {
            serviceName = serviceName + "Service";
        }

        sb.append("service ").append(serviceName).append(" {\n");

        Method[] methods = iface.getMethods();
        int fieldNumber = 1;
        for (Method method : methods) {
            writeRpcMethod(sb, method, fieldNumber++);
        }

        sb.append("}\n\n");
    }

    private void writeRpcMethod(StringBuilder sb, Method method, int fieldNumber) {
        String methodName = toCamelCase(method.getName());
        String requestType = method.getName() + "Request";
        String responseType = getResponseTypeName(method);

        sb.append("  rpc ").append(methodName)
                .append(" (").append(requestType).append(")")
                .append(" returns (").append(responseType).append(");\n");
    }

    private void writeMessages(StringBuilder sb, Class<?> iface) {
        Method[] methods = iface.getMethods();

        for (Method method : methods) {
            writeRequestMessage(sb, method);
            writeResponseMessage(sb, method);
        }

        for (Method method : methods) {
            Class<?>[] paramTypes = method.getParameterTypes();
            for (Class<?> paramType : paramTypes) {
                if (!isPrimitiveOrWrapper(paramType) && !paramType.getName().startsWith("java.")) {
                    writeMessageDefinition(sb, paramType, new HashSet<>());
                }
            }

            Class<?> returnType = unwrapGeneric(method.getGenericReturnType());
            if (returnType != null && !isPrimitiveOrWrapper(returnType) && !returnType.getName().startsWith("java.")) {
                if (!returnType.getName().contains("AftersaleDubboResult") &&
                        !returnType.getName().contains("AftersaleRpcResult")) {
                    writeMessageDefinition(sb, returnType, new HashSet<>());
                }
            }
        }
    }

    private void writeRequestMessage(StringBuilder sb, Method method) {
        String messageName = method.getName() + "Request";
        sb.append("message ").append(messageName).append(" {\n");

        Class<?>[] paramTypes = method.getParameterTypes();
        String[] paramNames = getParameterNames(method);

        int fieldNumber = 1;
        for (int i = 0; i < paramTypes.length; i++) {
            String fieldName = toSnakeCase(paramNames[i]);
            String fieldType = mapJavaTypeToProto(paramTypes[i]);

            sb.append("  ").append(fieldType).append(" ").append(fieldName)
                    .append(" = ").append(fieldNumber++).append(";\n");
        }

        sb.append("}\n\n");
    }

    private void writeResponseMessage(StringBuilder sb, Method method) {
        String messageName = method.getName() + "Response";
        sb.append("message ").append(messageName).append(" {\n");

        Type genericReturnType = method.getGenericReturnType();
        Class<?> returnType = unwrapGeneric(genericReturnType);

        if (returnType != null && (returnType.getName().contains("AftersaleDubboResult") ||
                returnType.getName().contains("AftersaleRpcResult"))) {
            sb.append("  string code = 1;\n");
            sb.append("  string msg = 2;\n");

            Type actualType = extractGenericType(genericReturnType);
            if (actualType != null && actualType instanceof Class) {
                Class<?> dataClass = (Class<?>) actualType;
                if (!dataClass.getName().equals("java.lang.Object")) {
                    String dataType = mapJavaTypeToProto(dataClass);
                    sb.append("  ").append(dataType).append(" data = 3;\n");
                }
            }
        } else {
            String fieldType = mapJavaTypeToProto(returnType);
            sb.append("  ").append(fieldType).append(" data = 1;\n");
        }

        sb.append("}\n\n");
    }

    private void writeMessageDefinition(StringBuilder sb, Class<?> clazz, Set<String> processedClasses) {
        if (clazz == null || isPrimitiveOrWrapper(clazz) || clazz.isArray() ||
                clazz.getName().startsWith("java.") || processedClasses.contains(clazz.getName())) {
            return;
        }

        processedClasses.add(clazz.getName());

        String messageName = clazz.getSimpleName();
        sb.append("message ").append(messageName).append(" {\n");

        Field[] fields = getAllFields(clazz);
        int fieldNumber = 1;

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            String fieldName = toSnakeCase(field.getName());
            String fieldType = mapJavaTypeToProto(field.getType());

            sb.append("  ").append(fieldType).append(" ").append(fieldName)
                    .append(" = ").append(fieldNumber++).append(";\n");
        }

        sb.append("}\n\n");

        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (!isPrimitiveOrWrapper(fieldType) && !fieldType.getName().startsWith("java.")) {
                writeMessageDefinition(sb, fieldType, processedClasses);
            }
        }
    }

    private String mapJavaTypeToProto(Class<?> javaType) {
        if (javaType == null) {
            return "string";
        }

        String typeName = javaType.getSimpleName();

        switch (typeName) {
            case "Long":
            case "long":
                return "int64";
            case "Integer":
            case "int":
                return "int32";
            case "Double":
            case "double":
                return "double";
            case "Float":
            case "float":
                return "float";
            case "Boolean":
            case "boolean":
                return "bool";
            case "String":
                return "string";
            case "ByteString":
                return "bytes";
            default:
                if (javaType.isArray() || javaType.getName().startsWith("java.util.List")) {
                    Class<?> componentType = javaType.isArray() ?
                            javaType.getComponentType() : Object.class;
                    return "repeated " + mapJavaTypeToProto(componentType);
                }
                return javaType.getSimpleName();
        }
    }

    private String getResponseTypeName(Method method) {
        return method.getName() + "Response";
    }

    private Class<?> unwrapGeneric(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type rawType = pt.getRawType();
            if (rawType instanceof Class) {
                Class<?> rawClass = (Class<?>) rawType;
                if (rawClass.getName().contains("AftersaleRpcResult") ||
                        rawClass.getName().contains("AftersaleDubboResult")) {
                    Type[] actualTypeArgs = pt.getActualTypeArguments();
                    if (actualTypeArgs.length > 0 && actualTypeArgs[0] instanceof Class) {
                        return (Class<?>) actualTypeArgs[0];
                    }
                }
            }
        }
        return Object.class;
    }

    private Type extractGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] actualTypeArgs = pt.getActualTypeArguments();
            if (actualTypeArgs.length > 0) {
                return actualTypeArgs[0];
            }
        }
        return null;
    }

    private String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] names = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            names[i] = parameters[i].getName();
        }
        return names;
    }

    private Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class || clazz == int.class ||
                clazz == Long.class || clazz == long.class ||
                clazz == Double.class || clazz == double.class ||
                clazz == Float.class || clazz == float.class ||
                clazz == Boolean.class || clazz == boolean.class ||
                clazz == Byte.class || clazz == byte.class ||
                clazz == Short.class || clazz == short.class ||
                clazz == Character.class || clazz == char.class;
    }

    private String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));

        for (int i = 1; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private String toCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }

    private void analyzeType(Class<?> type, Set<String> imports) {
        if (type == null) return;

        if (type.getName().startsWith("java.util")) {
            if (type.getName().contains("List") || type.getName().contains("Set")) {
                imports.add("google/protobuf/wrappers.proto");
            }
        }
    }

    private void writeFile(File file, String content) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setProtoPackage(String protoPackage) {
        this.protoPackage = protoPackage;
    }
}
