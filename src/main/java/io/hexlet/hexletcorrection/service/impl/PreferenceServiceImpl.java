package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.Preference;
import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.repository.PreferenceRepository;
import io.hexlet.hexletcorrection.repository.UserRepository;
import io.hexlet.hexletcorrection.service.PreferenceService;
import io.hexlet.hexletcorrection.service.dto.PreferenceDTO;
import io.hexlet.hexletcorrection.service.mapper.PreferenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Preference}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository preferenceRepository;

    private final PreferenceMapper preferenceMapper;

    private final UserRepository userRepository;

    /**
     * Save a preference.
     *
     * @param preferenceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PreferenceDTO save(PreferenceDTO preferenceDTO) {
        log.debug("Request to save Preference : {}", preferenceDTO);
        Preference preference = preferenceMapper.toEntity(preferenceDTO);
        long userId = preferenceDTO.getUserId();
        userRepository.findById(userId).ifPresent(preference::setUser);
        preference = preferenceRepository.save(preference);
        return preferenceMapper.toDto(preference);
    }

    @Override
    public PreferenceDTO createFromUser(User user) {
        log.debug("Create Preference from User : {}", user);
        final Preference preference = new Preference();
        preference.setUser(user);
        return preferenceMapper.toDto(preferenceRepository.save(preference));
    }

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PreferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Preferences");
        return preferenceRepository.findAll(pageable)
            .map(preferenceMapper::toDto);
    }

    /**
     * Get one preference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PreferenceDTO> findOne(Long id) {
        log.debug("Request to get Preference : {}", id);
        return preferenceRepository.findById(id)
            .map(preferenceMapper::toDto);
    }

    /**
     * Delete the preference by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Preference : {}", id);
        preferenceRepository.deleteById(id);
    }
}
