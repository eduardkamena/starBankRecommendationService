package pro.sky.recommendation_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.service.RulesService;

@Service
public class RulesServiceImpl implements RulesService {

    private final RulesDTO rules;

    @Autowired
    public RulesServiceImpl(RulesDTO rules) {
        this.rules = rules;
    }

    @Override
    public RulesDTO createRule(RulesDTO rulesDTO) {
        try {
            checkArguments(rulesDTO);
            RulesDTO newRules = new RulesDTO();
            newRules.setQuery(rules.getQuery());
            newRules.setArguments(rules.getArguments());
            newRules.setNegate(rules.isNegate());
            return newRules;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);

        }
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

