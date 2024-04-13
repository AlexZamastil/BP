package cz.uhk.fim.project.bakalarka.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/**
 * Utility class for converting byte arrays to Spring's MultipartFile objects.
 *
 * @author Alex Zamastil
 */
@Component
public class MultiPartFileConverter {
    public MultiPartFileConverter() {
    }
    /**
     * Converts a byte array to a MultipartFile object.
     *
     * @param fileName The name of the file.
     * @param bytes    The byte array representing the file content.
     * @return The MultipartFile object created from the byte array.
     * @throws IOException if an I/O error occurs while creating the MultipartFile.
     */
    public MultipartFile convert(String fileName, byte[] bytes) throws IOException {

        return new MockMultipartFile(fileName, bytes);
    }
}