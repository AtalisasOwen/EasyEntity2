package com.atalisas.utils;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;

public class JavaFileWriter {
    public static void writeJavaFile(JavaFileObject builderFile, String entity) throws IOException {
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.print(entity);
        }
    }


}
