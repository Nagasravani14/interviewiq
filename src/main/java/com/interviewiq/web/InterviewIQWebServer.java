package com.interviewiq.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interviewiq.core.AssessmentEngine;
import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.CodingChallenge;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import com.interviewiq.model.QuizQuestion;
import com.interviewiq.model.UserPreferences;
import com.interviewiq.repository.AttemptRepository;
import com.interviewiq.repository.JsonAttemptRepository;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.repository.QuestionRepository;
import com.interviewiq.service.AnalyticsService;
import com.interviewiq.service.HistoryService;
import com.interviewiq.service.QuestionService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * Built-in embedded HTTP web server for InterviewIQ running on localhost.
 * Serves the full single-page application and REST API endpoints.
 */
public class InterviewIQWebServer {
    private final int port;
    private HttpServer server;
    private final PreferencesRepository preferencesRepo;
    private final AttemptRepository attemptRepo;
    private final QuestionRepository questionRepo;
    private final QuestionService questionService;
    private final HistoryService historyService;
    private final AnalyticsService analyticsService;
    private final AssessmentEngine assessmentEngine;
    private final ObjectMapper mapper;

    public InterviewIQWebServer(int port) {
        this.port = port;
        this.preferencesRepo = new PreferencesRepository();
        this.attemptRepo = new JsonAttemptRepository();
        this.questionRepo = new QuestionRepository();

        this.questionService = new QuestionService(questionRepo);
        this.historyService = new HistoryService(attemptRepo);
        this.analyticsService = new AnalyticsService(attemptRepo);
        this.assessmentEngine = new AssessmentEngine();

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public int start() throws IOException {
        int currentPort = port;
        int[] fallbackPorts = { port, 8085, 8088, 8090, 8888, 9000, 3000 };
        boolean bound = false;

        for (int p : fallbackPorts) {
            try {
                server = HttpServer.create(new InetSocketAddress(p), 0);
                currentPort = p;
                bound = true;
                break;
            } catch (java.net.BindException ex) {
                System.out.println("Port " + p + " is occupied, trying next available port...");
            }
        }

        if (!bound) {
            server = HttpServer.create(new InetSocketAddress(0), 0);
            currentPort = server.getAddress().getPort();
        }

        server.setExecutor(Executors.newFixedThreadPool(10));

        // HTML Frontend Handler
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) {
                byte[] response = WebPageGenerator.getHtml().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            } else if (!path.startsWith("/api/")) {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        // REST API: Stats
        server.createContext("/api/stats", exchange -> {
            UserPreferences p = preferencesRepo.load();
            Map<String, Object> data = new HashMap<>();
            data.put("totalAttempts", analyticsService.getTotalAttempts());
            data.put("averageAccuracy", analyticsService.getAverageAccuracy());
            data.put("codingSolved", analyticsService.getCodingProblemsSolved());
            data.put("mockInterviews", analyticsService.getMockInterviewsCompleted());
            data.put("displayName", p.getDisplayName());
            data.put("totalStudyTime", analyticsService.getTotalStudyTimeSeconds());
            data.put("streak", analyticsService.calculateStudyStreakDays());
            data.put("recent", analyticsService.getRecentAttempts(5));
            data.put("topicAccuracy", analyticsService.getCategoryAccuracyMap());
            sendJson(exchange, 200, data);
        });

        // REST API: Questions
        server.createContext("/api/questions", exchange -> {
            List<Question> questions = questionService.getAllQuestions();
            sendJson(exchange, 200, questions);
        });

        // REST API: Coding Challenges
        server.createContext("/api/coding", exchange -> {
            List<CodingChallenge> challenges = questionService.getAllCodingChallenges();
            sendJson(exchange, 200, challenges);
        });

        // REST API: Coding Runner
        server.createContext("/api/coding/run", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, Object> req = parseJsonRequest(exchange);
                String challengeId = (String) req.get("challengeId");
                String code = (String) req.get("code");
                Boolean submit = (Boolean) req.get("submit");

                CodingChallenge c = questionService.getCodingChallengeById(challengeId);
                if (c != null) {
                    AssessmentEngine.EvaluationSummary summary = assessmentEngine.evaluateChallenge(c, code);
                    if (Boolean.TRUE.equals(submit)) {
                        AttemptRecord record = assessmentEngine.createAttemptRecord(summary, 60);
                        historyService.recordAttempt(record);
                    }
                    Map<String, Object> res = new HashMap<>();
                    res.put("passedCount", summary.getPassedCount());
                    res.put("totalCount", summary.getTotalCount());
                    res.put("scorePercentage", summary.getScorePercentage());
                    res.put("feedback", summary.getDiagnosticFeedback());
                    sendJson(exchange, 200, res);
                    return;
                }
            }
            sendJson(exchange, 400, Map.of("error", "Invalid request"));
        });

