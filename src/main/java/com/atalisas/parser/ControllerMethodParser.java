package com.atalisas.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerMethodParser {


    private String getMappingAnnotation(String methodName) {
        if (methodName.startsWith("get")) {
            return "@GetMapping";
        } else if (methodName.startsWith("post") || methodName.startsWith("add")) {
            return "@PostMapping";
        } else if (methodName.startsWith("put") || methodName.startsWith("update")) {
            return "@PutMapping";
        } else if (methodName.startsWith("delete")) {
            return "@DeleteMapping";
        } else {
            return "@GetMapping";
        }
    }

    public String translateMethodToAnnotation(String methodName) {
        String annotation = getMappingAnnotation(methodName);
        return annotation;
    }

}
