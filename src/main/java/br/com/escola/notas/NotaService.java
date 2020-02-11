package br.com.escola.notas;

import br.com.escola.aluno.AlunoService;
import br.com.escola.aluno.Alunos;
import br.com.escola.materia.Materia;
import br.com.escola.materia.MateriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotaService.class);

    private final INotasRepository iNotasRepository;
    private final AlunoService alunoService;
    private final MateriaService materiaService;


    public NotaService(INotasRepository iNotasRepository, AlunoService alunoService, MateriaService materiaService) {
        this.iNotasRepository = iNotasRepository;
        this.alunoService = alunoService;
        this.materiaService = materiaService;
    }

    public NotasDTO save(NotasDTO notasDTO) {
        LOGGER.info("Salvando nota");
        LOGGER.debug("Nota: {}", notasDTO);

        Notas notas = new Notas();

        Alunos alunosExistentes = alunoService.findById(notasDTO.getIdAluno());
        Materia materiaExistente = materiaService.findById(notasDTO.getIdMateria());

        notas.setAluno(alunosExistentes);
        notas.setMateria(materiaExistente);
        notas.setNota1(notasDTO.getNota1());
        notas.setNota2(notasDTO.getNota2());
        notas.setNota3(notasDTO.getNota3());

        notas = this.iNotasRepository.save(notas);

        return NotasDTO.of(notas);

    }

    public NotasDTO update(NotasDTO notasDTO, Long id) {
        Optional<Notas> notasOptional = this.iNotasRepository.findById(id);

        if (notasOptional.isPresent()) {
            Notas notasExistentes = notasOptional.get();

            LOGGER.info("Atualizando notas.... id:[{}]", notasExistentes.getId());
            LOGGER.debug("Payload: {}", notasDTO);
            LOGGER.debug("Notas existente: {}", notasExistentes);

            Alunos alunosExistentes = alunoService.findById(notasDTO.getIdAluno());
            Materia materiaExistente = materiaService.findById(notasDTO.getIdMateria());

            notasExistentes.setAluno(alunosExistentes);
            notasExistentes.setMateria(materiaExistente);
            notasExistentes.setNota1(notasDTO.getNota1());
            notasExistentes.setNota2(notasDTO.getNota2());
            notasExistentes.setNota3(notasDTO.getNota3());

            notasExistentes = this.iNotasRepository.save(notasExistentes);
            return NotasDTO.of(notasExistentes);

        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para produto de ID: [{}]", id);

        this.iNotasRepository.deleteById(id);
    }

    public Notas findById(Long id) {
        Optional<Notas> notasOptional = this.iNotasRepository.findById(id);

        if (notasOptional.isPresent()) {

            return notasOptional.get();
        }
        throw new IllegalArgumentException("ID %s não existe");

    }
}

