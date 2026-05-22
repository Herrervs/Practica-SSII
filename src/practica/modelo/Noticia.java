package practica.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Noticia implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String titulo;
    private String contenido;
    private String fuente;
    private String autor;
    private String url;
    private String fechaPublicacion;

    public Noticia() {
    }

    public Noticia(String id, String titulo, String contenido, String fuente, String autor, String url,
            String fechaPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fuente = fuente;
        this.autor = autor;
        this.url = url;
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getTextoParaAnalisis() {
        String tituloSeguro = titulo == null ? "" : titulo;
        String contenidoSeguro = contenido == null ? "" : contenido;
        return (tituloSeguro + "\n" + contenidoSeguro).trim();
    }

    public boolean tieneContenido() {
        return getTextoParaAnalisis().length() > 0;
    }

    @Override
    public String toString() {
        return "Noticia{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", fuente='" + fuente + '\'' +
                ", url='" + url + '\'' +
                ", fechaPublicacion='" + fechaPublicacion + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, fuente, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Noticia)) {
            return false;
        }
        Noticia otro = (Noticia) obj;
        return Objects.equals(id, otro.id)
                && Objects.equals(titulo, otro.titulo)
                && Objects.equals(fuente, otro.fuente)
                && Objects.equals(url, otro.url);
    }
}
