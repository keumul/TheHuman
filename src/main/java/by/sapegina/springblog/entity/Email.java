package by.sapegina.springblog.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String subject;
    private String recipient;
    private String body;
}
