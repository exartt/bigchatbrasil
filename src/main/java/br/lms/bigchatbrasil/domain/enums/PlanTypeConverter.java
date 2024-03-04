package br.lms.bigchatbrasil.domain.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PlanTypeConverter implements AttributeConverter<PlanType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PlanType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.ordinal();
    }

    @Override
    public PlanType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return PlanType.values()[dbData];
    }
}