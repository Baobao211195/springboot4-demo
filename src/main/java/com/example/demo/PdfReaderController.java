package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PdfReaderController {

    private static final Logger log = LoggerFactory.getLogger(PdfReaderController.class);
    private final ResourceLoader resourceLoader;

    public PdfReaderController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/read-pdf")
    public String readPdf() {
        // Tải file PDF từ thư mục `resources/templates`
        // !!! QUAN TRỌNG: Hãy thay "ten-file-cua-ban.pdf" bằng tên file PDF thực tế của bạn
        Resource pdfResource = resourceLoader.getResource("classpath:templates/A Latency Handbook for SWE - Quang Hoang.pdf");

        log.info("Bắt đầu đọc file PDF từ: {}", pdfResource.getFilename());
        TikaDocumentReader pdfReader = new TikaDocumentReader(pdfResource);
        List<Document> documents = pdfReader.get();
        documents.forEach(document -> log.info("Nội dung trang: {}", document.getText()));
        log.info("Đã đọc xong file PDF.");

        return "Đã đọc và in nội dung PDF ra console. Vui lòng kiểm tra log của ứng dụng.";
    }
}