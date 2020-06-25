package org.example.protobuf;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import javax.ws.rs.InternalServerErrorException;

import java.io.IOException;

public final class ProtobufJsonWriter {

    private static final JsonFormat.Printer JSON_PRINTER = configureJsonPrinter();

    private ProtobufJsonWriter() {
        throw new UnsupportedOperationException("Instantiation of utility class is not allowed");
    }

    /**
     * Prints the provided protocolBuffers {@link Message} as JSON to the provided Appendable (e.g. an OutputStream)
     *
     * @param message the message to print
     * @param out     where to print the json encoded message to
     */
    public static void printTo(Message message, Appendable out) {
        try {
            JSON_PRINTER.appendTo(message, out);
        } catch (IOException e) {
            String msg =
                    "Failed to convert Protocol Buffer message to JSON [class=" + message.getClass().getName() + "]";
            throw new InternalServerErrorException(msg, e);
        }
    }

    private static JsonFormat.Printer configureJsonPrinter() {
        JsonFormat.Printer printer = JsonFormat.printer();

        //cannot use getBoolean(prop) as default case is inverted
        boolean preserveWhiteSpace = Boolean.parseBoolean(
                System.getProperty("protobuf.jsonFormat.preserveWhitespace", "true"));
        if (!preserveWhiteSpace) {
            printer = printer.omittingInsignificantWhitespace();
        }

        if (Boolean.getBoolean("protobuf.jsonFormat.includeDefaultValueFields")) {
            printer = printer.includingDefaultValueFields();
        }

        if (Boolean.getBoolean("protobuf.jsonFormat.enumsAsInts")) {
            printer = printer.printingEnumsAsInts();
        }

        return printer;
    }
}
