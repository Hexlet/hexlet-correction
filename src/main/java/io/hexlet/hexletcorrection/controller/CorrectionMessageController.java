package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.persistence.entities.CorrectionMessage;
import io.hexlet.hexletcorrection.services.CorrectionMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;

import static java.util.Objects.isNull;

@Slf4j
@Controller
@RequestMapping("/correction")
@RequiredArgsConstructor
public class CorrectionMessageController {
    
    private static final String TEMPLATE_DIR = "correction";
    private static final String LIST = TEMPLATE_DIR + "/list";
    private static final String FORM = TEMPLATE_DIR + "/form";
    private final CorrectionMessageService corMsgService;
    
    @GetMapping(path = {"/list", ""})
    public String list(Model model) {
        model.addAttribute("correctionMessages", corMsgService.findAll());
        
        return LIST;
    }
    
    @GetMapping(path = "/view/{id}")
    public String showById(@PathVariable("id") Long id, Model model) {
        CorrectionMessage correctionMessage = corMsgService.findById(id);
        
        if (isNull(correctionMessage)) {
            return "redirect:/correction/list";
        }
        
        HashSet<CorrectionMessage> correctionMessages = new HashSet<>();
        correctionMessages.add(correctionMessage);
        
        model.addAttribute("correctionMessages", correctionMessages);
        
        return LIST;
    }
    
    @GetMapping(path = "/delete/{id}")
    public String deleteById(@PathVariable("id") Long id, Model model) {
        corMsgService.delete(id);
        
        model.addAttribute("correctionMessages", corMsgService.findAll());
        
        return LIST;
    }
    
    @GetMapping(value = "/new")
    public String createForm(Model model) {
        model.addAttribute("correctionMessage", new CorrectionMessage());
        return FORM;
    }
    
    //CREATE new itinerary, POST from front
    @PostMapping
    public String save(CorrectionMessage correctionMessage) {
        CorrectionMessage savedMessage = corMsgService.save(correctionMessage);
        
        return "redirect:/correction/view/" + savedMessage.getId();
    }
}
