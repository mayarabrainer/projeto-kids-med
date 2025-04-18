package kids.med.web_application.service;

import jakarta.transaction.Transactional;
import kids.med.web_application.model.consulta.Consulta;
import kids.med.web_application.model.consulta.DadosAgendamentoConsulta;
import kids.med.web_application.model.consulta.DadosListagemConsulta;
import kids.med.web_application.repositories.MedicoRepository;
import kids.med.web_application.repositories.ConsultaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository repository, MedicoRepository medicoRepository) {
        this.repository = repository;
        this.medicoRepository = medicoRepository;
    }

    public Page<DadosListagemConsulta> listar(Pageable paginacao) {
        return repository.findAllByOrderByData(paginacao).map(DadosListagemConsulta::new);
    }

    @Transactional
    public void cadastrar(DadosAgendamentoConsulta dados) {
        var medicoConsulta = medicoRepository.findById(dados.idMedico()).orElseThrow();
        if (dados.id() == null) {
            repository.save(new Consulta(medicoConsulta, dados));
        } else {
            var consulta = repository.findById(dados.id()).orElseThrow();
            consulta.modificarDados(medicoConsulta, dados);
        }
    }

    public DadosAgendamentoConsulta carregarPorId(Long id) {
        var consulta = repository.findById(id).orElseThrow();
        var medicoConsulta = medicoRepository.getReferenceById(consulta.getMedico().getId());
        return new DadosAgendamentoConsulta(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente(), consulta.getData(), medicoConsulta.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

}