        // REST API: Quiz Questions
        server.createContext("/api/quiz", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String cat = "ALL";
            int count = 5;
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    if (pair.length == 2) {
                        if ("cat".equals(pair[0])) cat = java.net.URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                        if ("count".equals(pair[0])) count = Integer.parseInt(pair[1]);
                    }
                }
            }
            List<QuizQuestion> pool = new ArrayList<>(questionService.getAllQuizQuestions());
            if (!"ALL".equalsIgnoreCase(cat)) {
                String finalCat = cat;
                pool.removeIf(q -> !q.getCategory().equalsIgnoreCase(finalCat));
            }
            Collections.shuffle(pool);
            List<QuizQuestion> result = pool.subList(0, Math.min(count, pool.size()));
            sendJson(exchange, 200, result);
        });

        // REST API: Quiz Submit
        server.createContext("/api/quiz/submit", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, Object> req = parseJsonRequest(exchange);
                int total = ((Number) req.get("total")).intValue();
                int correct = ((Number) req.get("correct")).intValue();
                double acc = ((Number) req.get("accuracy")).doubleValue();

                AttemptRecord r = new AttemptRecord(
                        UUID.randomUUID().toString(),
                        AssessmentType.QUIZ,
                        "Quiz Arena Assessment",
                        "Technical Quiz",
                        Difficulty.MEDIUM,
                        LocalDateTime.now(),
                        180,
                        total,
                        total,
                        correct,
                        total,
                        acc,
                        "COMPLETED",
                        acc >= 80 ? "A (Excellent)" : (acc >= 60 ? "B (Proficient)" : "C (Needs Practice)")
                );
                historyService.recordAttempt(r);
                sendJson(exchange, 200, Map.of("status", "saved"));
                return;
            }
            sendJson(exchange, 400, Map.of("error", "POST required"));
        });

        // REST API: History
        server.createContext("/api/history", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 200, historyService.getAllAttempts());
            } else {
                sendJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        server.createContext("/api/history/clear", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                historyService.clearHistory();
                sendJson(exchange, 200, Map.of("status", "cleared"));
            } else {
                sendJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        // REST API: Demo Data
        server.createContext("/api/demo-data", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                UserPreferences p = preferencesRepo.load();
                if (p.isDemoDataLoaded()) {
                    historyService.clearHistory();
                    p.setDemoDataLoaded(false);
                } else {
                    historyService.loadDemoData();
                    p.setDemoDataLoaded(true);
                }
                preferencesRepo.save(p);
                sendJson(exchange, 200, Map.of("demoLoaded", p.isDemoDataLoaded()));
            } else {
                sendJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        server.start();
        System.out.println("==================================================================");
        System.out.println("  InterviewIQ Localhost Server running at: http://localhost:" + currentPort);
        System.out.println("==================================================================");

        try {
            if (!java.awt.GraphicsEnvironment.isHeadless() && java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:" + currentPort));
            }
        } catch (Throwable ignored) {
        }

        return currentPort;
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void sendJson(HttpExchange exchange, int status, Object obj) throws IOException {
        byte[] bytes = mapper.writeValueAsBytes(obj);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonRequest(HttpExchange exchange) {
        try (InputStream is = exchange.getRequestBody()) {
            return mapper.readValue(is, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ignored) {}
        }
        try {
            InterviewIQWebServer server = new InterviewIQWebServer(port);
            server.start();
            System.out.println("Press Ctrl+C to terminate.");
        } catch (Exception e) {
            System.err.println("Error starting web server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
