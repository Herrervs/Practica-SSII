package practica.modelo;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReporteCredibilidad implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String VEREDICTO_RELIABLE = "RELIABLE";
    public static final String VEREDICTO_SUSPICIOUS = "SUSPICIOUS";
    public static final String VEREDICTO_UNRELIABLE = "UNRELIABLE";

    private Noticia noticia;
    private double scoreSentimiento;
    private double scoreSesgo;
    private double scoreReputacion;
    private double scoreFinalCredibilidad;
    private String veredicto;
    private String explicacion;
    private final Map<String, String> detallesExpertos;

    public ReporteCredibilidad() {
        this.detallesExpertos = new HashMap<>();
        this.veredicto = VEREDICTO_SUSPICIOUS;
    }

    public ReporteCredibilidad(Noticia noticia) {
        this();
        this.noticia = noticia;
    }

    public Noticia getNoticia() {
        return noticia;
    }

    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
    }

    public double getScoreSentimiento() {
        return scoreSentimiento;
    }

    public void setScoreSentimiento(double scoreSentimiento) {
        this.scoreSentimiento = clamp(scoreSentimiento);
    }

    public double getScoreSesgo() {
        return scoreSesgo;
    }

    public void setScoreSesgo(double scoreSesgo) {
        this.scoreSesgo = clamp(scoreSesgo);
    }

    public double getScoreReputacion() {
        return scoreReputacion;
    }

    public void setScoreReputacion(double scoreReputacion) {
        this.scoreReputacion = clamp(scoreReputacion);
    }

    public double getScoreFinalCredibilidad() {
        return scoreFinalCredibilidad;
    }

    public void setScoreFinalCredibilidad(double scoreFinalCredibilidad) {
        this.scoreFinalCredibilidad = clamp(scoreFinalCredibilidad);
        this.veredicto = calcularVeredicto(this.scoreFinalCredibilidad);
    }

    public String getVeredicto() {
        return veredicto;
    }

    public void setVeredicto(String veredicto) {
        this.veredicto = veredicto;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public void addDetalleExperto(String nombreExperto, String detalle) {
        detallesExpertos.put(nombreExperto, detalle);
    }

    public Map<String, String> getDetallesExpertos() {
        return Collections.unmodifiableMap(detallesExpertos);
    }

    public void calculateFinalScore() {
        double normalizedSentiment = 1.0 - Math.abs(clamp(scoreSentimiento));
        double normalizedBias = 1.0 - clamp(scoreSesgo);
        double score = (scoreReputacion * 0.40)
                + (normalizedBias * 0.35)
                + (normalizedSentiment * 0.25);
        setScoreFinalCredibilidad(score);
    }

    private static String calcularVeredicto(double score) {
        if (score >= 0.70) {
            return VEREDICTO_RELIABLE;
        }
        if (score >= 0.40) {
            return VEREDICTO_SUSPICIOUS;
        }
        return VEREDICTO_UNRELIABLE;
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
        String titulo = noticia == null ? "desconocido" : noticia.getTitulo();
        return "CredibilityReport{" +
                "titulo='" + titulo + '\'' +
                ", scoreSentimiento=" + scoreSentimiento +
                ", scoreSesgo=" + scoreSesgo +
                ", scoreReputacion=" + scoreReputacion +
                ", scoreFinalCredibilidad=" + scoreFinalCredibilidad +
                ", veredicto='" + veredicto + '\'' +
                '}';
    }
}
