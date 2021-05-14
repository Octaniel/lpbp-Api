package com.bsoftware.lpbp.repository.presenca;

import com.bsoftware.lpbp.model.Pessoa;
import com.bsoftware.lpbp.model.Pessoa_;
import com.bsoftware.lpbp.model.Presenca;
import com.bsoftware.lpbp.model.Presenca_;
import com.bsoftware.lpbp.repository.filter.PresencaFilter;
import com.bsoftware.lpbp.repository.projection.PresencaResumo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PresencaRepositoryImpl implements PresencaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<PresencaResumo> geralList(PresencaFilter presencaFilter) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PresencaResumo> criteria = builder.createQuery(PresencaResumo.class);
        Root<Pessoa> rootp = criteria.from(Pessoa.class);

        //Criar as Restrições
        Subquery<Long> sub = criteria.subquery(Long.class);
        Root<Presenca> root = sub.from(Presenca.class);
        sub.select(builder.count(root.get(Presenca_.ID)));
        sub.where(builder.equal(root.get("presente"), false),
                builder.equal(root.get("pessoa").get("id"), rootp.get("id")),
                criarRestricoesDe(presencaFilter, builder,root), criarRestricoesAte(presencaFilter, builder,root));

        Subquery<Long> sub1 = criteria.subquery(Long.class);
        Root<Presenca> root1 = sub1.from(Presenca.class);
        sub1.select(builder.count(root1.get(Presenca_.ID)));
        sub1.where(builder.equal(root1.get("justificada"), true),
                builder.equal(root1.get("pessoa").get("id"), rootp.get("id")),
                criarRestricoesDe(presencaFilter, builder,root1), criarRestricoesAte(presencaFilter, builder,root1));

        Subquery<Long> sub2 = criteria.subquery(Long.class);
        Root<Presenca> root2 = sub2.from(Presenca.class);
        sub2.select(builder.count(root2.get(Presenca_.ID)));
        sub2.where(builder.equal(root2.get("justificada"), true),
                builder.equal(root2.get("justificacaoAceitoPorAdministrador"), true),
                builder.equal(root2.get("pessoa").get("id"), rootp.get("id")),
                criarRestricoesDe(presencaFilter, builder,root2), criarRestricoesAte(presencaFilter, builder,root2));

        Subquery<Long> sub3 = criteria.subquery(Long.class);
        Root<Presenca> root3 = sub3.from(Presenca.class);
        sub3.select(builder.count(root3.get(Presenca_.ID)));
        sub3.where(builder.equal(root3.get("presente"), true),
                builder.equal(root3.get("pessoa").get("id"), rootp.get("id")),
                criarRestricoesDe(presencaFilter, builder,root3), criarRestricoesAte(presencaFilter, builder,root3));

        criteria.select(builder.construct(PresencaResumo.class,
                rootp.get(Pessoa_.NOME),
                sub,
                sub1,
                sub2,
                sub3
        ));

        TypedQuery<PresencaResumo> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate criarRestricoesDe(PresencaFilter presencaFilter, CriteriaBuilder builder,
                                        Root<Presenca> root) {
        if (presencaFilter.getLocalDateTime() != null) {
           return builder.greaterThanOrEqualTo(root.get("dataCriacao"), presencaFilter.getLocalDateTime());
        }
        return builder.greaterThanOrEqualTo(root.get("dataCriacao"), LocalDateTime.of(2000,1,1,0,0));
    }

    private Predicate criarRestricoesAte(PresencaFilter presencaFilter, CriteriaBuilder builder,
                                            Root<Presenca> root) {
        if (presencaFilter.getLocalDateTime2() != null) {
           return builder.lessThanOrEqualTo(root.get("dataCriacao"), presencaFilter.getLocalDateTime2());
        }
        return builder.lessThanOrEqualTo(root.get("dataCriacao"), LocalDateTime.of(2200,1,1,0,0));

    }
}
