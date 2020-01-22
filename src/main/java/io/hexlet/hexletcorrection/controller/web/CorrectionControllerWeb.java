package io.hexlet.hexletcorrection.controller.web;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import io.hexlet.hexletcorrection.dto.CorrectionPostDto;
import io.hexlet.hexletcorrection.dto.mapper.CorrectionMapper;
import io.hexlet.hexletcorrection.service.AccountService;
import io.hexlet.hexletcorrection.service.CorrectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.CORRECTIONS_PATH;
import static java.lang.String.format;

@Controller
@RequestMapping(CORRECTIONS_PATH)
@AllArgsConstructor
public class CorrectionControllerWeb {

    private static final String TEMPLATE_DIR = "correction";
    private static final String VIEW_TEMPLATE = TEMPLATE_DIR + "/view";
    private static final String LIST_TEMPLATE = TEMPLATE_DIR + "/list";
    private static final String NEW_TEMPLATE = TEMPLATE_DIR + "/new";

    private final CorrectionService correctionService;
    private final AccountService accountService;
    private final CorrectionMapper correctionMapper;

    @GetMapping
    public String getCorrectionsForAccount(Model model, Principal principal) {
        Runnable error = () -> model.addAttribute("error", format("Account with name=%s not found", principal.getName()));
        Consumer<Account> fillModel = account -> {
            List<CorrectionDto> correctionDtos = correctionService.findByAccountId(account.getId())
                .stream()
                .map(correctionMapper::toCorrectionDto)
                .collect(Collectors.toList());

            model.addAttribute("correctionDtos", correctionDtos);
        };

        accountService.findByEmail(principal.getName()).ifPresentOrElse(fillModel, error);

        return LIST_TEMPLATE;
    }

    @GetMapping("/{id}")
    public String getCorrectionById(Model model, @PathVariable Long id) {
        Runnable error = () -> model.addAttribute("error", format("Correction with id=%s not found", id));
        Consumer<Correction> fillModel = correction -> model.addAttribute("correctionDto", correctionMapper.toCorrectionDto(correction));

        correctionService.findById(id).ifPresentOrElse(fillModel, error);

        return VIEW_TEMPLATE;
    }

    @GetMapping(value = "/new")
    public String getCorrectionCreationForm(Model model) {
        model.addAttribute("correctionPostDto", new CorrectionPostDto());
        return NEW_TEMPLATE;
    }

    @PostMapping
    public String createCorrection(@Valid @ModelAttribute CorrectionPostDto correctionPostDto,
                                   Model model,
                                   Principal principal) {
        Optional<Account> account = accountService.findByEmail(principal.getName());

        if (account.isPresent()) {
            Correction correction = correctionMapper.postDtoToCorrection(correctionPostDto);
            correction.setAccount(account.get());
            Long correctionCreatedId = correctionService.create(correction).getId();
            return String.format("redirect:%s/%d", CORRECTIONS_PATH, correctionCreatedId);
        }

        model.addAttribute("error", format("Account with name=%s not found", principal.getName()));

        return String.format("redirect:%s/new", CORRECTIONS_PATH);
    }
}
