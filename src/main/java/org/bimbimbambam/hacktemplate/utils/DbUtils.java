package org.bimbimbambam.hacktemplate.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import liquibase.Beta;
import lombok.RequiredArgsConstructor;
import org.bimbimbambam.hacktemplate.controller.request.QuestionReq;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.exception.NotFoundException;
import org.bimbimbambam.hacktemplate.repository.CategoryRepository;
import org.bimbimbambam.hacktemplate.repository.QuestionRepository;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class DbUtils {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    public List<Map<String, String>> parseCsv(String filePath) throws IOException {
        List<Map<String, String>> rows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Read header row
            String[] line;
            while ((line = reader.readNext()) != null) {
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], line[i]);
                }
                rows.add(row);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    public void insertQuestions() {
        String[] questionLabels = {
                "EXT1", "EXT2", "EXT3", "AGR1", "AGR2", "AGR3", "CSN1", "EST1", "OPN1" // Add all labels
        };

        for (String label : questionLabels) {
            Question question = new Question();
            question.setContent(label); // Use column name as content
            question.setCategory(categoryRepository.findByName("Psychology").orElseThrow(() -> new NotFoundException("MDA"))); // Set a category
            questionRepository.save(question);
        }
    }
}
