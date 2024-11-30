package org.bimbimbambam.hacktemplate.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.bimbimbambam.hacktemplate.entity.Answer;
import org.bimbimbambam.hacktemplate.entity.Question;
import org.bimbimbambam.hacktemplate.entity.User;
import org.bimbimbambam.hacktemplate.repository.AnswerRepository;
import org.bimbimbambam.hacktemplate.repository.QuestionRepository;
import org.bimbimbambam.hacktemplate.repository.UserRepository;
import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class DataImportService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private String[] resizeArray(String[] original, int newSize) {
        String[] resizedArray = new String[newSize];
        System.arraycopy(original, 0, resizedArray, 0, Math.min(original.length, newSize));
        return resizedArray;
    }


    public void importData() throws IOException {
        try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("output.csv");
             InputStreamReader inputStreamReader = new InputStreamReader(resourceStream);
             CSVReader reader = new CSVReader(inputStreamReader)) {

            String[] headers = reader.readNext()[0].split("\t"); // Skip headers
            List<String[]> rows = reader.readAll();
            for (int i = 0; i < rows.size(); ++i) {
                rows.set(i, resizeArray(rows.get(i)[0].split("\t"), 100)); // Resize each row to 100 elements
            }

            headers = resizeArray(headers, 100);


            for (int i = 0; i < headers.length; ++i) {
                questionService.addQuestion(headers[i], "No", "Yes", 1L);
            }

            int cnt = 0;
            for (String[] row : rows) {
                User user = new User();
                user.setUsername("user228" + cnt++);
                user.setPassword("password");
                System.out.println(user.getUsername());
                System.out.println(user.getPassword());
                userRepository.save(user);

                // Process answers
                for (int i = 1; i < row.length; i++) {
                    Question question = questionRepository.findById((long) i).orElseThrow();
                    Long answerValue = parseAnswer(Long.parseLong(row[i]));

                    Answer answer = new Answer();
                    answer.setQuestion(question);
                    answer.setUser(user);
                    answer.setAnswer(answerValue);
                    answerRepository.save(answer);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }


    private Long parseAnswer(Long originalValue) {
        if (originalValue <= 2) return -1L;
        if (originalValue == 3) return 0L;
        return 1L;
    }
}
