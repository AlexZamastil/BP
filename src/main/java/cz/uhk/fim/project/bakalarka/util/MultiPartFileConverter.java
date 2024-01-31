package cz.uhk.fim.project.bakalarka.util;

import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MultiPartFileConverter {
    public MultiPartFileConverter() {
    }

    public MultipartFile convert(String fileName, byte[] bytes) throws IOException {

        return new MockMultipartFile(fileName, bytes);
    }
}