package com.example.footballhub_manage_backend.specification;

import com.example.core.specification.SpecSearchCriteria;
import com.example.core.specification.SpecificationBase;
import com.example.footballhub_manage_backend.model.Team;

public class TeamSpecification extends SpecificationBase<Team> {
    public TeamSpecification(SpecSearchCriteria criteria) {
        super(criteria);
    }
}
