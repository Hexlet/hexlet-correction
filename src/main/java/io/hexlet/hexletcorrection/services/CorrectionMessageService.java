package io.hexlet.hexletcorrection.services;

import io.hexlet.hexletcorrection.persistence.entities.CorrectionMessage;
import org.springframework.stereotype.Service;

@Service
public interface CorrectionMessageService extends GenericService<CorrectionMessage> {
}
