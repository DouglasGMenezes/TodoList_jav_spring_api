package br.com.douglasgm.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

//classe que cria as colunas da tabela de na parte de tarefas

@Data //anotation que cria os getters e setters para os atributos da classe
@Entity(name = "tb_task") // cria uma representação no banco identificando essa classe como uma tabela e suas colunas
public class TaskModel {

    @Id //Define o campo de identificação primária da entidade.
    @GeneratedValue(generator = "UUID") // Especifica como o valor da chave primária será gerado.
    private UUID id;

    private String description;

    @Column(length = 50) //Configura detalhes sobre a coluna que um campo da classe mapeia
    private String title;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;

    @CreationTimestamp //Ela registra o data e hora de quando a entidade foi criada pela primeira vez no banco de dados (é uma ferramenta do Hibernate)
    private LocalDateTime createAt;

    // ---------------------------- //

    public void setTitle(String title) throws Exception{
        if (title.length() > 50) {
            throw new Exception("O titulo deve ter no maxímo 50 caracteres.");
        }
        this.title = title;
    }

}
