package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.TypoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Templates.TYPO_LIST_TEMPLATE;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Order.asc;

@Controller
@RequestMapping(TYPOS)
@RequiredArgsConstructor
public class TypoController {

    private final TreeSet<Integer> pageSizes = new TreeSet<>(List.of(2, 5, 10, 15, 25, 50));

    private final TypoService service;

    @GetMapping
    String getPageTypo(Model model, @SortDefault(TYPO_SORT_FIELD) Pageable pageable) {
        final var size = Optional.ofNullable(pageSizes.floor(pageable.getPageSize())).orElseGet(pageSizes::first);
        final var pageRequest = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        final var typoPage = service.getTypoPage(pageRequest);

        final var sort = typoPage.getSort()
            .stream()
            .findFirst()
            .orElseGet(() -> asc(TYPO_SORT_FIELD));
        final var number = typoPage.getNumber() + 1;
        final var pageNumbers = IntStream.of(1, number - 1, number, number + 1, typoPage.getTotalPages())
            .filter(n -> n > 0)
            .filter(n -> n <= typoPage.getTotalPages())
            .distinct()
            .boxed()
            .collect(toList());

        if (pageNumbers.size() > 1) {
            if (pageNumbers.get(0) != pageNumbers.get(1) - 1) {
                pageNumbers.add(1, null);
            }
            if (pageNumbers.get(pageNumbers.size() - 2) != pageNumbers.get(pageNumbers.size() - 1) - 1) {
                pageNumbers.add(pageNumbers.size() - 1, null);
            }
        }

        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("typoPage", typoPage);
        model.addAttribute("pageSizes", pageSizes);
        model.addAttribute("sortProp", sort.getProperty());
        model.addAttribute("sortDir", sort.getDirection());
        return TYPO_LIST_TEMPLATE;
    }
}
