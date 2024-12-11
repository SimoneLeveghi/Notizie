public class Notizia {
    private String argomento;
    private String paese;
    private String notizia;

    public Notizia(String argomento, String paese, String notizia) {
        this.argomento = argomento;
        this.paese = paese;
        this.notizia = notizia;
    }

    public String toString() {
        return "[" + this.argomento + ", " + this.paese + ", " + this.notizia + "]";
    }
}
