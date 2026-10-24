package com.lostfound.MatchingService.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;

import java.util.List;
import java.util.Map;

@Service
public class ReportEmbeddingService {

    private final VectorStore vectorStore;

    private final EmbeddingModel embeddingModel;

    public ReportEmbeddingService(
            VectorStore vectorStore,
            EmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    public void saveReportEmbedding(
            Long reportId,
            Long userId,
            String type,
            String title,
            String description,
            String category,
            String locationText) {
        //coverting the report into readable text
        String content = """
                Report Type: %s
                Title: %s
                Description: %s
                Category: %s
                Location: %s
                """.formatted(
                valueOrDefault(type),
                valueOrDefault(title),
                valueOrDefault(description),
                valueOrDefault(category),
                valueOrDefault(locationText)
        );
        //text with metadata
        Document document = new Document(
                content,
                Map.of(
                        "reportId", reportId.toString(),
                        "userId", userId.toString(),
                        "type", valueOrDefault(type),
                        "category", valueOrDefault(category)
                )
        );
        //converts to embeddings and helps to store in vectore database
        vectorStore.add(List.of(document));
    }
    public void logEmbeddingModel() {

        if (embeddingModel instanceof GoogleGenAiTextEmbeddingModel googleModel) {

            System.out.println(
                    "Embedding Model Class: "
                            + googleModel.getClass().getSimpleName()
            );

            System.out.println(
                    "Embedding Model Configuration: "
                            + googleModel
            );

        } else {

            System.out.println(
                    "Embedding Model: "
                            + embeddingModel.getClass().getName()
            );
        }
    }

    private String valueOrDefault(String value) {
        return value == null ? "" : value;
    }
}


