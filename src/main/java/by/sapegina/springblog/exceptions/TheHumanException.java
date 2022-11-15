package by.sapegina.springblog.exceptions;

public class TheHumanException extends RuntimeException{
    public TheHumanException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }
    public TheHumanException(String exMessage){
        super(exMessage);
    }
}
