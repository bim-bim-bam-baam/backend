package org.bimbimbambam.hacktemplate.controller;

import org.bimbimbambam.hacktemplate.service.QuestionService;
import org.bimbimbambam.hacktemplate.service.impl.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private DataImportService dataImportService;

    @PostMapping("/insert-questions")
    public ResponseEntity<String> insertQuestions() {
        Map<String, String> questionDescriptions = QUESTION_DESCRIPTIONS;
        questionService.insertQuestions(questionDescriptions, "Psychology");
        return ResponseEntity.ok("Questions inserted successfully");
    }

    @PostMapping("/import-data")
    public ResponseEntity<String> importData() throws IOException, IOException {
        dataImportService.importData();
        return ResponseEntity.ok("Data imported successfully");
    }

    private static final Map<String, String> QUESTION_DESCRIPTIONS = Map.ofEntries(
            Map.entry("EXT1", "I am the life of the party."),
            Map.entry("EXT2", "I don't talk a lot."),
            Map.entry("EXT3", "I feel comfortable around people."),
            Map.entry("EXT4", "I keep in the background."),
            Map.entry("EXT5", "I start conversations."),
            Map.entry("EXT6", "I have little to say."),
            Map.entry("EXT7", "I talk to a lot of different people at parties."),
            Map.entry("EXT8", "I don't like to draw attention to myself."),
            Map.entry("EXT9", "I don't mind being the center of attention."),
            Map.entry("EXT10", "I am quiet around strangers."),
            Map.entry("EST1", "I get stressed out easily."),
            Map.entry("EST2", "I am relaxed most of the time."),
            Map.entry("EST3", "I worry about things."),
            Map.entry("EST4", "I seldom feel blue."),
            Map.entry("EST5", "I am easily disturbed."),
            Map.entry("EST6", "I get upset easily."),
            Map.entry("EST7", "I change my mood a lot."),
            Map.entry("EST8", "I have frequent mood swings."),
            Map.entry("EST9", "I get irritated easily."),
            Map.entry("EST10", "I often feel blue."),
            Map.entry("AGR1", "I feel little concern for others."),
            Map.entry("AGR2", "I am interested in people."),
            Map.entry("AGR3", "I insult people."),
            Map.entry("AGR4", "I sympathize with others' feelings."),
            Map.entry("AGR5", "I am not interested in other people's problems."),
            Map.entry("AGR6", "I have a soft heart."),
            Map.entry("AGR7", "I am not really interested in others."),
            Map.entry("AGR8", "I take time out for others."),
            Map.entry("AGR9", "I feel others' emotions."),
            Map.entry("AGR10", "I make people feel at ease."),
            Map.entry("CSN1", "I am always prepared."),
            Map.entry("CSN2", "I leave my belongings around."),
            Map.entry("CSN3", "I pay attention to details."),
            Map.entry("CSN4", "I make a mess of things."),
            Map.entry("CSN5", "I get chores done right away."),
            Map.entry("CSN6", "I often forget to put things back in their proper place."),
            Map.entry("CSN7", "I like order."),
            Map.entry("CSN8", "I shirk my duties."),
            Map.entry("CSN9", "I follow a schedule."),
            Map.entry("CSN10", "I am exacting in my work."),
            Map.entry("OPN1", "I have a rich vocabulary."),
            Map.entry("OPN2", "I have difficulty understanding abstract ideas."),
            Map.entry("OPN3", "I have a vivid imagination."),
            Map.entry("OPN4", "I am not interested in abstract ideas."),
            Map.entry("OPN5", "I have excellent ideas."),
            Map.entry("OPN6", "I do not have a good imagination."),
            Map.entry("OPN7", "I am quick to understand things."),
            Map.entry("OPN8", "I use difficult words."),
            Map.entry("OPN9", "I spend time reflecting on things."),
            Map.entry("OPN10", "I am full of ideas.")
    );

}
