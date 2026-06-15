package com.example.footballhub_manage_backend.specification;

import com.example.core.specification.SpecSearchCriteria;
import com.example.core.specification.SpecificationBase;
import com.example.footballhub_manage_backend.model.Match;

public class MatchSpecification extends SpecificationBase<Match> {
    public MatchSpecification(SpecSearchCriteria criteria) {
        super(criteria);
    }
}
