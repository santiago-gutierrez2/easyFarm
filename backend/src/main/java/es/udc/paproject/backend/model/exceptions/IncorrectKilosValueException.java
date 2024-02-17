package es.udc.paproject.backend.model.exceptions;

@SuppressWarnings("serial")
public class IncorrectKilosValueException extends Exception{

    private Integer kilos;

    public IncorrectKilosValueException(Integer kilos) {
        this.kilos = kilos;
    }

    public Integer getKilos() {return kilos;}

}
