package practica.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CredibilityReport implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String VERDICT_RELIABLE = "RELIABLE";
    public static final String VERDICT_SUSPICIOUS = "SUSPICIOUS";
    public static final String VERDICT_UNRELIABLE = "UNRELIABLE";

    private NewsItem newsItem;
    private double sentimentScore;
    private double biasScore;
    private double sourceReputationScore;
    private double finalCredibilityScore;
    private String verdict;
    private String explanation;
    private final Map<String, String> expertDetails;

    public CredibilityReport() {
        this.expertDetails = new HashMap<>();
        this.verdict = VERDICT_SUSPICIOUS;
    }

    public CredibilityReport(NewsItem newsItem) {
        this();
        this.newsItem = newsItem;
    }

    public NewsItem getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(NewsItem newsItem) {
        this.newsItem = newsItem;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = clamp(sentimentScore);
    }

    public double getBiasScore() {
        return biasScore;
    }

    public void setBiasScore(double biasScore) {
        this.biasScore = clamp(biasScore);
    }

    public double getSourceReputationScore() {
        return sourceReputationScore;
    }

    public void setSourceReputationScore(double sourceReputationScore) {
        this.sourceReputationScore = clamp(sourceReputationScore);
    }

    public double getFinalCredibilityScore() {
        return finalCredibilityScore;
    }

    public void setFinalCredibilityScore(double finalCredibilityScore) {
        this.finalCredibilityScore = clamp(finalCredibilityScore);
        this.verdict = calculateVerdict(this.finalCredibilityScore);
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void addExpertDetail(String expertName, String detail) {
        expertDetails.put(expertName, detail);
    }

    public Map<String, String> getExpertDetails() {
        return Collections.unmodifiableMap(expertDetails);
    }

    public void calculateFinalScore() {
        double normalizedSentiment = 1.0 - Math.abs(clamp(sentimentScore));
        double normalizedBias = 1.0 - clamp(biasScore);
        double score = (sourceReputationScore * 0.40)
                + (normalizedBias * 0.35)
                + (normalizedSentiment * 0.25);
        setFinalCredibilityScore(score);
    }

    private static String calculateVerdict(double score) {
        if (score >= 0.70) {
            return VERDICT_RELIABLE;
        }
        if (score >= 0.40) {
            return VERDICT_SUSPICIOUS;
        }
        return VERDICT_UNRELIABLE;
    }

    private static double clamp(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return 0.0;
        }
        if (value < 0.0) {
            return 0.0;
        }
        if (value > 1.0) {
            return 1.0;
        }
        return value;
    }

    @Override
    public String toString() {
        String title = newsItem == null ? "unknown" : newsItem.getTitle();
        return "CredibilityReport{" +
                "title='" + title + '\'' +
                ", sentimentScore=" + sentimentScore +
                ", biasScore=" + biasScore +
                ", sourceReputationScore=" + sourceReputationScore +
                ", finalCredibilityScore=" + finalCredibilityScore +
                ", verdict='" + verdict + '\'' +
                '}';
    }
}
