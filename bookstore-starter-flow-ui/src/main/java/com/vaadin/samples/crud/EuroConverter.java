package com.vaadin.samples.crud;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A converter that adds/removes the euro sign and formats currencies with two
 * decimal places.
 */
public class EuroConverter extends StringToBigDecimalConverter {

    public EuroConverter() {
        super("Cannot convert value to a number");
    }

    @Override
    public Result<BigDecimal> convertToModel(String value,
            ValueContext context) {
        value = value.replaceAll("[€\\s]", "").trim();
        if ("".equals(value)) {
            value = "0";
        }
        return super.convertToModel(value, context);
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        // Always display currency with two decimals
        NumberFormat format = super.getFormat(locale);
        if (format instanceof DecimalFormat) {
            format.setMaximumFractionDigits(2);
            format.setMinimumFractionDigits(2);
        }
        return format;
    }

    @Override
    public String convertToPresentation(BigDecimal value,
            ValueContext context) {
        return super.convertToPresentation(value, context) + " €";
    }
}
