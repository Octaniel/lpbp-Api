package com.bsoftware.lpbp.repository.presenca;

import com.bsoftware.lpbp.repository.filter.PresencaFilter;
import com.bsoftware.lpbp.repository.projection.PresencaResumo;

import java.time.LocalDateTime;
import java.util.List;

public interface PresencaRepositoryQuery {
    List<PresencaResumo> geralList(PresencaFilter presencaFilter);
}
