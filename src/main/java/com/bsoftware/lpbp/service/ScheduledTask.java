package com.bsoftware.lpbp.service;

import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.model.Turno;
import com.bsoftware.lpbp.model.Usuario;
import com.bsoftware.lpbp.repository.PessoaRepository;
import com.bsoftware.lpbp.repository.PresencaRepository;
import com.bsoftware.lpbp.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class ScheduledTask {

    private final PessoaRepository pessoaRepository;
    private final PresencaRepository presencaRepository;
    private final UsuarioRepository usuarioRepository;

    public ScheduledTask(PessoaRepository pessoaRepository, PresencaRepository presencaRepository,
                         UsuarioRepository usuarioRepository) {
        this.pessoaRepository = pessoaRepository;
        this.presencaRepository = presencaRepository;
        this.usuarioRepository = usuarioRepository;
    }

//    @Scheduled(cron = "0 0 1 * * SUN")
    public void trocarTurno(){
        List<Pessoa> allByTurno = pessoaRepository.findAllByTurno(Turno.MANHA);
        List<Pessoa> allByTurno1 = pessoaRepository.findAllByTurno(Turno.TARDE);
        List<Pessoa> collect = allByTurno.stream().peek(pessoa -> pessoa.setTurno(Turno.TARDE)).
                collect(Collectors.toList());
        pessoaRepository.saveAll(collect);
        List<Pessoa> collect1 = allByTurno1.stream().peek(pessoa -> pessoa.setTurno(Turno.MANHA)).
                collect(Collectors.toList());
        pessoaRepository.saveAll(collect1);
    }

    //@Scheduled(cron = "0 0 8-18 ? * MON-SAT")
    public void registarScheduled(){
        Random random = new Random();
        int i = random.nextInt(59) + 1;
        long i1 = 1000 * 60 * i;
        try {
            Thread.sleep(i1);
            registar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void registar(){
        try {
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic ODZlNzYzZjQtNmVkYy00MjkwLWJkZTEtY2FiYWIyMTJjM2M4");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"5d40694c-ae64-4a45-8e40-49e2d3820f42\","
                    +   "\"included_segments\": [\"Total Subscriptions\"],"
                    +   "\"contents\": {\"en\": \"Os funcionarios devem marcar ponto agora\"},"
                    +   "\"headings\": {\"en\": \"Marcar ponto\"}"
                    + "}";

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            con.getResponseCode();

            Thread.sleep(600000);

            marcarFalta();

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public void marcarFalta() {
        List<Presenca> allByValidado = presencaRepository.presencasDeParaca(LocalDateTime.now().minusMinutes(10));
        List<Pessoa> collect = allByValidado.stream().map(Presenca::getPessoa).
                collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        Turno turno;
        if (now.getHour() > 13 || (now.getHour() == 13 && now.getMinute()-10 >= 30)) {
            turno = Turno.TARDE;
        } else {
            turno = Turno.MANHA;
        }
        List<Pessoa> collect1 = pessoaRepository.findAllByTurno(turno).stream().filter(collect::contains).collect(Collectors.toList());
        salvarPresencaFalta(collect1);
    }

    private void salvarPresencaFalta(List<Pessoa> collect) {
        collect.forEach(pessoa -> {
            Presenca presenca = new Presenca();
            presenca.setPresente(false);
            presenca.setValidado(true);
            presenca.setPessoa(pessoa);
            LocalDateTime now1 = LocalDateTime.now();
            presenca.setDataAlteracao(now1);
            presenca.setDataCriacao(now1);
            presenca.setJustificada(false);
            presencaRepository.save(presenca);
        });

    }

    public void setarTodosAsPresencasParaPresenteEntre(LocalDateTime de, LocalDateTime ate){
        List<Presenca> presencas = presencaRepository.presencasEntre(de, ate);
        presencaRepository.deleteAll(presencas);
    }
}
