package com.example.CsvProcessing.Service;

import com.example.CsvProcessing.Clients.DatabaseServiceClient;
import com.example.CsvProcessing.Clients.EligibilityServiceClient;
import com.example.CsvProcessing.Models.EligibilityCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CsvProcessingService {

    @Autowired
    private EligibilityServiceClient eligibilityServiceClient;

    @Autowired
    private DatabaseServiceClient databaseServiceClient;

    public String checkEligibility(Long rollNumber) {
        return databaseServiceClient.checkEligibility(rollNumber);
    }

    public void setEligibility(EligibilityCriteria object) {
        eligibilityServiceClient.setEligibility(object);
    }

    public byte[] processMultipleCsvFiles(List<MultipartFile> files) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ExecutorService executorService = Executors.newFixedThreadPool(files.size());
            List<Future<File>> futures = new ArrayList<>();

            for (MultipartFile file : files) {
                Callable<File> task = () -> processCsvFile(file);
                futures.add(executorService.submit(task));
            }

            for (Future<File> future : futures) {
                File updatedCsvFile = future.get();
                addToZipFile(updatedCsvFile, zos);
            }

            executorService.shutdown();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private File processCsvFile(MultipartFile file) throws IOException {
        File updatedCsvFile = File.createTempFile("updated_file", ".csv");
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new FileWriter(updatedCsvFile));
        ) {
            String line;
            synchronized (this) {
                while ((line = reader.readLine()) != null) {
                    String[] studentData = line.split(",");
                    Long rollNumber = Long.parseLong(studentData[0]);
                    boolean isEligible = eligibilityServiceClient.checkEligibility(studentData);
                    databaseServiceClient.saveOrUpdateStudent(rollNumber, isEligible);
                    line += "," + (isEligible ? "YES" : "NO");
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        return updatedCsvFile;
    }

    private void addToZipFile(File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        zos.putNextEntry(new ZipEntry(file.getName()));
        while ((bytesRead = fis.read(buffer)) != -1) {
            zos.write(buffer, 0, bytesRead);
        }
        fis.close();
        zos.closeEntry();
    }
}