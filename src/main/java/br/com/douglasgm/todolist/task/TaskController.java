package br.com.douglasgm.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglasgm.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;


@RestController // Define uma classe como um controlador REST. Combina as funcionalidades de @Controller e @ResponseBody, retornando dados diretamente no corpo da resposta HTTP, geralmente em formato JSON.
@RequestMapping("/tasks") // Mapeia URLs específicas para métodos de um controlador. Pode definir padrões de URL e métodos HTTP (GET, POST, etc.) que o método deve atender.
public class TaskController {

    @Autowired // O Spring gerencia automaticamente a criação e injeção das dependências, simplificando o código e promovendo a inversão de controle 
    private ITaskRepository taskRepository;



    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();


        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de deve inicio / data de termino deve ser maior que a data atual.");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de deve inicio deve ser menor que a data de termino.");
        }
            
        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);   
        
    }


    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }
    

    @PutMapping("/{id}")
    public ResponseEntity updade( @RequestBody TaskModel taskModel , @PathVariable UUID id , HttpServletRequest request ) {

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Tarefa não encontrada.");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("USUÁRIO NÃO TEM PERMISSÃO PARA ALTERAR ESSA TAREFA.");
        }

        Utils.copyNonNullProperties( taskModel , task );

        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);
    }
    
}
