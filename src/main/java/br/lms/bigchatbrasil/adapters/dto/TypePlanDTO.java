package br.lms.bigchatbrasil.adapters.dto;

import br.lms.bigchatbrasil.domain.enums.PlanType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypePlanDTO {
    private int typePlan;
    public PlanType getPlanType() {
        return PlanType.fromOrdinal(typePlan);
    }
}
