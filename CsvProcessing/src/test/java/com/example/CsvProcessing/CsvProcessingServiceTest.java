package com.example.CsvProcessing;

import com.example.CsvProcessing.Clients.DatabaseServiceClient;
import com.example.CsvProcessing.Clients.EligibilityServiceClient;
import com.example.CsvProcessing.Models.EligibilityCriteria;
import com.example.CsvProcessing.Service.CsvProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CsvProcessingServiceTest {

    @Mock
    private EligibilityServiceClient eligibilityServiceClient;

    @Mock
    private DatabaseServiceClient databaseServiceClient;

    @InjectMocks
    private CsvProcessingService csvProcessingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckEligibility() {
        when(csvProcessingService.checkEligibility(any(Long.class))).thenReturn("Not Applicable");
        String result = csvProcessingService.checkEligibility(123L);
        assertEquals("Not Applicable", result);
    }

    @Test
    public void testSetEligibility() {
        EligibilityCriteria criteria = new EligibilityCriteria();
        criteria.setScienceMarks(80);
        criteria.setMathsMarks(75);
        criteria.setEnglishMarks(85);
        criteria.setComputerMarks(70);
        csvProcessingService.setEligibility(criteria);
        verify(eligibilityServiceClient, times(1)).setEligibility(criteria);
    }

    @Test
    public void testProcessMultipleCsvFiles() throws IOException {
        String filePath1 = "src/test/java/TestFiles/sheet1.csv";
        String filePath2 = "src/test/java/TestFiles/sheet2.csv";
        MockMultipartFile file1 = new MockMultipartFile("file", "file1.csv", "text/csv", Files.readAllBytes(Paths.get(filePath1)));
        MockMultipartFile file2 = new MockMultipartFile("file", "file2.csv", "text/csv", Files.readAllBytes(Paths.get(filePath2)));
        List<MultipartFile> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);
        byte[] result = csvProcessingService.processMultipleCsvFiles(files);
        assertEquals(result.length>0,true);
    }

}
