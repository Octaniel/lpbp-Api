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

    @Scheduled(cron = "0 0 1 * * SUN")
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
//    @Scheduled(cron = "0 0 8-18 ? * MON-SAT")
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
            con.setRequestProperty("Authorization", "Basic ODFlODdhMTUtMDAwNy00MTIxLTkwZTgtYTVlNDM0OGM3MTA0");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"b856e4e5-e7c5-46cf-95e6-b67aea0fa4e7\","
                    +   "\"included_segments\": [\"Subscribed Users\"],"
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
        List<Presenca> allByValidado = presencaRepository.findAllByValidado(false);
        List<Pessoa> collect = allByValidado.stream().map(Presenca::getPessoa).
                collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() > 13 || (now.getHour() == 13 && now.getMinute() >= 30)) {
            usuarioRepository.findByPessoaTurnoAndTipo(Turno.TARDE, "Vendedor").stream().map(Usuario::getPessoa).
                    collect(Collectors.toList()).forEach(pessoa -> salvarPresencaFalta(collect, pessoa));
        } else {
            usuarioRepository.findByPessoaTurnoAndTipo(Turno.MANHA, "Vendedor").stream().map(Usuario::getPessoa).
            collect(Collectors.toList()).forEach(pessoa -> salvarPresencaFalta(collect, pessoa));
        }
        List<Presenca> collect1 = allByValidado.stream().peek(presenca -> presenca.setValidado(true)).
                collect(Collectors.toList());
        presencaRepository.saveAll(collect1);
    }

    private void salvarPresencaFalta(List<Pessoa> collect, Pessoa pessoa) {
        if (!containPessoa(collect,pessoa)) {
            Presenca presenca = new Presenca();
            presenca.setPresente(false);
            presenca.setValidado(true);
            presenca.setPessoa(pessoa);
            LocalDateTime now1 = LocalDateTime.now();
            presenca.setDataAlteracao(now1);
            presenca.setDataCriacao(now1);
            presenca.setJustificada(false);
            presencaRepository.save(presenca);
        }
    }

    private boolean containPessoa(List<Pessoa> pessoas, Pessoa pessoa) {
        for (Pessoa pessoa1 : pessoas){
            if(pessoa1.getId().equals(pessoa.getId())){
                return true;
            }
        }
        return false;
    }

    public void setarTodosAsPresencasParaPresenteEntre(LocalDateTime de, LocalDateTime ate){
        List<Presenca> presencas = presencaRepository.presencasEntre(de, ate);
        presencaRepository.deleteAll(presencas);
    }
}
