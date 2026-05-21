package practica.nlp;

import java.util.Map;

public class SourceReputationDB {
    private static final Map<String, Double> SCORES = Map.of(
        "bbc.com", 0.95,
        "reuters.com", 0.95,
        "elpais.com", 0.88,
        "elmundo.es", 0.85,
        "rtve.es", 0.90,
        "elconfidencial.com", 0.72,
        "okdiario.com", 0.35,
        "elplural.com", 0.40);

    public double getScore(String sourceUrl) {
        return SCORES.entrySet().stream()
            .filter(e -> sourceUrl.contains(e.getKey()))
            .mapToDouble(Map.Entry::getValue)
            .findFirst()
            .orElse(0.5); // score neutro si no se conoce la fuente
    }
}