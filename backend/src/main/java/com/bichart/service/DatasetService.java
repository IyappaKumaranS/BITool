package com.bichart.service;

import com.bichart.model.ColumnSchema;
import com.bichart.model.Dataset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatasetService {

    private static final String DUCKDB_URL = "jdbc:duckdb:";

    public Dataset processDataset(String datasetId, String filePath) throws Exception {

        List<ColumnSchema> columns = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DUCKDB_URL);
             Statement stmt = con.createStatement()) {

            
            stmt.executeQuery(
                "SELECT COUNT(*) FROM read_csv_auto('" + filePath + "')"
            );

            
            ResultSet rs = stmt.executeQuery(
                "DESCRIBE SELECT * FROM read_csv_auto('" + filePath + "')"
            );

            while (rs.next()) {
                columns.add(new ColumnSchema(
                        rs.getString("column_name"),
                        rs.getString("column_type")
                ));
            }
        }

        
        Dataset dataset = new Dataset();
        dataset.setDatasetId(datasetId);
        dataset.setFilePath(filePath);
        dataset.setColumns(columns);

        return dataset;
    }
}
