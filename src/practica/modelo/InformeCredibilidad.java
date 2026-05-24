package practica.modelo;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InformeCredibilidad implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String VEREDICTO_CONFIABLE = "CONFIABLE";
    public static final String VEREDICTO_SOSPECHOSO = "SOSPECHOSO";
    public static final String VEREDICTO_NO_CONFIABLE = "NO_CONFIABLE";

    private Noticia noticia;
    private double puntuacionSentimiento;
    private double puntuacionSesgo;
    private double puntuacionReputacionFuente;
    private double puntuacionCredibilidadFinal;
    private String veredicto;
    private String explicacion;
    private final Map<String, String> detallesExpertos;

    public InformeCredibilidad() {
        this.detallesExpertos = new HashMap<>();
        this.veredicto = VEREDICTO_SOSPECHOSO;
    }

    public InformeCredibilidad(Noticia noticia) {
        this();
        this.noticia = noticia;
    }

    public Noticia getNoticia() {
        return noticia;
    }

    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
    }

    public double getPuntuacionSentimiento() {
        return puntuacionSentimiento;
    }

    public void setPuntuacionSentimiento(double puntuacionSentimiento) {
        this.puntuacionSentimiento = limitar(puntuacionSentimiento);
    }

    public double getPuntuacionSesgo() {
        return puntuacionSesgo;
    }

    public void setPuntuacionSesgo(double puntuacionSesgo) {
        this.puntuacionSesgo = limitar(puntuacionSesgo);
    }

    public double getPuntuacionReputacionFuente() {
        return puntuacionReputacionFuente;
    }

    public void setPuntuacionReputacionFuente(double puntuacionReputacionFuente) {
        this.puntuacionReputacionFuente = limitar(puntuacionReputacionFuente);
    }

    public double getPuntuacionCredibilidadFinal() {
        return puntuacionCredibilidadFinal;
    }

    public void setPuntuacionCredibilidadFinal(double puntuacionCredibilidadFinal) {
        this.puntuacionCredibilidadFinal = limitar(puntuacionCredibilidadFinal);
        this.veredicto = calcularVeredicto(this.puntuacionCredibilidadFinal);
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

    public void calcularPuntuacionFinal() {
        double sentimientoNormalizado = limitar(puntuacionSentimiento);
        double sesgoNormalizado = limitar(puntuacionSesgo);
        double puntuacion = (puntuacionReputacionFuente * 0.40)
                + (sesgoNormalizado * 0.35)
                + (sentimientoNormalizado * 0.25);
        setPuntuacionCredibilidadFinal(puntuacion);
    }

    private static String calcularVeredicto(double puntuacion) {
        if (puntuacion >= 0.70) {
            return VEREDICTO_CONFIABLE;
        }
        if (puntuacion >= 0.40) {
            return VEREDICTO_SOSPECHOSO;
        }
        return VEREDICTO_NO_CONFIABLE;
    }

    private static double limitar(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            return 0.0;
        }
        if (valor < 0.0) {
            return 0.0;
        }
        if (valor > 1.0) {
            return 1.0;
        }
        return valor;
    }

    @Override
    public String toString() {
        String titulo = noticia == null ? "desconocido" : noticia.getTitulo();
        return "InformeCredibilidad{" +
                "titulo='" + titulo + '\'' +
                ", puntuacionSentimiento=" + puntuacionSentimiento +
                ", puntuacionSesgo=" + puntuacionSesgo +
                ", puntuacionReputacionFuente=" + puntuacionReputacionFuente +
                ", puntuacionCredibilidadFinal=" + puntuacionCredibilidadFinal +
                ", veredicto='" + veredicto + '\'' +
                '}';
    }
}
