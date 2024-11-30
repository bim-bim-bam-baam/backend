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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class DataImportService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public void importData() throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader("output.csv"))) {
            String[] headers = reader.readNext(); // Skip headers
            List<String[]> rows = reader.readAll();

            for (String[] row : rows) {
                User user = new User();
                user.setUsername("user" + row[0]);
                user.setPassword("password");
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
