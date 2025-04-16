package kids.med.web_application.service;

import jakarta.transaction.Transactional;
import kids.med.web_application.model.RegraDeNegocioException;
import kids.med.web_application.model.medico.DadosCadastroMedico;
import kids.med.web_application.model.medico.DadosListagemMedico;
import kids.med.web_application.model.medico.Especialidade;
import kids.med.web_application.model.medico.Medico;
import kids.med.web_application.repositories.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public Page<DadosListagemMedico> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemMedico::new);
    }

    @Transactional
    public void cadastrar(DadosCadastroMedico dados) {
        if (repository.isJaCadastrado(dados.email(), dados.crm(), dados.id())) {
            throw new RegraDeNegocioException("E-mail ou CRM já cadastrado para outro médico!");
        }

        if (dados.id() == null) {
            repository.save(new Medico(dados));
        } else {
            var medico = repository.findById(dados.id()).orElseThrow();
            medico.atualizarDados(dados);
        }
    }

    public DadosCadastroMedico carregarPorId(Long id) {
        var medico = repository.findById(id).orElseThrow();
        return new DadosCadastroMedico(medico.getId(), medico.getNome(), medico.getEmail(), medico.getTelefone(), medico.getCrm(), medico.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public List<DadosListagemMedico> listarPorEspecialidade(Especialidade especialidade) {
        return repository.findByEspecialidade(especialidade).stream().map(DadosListagemMedico::new).toList();
    }

}
