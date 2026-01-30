package com.bichart.servlet;

import com.bichart.service.DatasetService;
import com.bichart.model.Dataset;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.SQLException;
import java.sql.DriverManager;

@MultipartConfig
public class UploadDatasetServlet extends HttpServlet {

    private final DatasetService datasetService = new DatasetService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        String UPLOAD_DIR = getServletContext().getRealPath("/uploads");


        try {
         
            Part filePart = request.getPart("file");

            if (filePart == null || filePart.getSize() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"CSV file is required\"}");
                return;
            }

            String fileName = Paths.get(filePart.getSubmittedFileName())
                                   .getFileName()
                                   .toString();

            if (!fileName.endsWith(".csv")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Only CSV files are allowed\"}");
                return;
            }

            
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

      
            String datasetId = "dataset_" + System.currentTimeMillis();
            Path filePath = uploadPath.resolve(datasetId + ".csv");

        
            try (InputStream inputStream = filePart.getInputStream()) {
                Files.copy(inputStream, filePath);
            }

          
            Dataset dataset = datasetService.processDataset(datasetId, filePath.toString());

 
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(dataset));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write(
                "{\"error\":\"" + e.getMessage() + "\"}"
            );
        }
    }
}
