package kids.med.web_application.model.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import kids.med.web_application.model.medico.Especialidade;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(

        Long id,
        Long idMedico,

        @NotNull
        String paciente,

        @NotNull
        @Future
        LocalDateTime data,

        Especialidade especialidade) {
}
