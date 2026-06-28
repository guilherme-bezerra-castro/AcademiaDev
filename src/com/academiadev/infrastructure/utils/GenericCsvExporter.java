package com.academiadev.infrastructure.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

public class GenericCsvExporter {
	public <T> String export(List<T> data, List<String> fieldNames) {
        if (data == null || data.isEmpty()) {
            return "(Nenhum dado para exportar)";
        }
 
        StringBuilder sb = new StringBuilder();
 
        sb.append(String.join(",", fieldNames)).append("\n");
 
        for (T obj : data) {
            StringJoiner row = new StringJoiner(",");
 
            for (String fieldName : fieldNames) {
                String value = extractField(obj, fieldName);
                // Escapa vírgulas e aspas dentro do valor (CSV padrão)
                if (value.contains(",") || value.contains("\"")) {
                    value = "\"" + value.replace("\"", "\"\"") + "\"";
                }
                row.add(value);
            }
 
            sb.append(row).append("\n");
        }
 
        return sb.toString();
    }
 
    private String extractField(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
 
        // Sobe na hierarquia de classes para encontrar campos herdados
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                return value != null ? value.toString() : "";
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                return "[acesso negado]";
            }
        }
        return "[campo não encontrado: " + fieldName + "]";
    }
}
