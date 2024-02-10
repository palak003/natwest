package com.example.CsvProcessing.Service;

import com.example.CsvProcessing.Clients.DatabaseServiceClient;
import com.example.CsvProcessing.Clients.EligibilityServiceClient;
import com.example.CsvProcessing.Models.EligibilityCriteria;
import com.example.CsvProcessing.Models.Student;
import org.springframework.beans.factory.annotation.Autowired;
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
        int BATCH_SIZE = 500;
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new FileWriter(updatedCsvFile));
        ) {
            String line;
            List<String[]> batch = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    String[] studentData = line.split(",");
                    batch.add(studentData);
                    if (batch.size() == BATCH_SIZE) {
                        processBatch(batch, writer);
                        batch.clear();
                    }
                }
                if (!batch.isEmpty()) {
                    processBatch(batch, writer);
                    batch.clear();
                }

        }
        return updatedCsvFile;
    }

    private void processBatch(List<String[]> batch, BufferedWriter writer) throws IOException {
        List<Boolean> eligibilityResults = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        for (String[] studentData : batch) {
            Long rollNumber = Long.parseLong(studentData[0]);
            boolean isEligible = eligibilityServiceClient.checkEligibility(studentData);
            Student student=new Student();
            student.setEligible(isEligible);
            student.setRollNumber(rollNumber);
            students.add(student);
            eligibilityResults.add(isEligible);
        }
        databaseServiceClient.saveOrUpdateStudents(students);
        for (int i = 0; i < batch.size(); i++) {
            String[] studentData = batch.get(i);
            boolean isEligible = eligibilityResults.get(i);
            String line = String.join(",", studentData) + "," + (isEligible ? "YES" : "NO");
            writer.write(line);
            writer.newLine();
        }
    }

    private void addToZipFile(File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        String fileName = "updated_files_"+ ".csv";
        zos.putNextEntry(new ZipEntry(file.getName()));
        while ((bytesRead = fis.read(buffer)) != -1) {
            zos.write(buffer, 0, bytesRead);
        }
        fis.close();
        zos.closeEntry();
    }
}