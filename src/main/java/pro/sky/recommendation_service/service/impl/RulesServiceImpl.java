package pro.sky.recommendation_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.repository.RulesRepository;
import pro.sky.recommendation_service.service.RulesService;

@Service
public class RulesServiceImpl implements RulesService {

    private final RulesDTO rules;
    private final RulesRepository rulesRepository;

    @Autowired
    public RulesServiceImpl(RulesDTO rules, RulesRepository rulesRepository) {
        this.rules = rules;
        this.rulesRepository = rulesRepository;
    }

    @Override
    public RulesDTO createRule(RulesDTO rulesDTO) {
        return rulesRepository.createRule(rulesDTO);
    }

    private void checkArguments(RulesDTO rulesDTO) {
//        boolean flag = true;
//        if (rules.getQuery().isEmpty() || rules.getArguments() == null) {
//            throw new IllegalArgumentException("Rules must have at least one argument");
//        }

        String[] arguments = rulesDTO.getArguments();
        for (String argument : arguments) {
//            if (flag) {
            if (argument.equals("DEBIT")
                    || argument.equals("CREDIT")
                    || argument.equals("INVEST")
                    || argument.equals("SAVING")
                    || argument.equals(">")
                    || argument.equals("<")
                    || argument.equals(">=")
                    || argument.equals("<=")
                    || argument.equals("=")
                    || argument.equals("100000")) {

//                } else {
//                    flag = false;
//                }
            } else {
                throw new IllegalArgumentException("Rules must have at least one argument");
            }
        }
    }

}

